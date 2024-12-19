package com.imagina.core_producer.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imagina.core_producer.model.FileKafka;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class FileKafka2Producer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void sendFileToPartition(FileKafka fileKafka, int partition) throws JsonProcessingException {
        var json = objectMapper.writeValueAsString(fileKafka);
        kafkaTemplate.send("t-file-2", partition, fileKafka.getExtension(), json);
    }

}