## Average Rating
- Para calcular la evaluación media de una sucursal lo haremos sumando todos los ratings y dividiendo por el número de ratings
- Para esto, necesitamos el processorAPI y la state store

### FeedbackRatingMessage.java

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackRatingMessage {

    private String location;

    private double averageRating;

    @Override
    public String toString() {
        return "FeedbackRatingMessage{" +
                "location='" + location + '\'' +
                ", averageRating=" + averageRating +
                '}';
    }

}

```

### FeedbackRatingStoreValue.java

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackRatingStoreValue {

    private long countRating;

    private long sumRating;

    @Override
    public String toString() {
        return "FeedbackRatingOneStoreValue{" +
                "countRating=" + countRating +
                ", sumRating=" + sumRating +
                '}';
    }

}
```

### FeedbackRatingFixedKeyProcessor.java

```java
public class FeedbackRatingFixedKeyProcessor
        implements FixedKeyProcessor<String, FeedbackMessage, FeedbackRatingMessage> {

    private FixedKeyProcessorContext<String, FeedbackRatingMessage> processorContext;

    private final String stateStoreName;

    private KeyValueStore<String, FeedbackRatingOneStoreValue> ratingStateStore;

    public FeedbackRatingFixedKeyProcessor(String stateStoreName) {
        if (StringUtils.isEmpty(stateStoreName)) {
            throw new IllegalArgumentException("State store name must not be empty");
        }

        this.stateStoreName = stateStoreName;
    }

    @Override
    public void init(FixedKeyProcessorContext<String, FeedbackRatingMessage> context) {
        this.processorContext = context;
        this.ratingStateStore = this.processorContext.getStateStore(stateStoreName);
    }

    @Override
    public void process(FixedKeyRecord<String, FeedbackMessage> record) {
        var originalValue = record.value();
        var storeValue = Optional.ofNullable(ratingStateStore.get(originalValue.getLocation()))
                .orElse(new FeedbackRatingStoreValue());
        var newSumRating = storeValue.getSumRating() + originalValue.getRating();
        var newCountRating = storeValue.getCountRating() + 1;

        storeValue.setSumRating(newSumRating);
        storeValue.setCountRating(newCountRating);

        ratingStateStore.put(originalValue.getLocation(), storeValue);

        // new value to be forwarded
        var branchRating = new FeedbackRatingMessage();

        branchRating.setLocation(originalValue.getLocation());

        double averageRating = Math.round((double) newSumRating / newCountRating * 10.0) / 10.0;

        branchRating.setAverageRating(averageRating);

        processorContext.forward(record.withValue(branchRating));
    }

}
```

### FeedbackRatingStream.java

```java
@Component
public class FeedbackRatingStream {

    @Autowired
    void kstreamFeedbackRating(StreamsBuilder builder) {
        var stringSerde = Serdes.String();
        var feedbackSerde = new JsonSerde<>(FeedbackMessage.class);
        var feedbackRatingSerde = new JsonSerde<>(FeedbackRatingMessage.class);
        var feedbackRatingStoreValueSerde = new JsonSerde<>(FeedbackRatingStoreValue.class);
        var feedbackRatingStateStoreName = "feedbackRatingStateStore";
        var storeSupplier = Stores.inMemoryKeyValueStore(feedbackRatingStateStoreName);
        var storeBuilder = Stores.keyValueStoreBuilder(storeSupplier, stringSerde, feedbackRatingStoreValueSerde);

        builder.addStateStore(storeBuilder);

        builder.stream("t-commodity-feedback", Consumed.with(stringSerde, feedbackSerde))
                .processValues(
                        () -> new FeedbackRatingFixedKeyProcessor(feedbackRatingStateStoreName),
                        feedbackRatingStateStoreName)
                .to("t-commodity-feedback-rating", Produced.with(stringSerde, feedbackRatingSerde));
    }

}

```

## Detailed Rating

### FeedbackDetailedRatingMessage.java

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDetailedRatingMessage {

    private String location;

    private double averageRating;

    private Map<Integer, Long> ratingMap;

    @Override
    public String toString() {
        return "FeedbackDetailedRatingMessage{" +
                "location='" + location + '\'' +
                ", averageRating=" + averageRating +
                ", ratingMap=" + ratingMap +
                '}';
    }

}

```

### FeedbackDetailedStoreValue.java

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDetailedStoreValue {

    private Map<Integer, Long> ratingMap = new TreeMap<>();

    @Override
    public String toString() {
        return "FeedbackDetailedStoreValue{" +
                "ratingMap=" + ratingMap +
                '}';
    }

}
```

### FeedbackDetailedFixedKeyProcessor.java

```java
public class FeedbackDetailedFixedKeyProcessor
        implements FixedKeyProcessor<String, FeedbackMessage, FeedbackDetailedRatingMessage> {

    private FixedKeyProcessorContext<String, FeedbackDetailedRatingMessage> processorContext;

    private final String stateStoreName;

    private KeyValueStore<String, FeedbackDetailedStoreValue> ratingStateStore;

    public FeedbackDetailedFixedKeyProcessor(String stateStoreName) {
        if (StringUtils.isBlank(stateStoreName)) {
            throw new IllegalArgumentException("State store name must not be blank");
        }

        this.stateStoreName = stateStoreName;
    }

    @Override
    public void init(FixedKeyProcessorContext<String, FeedbackDetailedRatingMessage> context) {
        this.processorContext = context;
        this.ratingStateStore = this.processorContext.getStateStore(stateStoreName);
    }

    @Override
    public void process(FixedKeyRecord<String, FeedbackMessage> record) {
        var originalValue = record.value();
        var storeValue = Optional.ofNullable(ratingStateStore.get(originalValue.getLocation()))
                .orElse(new FeedbackDetailedStoreValue());
        var ratingMap = Optional.ofNullable(storeValue.getRatingMap()).orElse(new TreeMap<Integer, Long>());
        var currentRatingCount = Optional.ofNullable(ratingMap.get(originalValue.getRating()))
                .orElse(Long.valueOf(0));
        var newRatingCount = currentRatingCount + 1;

        ratingMap.put(originalValue.getRating(), newRatingCount);

        ratingStateStore.put(originalValue.getLocation(), storeValue);

        var branchRating = new FeedbackDetailedRatingMessage();

        branchRating.setLocation(originalValue.getLocation());
        branchRating.setRatingMap(ratingMap);
        branchRating.setAverageRating(calculateAverageRating(ratingMap));

        processorContext.forward(record.withValue(branchRating));
    }

    private double calculateAverageRating(Map<Integer, Long> ratingMap) {
        var totalRating = ratingMap.entrySet().stream().mapToDouble(entry -> entry.getKey() * entry.getValue()).sum();
        var totalFeedback = ratingMap.values().stream().mapToLong(Long::longValue).sum();

        return totalFeedback == 0 ? 0 : Math.round(totalRating / totalFeedback * 10.0) / 10.0;
    }

}

```

### FeedbackDetailedRatingStream.java

```java
@Component
public class FeedbackDetailedRatingStream {

    @Autowired
    void kstreamFeedbackDetailedRating(StreamsBuilder builder) {
        var stringSerde = Serdes.String();
        var feedbackSerde = new JsonSerde<>(FeedbackMessage.class);
        var feedbackDetailedRatingSerde = new JsonSerde<>(FeedbackDetailedRatingMessage.class);
        var feedbackDetailedRatingStoreValueSerde = new JsonSerde<>(FeedbackDetailedStoreValue.class);
        var feedbackDetailedRatingStateStoreName = "feedbackRatingTwoStateStore";
        var storeSupplier = Stores.inMemoryKeyValueStore(feedbackRatingStateStoreName);
        var storeBuilder = Stores.keyValueStoreBuilder(storeSupplier, stringSerde, feedbackDetailedRatingStoreValueSerde);

        builder.addStateStore(storeBuilder);

        builder.stream("t-commodity-feedback", Consumed.with(stringSerde, feedbackSerde))
                .processValues(
                        () -> new FeedbackDetailedFixedKeyProcessor(feedbackRatingStateStoreName),
                        feedbackRatingStateStoreName)
                .to("t-commodity-feedback-rating-two", Produced.with(stringSerde, feedbackDetailedRatingSerde));
    }

}

```