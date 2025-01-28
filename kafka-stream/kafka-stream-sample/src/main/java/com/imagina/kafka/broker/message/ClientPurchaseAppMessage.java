package com.imagina.kafka.broker.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientPurchaseAppMessage {

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Location {
		private double latitude;

		private double longitude;

		@Override
		public String toString() {
			return "Location [latitude=" + latitude + ", longitude=" + longitude + "]";
		}
	}

	private String purchaseNumber;

	private int purchaseAmount;

	private String mobileAppVersion;

	private String operatingSystem;

	private Location location;

	@Override
	public String toString() {
		return "ClientPurchaseAppMessage [purchaseAmount=" + purchaseAmount + ", mobileAppVersion=" + mobileAppVersion
				+ ", operatingSystem=" + operatingSystem + ", location=" + location + "]";
	}

}
