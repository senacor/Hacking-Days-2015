package com.senacor.hackingdays.lmax.queue;

import com.senacor.hackingdays.serialization.data.Profile;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

public class QueueConsumer {

    private final ConcurrentLinkedQueue<Profile> blockingQueue;
    private final CountDownLatch latch;

    public QueueConsumer(ConcurrentLinkedQueue<Profile> blockingQueue, CountDownLatch latch) {
        this.blockingQueue = blockingQueue;
        this.latch = latch;
    }


    public void poll() {
        while (true) {
            Profile profile = blockingQueue.poll();
            if (profile != null) {
                latch.countDown();
            }
        }
    }
}
