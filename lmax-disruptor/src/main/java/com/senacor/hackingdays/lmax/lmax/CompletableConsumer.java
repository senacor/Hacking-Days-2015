package com.senacor.hackingdays.lmax.lmax;

import com.lmax.disruptor.EventHandler;

public abstract class CompletableConsumer implements EventHandler<DisruptorEnvelope> {

    private final int maxSequence;
    private final Runnable onComplete;

    public CompletableConsumer(int expectedMessages, Runnable onComplete) {
        this.maxSequence = expectedMessages -1;
        this.onComplete = onComplete;
    }

    @Override
    public final void onEvent(DisruptorEnvelope event, long sequence, boolean endOfBatch) throws Exception {

        processEvent(event, sequence, endOfBatch);
        if (maxSequence == sequence)
            onComplete.run();

    }

    protected abstract void processEvent(DisruptorEnvelope event, long sequence, boolean endOfBatch);
}
