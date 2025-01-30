## Venta Relámpago Stream - Datos más recientes
- Se ofrecen 3 items como candidatos a ofertas relámpago
- Los clientes pueden votar por estos ítems para ver cuál será la oferta relámpago 
- Cada cliente sólo puede votar un ítem, pero pueden modificar su voto tantas veces como deseen 
dentro del rango de tiempo durante el cual la votación está activa

Implementación

- Se usará KTable ya que sólo nos interesa el último item seleccionado por cada cliente
- Los records constarán de el customerId como clave y del producto votado como valor
- Sobre la tabla se harán "upserts" (actualizar si existe, insertar si no)

### VentaRelampagoMessage.java

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentaRelampagoMessage {

    private String customerId;

    private String itemName;

    @Override
    public String toString() {
        return "FlashSaleVoteMessage [customerId=" + customerId + ", itemName=" + itemName + "]";
    }

}
```

- Como entrada, recibimos {null, flashSaleVoteMessage}
- Debemos transformar esto a un mensaje en el que la key sea el customerId y el valor sea el itemName

Ejemplo de ejecución

```
Entrada
Laura: Pantalones

Stream
Pantalones: 1

Entrada
Juan: Pantalones

Stream
Pantalones: 2

Entrada
Juan: Zapatillas

Stream
Pantalones: 1
Zapatillas: 1

Entrada
Laura: Zapatillas

Stream
Pantalones: 0
Zapatillas: 2

Entrada
Pedro: Pantalones

Stream
Pantalones: 1
Zapatillas: 2

```

### VentaRelampagoVoteStream.java

```java
@Component
public class VentaRelampagoVoteStream {

    @Autowired
    void ventaRelampagoVoteStream(StreamsBuilder builder) {
        var stringSerde = Serdes.String();
        var ventaRelampagoVoteSerde = new JsonSerde<>(VentaRelampagoMessage.class);

        builder.stream("t-commodity-ventarelampago-vote", Consumed.with(stringSerde, ventaRelampagoVoteSerde))
                .map(
                        (key, value) -> KeyValue.pair(value.getCustomerId(), value.getItemName()))
                .to("t-commodity-ventarelampago-vote-one-user-item");

        builder.table("t-commodity-ventarelampago-vote-one-user-item", Consumed.with(stringSerde, stringSerde))
                .groupBy(
                        (user, votedItem) -> KeyValue.pair(votedItem, votedItem))
                .count()
                .toStream()
                .to("t-commodity-ventarelampago-vote-one-result");
    }

}
```
---

## Venta Relámpago Timestamp

```java
public class VentaRelampagoVoteFixedKeyProcessor
        implements FixedKeyProcessor<String, VentaRelampagoVoteMessage, VentaRelampagoVoteMessage> {

    private final long voteStartTime;

    private final long voteEndTime;

    private FixedKeyProcessorContext<String, VentaRelampagoVoteMessage> processorContext;

    public VentaRelampagoVoteFixedKeyProcessor(OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        this.voteStartTime = startDateTime.toInstant().toEpochMilli();
        this.voteEndTime = endDateTime.toInstant().toEpochMilli();
    }

    @Override
    public void init(FixedKeyProcessorContext<String, VentaRelampagoVoteMessage> context) {
        this.processorContext = context;
    }

    @Override
    public void process(FixedKeyRecord<String, VentaRelampagoVoteMessage> record) {
        var recordTime = processorContext.currentSystemTimeMs();

        if (recordTime >= voteStartTime && recordTime <= voteEndTime) {
            processorContext.forward(record.withValue(record.value()));
        }
    }

}
```

```java
@Component
public class VentaRelampagoVoteThreeStream {

    @Autowired
    void ventaRelampagoVoteStream(StreamsBuilder builder) {
        var stringSerde = Serdes.String();
        var flashSaleVoteSerde = new JsonSerde<>(VentaRelampagoVoteMessage.class);
        var voteStart = OffsetDateTime.now().plusMinutes(2);
        var voteEnd = voteStart.plusHours(1);

        builder.stream("t-commodity-venta-relampago-vote", Consumed.with(stringSerde, flashSaleVoteSerde))
                .processValues(
                        () -> new VentaRelampagoFixedKeyProcessor(voteStart, voteEnd))
                .filter(
                        (key, transformedValue) -> transformedValue != null)
                .map(
                        (key, value) -> KeyValue.pair(value.getCustomerId(), value.getItemName()))
                .to("t-commodity-venta-relampago-vote-user-item");

        builder.table("t-commodity-venta-relampago-vote-user-item", Consumed.with(stringSerde, stringSerde))
                .groupBy(
                        (user, votedItem) -> KeyValue.pair(votedItem, votedItem))
                .count()
                .toStream()
                .to("t-commodity-venta-relampago-vote-result");
    }

}
```

