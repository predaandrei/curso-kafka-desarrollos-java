package com.imagina.kafka.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountRequest {

	private String discountCode;

	private int discountPercentage;

	@Override
	public String toString() {
		return "DiscountRequest{" +
				"discountCode='" + discountCode + '\'' +
				", discountPercentage=" + discountPercentage +
				'}';
	}
}
