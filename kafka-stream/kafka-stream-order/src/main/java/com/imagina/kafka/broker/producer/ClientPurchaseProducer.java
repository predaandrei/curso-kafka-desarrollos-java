package com.imagina.kafka.broker.producer;

import com.imagina.kafka.broker.message.ClientPurchaseAppMessage;
import com.imagina.kafka.broker.message.ClientPurchaseWebMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class ClientPurchaseProducer {

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	public void publishPurchaseApp(ClientPurchaseAppMessage message) {
		kafkaTemplate.send("t-client-purchase-app", message.getPurchaseNumber(), message);
	}

	public void publishPurchaseWeb(ClientPurchaseWebMessage message) {
		kafkaTemplate.send("t-client-purchase-web", message.getPurchaseNumber(), message);
	}

}
