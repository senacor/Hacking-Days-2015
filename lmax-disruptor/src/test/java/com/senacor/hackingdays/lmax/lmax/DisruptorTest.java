package com.senacor.hackingdays.lmax.lmax;

import com.google.common.base.Stopwatch;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.senacor.hackingdays.lmax.queue.QueueConsumer;
import com.senacor.hackingdays.lmax.queue.QueuePublisher;
import com.senacor.hackingdays.serialization.data.Profile;
import com.senacor.hackingdays.serialization.data.generate.ProfileGenerator;
import org.junit.Test;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class DisruptorTest {


    public static final int SAMPLE_SIZE = 1_000_000;

    @Test
    public void testDisruptor() throws InterruptedException {
        // Executor that will be used to construct new threads for consumers
        Executor executor = Executors.newFixedThreadPool(12);

        // Specify the size of the ring buffer, must be power of 2.
        int bufferSize = 1024;

        // Construct the Disruptor
        Disruptor<DisruptorEnvelope> disruptor = new Disruptor<>(DisruptorEnvelope::new, bufferSize, executor);

        // Connect the handler
        Stopwatch stopwatch = Stopwatch.createUnstarted();
        disruptor.handleEventsWith(new DisruptorConsumer(SAMPLE_SIZE, () -> stopwatch.stop()));

        // Start the Disruptor, starts all threads running
        disruptor.start();

        // Get the ring buffer from the Disruptor to be used for publishing.
        RingBuffer<DisruptorEnvelope> ringBuffer = disruptor.getRingBuffer();

        ProfileGenerator profiles = new ProfileGenerator(SAMPLE_SIZE);

        stopwatch.start();
        profiles.forEach(profile -> ringBuffer.publishEvent((envelope, sequence) -> envelope.setProfile(profile)));
        while (stopwatch.isRunning()) {
            TimeUnit.MILLISECONDS.sleep(100);
        }
        TimeUnit.MILLISECONDS.sleep(1000);
        System.err.println("Processed " + SAMPLE_SIZE + " profiles in " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " milliseconds.");
    }

    @Test
    public void testLinkedQueue() throws InterruptedException {

        Executor executor = Executors.newFixedThreadPool(12);

        ConcurrentLinkedQueue<Profile> queue = new ConcurrentLinkedQueue<>();
        QueuePublisher publisher = new QueuePublisher(queue);

        CountDownLatch latch = new CountDownLatch(SAMPLE_SIZE);
        QueueConsumer consumer = new QueueConsumer(queue, latch);

        Stopwatch stopwatch = Stopwatch.createStarted();
        new ProfileGenerator(SAMPLE_SIZE).forEach(profile -> executor.execute(() -> publisher.publish(profile)));


        IntStream.range(1, 10).forEach(i -> executor.execute(() -> consumer.poll()));


        latch.await();
        TimeUnit.MILLISECONDS.sleep(1000);
        System.err.println("Processed " + SAMPLE_SIZE + " profiles in " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " milliseconds.");

    }

}