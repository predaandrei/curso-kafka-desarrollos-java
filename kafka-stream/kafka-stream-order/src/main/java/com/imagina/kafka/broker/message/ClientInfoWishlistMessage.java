package com.imagina.kafka.broker.message;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientInfoWishlistMessage {

	private String clientId;
	private String itemName;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	private OffsetDateTime wishlistDatetime;
}
