package com.imagina.core_producer;

import com.imagina.core_producer.model.Empleado;
import com.imagina.core_producer.producer.EmpleadoProducer;
import com.imagina.core_producer.producer.HolaMundoKafkaProducer;
import com.imagina.core_producer.producer.KafkaKeyProducer;
import com.imagina.core_producer.producer.KafkaNumberProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableScheduling
public class CoreProducerApplication implements CommandLineRunner {

	//@Autowired
	//private final HolaMundoKafkaProducer holaMundoKafkaProducer;

	//@Autowired
	//private KafkaKeyProducer kafkaKeyProducer;

	//@Autowired
	//private KafkaNumberProducer kafkaNumberProducer;

	@Autowired
	private EmpleadoProducer empleadoProducer;

	public static void main(String[] args) {
		SpringApplication.run(CoreProducerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
			//kafkaNumberProducer.sendMessage();

		for (int i = 0; i<5; i++){
			Empleado empleado = new Empleado(UUID.randomUUID().toString(), "Andrei");
			empleadoProducer.sendEmpleado(empleado);
		}
	}
}
