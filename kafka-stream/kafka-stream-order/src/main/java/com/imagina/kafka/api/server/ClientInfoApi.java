package com.imagina.kafka.api.server;

import com.imagina.kafka.api.command.service.ClientInfoService;
import com.imagina.kafka.api.request.ClientInfoShoppingCartRequest;
import com.imagina.kafka.api.request.ClientInfoWishlistRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client/info")
public class ClientInfoApi {

	@Autowired
	private ClientInfoService service;

	@PostMapping(value = "/shopping-cart", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> createShoppingCart(@RequestBody ClientInfoShoppingCartRequest request) {
		service.createShoppingCart(request);
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body("Added shopping cart " + request.getItemName() + " for customer " + request.getClientId());
	}

	@PostMapping(value = "/wishlist", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> createWishlist(@RequestBody ClientInfoWishlistRequest request) {
		service.createWishlist(request);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body("Added wishlist " + request.getItemName() + " for customer " + request.getClientId());
	}

}
