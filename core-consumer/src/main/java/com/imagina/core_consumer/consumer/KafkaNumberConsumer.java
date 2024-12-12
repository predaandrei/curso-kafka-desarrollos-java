package com.imagina.core_consumer.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

//@Service
@Slf4j
public class KafkaNumberConsumer {

    @KafkaListener(topics = "t-numbers")
    public void consume(String number) throws InterruptedException {
        if ( Integer.valueOf(number) % 2 == 0 )
            log.info("number {} es  PAR", number);
        else
            log.info("number {} es  IMPAR", number);

        TimeUnit.SECONDS.sleep(1);
    }
}
