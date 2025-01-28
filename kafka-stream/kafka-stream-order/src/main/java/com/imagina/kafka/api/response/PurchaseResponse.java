package com.imagina.kafka.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseResponse {

	private String purchaseNumber;

	@Override
	public String toString() {
		return "PurchaseResponse [purchaseNumber=" + purchaseNumber + "]";
	}

}
