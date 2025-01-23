package com.imagina.kafka.broker.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackMessage {

	private String feedback;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	private OffsetDateTime feedbackDateTime;

	private String location;

	private int rating;

	@Override
	public String toString() {
		return "FeedbackMessage [feedback=" + feedback + ", feedbackDateTime=" + feedbackDateTime + ", location="
				+ location + ", rating=" + rating + "]";
	}

}
