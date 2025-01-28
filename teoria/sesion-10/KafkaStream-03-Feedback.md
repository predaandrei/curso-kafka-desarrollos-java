## Planteamiento del problema

- Tenemos un formulario de feedback con texto libre.
- Queremos analizar este texto e identificar si contiene una serie de palabras que asociamos con buen feedback.

## FeedbackOneStream.java

Creamos la clase `broker.stream.feedback.FeedbackOneStream.java`

```java
@Component
public class FeedbackOneStream {

    private static final java.util.Set<String> GOOD_WORDS = java.util.Set.of("contento", "bueno", "amable");

    @Autowired
    void kstreamFeedback(StreamsBuilder builder) {
        var stringSerde = Serdes.String();
        var feedbackSerde = new JsonSerde<>(FeedbackMessage.class);

        var goodFeedbackStream = builder.stream("t-commodity-feedback", Consumed.with(stringSerde, feedbackSerde))
                .flatMapValues(mapperGoodWords());

        goodFeedbackStream.to("t-commodity-feedback-one-good");
    }

    private ValueMapper<FeedbackMessage, Iterable<String>> mapperGoodWords() {
        return feedback -> java.util.Arrays.asList(feedback.getFeedback().toLowerCase().split("\\s+")).stream()
                .filter(GOOD_WORDS::contains).distinct().collect(java.util.stream.Collectors.toList());
    }

}

```

Con esta solución estamos enviando el feedback bueno a un nuevo topic pero la key es null.

Ejemplo de ejecución

```bash
kafka-console.consumer.sh --bootstrap-server localhost:9092 --property print.key=true --topic t-commodity-feedback-one-good
```

Salida

```
null happy
null helpful
```

## FeedbackTwoStream.java

- Queremos enviar el feedback bueno pero añadiendo como key a los mensajes la localización.
- En lugar de utilizar `flatMapValues` usaremos `flatMap`
- Ten en cuenta que el segundo `map` tras `distinct()`es un map de java stream.

Creamos la clase `broker.stream.feedback.FeedbackTwoStream.java`

```java
@Component
public class FeedbackTwoStream {

    private static final Set<String> GOOD_WORDS = java.util.Set.of("contento", "bueno", "amable");

    @Autowired
    void kstreamFeedback(StreamsBuilder builder) {
        var stringSerde = Serdes.String();
        var feedbackSerde = new JsonSerde<>(FeedbackMessage.class);

        var goodFeedbackStream = builder
                .stream("t-commodity-feedback", Consumed.with(stringSerde, feedbackSerde))
                .flatMap(
                        (key, value) -> Arrays
                                .asList(value.getFeedback().replaceAll("[^a-zA-Z ]", "")
                                        .toLowerCase().split("\\s+"))
                                .stream()
                                .filter(word -> GOOD_WORDS.contains(word))
                                .distinct()
                                .map(goodWord -> KeyValue.pair(value.getLocation(),
                                        goodWord))
                                .collect(Collectors.toList()));

        goodFeedbackStream.to("t-commodity-feedback-two-good");
    }

}
```

Ejemplo de ejecución

```bash
kafka-console.consumer.sh --bootstrap-server localhost:9092 --property print.key=true --topic t-commodity-feedback-two-good
```

Salida

```
Colombia    feliz
Colombia    contento
España  amable
```

## FeedbackThreeStream.java (Ejercicio)

- El objetivo es tener además de la lista de buenas palabras una lista de palabras negativas
- Se debe recuperar los mensajes de feedback y dividirlos en buenos y malos
- Los buenos se enviaran al topic `t-commodity-feedback-three-good`
- El feedback malo se enviara al topic `t-commodity-feedback-three-bad`

```java
private static final Set<String> BAD_WORDS = Set.of("enfadado", "descontento", "malo");
```

Ejemplo de ejecución

```bash
kafka-console.consumer.sh --bootstrap-server localhost:9092 --property print.key=true --topic t-commodity-feedback-three-good
```

Salida

```
Colombia    feliz
Colombia    contento
España  amable
```

```bash
kafka-console.consumer.sh --bootstrap-server localhost:9092 --property print.key=true --topic t-commodity-feedback-three-bad
```

Salida

```
Venezuela    enfadado
Colombia    descontento
España  malo
```