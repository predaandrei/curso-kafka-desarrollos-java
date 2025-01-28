package com.imagina.kafka.api.command.service;

import com.imagina.kafka.api.command.action.ClientPurchaseAction;
import com.imagina.kafka.api.request.ClientPurchaseAppRequest;
import com.imagina.kafka.api.request.ClientPurchaseWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientPurchaseService {

	@Autowired
	private ClientPurchaseAction action;

	public String createPurchaseApp(ClientPurchaseAppRequest request) {
		return action.publishAppToKafka(request);
	}

	public String createPurchaseWeb(ClientPurchaseWebRequest request) {
		return action.publishWebToKafka(request);
	}
}
