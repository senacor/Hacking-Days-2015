package com.senacor.hackingdays.lmax.lmax;

import com.google.common.base.Stopwatch;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.senacor.hackingdays.lmax.generate.ProfileGenerator;
import com.senacor.hackingdays.lmax.generate.model.Profile;
import com.senacor.hackingdays.lmax.queue.QueueConsumer;
import com.senacor.hackingdays.lmax.queue.QueuePublisher;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static junitparams.JUnitParamsRunner.$;

@RunWith(JUnitParamsRunner.class)
public class DisruptorTest {


    public static final int SAMPLE_SIZE = 100_000;

    @Test
    @Parameters(method = "poolSize")
    public void testDisruptor(int poolSize) throws InterruptedException {
        // Executor that will be used to construct new threads for consumers
        Executor executor = Executors.newFixedThreadPool(poolSize);

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

        ProfileGenerator profiles = ProfileGenerator.newInstance(SAMPLE_SIZE);

        stopwatch.start();
        profiles.forEach(profile -> ringBuffer.publishEvent((envelope, sequence) -> envelope.setProfile(profile)));
        while (stopwatch.isRunning()) {
            TimeUnit.MILLISECONDS.sleep(100);
        }
        TimeUnit.MILLISECONDS.sleep(1000);
        disruptor.shutdown();
        System.err.println("Processed " + SAMPLE_SIZE + " profiles in " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " milliseconds.");
    }

    @Test
    @Parameters(method = "poolSize")
    public void testLinkedQueue(int poolSize) throws InterruptedException {

        Executor executor = Executors.newFixedThreadPool(poolSize);

        ConcurrentLinkedQueue<Profile> queue = new ConcurrentLinkedQueue<>();
        QueuePublisher publisher = new QueuePublisher(queue);

        CountDownLatch latch = new CountDownLatch(SAMPLE_SIZE);
        QueueConsumer consumer = new QueueConsumer(queue, latch);

        Stopwatch stopwatch = Stopwatch.createStarted();
        ProfileGenerator.newInstance(SAMPLE_SIZE).forEach(profile -> executor.execute(() -> publisher.publish(profile)));


        IntStream.range(1, 3).forEach(i -> executor.execute(() -> consumer.poll()));


        latch.await();
        TimeUnit.MILLISECONDS.sleep(1000);
        System.err.println("Processed " + SAMPLE_SIZE + " profiles in " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " milliseconds.");

    }


    @SuppressWarnings("unusedDeclaration")
    static Object[] poolSize() {
        return $(
                $(1),
                $(4),
                $(8),
                $(12),
                $(16),
                $(20),
                $(40),
                $(80)
        );
    }

}