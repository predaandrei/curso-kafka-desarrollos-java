package com.imagina.kafka.api.command.service;

import com.imagina.kafka.api.command.action.InventoryAction;
import com.imagina.kafka.api.request.InventoryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

	@Autowired
	private InventoryAction action;

	public void addInventory(InventoryRequest request) {
		action.publishToKafka(request, "ADD");
	}

	public void subtractInventory(InventoryRequest request) {
		action.publishToKafka(request, "REMOVE");
	}

}
