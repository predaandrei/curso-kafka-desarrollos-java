package com.imagina.kafka.api.command.service;

import com.imagina.kafka.api.command.action.FeedbackAction;
import com.imagina.kafka.api.request.FeedbackRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {

	@Autowired
	private FeedbackAction action;

	public void createFeedback(FeedbackRequest request) {
		action.publishToKafka(request);
	}

}
