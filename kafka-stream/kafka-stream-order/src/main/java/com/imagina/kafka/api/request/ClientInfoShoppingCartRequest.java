package com.imagina.kafka.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientInfoShoppingCartRequest {

	private String clientId;
	private String itemName;
	private int cartAmount;

	@Override
	public String toString() {
		return "ClientInfoShoppingCartRequest [clientId=" + clientId + ", itemName=" + itemName
				+ ", cartAmount=" + cartAmount + "]";
	}

}
