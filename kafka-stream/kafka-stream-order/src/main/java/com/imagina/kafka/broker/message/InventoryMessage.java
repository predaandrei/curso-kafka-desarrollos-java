package com.imagina.kafka.broker.message;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryMessage {

	private String item;
	private String location;
	private long quantity;
	private String type;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	private OffsetDateTime transactionTime;

	@Override
	public String toString() {
		return "InventoryMessage [item=" + item + ", location=" + location + ", quantity=" + quantity + ", type=" + type
				+ ", transactionTime=" + transactionTime + "]";
	}

}
