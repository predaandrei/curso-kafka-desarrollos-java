package com.imagina.core_producer.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class KafkaNumberProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedRate = 1000)
    public void sendMessage() throws InterruptedException {
        int randomNumber = (int) (Math.random() * 15000) +1;
        kafkaTemplate.send("t-numbers", "" + randomNumber);
        TimeUnit.SECONDS.sleep(1);
    }
}
