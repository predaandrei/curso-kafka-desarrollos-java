package com.imagina.core_consumer.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imagina.core_consumer.model.FileKafka;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

//@Service
@Slf4j
public class FileKafkaConsumer {

    //@Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "t-file", containerFactory = "fileRetryContainerFactory", concurrency = "2")
    public void consume(ConsumerRecord<String, String> consumerRecord) throws JsonMappingException, JsonProcessingException {
        var file = objectMapper.readValue(consumerRecord.value(), FileKafka.class);

        if (file.getExtension().equalsIgnoreCase("xslt")) {
            log.warn("Throwing exception on partition {} for file {}", consumerRecord.partition(), file);
            throw new IllegalArgumentException("Simulate API call failed");
        }

        log.info("Processing on partition {} for file {}", consumerRecord.partition(), file);
    }
}