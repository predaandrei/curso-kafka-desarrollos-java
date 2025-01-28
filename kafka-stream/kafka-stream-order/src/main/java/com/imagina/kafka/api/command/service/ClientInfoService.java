package com.imagina.kafka.api.command.service;

import com.imagina.kafka.api.command.action.ClientInfoAction;
import com.imagina.kafka.api.request.ClientInfoShoppingCartRequest;
import com.imagina.kafka.api.request.ClientInfoWishlistRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientInfoService {

	@Autowired
	private ClientInfoAction action;

	public void createShoppingCart(ClientInfoShoppingCartRequest request) {
		action.publishShoppingCart(request);
	}

	public void createWishlist(ClientInfoWishlistRequest request) {
		action.publishWishlist(request);
	}
}
