package com.imagina.kafka.api.server;

import com.imagina.kafka.api.command.service.ClientPurchaseService;
import com.imagina.kafka.api.request.ClientPurchaseAppRequest;
import com.imagina.kafka.api.request.ClientPurchaseWebRequest;
import com.imagina.kafka.api.response.PurchaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client/purchase")
public class ClientPurchaseApi {

	@Autowired
	private ClientPurchaseService service;

	@PostMapping(value = "/app", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PurchaseResponse> createPurchaseMobile(@RequestBody ClientPurchaseAppRequest request) {
		var purchaseNumber = service.createPurchaseApp(request);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new PurchaseResponse(purchaseNumber));
	}

	@PostMapping(value = "/web", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PurchaseResponse> createPurchaseWeb(@RequestBody ClientPurchaseWebRequest request) {
		var purchaseNumber = service.createPurchaseWeb(request);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new PurchaseResponse(purchaseNumber));
	}

}
