package com.senacor.hackingdays.lmax.lmax;

import com.lmax.disruptor.EventHandler;

public class DisruptorConsumer implements EventHandler<DisruptorEnvelope> {

    private final int maxSequence;
    private final Runnable onComplete;

    public DisruptorConsumer(int expectedMessages, Runnable onComplete) {
        this.maxSequence = expectedMessages -1;
        this.onComplete = onComplete;
    }

    @Override
    public void onEvent(DisruptorEnvelope event, long sequence, boolean endOfBatch) throws Exception {
        if (endOfBatch) {
//            System.out.println("End of batch");
        }
//        System.out.println(sequence);
        if (maxSequence == sequence)
            onComplete.run();
    }
}
