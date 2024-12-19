package com.imagina.core_producer.service;

import com.imagina.core_producer.model.FileKafka;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class FileKafkaService {

    private static AtomicInteger counter = new AtomicInteger();

    public FileKafka generateFile(String extension) {
        var name = "file-" + counter.incrementAndGet();
        var size = ThreadLocalRandom.current().nextLong(100, 10_000);

        return new FileKafka(name, size, extension);
    }
}