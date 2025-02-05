package com.imagina.kafka.broker.stream.feedback.rating;

import com.imagina.kafka.broker.message.FeedbackDetailedRatingMessage;
import com.imagina.kafka.broker.message.FeedbackMessage;
import com.imagina.kafka.broker.message.FeedbackRatingMessage;
import org.apache.kafka.streams.processor.api.FixedKeyProcessor;
import org.apache.kafka.streams.processor.api.FixedKeyProcessorContext;
import org.apache.kafka.streams.processor.api.FixedKeyRecord;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class FeedbackRatingFixedKeyProcessor implements FixedKeyProcessor<String, FeedbackMessage, FeedbackDetailedRatingMessage> {

    private FixedKeyProcessorContext<String, FeedbackDetailedRatingMessage> context;

    private KeyValueStore<String, FeedbackDetailedStoreValue> store;

    private final String stateStoreName;

    public FeedbackRatingFixedKeyProcessor(String stateStoreName) {
        if(StringUtils.isEmpty(stateStoreName)) {
            throw new IllegalArgumentException("stateStoreName cannot be empty");
        }
        this.stateStoreName = stateStoreName;
    }

    @Override
    public void init(FixedKeyProcessorContext<String, FeedbackDetailedRatingMessage> context) {
        this.context = context;
        this.store = this.context.getStateStore(stateStoreName);
    }

    @Override
    public void process(FixedKeyRecord<String, FeedbackMessage> fixedKeyRecord) {
        var originalValue = fixedKeyRecord.value();
        var storeValue = Optional.ofNullable(store.get(originalValue.getLocation()))
                .orElse(new FeedbackDetailedStoreValue());
        var ratingMap = Optional.ofNullable(storeValue.getRatingMap()).orElse(new TreeMap<Integer, Long>());
        var currentRatingCount = Optional.ofNullable(ratingMap.get(originalValue.getRating()))
                .orElse(Long.valueOf(0));

        var newRatingCount = currentRatingCount + 1;
        ratingMap.put(originalValue.getRating(), newRatingCount);


        store.put(originalValue.getLocation(), storeValue);

        var branchRating = new FeedbackDetailedRatingMessage();

        branchRating.setLocation(originalValue.getLocation());
        branchRating.setRatingMap(ratingMap);
        branchRating.setAverageRating(calculateAverageRating(ratingMap));

        context.forward(fixedKeyRecord.withValue(branchRating));
    }

    private double calculateAverageRating(Map<Integer, Long> ratingMap) {
        var totalRating = ratingMap.entrySet().stream().mapToDouble(entry -> entry.getKey() * entry.getValue()).sum();
        var totalFeedback = ratingMap.values().stream().mapToLong(Long::longValue).sum();

        return totalFeedback == 0 ? 0 : Math.round(totalRating / totalFeedback * 10.0) / 10.0;
    }
}
