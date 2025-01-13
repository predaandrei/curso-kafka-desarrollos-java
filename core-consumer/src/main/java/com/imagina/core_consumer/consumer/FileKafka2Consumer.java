package com.imagina.core_consumer.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imagina.core_consumer.model.FileKafka;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FileKafka2Consumer {

    @Autowired
    private ObjectMapper objectMapper;

    @RetryableTopic(
            autoCreateTopics = "true",
            attempts = "4",
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE,
            backoff = @Backoff(
                    delay = 3000,
                    maxDelay = 10000,
                    multiplier = 1.5,
                    random = true
            ),
            dltTopicSuffix = "-dead"
    )
    @KafkaListener(topics = "t-file-2", concurrency = "2")
    public void consume(ConsumerRecord<String, String> consumerRecord) throws JsonProcessingException {
        var file = objectMapper.readValue(consumerRecord.value(), FileKafka.class);

        if (file.getExtension().equalsIgnoreCase("xslt")) {
            log.warn("Throwing exception on partition {} for file {}", consumerRecord.partition(), file);
            throw new IllegalArgumentException("Simulate API call failed");
        }

        log.info("Processing on partition {} for file {}", consumerRecord.partition(), file);
    }
}