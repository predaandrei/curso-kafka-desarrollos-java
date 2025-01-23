package com.imagina.kafka.broker.stream.feedback;

import com.imagina.kafka.broker.message.FeedbackMessage;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonSerde;

// @Component
public class FeedbackOneStream {

    private static final java.util.Set<String> GOOD_WORDS = java.util.Set.of("contento", "bueno", "amable");

    // @Autowired
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
