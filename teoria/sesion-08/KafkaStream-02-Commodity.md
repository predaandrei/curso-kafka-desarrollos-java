## Commodity

### Topology

- Tomaremos los mensajes del topic `t-commodity-order`
- El número de tarjeta de crédito es confidencial, por lo que crearemos un procesador que enmascare el
  número de tarjeta de crédito para cada pedido y lo envíe al topi `t-commodity-order-masked`.
- A continuación enviaremos estos números "enmascarados" a un procesador de patrones, de recompensas y de almacenamiento.
- Cada dato en Kafka consta de una clave y un valor.
- Los datos originales del topic `t-commodity-order` tienen como clave el número de pedido.
- La misma clave tendrá también el mismo valor en su procesador hijo, a menos que la cambiemos, como veremos más adelante.

Para ver la key en el console consumer, podemos ejecutarlo con esta propiedad

```bash
kafka-console-consumer.sh ... --property print.key=true
```

---

### First Step

1. Crea la clase `util.CommodityStreamUtil.java`

```java
public class CommodityStreamUtil {

    public static OrderMessage maskCreditCardNumber(OrderMessage orderMessage) {
        String creditCardNumber = orderMessage.getCreditCardNumber();
        String maskedCreditCardNumber = "****-****-****-" + creditCardNumber.substring(creditCardNumber.length() - 4);

        return new OrderMessage(
                orderMessage.getOrderLocation(),
                orderMessage.getOrderNumber(),
                maskedCreditCardNumber,
                orderMessage.getOrderDateTime(),
                orderMessage.getItemName(),
                orderMessage.getPrice(),
                orderMessage.getQuantity());
    }
}
```

2. Crea la clase `broker.stream.commodity.MaskOrderStream.java` en el proyecto `kafka-stream-sample`

```java
@Component
public class MaskOrderStream {

    @Autowired
    void kstreamCommodityMask(StreamsBuilder builder) {
        var orderSerde = new JsonSerde<>(OrderMessage.class);
        var maskedCreditCardStream = builder.stream("t-commodity-order", Consumed.with(Serdes.String(), orderSerde))
                .mapValues(CommodityStreamUtil::maskCreditCardNumber);

        maskedCreditCardStream.to("t-commodity-order-masked", Produced.with(Serdes.String(), orderSerde));

        maskedCreditCardStream.print(Printed.<String, OrderMessage>toSysOut().withLabel("Masked Order Stream"));
    }

}
```

---

### Sink Processors

1. Crea la clase `broker.message.OrderPatternMessage.java` en el proyecto `kafka-stream-sample`

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderPatternMessage {

    private String itemName;
    private long totalItemAmount;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private OffsetDateTime orderDateTime;
    private String orderLocation;
    private String orderNumber;

    @Override
    public String toString() {
        return "OrderPatternMessage{" +
                "itemName='" + itemName + '\'' +
                ", totalItemAmount=" + totalItemAmount +
                ", orderDateTime=" + orderDateTime +
                ", orderLocation='" + orderLocation + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                '}';
    }

}
```

2. Crea la clase `broker.message.OrderRewardMessage.java` en el proyecto `kafka-stream-sample`

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRewardMessage {

    private String orderLocation;
    private String orderNumber;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private OffsetDateTime orderDateTime;
    private String itemName;
    private int price;
    private int quantity;

    // toString method
    @Override
    public String toString() {
        return "OrderRewardMessage{" +
                "orderLocation='" + orderLocation + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", orderDateTime=" + orderDateTime +
                ", itemName='" + itemName + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }

}
```

3. Crea métodos en la clase `CommodityStreamUtil.java` para convertir de `OrderMessage` a `OrderPatternMessage`

```java
public static OrderPatternMessage convertToOrderPatternMessage(OrderMessage orderMessage) {
    String itemName = orderMessage.getItemName();
    long totalItemAmount = orderMessage.getPrice() * orderMessage.getQuantity();
    OffsetDateTime orderDateTime = orderMessage.getOrderDateTime();
    String orderLocation = orderMessage.getOrderLocation();
    String orderNumber = orderMessage.getOrderNumber();
    
    return new OrderPatternMessage(itemName, totalItemAmount, orderDateTime, orderLocation, orderNumber);
}
```

4. Crea métodos en la clase `CommodityStreamUtil.java` para convertir de `OrderMessage` a `OrderRewardMessage`

```java
public static OrderRewardMessage convertToOrderRewardMessage(OrderMessage orderMessage) {
    String orderLocation = orderMessage.getOrderLocation();
    String orderNumber = orderMessage.getOrderNumber();
    OffsetDateTime orderDateTime = orderMessage.getOrderDateTime();
    String itemName = orderMessage.getItemName();
    int price = orderMessage.getPrice();
    int quantity = orderMessage.getQuantity();
    
    return new OrderRewardMessage(orderLocation, orderNumber, orderDateTime, itemName, price, quantity);
}
```

5. Crea un método en la clase `CommodityStreamUtil.java` que devuelva un predicado que compruebe si el valor de una orden es mayor a 200.

**Ten en cuenta que este predicado es un predicado de `org.apache.kafka.streams.kstream.Predicate` y no de java.

```java
public static Predicate<String, OrderMessage> isLargeQuantity() {
    return (key, orderMessage) -> orderMessage.getQuantity() > 200;
}
```

6. Crea la clase `broker.stream.commodity.CommodityOneStream.java` en el proyecto `kafka-stream-sample`

```java
@Component
public class CommodityOneStream {

    @Autowired
    void kstreamCommodityTrading(StreamsBuilder builder) {
        var orderSerde = new JsonSerde<>(OrderMessage.class);
        var orderPatternSerde = new JsonSerde<>(OrderPatternMessage.class);
        var orderRewardSerde = new JsonSerde<>(OrderRewardMessage.class);

        var maskedCreditCardStream = builder
                .stream("t-commodity-order", Consumed.with(Serdes.String(), orderSerde))
                .mapValues(CommodityStreamUtil::maskCreditCardNumber);

        maskedCreditCardStream.mapValues(CommodityStreamUtil::convertToOrderPatternMessage)
                .to("t-commodity-pattern-one", Produced.with(Serdes.String(), orderPatternSerde));

        maskedCreditCardStream.filter(CommodityStreamUtil.isLargeQuantity())
                .mapValues(CommodityStreamUtil::convertToOrderRewardMessage)
                .to("t-commodity-reward-one", Produced.with(Serdes.String(), orderRewardSerde));

        maskedCreditCardStream.to("t-commodity-storage-one", Produced.with(Serdes.String(), orderSerde));
    }
}
```

--- 

### Additional Requirements

1. Añade los métodos `isPlastic()`, `isCheap()` y `generateStorageKey()` en `CommodityStreamUtil.java`

```java
public static Predicate<String, OrderPatternMessage> isPlastic() {
    return (key, orderPatternMessage) -> orderPatternMessage.getItemName().toUpperCase().startsWith("PLASTIC");
}

public static Predicate<String, OrderMessage> isCheap() {
    return (key, orderMessage) -> orderMessage.getPrice() < 100;
}

public static KeyValueMapper<String, OrderMessage, String> generateStorageKey() {
    return (key, orderMessage) -> Base64.getEncoder().encodeToString(orderMessage.getOrderNumber().getBytes());
}
```

2. Crea la clase `broker.stream.commodity.CommodityTwoStream.java`

```java
@Component
public class CommodityTwoStream {

        @Autowired
        void kstreamCommodityTrading(StreamsBuilder builder) {
                var orderSerde = new JsonSerde<>(OrderMessage.class);
                var orderPatternSerde = new JsonSerde<>(OrderPatternMessage.class);
                var orderRewardSerde = new JsonSerde<>(OrderRewardMessage.class);
                var stringSerde = Serdes.String();

                var maskedCreditCardStream = builder
                                .stream("t-commodity-order", Consumed.with(Serdes.String(), orderSerde))
                                .mapValues(CommodityStreamUtil::maskCreditCardNumber);

                maskedCreditCardStream.mapValues(CommodityStreamUtil::convertToOrderPatternMessage)
                                .split()
                                .branch(CommodityStreamUtil.isPlastic(),
                                                Branched.<String, OrderPatternMessage>withConsumer(
                                                                ks -> ks.to("t-commodity-pattern-two-plastic",
                                                                                Produced.with(stringSerde,
                                                                                                orderPatternSerde))))
                                .defaultBranch(
                                                Branched.<String, OrderPatternMessage>withConsumer(
                                                                ks -> ks.to("t-commodity-pattern-two-notplastic",
                                                                                Produced.with(stringSerde,
                                                                                                orderPatternSerde))));

                maskedCreditCardStream.filter(CommodityStreamUtil.isLargeQuantity())
                                .filterNot(CommodityStreamUtil.isCheap())
                                .mapValues(CommodityStreamUtil::convertToOrderRewardMessage)
                                .to("t-commodity-reward-two", Produced.with(Serdes.String(), orderRewardSerde));

                maskedCreditCardStream
                                .selectKey(CommodityStreamUtil.generateStorageKey())
                                .to("t-commodity-storage-two", Produced.with(Serdes.String(), orderSerde));
        }

}
```

---

### Reward Each Location

1. Crea el método `mapToOrderRewardChangeKey` en la clase `CommodityStreamUtil.java`

```java
public static KeyValueMapper<String, OrderMessage, KeyValue<String, OrderRewardMessage>> mapToOrderRewardChangeKey() {
        return (key, orderMessage) -> KeyValue.pair(orderMessage.getOrderLocation(),
                convertToOrderRewardMessage(orderMessage));
    }
```

2. Crea la clase `CommodityThreeStream.java`

```java
@Component
public class CommodityThreeStream {

        @Autowired
        void kstreamCommodityTrading(StreamsBuilder builder) {
                var orderSerde = new JsonSerde<>(OrderMessage.class);
                var orderPatternSerde = new JsonSerde<>(OrderPatternMessage.class);
                var orderRewardSerde = new JsonSerde<>(OrderRewardMessage.class);
                var stringSerde = Serdes.String();

                var maskedCreditCardStream = builder
                                .stream("t-commodity-order", Consumed.with(Serdes.String(), orderSerde))
                                .mapValues(CommodityStreamUtil::maskCreditCardNumber);

                maskedCreditCardStream.mapValues(CommodityStreamUtil::convertToOrderPatternMessage)
                                .split()
                                .branch(CommodityStreamUtil.isPlastic(),
                                                Branched.<String, OrderPatternMessage>withConsumer(
                                                                ks -> ks.to("t-commodity-pattern-three-plastic",
                                                                                Produced.with(stringSerde,
                                                                                                orderPatternSerde))))
                                .defaultBranch(
                                                Branched.<String, OrderPatternMessage>withConsumer(
                                                                ks -> ks.to("t-commodity-pattern-three-notplastic",
                                                                                Produced.with(stringSerde,
                                                                                                orderPatternSerde))));

                maskedCreditCardStream.filter(CommodityStreamUtil.isLargeQuantity())
                                .filterNot(CommodityStreamUtil.isCheap())
                                .map(CommodityStreamUtil.mapToOrderRewardChangeKey())
                                .to("t-commodity-reward-three", Produced.with(Serdes.String(), orderRewardSerde));

                maskedCreditCardStream
                                .selectKey(CommodityStreamUtil.generateStorageKey())
                                .to("t-commodity-storage-three", Produced.with(Serdes.String(), orderSerde));
        }

}
```

---

### Calling API or Other Process

1. Crea la clase `CommodityFourStream.java`

```java
@Component
@Slf4j
public class CommodityFourStream {

    private void reportFraud(OrderMessage orderMessage) {
        log.info("Reporting fraud {}", orderMessage);
    }

    @Autowired
    void kstreamCommodityTrading(StreamsBuilder builder) {
        var orderSerde = new JsonSerde<>(OrderMessage.class);
        var orderPatternSerde = new JsonSerde<>(OrderPatternMessage.class);
        var orderRewardSerde = new JsonSerde<>(OrderRewardMessage.class);
        var stringSerde = Serdes.String();

        var maskedCreditCardStream = builder
                .stream("t-commodity-order", Consumed.with(Serdes.String(), orderSerde))
                .mapValues(CommodityStreamUtil::maskCreditCardNumber);

        maskedCreditCardStream.mapValues(CommodityStreamUtil::convertToOrderPatternMessage)
                .split()
                .branch(CommodityStreamUtil.isPlastic(),
                        Branched.<String, OrderPatternMessage>withConsumer(
                                ks -> ks.to("t-commodity-pattern-four-plastic",
                                        Produced.with(stringSerde,
                                                orderPatternSerde))))
                .defaultBranch(
                        Branched.<String, OrderPatternMessage>withConsumer(
                                ks -> ks.to("t-commodity-pattern-four-notplastic",
                                        Produced.with(stringSerde,
                                                orderPatternSerde))));

        maskedCreditCardStream.filter(CommodityStreamUtil.isLargeQuantity())
                .filterNot(CommodityStreamUtil.isCheap())
                .map(CommodityStreamUtil.mapToOrderRewardChangeKey())
                .to("t-commodity-reward-four", Produced.with(Serdes.String(), orderRewardSerde));

        maskedCreditCardStream
                .selectKey(CommodityStreamUtil.generateStorageKey())
                .to("t-commodity-storage-four", Produced.with(Serdes.String(), orderSerde));

        maskedCreditCardStream.filter(
                (k, v) -> v.getOrderLocation().toUpperCase().startsWith("C")).foreach(
                (k, v) -> reportFraud(v));
    }

}

```

---

### Further Fraud Processing

1. Crea la clase `CommodityFiveStream.java`
