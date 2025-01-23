package com.imagina.kafka.broker.stream.feedback.rating;

import java.util.Map;
import java.util.TreeMap;

public class FeedbackRatingTwoStoreValue {

    private Map<Integer, Long> ratingMap = new TreeMap<>();

    public FeedbackRatingTwoStoreValue() {
    }

    public FeedbackRatingTwoStoreValue(Map<Integer, Long> ratingMap) {
        this.ratingMap = ratingMap;
    }

    public Map<Integer, Long> getRatingMap() {
        return ratingMap;
    }

    public void setRatingMap(Map<Integer, Long> ratingMap) {
        this.ratingMap = ratingMap;
    }

    @Override
    public String toString() {
        return "FeedbackRatingTwoStoreValue{" +
                "ratingMap=" + ratingMap +
                '}';
    }

}
