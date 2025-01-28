package com.imagina.kafka.broker.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientPurchaseWebMessage {

	private String purchaseNumber;

	private int purchaseAmount;

	private String browser;

	private String operatingSystem;

	@Override
	public String toString() {
		return "ClientPurchaseWebMessage [purchaseAmount=" + purchaseAmount + ", browser=" + browser
				+ ", operatingSystem=" + operatingSystem + "]";
	}
}
