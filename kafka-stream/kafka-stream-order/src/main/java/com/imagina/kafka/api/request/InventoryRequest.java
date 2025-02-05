package com.imagina.kafka.api.request;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRequest {

	private String item;
	private long quantity;
	private String location;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	private OffsetDateTime transactionTime;

	@Override
	public String toString() {
		return "InventoryRequest [item=" + item + ", quantity=" + quantity + ", location=" + location
				+ ", transactionTime=" + transactionTime + "]";
	}

}
