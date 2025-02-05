package com.imagina.kafka.broker.stream.inventory;

import com.imagina.kafka.broker.message.InventoryMessage;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

@Component
public class InventoryOneStream {

    @Autowired
    void kstreamInventoryOne(StreamsBuilder builder) {
        var inventorySerde = new JsonSerde<>(InventoryMessage.class);


        builder.stream("t-commodity-inventory", Consumed.with(Serdes.String(), inventorySerde))
                .mapValues((item, inventory) -> inventory.getQuantity())
                .groupByKey()
                .aggregate(
                        () -> 0L,
                        (aggKey, newValue, aggValue) -> aggValue + newValue,
                        Materialized.with(Serdes.String(), Serdes.Long())
                ).toStream()
                .to("t-commodity-inventory-total-one", Produced.with(Serdes.String(), Serdes.Long()));

    }
}
