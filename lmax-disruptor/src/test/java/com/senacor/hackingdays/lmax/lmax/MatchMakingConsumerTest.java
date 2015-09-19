package com.senacor.hackingdays.lmax.lmax;

import com.google.common.base.Stopwatch;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.senacor.hackingdays.lmax.generate.ProfileGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.concurrent.*;

/**
 * Created by mhaunolder on 19.09.15.
 */
public class MatchMakingConsumerTest {

    private Disruptor<DisruptorEnvelope> disruptor;
    private ExecutorService executor;
    private final int POOL_SIZE = 10;
    public static final int SAMPLE_SIZE = 1000000;


    @ClassRule
    public static final ResultCollector resultCollector = new ResultCollector();

    @Before
    public void setUp(){
        // Executor that will be used to construct new threads for consumers
        executor = Executors.newFixedThreadPool(POOL_SIZE);

        // Specify the size of the ring buffer, must be power of 2.
        int bufferSize = 4096;
        // Construct the Disruptor
        disruptor = new Disruptor<>(DisruptorEnvelope::new, bufferSize, executor);

    }

    @Test
    public void testMatch() throws InterruptedException {

        // Connect the handler
        CountDownLatch countDownLatch = new CountDownLatch(1);

        Runnable onComplete = () -> countDownLatch.countDown();

        CompletableConsumer matchmakingConsumer = new MatchMakingConsumer(SAMPLE_SIZE, onComplete);
        disruptor.handleEventsWith(matchmakingConsumer);

        // Start the Disruptor, starts all threads running
        disruptor.start();

        // Get the ring buffer from the Disruptor to be used for publishing.
        RingBuffer<DisruptorEnvelope> ringBuffer = disruptor.getRingBuffer();

        ProfileGenerator generator = ProfileGenerator.newInstance(SAMPLE_SIZE);

        Stopwatch stopwatch = Stopwatch.createStarted();
        generator.forEach(profile -> ringBuffer.publishEvent((envelope, sequence) -> envelope.setProfile(profile)));
        countDownLatch.await();
        stopwatch.stop();
        resultCollector.addResult(POOL_SIZE, stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    @After
    public void tearDown(){
        disruptor.shutdown();
        executor.shutdown();

    }
}
