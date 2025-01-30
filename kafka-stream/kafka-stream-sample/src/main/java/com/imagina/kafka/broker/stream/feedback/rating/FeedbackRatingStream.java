package com.imagina.kafka.broker.stream.feedback.rating;

import com.imagina.kafka.broker.message.FeedbackMessage;
import com.imagina.kafka.broker.message.FeedbackRatingMessage;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.Stores;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

@Component
public class FeedbackRatingStream {

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
                .processValues(() -> new FeedbackRatingFixedKeyProcessor(feedbackRatingStateStoreName), feedbackRatingStateStoreName)
                .to("t-commodity-feedback-rating", Produced.with(stringSerde, feedbackRatingSerde));
    }
}
