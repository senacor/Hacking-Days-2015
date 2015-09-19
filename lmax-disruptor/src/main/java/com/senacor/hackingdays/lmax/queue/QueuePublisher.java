package com.senacor.hackingdays.lmax.queue;


import com.senacor.hackingdays.lmax.generate.model.Profile;

import java.util.concurrent.ConcurrentLinkedQueue;

public class QueuePublisher {

    private final ConcurrentLinkedQueue<Profile> blockingQueue;

    public QueuePublisher(ConcurrentLinkedQueue<Profile> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    public void publish(Profile profile) {
        blockingQueue.add(profile);
    }
}
