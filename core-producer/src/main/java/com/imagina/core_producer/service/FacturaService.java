package com.imagina.core_producer.service;

import com.imagina.core_producer.model.Factura;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class FacturaService {

    private AtomicInteger counter = new AtomicInteger();

    public Factura generaFactura() {
        var numeroFactura = "FAC-" + counter.incrementAndGet();
        var cantidad = ThreadLocalRandom.current().nextInt(1, 1000);

        return new Factura(numeroFactura, cantidad, "EUR");
    }
}