package com.imagina.kafka.api.command.action;

import com.imagina.kafka.api.request.ClientPurchaseAppRequest;
import com.imagina.kafka.api.request.ClientPurchaseWebRequest;
import com.imagina.kafka.broker.message.ClientPurchaseAppMessage;
import com.imagina.kafka.broker.message.ClientPurchaseWebMessage;
import com.imagina.kafka.broker.producer.ClientPurchaseProducer;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClientPurchaseAction {

	@Autowired
	private ClientPurchaseProducer producer;

	public String publishAppToKafka(ClientPurchaseAppRequest request) {
		var purchaseNumber = "CP-APP-" + RandomStringUtils.randomAlphanumeric(6).toUpperCase();
		var location = new ClientPurchaseAppMessage.Location(request.getLocation().getLatitude(),
				request.getLocation().getLongitude());

		var message = new ClientPurchaseAppMessage(purchaseNumber, request.getPurchaseAmount(),
				request.getMobileAppVersion(), request.getOperatingSystem(), location);

		producer.publishPurchaseApp(message);

		return purchaseNumber;
	}

	public String publishWebToKafka(ClientPurchaseWebRequest request) {
		var purchaseNumber = "CP-WEB-" + RandomStringUtils.randomAlphanumeric(6).toUpperCase();

		var message = new ClientPurchaseWebMessage(purchaseNumber, request.getPurchaseAmount(), request.getBrowser(),
				request.getOperatingSystem());

		producer.publishPurchaseWeb(message);

		return purchaseNumber;
	}

}
