package com.imagina.kafka.broker.stream.feedback.rating;

import com.imagina.kafka.broker.message.FeedbackMessage;
import com.imagina.kafka.broker.message.FeedbackRatingMessage;
import org.apache.kafka.streams.processor.api.FixedKeyProcessor;
import org.apache.kafka.streams.processor.api.FixedKeyProcessorContext;
import org.apache.kafka.streams.processor.api.FixedKeyRecord;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.util.StringUtils;

import java.util.Optional;

public class FeedbackRatingFixedKeyProcessor implements FixedKeyProcessor<String, FeedbackMessage, FeedbackRatingMessage> {

    private FixedKeyProcessorContext<String, FeedbackRatingMessage> context;

    private KeyValueStore<String, FeedbackRatingStoreValue> store;

    private final String stateStoreName;

    public FeedbackRatingFixedKeyProcessor(String stateStoreName) {
        if(StringUtils.isEmpty(stateStoreName)) {
            throw new IllegalArgumentException("stateStoreName cannot be empty");
        }
        this.stateStoreName = stateStoreName;
    }

    @Override
    public void init(FixedKeyProcessorContext<String, FeedbackRatingMessage> context) {
        this.context = context;
        this.store = this.context.getStateStore(stateStoreName);
    }

    @Override
    public void process(FixedKeyRecord<String, FeedbackMessage> fixedKeyRecord) {
        var originalValue = fixedKeyRecord.value();
        var storeValue = Optional.ofNullable(store.get(originalValue.getLocation()))
                .orElse(new FeedbackRatingStoreValue());
        var newSumRating = storeValue.getSumRating() + originalValue.getRating();
        var newCountRating = storeValue.getCountRating() + 1;

        storeValue.setSumRating(newSumRating);
        storeValue.setCountRating(newCountRating);

        store.put(originalValue.getLocation(), storeValue);

        var returnValue = new FeedbackRatingMessage();
        returnValue.setLocation(originalValue.getLocation());

        double averageRating = Math.round((double) newSumRating / newCountRating * 10.0) / 10.0;

        returnValue.setAverageRating(averageRating);

        context.forward(fixedKeyRecord.withValue(returnValue));
    }
}
