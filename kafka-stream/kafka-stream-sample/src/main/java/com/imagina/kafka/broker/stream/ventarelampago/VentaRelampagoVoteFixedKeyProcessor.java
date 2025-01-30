package com.imagina.kafka.broker.stream.ventarelampago;

import com.imagina.kafka.broker.message.VentaRelampagoMessage;
import org.apache.kafka.streams.processor.api.FixedKeyProcessor;
import org.apache.kafka.streams.processor.api.FixedKeyProcessorContext;
import org.apache.kafka.streams.processor.api.FixedKeyRecord;

public class VentaRelampagoVoteFixedKeyProcessor implements FixedKeyProcessor<String, VentaRelampagoMessage, VentaRelampagoMessage> {

    private final long voteStartTime;
    private final long voteEndTime;

    private FixedKeyProcessorContext<String, VentaRelampagoMessage> context;

    public VentaRelampagoVoteFixedKeyProcessor(long voteStartTime, long voteEndTime) {
        this.voteStartTime = voteStartTime;
        this.voteEndTime = voteEndTime;
    }

    @Override
    public void init(FixedKeyProcessorContext<String, VentaRelampagoMessage> context) {
        FixedKeyProcessor.super.init(context);
    }

    @Override
    public void process(FixedKeyRecord<String, VentaRelampagoMessage> fixedKeyRecord) {
        var recordTime = context.currentSystemTimeMs();
        var voteStartTimeMs = voteStartTime * 1000;
        var voteEndTimeMs = voteEndTime * 1000;

        if (recordTime >= voteStartTimeMs && recordTime <= voteEndTimeMs) {
            context.forward(fixedKeyRecord.withValue(fixedKeyRecord.value()));
        }
    }
}
