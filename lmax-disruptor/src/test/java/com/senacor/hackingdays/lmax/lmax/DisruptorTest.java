package com.senacor.hackingdays.lmax.lmax;

import com.google.common.base.Stopwatch;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.senacor.hackingdays.lmax.generate.ProfileGenerator;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static junitparams.JUnitParamsRunner.$;

@RunWith(JUnitParamsRunner.class)
public class DisruptorTest {


    public static final int SAMPLE_SIZE = 100_000;

    @Test
    @Parameters(method = "poolSize")
    public void testDisruptor(int poolSize) throws InterruptedException {
        // Executor that will be used to construct new threads for consumers
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);

        // Specify the size of the ring buffer, must be power of 2.
        int bufferSize = 4096;
        // Construct the Disruptor
        Disruptor<DisruptorEnvelope> disruptor = new Disruptor<>(DisruptorEnvelope::new, bufferSize, executor);

        // Connect the handler
        CountDownLatch countDownLatch = new CountDownLatch(1);
        DisruptorConsumer c1 = new DisruptorConsumer(SAMPLE_SIZE, () -> countDownLatch.countDown());
//        DisruptorConsumer c2 = new DisruptorConsumer(SAMPLE_SIZE, () -> countDownLatch.countDown());
//        DisruptorConsumer c3 = new DisruptorConsumer(SAMPLE_SIZE, () -> countDownLatch.countDown());
        disruptor.handleEventsWith(c1);

        // Start the Disruptor, starts all threads running
        disruptor.start();

        // Get the ring buffer from the Disruptor to be used for publishing.
        RingBuffer<DisruptorEnvelope> ringBuffer = disruptor.getRingBuffer();

        ProfileGenerator generator = ProfileGenerator.newInstance(SAMPLE_SIZE);

        Stopwatch stopwatch = Stopwatch.createStarted();
        generator.forEach(profile -> ringBuffer.publishEvent((envelope, sequence) -> envelope.setProfile(profile)));
        countDownLatch.await();
        stopwatch.stop();
        disruptor.shutdown();
        executor.shutdownNow();
        System.err.println("Pool size: " + poolSize + " Time: " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " milliseconds.");
    }


    @SuppressWarnings("unusedDeclaration")
    static Object[] poolSize() {
        return $(
                $(1),
                $(2),
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