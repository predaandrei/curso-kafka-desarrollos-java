package com.imagina.kafka.broker.stream.inventory;

import com.imagina.kafka.broker.message.InventoryMessage;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;

@Component
public class InventoryFiveStream {

    /*@Autowired
    void kstreamInventoryFive(StreamsBuilder builder) {
        var inventorySerde = new JsonSerde<>(InventoryMessage.class);
        var inventoryTimestampExtractor = new InventoryTimestampExtractor();
        var windowLenght = Duration.ofHours(1L);
        var windowSerde = WindowedSerdes.timeWindowedSerdeFrom(String.class, windowLenght.toMillis());

        builder.stream("t-commodity-inventory", Consumed.with(Serdes.String(), inventorySerde, inventoryTimestampExtractor, null))
                .mapValues((item, inventory) -> inventory.getType().equalsIgnoreCase("ADD") ?
                        inventory.getQuantity() : -1 * inventory.getQuantity())
                .groupByKey()
                .windowedBy(TimeWindows.ofSizeWithNoGrace(windowLenght))
                .reduce(Long::sum, Materialized.with(Serdes.String(), Serdes.Long())
                ).toStream()
                .peek((key, value) -> {
                    var windowStartTime = Instant.ofEpochMilli(key.window().start()).atOffset(ZoneOffset.UTC);
                    var windowEndTime = Instant.ofEpochMilli(key.window().end()).atOffset(ZoneOffset.UTC);
                    System.out.println("[" + key.key() + "@" + windowStartTime + "/" + windowEndTime + "], " + value);
                })
                .to("t-commodity-inventory-total-one", Produced.with(windowSerde, Serdes.Long()));

    }*/

    /*@Autowired
    void kstreamInventoryFive(StreamsBuilder builder) {
        var inventorySerde = new JsonSerde<>(InventoryMessage.class);
        var inventoryTimestampExtractor = new InventoryTimestampExtractor();
        var windowLength = Duration.ofHours(1);
        var windowSerde = WindowedSerdes.timeWindowedSerdeFrom(String.class, windowLength.toMillis());
        var hopLength = Duration.ofMinutes(20);

        builder.stream("t-commodity-inventory", Consumed.with(Serdes.String(), inventorySerde, inventoryTimestampExtractor, null))
                .mapValues((item, inventory) -> inventory.getType().equalsIgnoreCase("ADD") ?
                        inventory.getQuantity() : -1 * inventory.getQuantity())
                .groupByKey()
                .windowedBy(TimeWindows.ofSizeWithNoGrace(windowLength).advanceBy(hopLength))
                .reduce(Long::sum, Materialized.with(Serdes.String(), Serdes.Long())
                ).toStream()
                .peek((key, value) -> {
                    var windowStartTime = Instant.ofEpochMilli(key.window().start()).atOffset(ZoneOffset.UTC);
                    var windowEndTime = Instant.ofEpochMilli(key.window().end()).atOffset(ZoneOffset.UTC);
                    System.out.println("[" + key.key() + "@" + windowStartTime + "/" + windowEndTime + "], " + value);
                })
                .to("t-commodity-inventory-total-one", Produced.with(windowSerde, Serdes.Long()));

    }*/

    @Autowired
    void kstreamInventoryFive(StreamsBuilder builder) {
        var inventorySerde = new JsonSerde<>(InventoryMessage.class);
        var inventoryTimestampExtractor = new InventoryTimestampExtractor();
        var windowLength = Duration.ofMinutes(30L);
        var windowSerde = WindowedSerdes.timeWindowedSerdeFrom(String.class, windowLength.toMillis());

        builder.stream("t-commodity-inventory", Consumed.with(Serdes.String(), inventorySerde, inventoryTimestampExtractor, null))
                .mapValues((item, inventory) -> inventory.getType().equalsIgnoreCase("ADD") ?
                        inventory.getQuantity() : -1 * inventory.getQuantity())
                .groupByKey()
                .windowedBy(SessionWindows.ofInactivityGapWithNoGrace(windowLength))
                .reduce(Long::sum, Materialized.with(Serdes.String(), Serdes.Long())
                ).toStream()
                .peek((key, value) -> {
                    var windowStartTime = Instant.ofEpochMilli(key.window().start()).atOffset(ZoneOffset.UTC);
                    var windowEndTime = Instant.ofEpochMilli(key.window().end()).atOffset(ZoneOffset.UTC);
                    System.out.println("[" + key.key() + "@" + windowStartTime + "/" + windowEndTime + "], " + value);
                })
                .to("t-commodity-inventory-total-one", Produced.with(windowSerde, Serdes.Long()));

    }



}
