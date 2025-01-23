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

## FeedbackTwoStream.java

- Queremos enviar el feedback bueno pero añadiendo como key a los mensajes la localización.

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


## FeedbackThreeStream.java (Ejercicio)

- El objetivo es tener además de la lista de buenas palabras una lista de palabras negativas
- Se debe recuperar los mensajes de feedback y dividirlos en buenos y malos
- Los buenos se enviara al topic `t-commodity-feedback-three-good`
- El feedback malo se enviara al topic `t-commodity-feedback-three-bad`

```java
private static final Set<String> BAD_WORDS = Set.of("enfadado", "descontento", "malo");
```

## FeedbackFourStream.java

- Ahora vamos a contar las palabras 

Creamos la clase `broker.stream.feedback.FeedbackFourStream.java`

```java
@Component
public class FeedbackFourStream {

    private static final Set<String> GOOD_WORDS = java.util.Set.of("contento", "bueno", "amable");
    private static final Set<String> BAD_WORDS = Set.of("enfadado", "descontento", "malo");

    @Autowired
    void kstreamFeedback(StreamsBuilder builder) {
        var stringSerde = Serdes.String();
        var feedbackSerde = new JsonSerde<>(FeedbackMessage.class);

        builder.stream("t-commodity-feedback", Consumed.with(stringSerde, feedbackSerde))
                .flatMap(splitWords())
                .split()
                .branch(isGoodWord(), Branched.withConsumer(
                        ks -> {
                            ks.to("t-commodity-feedback-four-good");
                            ks.groupByKey().count().toStream().to("t-commodity-feedback-four-good-count");
                        }))
                .branch(isBadWord(), Branched.withConsumer(
                        ks -> {
                            ks.to("t-commodity-feedback-four-bad");
                            ks.groupByKey().count().toStream().to("t-commodity-feedback-four-bad-count");
                        }));
    }

    private Predicate<String, String> isBadWord() {
        return (key, value) -> BAD_WORDS.contains(value);
    }

    private Predicate<String, String> isGoodWord() {
        return (key, value) -> GOOD_WORDS.contains(value);
    }

    private KeyValueMapper<String, FeedbackMessage, Iterable<KeyValue<String, String>>> splitWords() {
        return (key, value) -> Arrays
                .asList(value.getFeedback().replaceAll("[^a-zA-Z]", " ")
                        .toLowerCase().split("\\s+"))
                .stream()
                .distinct()
                .map(word -> KeyValue.pair(value.getLocation(), word)).collect(Collectors.toList());
    }

}
```

## FeedbackFiveStream.java

- Ahora vamos a contar las palabras usando una nomenclatura distinta

Creamos la clase `broker.stream.feedback.FeedbackFiveStream.java`

```java
@Component
public class FeedbackFiveStream {

    private static final Set<String> GOOD_WORDS = java.util.Set.of("contento", "bueno", "amable");
    private static final Set<String> BAD_WORDS = Set.of("enfadado", "descontento", "malo");

    @Autowired
    void kstreamFeedback(StreamsBuilder builder) {
        var stringSerde = Serdes.String();
        var feedbackSerde = new JsonSerde<>(FeedbackMessage.class);

        builder.stream("t-commodity-feedback", Consumed.with(stringSerde, feedbackSerde))
                .flatMap(splitWords())
                .split()
                .branch(isGoodWord(),
                        Branched.withConsumer(
                                ks -> ks.repartition(Repartitioned.as("t-commodity-feedback-five-good"))
                                        .groupByKey().count().toStream().to("t-commodity-feedback-five-good-count")))
                .branch(isBadWord(),
                        Branched.withConsumer(
                                ks -> ks.repartition(Repartitioned.as("t-commodity-feedback-five-bad"))
                                        .groupByKey().count().toStream().to("t-commodity-feedback-five-bad-count")));
    }

    private Predicate<String, String> isBadWord() {
        return (key, value) -> BAD_WORDS.contains(value);
    }

    private Predicate<String, String> isGoodWord() {
        return (key, value) -> GOOD_WORDS.contains(value);
    }

    private KeyValueMapper<String, FeedbackMessage, Iterable<KeyValue<String, String>>> splitWords() {
        return (key, value) -> Arrays
                .asList(value.getFeedback().replaceAll("[^a-zA-Z]", " ")
                        .toLowerCase().split("\\s+"))
                .stream()
                .distinct()
                .map(word -> KeyValue.pair(value.getLocation(), word)).collect(Collectors.toList());
    }

}
```

## FeedbackSixStream.java

- Ahora vamos a contar el total de palabras buenas y malas para la empresa

Creamos la clase `broker.stream.feedback.FeedbackSixStream.java`

```java
@Component
public class FeedbackSixStream {

    private static final Set<String> GOOD_WORDS = java.util.Set.of("contento", "bueno", "amable");
    private static final Set<String> BAD_WORDS = Set.of("enfadado", "descontento", "malo");

    @Autowired
    void kstreamFeedback(StreamsBuilder builder) {
        var stringSerde = Serdes.String();
        var feedbackSerde = new JsonSerde<>(FeedbackMessage.class);

        builder.stream("t-commodity-feedback", Consumed.with(stringSerde, feedbackSerde))
                .flatMap(splitWords())
                .split()
                .branch(isGoodWord(),
                        Branched.withConsumer(
                                ks -> {
                                    ks.repartition(Repartitioned.as("t-commodity-feedback-six-good"))
                                            .groupByKey().count().toStream().to("t-commodity-feedback-six-good-count");
                                    ks.groupBy(
                                            (key, value) -> value).count().toStream()
                                            .to("t-commodity-feedback-six-good-count-word");
                                }))
                .branch(isBadWord(),
                        Branched.withConsumer(
                                ks -> {
                                    ks.repartition(Repartitioned.as("t-commodity-feedback-six-bad"))
                                            .groupByKey().count().toStream().to("t-commodity-feedback-six-bad-count");
                                    ks.groupBy(
                                            (key, value) -> value).count().toStream()
                                            .to("t-commodity-feedback-six-bad-count-word");
                                }));
    }

    private Predicate<String, String> isBadWord() {
        return (key, value) -> BAD_WORDS.contains(value);
    }

    private Predicate<String, String> isGoodWord() {
        return (key, value) -> GOOD_WORDS.contains(value);
    }

    private KeyValueMapper<String, FeedbackMessage, Iterable<KeyValue<String, String>>> splitWords() {
        return (key, value) -> Arrays
                .asList(value.getFeedback().replaceAll("[^a-zA-Z]", " ")
                        .toLowerCase().split("\\s+"))
                .stream()
                .distinct()
                .map(word -> KeyValue.pair(value.getLocation(), word)).collect(Collectors.toList());
    }

}
```