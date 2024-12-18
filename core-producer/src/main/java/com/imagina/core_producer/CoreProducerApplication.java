package com.imagina.core_producer;

import com.imagina.core_producer.model.Empleado;
import com.imagina.core_producer.model.OrdenProducto;
import com.imagina.core_producer.model.SolicitudCompra;
import com.imagina.core_producer.producer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
//@EnableScheduling
public class CoreProducerApplication implements CommandLineRunner {

	//@Autowired
	//private final HolaMundoKafkaProducer holaMundoKafkaProducer;

	//@Autowired
	//private KafkaKeyProducer kafkaKeyProducer;

	//@Autowired
	//private EmpleadoProducer empleadoProducer;

	//@Autowired
	//private ContadorProducer producer;

	//@Autowired
	//private SolicitudCompraProducer solicitudCompraProducer;

	@Autowired
	private OrdenProductoProducer ordenProductoProducer;

	public static void main(String[] args) {
		SpringApplication.run(CoreProducerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//holaMundoKafkaProducer.sendHolaMundo("David " + ThreadLocalRandom.current().nextInt(1000));
		/*for(int i = 1; i <= 30; i++) {
			String key = "key-" + i;
			String message = "Message" + i;
			kafkaKeyProducer.sendMessage(key, message);

			TimeUnit.SECONDS.sleep(1);
		}*/

		/*for (int i = 0; i < 5; i++) {
			Empleado empleado = new Empleado(UUID.randomUUID().toString(), "Empleado" + i);
			empleadoProducer.sendEmpleado(empleado);
		}*/

		//producer.send(100);

		/*SolicitudCompra solicitud1 = new SolicitudCompra(UUID.randomUUID(), "SOL-001", 100, "EUR");
		SolicitudCompra solicitud2 = new SolicitudCompra(UUID.randomUUID(), "SOL-002", 200, "EUR");
		SolicitudCompra solicitud3 = new SolicitudCompra(UUID.randomUUID(), "SOL-003", 300, "USD");

		solicitudCompraProducer.send(solicitud1);
		solicitudCompraProducer.send(solicitud2);
		solicitudCompraProducer.send(solicitud3);

		solicitudCompraProducer.send(solicitud1);*/


		OrdenProducto zapatillas = new OrdenProducto(3, "zapatillas");
		OrdenProducto colonia = new OrdenProducto(10, "colonia");
		OrdenProducto camisetas = new OrdenProducto(5, "camisetas");

		ordenProductoProducer.send(zapatillas);
		ordenProductoProducer.send(colonia);
		ordenProductoProducer.send(camisetas);



	}
}
