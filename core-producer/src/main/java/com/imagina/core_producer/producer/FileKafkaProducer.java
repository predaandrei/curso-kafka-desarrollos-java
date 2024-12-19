package com.imagina.core_producer.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imagina.core_producer.model.FileKafka;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

//@Service
@Slf4j
public class FileKafkaProducer {

    //@Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    //@Autowired
    private ObjectMapper objectMapper;

    public void sendFileToPartition(FileKafka fileKafka, int partition) throws JsonProcessingException {
        var json = objectMapper.writeValueAsString(fileKafka);
        log.info("Sending file {}", fileKafka);
        kafkaTemplate.send("t-file", partition, fileKafka.getExtension(), json);
    }

}
