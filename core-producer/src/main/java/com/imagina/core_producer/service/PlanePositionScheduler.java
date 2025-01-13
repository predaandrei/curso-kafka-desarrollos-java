package com.imagina.core_producer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.imagina.core_producer.model.PlanePosition;
import com.imagina.core_producer.producer.PlanePositionProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

//@Service
@Slf4j
public class PlanePositionScheduler {

    private PlanePosition planeOne;
    private PlanePosition planeTwo;
    private PlanePosition planeThree;

    //@Autowired
    private PlanePositionProducer producer;

    public PlanePositionScheduler() {
        var now = System.currentTimeMillis();

        planeOne = new PlanePosition("plane-1", now, 0);
        planeTwo = new PlanePosition("plane-2", now, 110);
        planeThree = new PlanePosition("plane-3", now, 95);
    }

    @Scheduled(fixedRate = 3000)
    public void generatePlanePosition() throws JsonProcessingException {
        var now = System.currentTimeMillis();

        planeOne.setTimestamp(now);
        planeTwo.setTimestamp(now);
        planeThree.setTimestamp(now);

        planeOne.setDistancia(planeOne.getDistancia() + 1);
        planeTwo.setDistancia(planeTwo.getDistancia() - 1);
        planeThree.setDistancia(planeThree.getDistancia() + 1);

        producer.sendPosition(planeOne);
        producer.sendPosition(planeTwo);
        producer.sendPosition(planeThree);

        log.info("Sent : {}", planeOne);
        log.info("Sent : {}", planeTwo);
        log.info("Sent : {}", planeThree);
    }
}
