package com.imagina.kafka.broker.stream.inventory;

import com.imagina.kafka.broker.message.InventoryMessage;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;

public class InventoryTimestampExtractor implements TimestampExtractor {


    @Override
    public long extract(ConsumerRecord<Object, Object> record, long partitionTime) {
        var inventoryMessage = (InventoryMessage) record.value();

        return inventoryMessage != null ? inventoryMessage.getTransactionTime().toInstant().toEpochMilli() :
                record.timestamp();
    }
}
