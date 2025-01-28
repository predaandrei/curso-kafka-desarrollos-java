package com.imagina.kafka.api.request;

import lombok.Data;

@Data
public class ClientPurchaseWebRequest {

	private int purchaseAmount;
	
	private String browser;
	
	private String operatingSystem;

	@Override
	public String toString() {
		return "ClientPurchaseWebMessage [purchaseAmount=" + purchaseAmount + ", browser=" + browser
				+ ", operatingSystem=" + operatingSystem + "]";
	}
}
