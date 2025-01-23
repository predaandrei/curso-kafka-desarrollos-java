package com.imagina.kafka.broker.consumer;

import java.util.Objects;

import com.imagina.kafka.broker.message.OrderMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderConsumer {

    @KafkaListener(topics = "t-commodity-order")
    public void consumeOrder(ConsumerRecord<String, OrderMessage> consumerRecord) {
        var headers = consumerRecord.headers();
        var orderMessage = consumerRecord.value();

        log.info("Kafka headers:");

        headers.forEach(header -> log.info("header {} : {}", header.key(), new String(header.value())));

        log.info("Order: {}", orderMessage);

        var bonusPercentage = Objects.isNull(headers.lastHeader("surpriseBonus")) ? 0
                : Integer.parseInt(new String(headers.lastHeader("surpriseBonus").value()));

        log.info("Surprise bonus is {}%", bonusPercentage);
    }

}