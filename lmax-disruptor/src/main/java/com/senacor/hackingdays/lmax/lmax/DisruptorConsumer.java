package com.senacor.hackingdays.lmax.lmax;

import com.lmax.disruptor.EventHandler;

public class DisruptorConsumer implements EventHandler<DisruptorEnvelope> {

    private int expectedMessages;
    private final Runnable onComplete;

    public DisruptorConsumer(int expectedMessages, Runnable onComplete) {
        this.expectedMessages = expectedMessages;
        this.onComplete = onComplete;
    }

    @Override
    public void onEvent(DisruptorEnvelope event, long sequence, boolean endOfBatch) throws Exception {
        if (endOfBatch) {
            System.out.println("End of batch");
        }
        expectedMessages--;
        if (expectedMessages == 0)
            onComplete.run();
    }
}
