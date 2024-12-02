package com.talan.kafka_core_producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import producer.HolaMundoKafkaProducer;


@SpringBootApplication
@ComponentScan(basePackages = {"com.talan.kafka_core_producer", "producer"})
public class KafkaCoreProducerApplication implements CommandLineRunner{

	HolaMundoKafkaProducer service;

	public static void main(String[] args) {
		SpringApplication.run(KafkaCoreProducerApplication.class, args);
	}

	// Inyección de dependencia mediante el constructor
	@Autowired
	public KafkaCoreProducerApplication(HolaMundoKafkaProducer service) {
		this.service = service;
	}

	@Override
	public void run(String... args) throws Exception {
		this.service.sendHolaMundo("Andrei");
	}
}
