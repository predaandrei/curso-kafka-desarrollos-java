package com.imagina.kafka.broker.producer;

import com.imagina.kafka.broker.message.ClientInfoShoppingCartMessage;
import com.imagina.kafka.broker.message.ClientInfoWishlistMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ClientInfoProducer {

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	public void publishShoppingCart(ClientInfoShoppingCartMessage message) {
		kafkaTemplate.send("t-client-info-shopping-cart", message.getClientId(), message);
	}

	public void publishWishlist(ClientInfoWishlistMessage message) {
		kafkaTemplate.send("t-client-info-wishlist", message.getClientId(), message);
	}

}
