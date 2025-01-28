package com.imagina.kafka.api.command.action;

import java.time.OffsetDateTime;

import com.imagina.kafka.api.request.ClientInfoShoppingCartRequest;
import com.imagina.kafka.api.request.ClientInfoWishlistRequest;
import com.imagina.kafka.broker.message.ClientInfoShoppingCartMessage;
import com.imagina.kafka.broker.message.ClientInfoWishlistMessage;
import com.imagina.kafka.broker.producer.ClientInfoProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClientInfoAction {

	@Autowired
	private ClientInfoProducer producer;

	public void publishShoppingCart(ClientInfoShoppingCartRequest request) {
		var message = new ClientInfoShoppingCartMessage(request.getClientId(), request.getItemName(),
				request.getCartAmount(), OffsetDateTime.now());

		producer.publishShoppingCart(message);
	}

	public void publishWishlist(ClientInfoWishlistRequest request) {
		var message = new ClientInfoWishlistMessage(request.getClientId(), request.getItemName(),
				OffsetDateTime.now());

		producer.publishWishlist(message);
	}

}
