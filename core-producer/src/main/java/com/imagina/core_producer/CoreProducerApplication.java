package com.imagina.core_producer;

<<<<<<< HEAD
import com.imagina.core_producer.model.Empleado;
<<<<<<< HEAD
=======
import com.imagina.core_producer.producer.ContadorProducer;
>>>>>>> a09dc005118e511cf2c2358a8575f9f9500c7bae
import com.imagina.core_producer.producer.EmpleadoProducer;
import com.imagina.core_producer.producer.HolaMundoKafkaProducer;
import com.imagina.core_producer.producer.KafkaKeyProducer;
import com.imagina.core_producer.producer.KafkaNumberProducer;
=======
import com.imagina.core_producer.model.*;
import com.imagina.core_producer.producer.*;
import com.imagina.core_producer.service.FacturaService;
import com.imagina.core_producer.service.FileKafkaService;
import lombok.extern.slf4j.Slf4j;
>>>>>>> b78ec5fd80814d2ffe832fa028c69ebc9da60728
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
<<<<<<< HEAD
@EnableScheduling
=======
@Slf4j
//@EnableScheduling
>>>>>>> b78ec5fd80814d2ffe832fa028c69ebc9da60728
public class CoreProducerApplication implements CommandLineRunner {

	//@Autowired
	//private final HolaMundoKafkaProducer holaMundoKafkaProducer;

	//@Autowired
	//private KafkaKeyProducer kafkaKeyProducer;

	//@Autowired
<<<<<<< HEAD
	//private KafkaNumberProducer kafkaNumberProducer;

	@Autowired
	private EmpleadoProducer empleadoProducer;
=======
	//private EmpleadoProducer empleadoProducer;

	//@Autowired
	//private ContadorProducer producer;

	//@Autowired
	//private SolicitudCompraProducer solicitudCompraProducer;

	//@Autowired
	//private OrdenProductoProducer ordenProductoProducer;

	//@Autowired
	//private NumeroSimpleProducer numeroSimpleProducer;

	@Autowired
<<<<<<< HEAD
	private ContadorProducer producer;
>>>>>>> a09dc005118e511cf2c2358a8575f9f9500c7bae
=======
	private FileKafkaService fileKafkaService;

	@Autowired
	private FileKafka2Producer fileKafkaProducer;

	//@Autowired
	//private FacturaService facturaService;

	//@Autowired
	//private FacturaProducer facturaProducer;
>>>>>>> b78ec5fd80814d2ffe832fa028c69ebc9da60728

	public static void main(String[] args) {
		SpringApplication.run(CoreProducerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
<<<<<<< HEAD
			//kafkaNumberProducer.sendMessage();

		for (int i = 0; i<5; i++){
			Empleado empleado = new Empleado(UUID.randomUUID().toString(), "Andrei");
			empleadoProducer.sendEmpleado(empleado);
		}
=======
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

		/*
		OrdenProducto zapatillas = new OrdenProducto(3, "zapatillas");
		OrdenProducto colonia = new OrdenProducto(10, "colonia");
		OrdenProducto camisetas = new OrdenProducto(5, "camisetas");

		ordenProductoProducer.send(zapatillas);
		ordenProductoProducer.send(colonia);
		ordenProductoProducer.send(camisetas);

		for(int i = 100; i < 103; i++) {
			var numeroSimple = new NumeroSimple(i);
			log.info("Sending numero simple" + numeroSimple.getNumero());
			numeroSimpleProducer.send(numeroSimple);
		}*/

		FileKafka file1 = fileKafkaService.generateFile("pdf");
		FileKafka file2 = fileKafkaService.generateFile("xslt");
		FileKafka file3 = fileKafkaService.generateFile("xml");
		FileKafka file4 = fileKafkaService.generateFile("pdf");
		FileKafka file5 = fileKafkaService.generateFile("csv");
		FileKafka file6 = fileKafkaService.generateFile("txt");

		fileKafkaProducer.sendFileToPartition(file1, 0);
		fileKafkaProducer.sendFileToPartition(file2, 0);
		fileKafkaProducer.sendFileToPartition(file3, 0);

		fileKafkaProducer.sendFileToPartition(file4, 1);
		fileKafkaProducer.sendFileToPartition(file5, 1);
		fileKafkaProducer.sendFileToPartition(file6, 1);

		/*for(int i = 0; i < 10; i++) {
			var factura = facturaService.generaFactura();

			if(i > 5) {
				factura.setCantidad(0d);
			}

			facturaProducer.sendFactura(factura);
		}*/

>>>>>>> a09dc005118e511cf2c2358a8575f9f9500c7bae
	}
}
