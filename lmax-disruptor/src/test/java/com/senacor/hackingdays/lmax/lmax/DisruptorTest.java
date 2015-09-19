package com.senacor.hackingdays.lmax.lmax;

import com.google.common.base.Stopwatch;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.senacor.hackingdays.lmax.generate.ProfileGenerator;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static junitparams.JUnitParamsRunner.$;

@RunWith(JUnitParamsRunner.class)
public class DisruptorTest {


    public static final int SAMPLE_SIZE = 500_000;

    @ClassRule
    public static final ResultCollector resultCollector = new ResultCollector();

    @Test
    @Parameters(method = "poolSize")
    public void testDisruptor(int poolSize) throws InterruptedException {
        // Executor that will be used to construct new threads for consumers
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);

        // Specify the size of the ring buffer, must be power of 2.
        int bufferSize = 4096;
        // Construct the Disruptor
//        Disruptor<DisruptorEnvelope> disruptor = new Disruptor<>(DisruptorEnvelope::new, bufferSize, executor, ProducerType.SINGLE, new YieldingWaitStrategy());
        Disruptor<DisruptorEnvelope> disruptor = new Disruptor<>(DisruptorEnvelope::new, bufferSize, executor);

        // Connect the handler
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Runnable onComplete = () -> countDownLatch.countDown();
        CompletableConsumer unisexNameConsumer = new UnisexNameConsumer(SAMPLE_SIZE, onComplete);
        disruptor.handleEventsWith(unisexNameConsumer);

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
        executor.shutdown();
        resultCollector.addResult(poolSize, stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }


    @SuppressWarnings("unusedDeclaration")
    static Object[] poolSize() {
        return $(
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