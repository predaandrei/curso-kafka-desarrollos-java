package com.imagina.kafka.broker.stream.ventarelampago;

import com.imagina.kafka.broker.message.VentaRelampagoMessage;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class VentaRelampagoVoteStream {

    @Autowired
    void ventarelampagoVote(StreamsBuilder builder) {
        var stringSerde = Serdes.String();
        var ventaRelampagoVoteSerde = new JsonSerde<>(VentaRelampagoMessage.class);

        var voteStart = OffsetDateTime.now().plusMinutes(2).toEpochSecond();
        var voteEnd = OffsetDateTime.now().plusHours(1).toEpochSecond();

        builder.stream("t-commodity-ventarelampago-vote", Consumed.with(stringSerde, ventaRelampagoVoteSerde))
                .processValues(() -> new VentaRelampagoVoteFixedKeyProcessor(voteStart, voteEnd))
        .filter((key, transformedValue) -> transformedValue != null)
                .map((key, value) -> KeyValue.pair(value.getCustomerId(), value.getItemName()))
                .to("t-commodity-ventarelampago-vote-user-item");

        builder.table("t-commodity-ventarelampago-vote-user-item", Consumed.with(stringSerde, stringSerde))
                .groupBy((user, votedItem) -> KeyValue.pair(votedItem, votedItem))
                .count()
                .toStream()
                .to("t-commodity-ventarelampago-vote-result");
    }

}
