package com.senacor.hackingdays.lmax.lmax;

import com.lmax.disruptor.EventHandler;
import com.senacor.hackingdays.lmax.generate.model.Profile;
import com.senacor.hackingdays.lmax.lmax.matchmaking.Match;

import java.util.List;

public abstract class CompletableConsumer implements EventHandler<DisruptorEnvelope> {

    protected final int maxSequence;
    protected final Runnable onComplete;
    protected List<Match> matchList;

    public CompletableConsumer(int expectedMessages, Runnable onComplete) {
        this.maxSequence = expectedMessages - 1;
        this.onComplete = onComplete;
    }

    @Override
    public final void onEvent(DisruptorEnvelope event, long sequence, boolean endOfBatch) throws Exception {

        processEvent(event.getProfile(), sequence, endOfBatch);
        if (maxSequence == sequence) {
            onComplete();
            onComplete.run();
        }
    }

    protected abstract void onComplete();

    protected abstract void processEvent(Profile profile, long sequence, boolean endOfBatch);

    protected List<Match> getMatchList() {
        return matchList;
    }
}
