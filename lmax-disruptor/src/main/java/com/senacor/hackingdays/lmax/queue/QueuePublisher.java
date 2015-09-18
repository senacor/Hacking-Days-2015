package com.senacor.hackingdays.lmax.queue;

import com.senacor.hackingdays.serialization.data.Profile;
import scala.collection.mutable.Publisher;

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
