package com.imagina.kafka.broker.stream.feedback.rating;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.Stores;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonSerde;

import com.course.kafka.broker.message.FeedbackMessage;
import com.course.kafka.broker.message.FeedbackRatingTwoMessage;

// @Component
public class FeedbackRatingTwoStream {

    @Autowired
    void kstreamFeedbackRating(StreamsBuilder builder) {
        var stringSerde = Serdes.String();
        var feedbackSerde = new JsonSerde<>(FeedbackMessage.class);
        var feedbackRatingTwoSerde = new JsonSerde<>(FeedbackRatingTwoMessage.class);
        var feedbackRatingTwoStoreValueSerde = new JsonSerde<>(FeedbackRatingTwoStoreValue.class);
        var feedbackRatingStateStoreName = "feedbackRatingTwoStateStore";
        var storeSupplier = Stores.inMemoryKeyValueStore(feedbackRatingStateStoreName);
        var storeBuilder = Stores.keyValueStoreBuilder(storeSupplier, stringSerde, feedbackRatingTwoStoreValueSerde);

        builder.addStateStore(storeBuilder);

        builder.stream("t-commodity-feedback", Consumed.with(stringSerde, feedbackSerde))
                .processValues(
                        () -> new FeedbackRatingTwoFixedKeyProcessor(feedbackRatingStateStoreName),
                        feedbackRatingStateStoreName)
                .to("t-commodity-feedback-rating-two", Produced.with(stringSerde, feedbackRatingTwoSerde));
    }

}
