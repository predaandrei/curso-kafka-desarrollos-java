## Sumando valores

- Vamos a sumar los mensajes relativos a la gestión de Inventario
- Usaremos Kafka stream `aggregate`
- Lay key del mensaje será el nombre del producto `item`
- El valor contiene el nombre del producto, la ubicación del inventario, la cantidad y el tipo
- Opcionalmente podrá contener un `transactionTime` si este valor está vacío usaremos el publish time del producer
- No usaremos validación

``InventoryMessage.java``

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryMessage {

	private String item;
	private String location;
	private long quantity;
	private String type;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	private OffsetDateTime transactionTime;

	@Override
	public String toString() {
		return "InventoryMessage [item=" + item + ", location=" + location + ", quantity=" + quantity + ", type=" + type
				+ ", transactionTime=" + transactionTime + "]";
	}

}

```

```java
@Component
public class InventoryOneStream {

    @Autowired
    void kstreamInventory(StreamsBuilder builder) {
        var stringSerde = Serdes.String();
        var inventorySerde = new JsonSerde<>(InventoryMessage.class);
        var longSerde = Serdes.Long();

        builder.stream("t-commodity-inventory", Consumed.with(stringSerde, inventorySerde))
                .mapValues(
                        (item, inventory) -> inventory.getQuantity())
                .groupByKey()
                .aggregate(
                        () -> 0L,
                        (aggKey, newValue, aggValue) -> aggValue + newValue,
                        Materialized.with(stringSerde, longSerde))
                .toStream()
                .to("t-commodity-inventory-total-one", Produced.with(stringSerde, longSerde));
    }

}

```

## Restando valores

Para ser capaces no sólo de sumar el número de elementos de un producto, sino también de poder reducir el número de elementos, 
añadiremos un nuevo campo que nos indicará si la operación es para añadir un elemento o para restarlo.

```java
@Component
public class InventoryTwoStream {

    @Autowired
    void kstreamInventory(StreamsBuilder builder) {
        var stringSerde = Serdes.String();
        var inventorySerde = new JsonSerde<>(InventoryMessage.class);
        var longSerde = Serdes.Long();

        builder.stream("t-commodity-inventory", Consumed.with(stringSerde, inventorySerde))
                .mapValues(
                        (item, inventory) -> inventory.getType().equalsIgnoreCase("ADD") ? inventory.getQuantity()
                                : -1 * inventory.getQuantity())
                .groupByKey()
                .aggregate(
                        () -> 0L,
                        (aggKey, newValue, aggValue) -> aggValue + newValue,
                        Materialized.with(stringSerde, longSerde))
                .toStream()
                .to("t-commodity-inventory-total-two", Produced.with(stringSerde, longSerde));
    }

}
```

## Usando `reduce`

Como el tipo de los datos siempre es el mismo, en este caso Long, podemos usar el método `reduce`

```java
@Component
public class InventoryThreeStream {

    @Autowired
    void kstreamInventory(StreamsBuilder builder) {
        var stringSerde = Serdes.String();
        var inventorySerde = new JsonSerde<>(InventoryMessage.class);
        var longSerde = Serdes.Long();

        builder.stream("t-commodity-inventory", Consumed.with(stringSerde, inventorySerde))
                .mapValues(
                        (item, inventory) -> inventory.getType().equalsIgnoreCase("ADD") ? inventory.getQuantity()
                                : -1 * inventory.getQuantity())
                .groupByKey()
                .reduce(Long::sum, Materialized.with(stringSerde, longSerde))
                .toStream()
                .to("t-commodity-inventory-total-three", Produced.with(stringSerde, longSerde));
    }

}
```

## Timestamp Extractor

`InventoryTimestampExtractor.java`

```java
public class InventoryTimestampExtractor implements TimestampExtractor {

    @Override
    public long extract(ConsumerRecord<Object, Object> record, long partitionTime) {
        var inventoryMessage = (InventoryMessage) record.value();

        return inventoryMessage != null ? inventoryMessage.getTransactionTime().toInstant().toEpochMilli()
                : record.timestamp();
    }

}
```

```java
@Component
public class InventoryFourStream {

    @Autowired
    void kstreamInventory(StreamsBuilder builder) {
        var stringSerde = Serdes.String();
        var inventorySerde = new JsonSerde<>(InventoryMessage.class);
        var inventoryTimestampExtractor = new InventoryTimestampExtractor();

        builder.stream("t-commodity-inventory",
                Consumed.with(stringSerde, inventorySerde, inventoryTimestampExtractor, null))
                .to("t-commodity-inventory-four", Produced.with(stringSerde, inventorySerde));
    }

}
```
Ejemplo de entrada

```
{
    "item": "Laptop",
    "location": "Warehouse-1",
    "quantity": 50,
    "type": "ADD",
    "transactionTime": "2025-02-06T10:15:30.123+0000"
}

```

Ejemplo de salida

```bash
bin/kafka-console-consumer.sh --topic t-commodity-inventory-four \
    --bootstrap-server localhost:9092 --from-beginning \
    --property print.timestamp=true
```
```
CreateTime: 1707214530123   Key: null   Value: {"item":"Laptop","location":"Warehouse-1","quantity":50,"type":"ADD","transactionTime":"2025-02-06T10:15:30.123+0000"}
```

## Tumbling Time Window

- Vamos a calcular el valor de cambio de Inventario en cada hora.

```java
@Component
public class InventoryFiveStream {

    @Autowired
    void kstreamInventory(StreamsBuilder builder) {
        var stringSerde = Serdes.String();
        var inventorySerde = new JsonSerde<>(InventoryMessage.class);
        var inventoryTimestampExtractor = new InventoryTimestampExtractor();
        var longSerde = Serdes.Long();
        var windowLength = Duration.ofHours(1l);
        var windowSerde = WindowedSerdes.timeWindowedSerdeFrom(String.class, windowLength.toMillis());

        builder.stream("t-commodity-inventory",
                Consumed.with(stringSerde, inventorySerde, inventoryTimestampExtractor, null))
                .mapValues(
                        (k, v) -> v.getType().equalsIgnoreCase("ADD") ? v.getQuantity() : -1 * v.getQuantity())
                .groupByKey()
                .windowedBy(TimeWindows.ofSizeWithNoGrace(windowLength))
                .reduce(Long::sum, Materialized.with(stringSerde, longSerde))
                .toStream()
                .peek(
                        (k, v) -> {
                            var windowStartTime = Instant.ofEpochMilli(k.window().start()).atOffset(ZoneOffset.UTC);
                            var windowEndTime = Instant.ofEpochMilli(k.window().end()).atOffset(ZoneOffset.UTC);

                            System.out.println("[" + k.key() + "@" + windowStartTime + "/" + windowEndTime + "], " + v);
                        })
                .to("t-commodity-inventory-five", Produced.with(windowSerde, longSerde));
    }

}
```
Salida
```
[Apple@2025-01-15T08:00Z/2025-01-15T09:00Z],64
[Apple@2025-01-15T08:00Z/2025-01-15T09:00Z],63
[Apple@2025-01-15T09:00Z/2025-01-15T10:00Z],7
[Apple@2025-01-15T09:00Z/2025-01-15T10:00Z],8
[Apple@2025-01-15T10:00Z/2025-01-15T11:00Z],-1
[Apple@2025-01-15T10:00Z/2025-01-15T11:00Z],-18
```
Salida en `t-commodity-inventory-five`
```
{
  "window": {
    "key": "Item123",
    "start": "2025-02-05T10:00:00.000+0000",
    "end": "2025-02-05T11:00:00.000+0000"
  },
  "quantity": 8
}
```

## Hopping Time Window

```java
@Component
public class InventorySixStream {

    @Autowired
    void kstreamInventory(StreamsBuilder builder) {
        var stringSerde = Serdes.String();
        var inventorySerde = new JsonSerde<>(InventoryMessage.class);
        var inventoryTimestampExtractor = new InventoryTimestampExtractor();
        var longSerde = Serdes.Long();
        var windowLength = Duration.ofHours(1l);
        var windowSerde = WindowedSerdes.timeWindowedSerdeFrom(String.class, windowLength.toMillis());
        var hopLength = Duration.ofMinutes(20l);

        builder.stream("t-commodity-inventory",
                Consumed.with(stringSerde, inventorySerde, inventoryTimestampExtractor, null))
                .mapValues(
                        (k, v) -> v.getType().equalsIgnoreCase("ADD") ? v.getQuantity() : -1 * v.getQuantity())
                .groupByKey()
                .windowedBy(TimeWindows.ofSizeWithNoGrace(windowLength).advanceBy(hopLength))
                .reduce(Long::sum, Materialized.with(stringSerde, longSerde))
                .toStream()
                .peek(
                        (k, v) -> {
                            var windowStartTime = Instant.ofEpochMilli(k.window().start()).atOffset(ZoneOffset.UTC);
                            var windowEndTime = Instant.ofEpochMilli(k.window().end()).atOffset(ZoneOffset.UTC);

                            System.out.println("[" + k.key() + "@" + windowStartTime + "/" + windowEndTime + "], " + v);
                        })
                .to("t-commodity-inventory-six", Produced.with(windowSerde, longSerde));
    }

}
```

## Session Window

```java
@Component
public class InventorySevenStream {

    @Autowired
    void kstreamInventory(StreamsBuilder builder) {
        var stringSerde = Serdes.String();
        var inventorySerde = new JsonSerde<>(InventoryMessage.class);
        var inventoryTimestampExtractor = new InventoryTimestampExtractor();
        var longSerde = Serdes.Long();
        var inactivityGap = Duration.ofMinutes(30l);
        var windowSerde = WindowedSerdes.sessionWindowedSerdeFrom(String.class);

        builder.stream("t-commodity-inventory",
                Consumed.with(stringSerde, inventorySerde, inventoryTimestampExtractor, null))
                .groupByKey()
                .windowedBy(SessionWindows.ofInactivityGapWithNoGrace(inactivityGap))
                .count()
                .toStream()
                .filter(
                        (k, v) -> v != null && v > 0)
                .peek(
                        (k, v) -> {
                            var windowStartTime = Instant.ofEpochMilli(k.window().start()).atOffset(ZoneOffset.UTC);
                            var windowEndTime = Instant.ofEpochMilli(k.window().end()).atOffset(ZoneOffset.UTC);

                            System.out.println("[" + k.key() + "@" + windowStartTime + "/" + windowEndTime + "], " + v);
                        })
                .to("t-commodity-inventory-seven", Produced.with(windowSerde, longSerde));
    }

}
```