package com.imagina.kafka.broker.producer;

import com.imagina.kafka.broker.message.DiscountMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class DiscountProducer {

	@Autowired
	private KafkaTemplate<String, DiscountMessage> kafkaTemplate;

	public void publish(DiscountMessage message) {

		kafkaTemplate.send("t-commodity-promotion", message);
	}

}
