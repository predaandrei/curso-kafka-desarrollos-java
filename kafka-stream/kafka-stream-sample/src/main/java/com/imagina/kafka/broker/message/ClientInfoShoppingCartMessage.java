package com.imagina.kafka.broker.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientInfoShoppingCartMessage {

	private String clientId;
	private String itemName;
	private int cartAmount;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	private OffsetDateTime cartDatetime;

	@Override
	public String toString() {
		return "ClientInfoShoppingCartMessage [clientId=" + clientId + ", itemName=" + itemName
				+ ", cartAmount=" + cartAmount + ", cartDatetime=" + cartDatetime + "]";
	}

}
