package com.senacor.hackingdays.lmax.lmax;

import com.google.common.base.Stopwatch;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.senacor.hackingdays.lmax.generate.ProfileGenerator;
import com.senacor.hackingdays.lmax.lmax.fraud.FraudConsumer;
import com.senacor.hackingdays.lmax.generate.model.Profile;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
        int bufferSize = 1024;
        // Construct the Disruptor
//        Disruptor<DisruptorEnvelope> disruptor = new Disruptor<>(DisruptorEnvelope::new, bufferSize, executor, ProducerType.SINGLE, new YieldingWaitStrategy());
        Disruptor<DisruptorEnvelope> disruptor = new Disruptor<>(DisruptorEnvelope::new, bufferSize, executor);

        CountDownLatch countDownLatch = registerConsumers(disruptor);

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

    private CountDownLatch registerConsumers(Disruptor<DisruptorEnvelope> disruptor) {
        // Connect the handler
        CountDownLatch countDownLatch = new CountDownLatch(4);

        Runnable onComplete = () -> countDownLatch.countDown();
        CompletableConsumer unisexNameConsumer = new UnisexNameConsumer(SAMPLE_SIZE, onComplete);
        CompletableConsumer loggedInToday = new LoggedInTodayConsumer(SAMPLE_SIZE, onComplete);
        CompletableConsumer creepyOldMenConsumer = new CreepyOldMenConsumer(SAMPLE_SIZE, onComplete);
        CompletableConsumer averageAgeEventHandler = new AverageAgeConsumer(SAMPLE_SIZE, onComplete);
        CompletableConsumer fraudConsumer = new FraudConsumer(SAMPLE_SIZE, onComplete);

        disruptor.handleEventsWith(
                unisexNameConsumer,
                loggedInToday,
                creepyOldMenConsumer,
                fraudConsumer,
                averageAgeEventHandler
        );
        return countDownLatch;
    }

    @SuppressWarnings("unusedDeclaration")
    static Object[] poolSize() {
        return $(
                $(10),
                $(12)
        );
    }


}