package com.imagina.kafka.api.command.service;

import com.imagina.kafka.api.command.action.DiscountAction;
import com.imagina.kafka.api.request.DiscountRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiscountService {

	@Autowired
	private DiscountAction action;

	public void createDiscount(DiscountRequest request) {
		var discountMessage = action.convertToMessage(request);
		action.sendToKafka(discountMessage);
	}

}
