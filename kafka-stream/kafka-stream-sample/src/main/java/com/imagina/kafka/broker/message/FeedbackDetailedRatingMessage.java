package com.imagina.kafka.broker.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDetailedRatingMessage {

    private String location;

    private double averageRating;

    private Map<Integer, Long> ratingMap;

    @Override
    public String toString() {
        return "FeedbackDetailedRatingMessage{" +
                "location='" + location + '\'' +
                ", averageRating=" + averageRating +
                ", ratingMap=" + ratingMap +
                '}';
    }

}