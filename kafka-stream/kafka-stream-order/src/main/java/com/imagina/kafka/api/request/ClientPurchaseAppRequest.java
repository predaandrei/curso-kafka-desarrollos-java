package com.imagina.kafka.api.request;

import lombok.Data;

@Data
public class ClientPurchaseAppRequest {

	@Data
	public static class Location {
		private double latitude;

		private double longitude;

		@Override
		public String toString() {
			return "Location [latitude=" + latitude + ", longitude=" + longitude + "]";
		}
	}

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
