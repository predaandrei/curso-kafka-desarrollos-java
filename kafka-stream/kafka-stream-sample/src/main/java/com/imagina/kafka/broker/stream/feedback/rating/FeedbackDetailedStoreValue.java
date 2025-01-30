package com.imagina.kafka.broker.stream.feedback.rating;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.TreeMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDetailedStoreValue {

    private Map<Integer, Long> ratingMap = new TreeMap<>();

    @Override
    public String toString() {
        return "FeedbackDetailedStoreValue{" +
                "ratingMap=" + ratingMap +
                '}';
    }

}