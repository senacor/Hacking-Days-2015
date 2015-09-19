package com.senacor.hackingdays.lmax.lmax;

import com.google.common.base.Stopwatch;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.senacor.hackingdays.lmax.generate.ProfileGenerator;
import com.senacor.hackingdays.lmax.lmax.fraudrule.RuleBasedFraudDetector;
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
    public static final int POOL_SIZE = 12;

    @Test
    public void testDisruptor() throws InterruptedException {
        // Executor that will be used to construct new threads for consumers
        ExecutorService executor = Executors.newFixedThreadPool(POOL_SIZE);

        // Specify the size of the ring buffer, must be power of 2.
        int bufferSize = 4096 * 2;
        // Construct the Disruptor
//        Disruptor<DisruptorEnvelope> disruptor = new Disruptor<>(DisruptorEnvelope::new, bufferSize, executor, ProducerType.SINGLE, new YieldingWaitStrategy());
        Disruptor<DisruptorEnvelope> disruptor = new Disruptor<>(DisruptorEnvelope::new, bufferSize, executor);

        CountDownLatch countDownLatch = registerConsumers(disruptor);

        // Start the Disruptor, starts all threads running
        disruptor.start();

        // Get the ring buffer from the Disruptor to be used for publishing.
        RingBuffer<DisruptorEnvelope> ringBuffer = disruptor.getRingBuffer();

        ProfileGenerator generator = setupGenerator();

        Stopwatch stopwatch = Stopwatch.createStarted();
        generator.forEach(prof -> ringBuffer.publishEvent(
                        (envelope, sequence, profile) -> envelope.setProfile(profile), prof)
        );

        // wait for termination
        countDownLatch.await();
        stopwatch.stop();
        disruptor.shutdown();
        executor.shutdown();
        resultCollector.addResult(POOL_SIZE, stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    private ProfileGenerator setupGenerator() {
        return new ProfileGenerator.Builder(SAMPLE_SIZE)
                .build();
    }

    private CountDownLatch registerConsumers(Disruptor<DisruptorEnvelope> disruptor) {
        // Connect the handler
        CountDownLatch countDownLatch = new CountDownLatch(6);

        Runnable onComplete = () -> countDownLatch.countDown();
        CompletableConsumer unisexNameConsumer = new UnisexNameConsumer(SAMPLE_SIZE, onComplete);
        CompletableConsumer loggedInToday = new LoggedInTodayConsumer(SAMPLE_SIZE, onComplete);
        CompletableConsumer creepyOldMenConsumer = new CreepyOldMenConsumer(SAMPLE_SIZE, onComplete);
        CompletableConsumer averageAgeEventHandler = new AverageAgeConsumer(SAMPLE_SIZE, onComplete);
        CompletableConsumer fraudConsumer = new RuleBasedFraudDetector(SAMPLE_SIZE, onComplete);
        CompletableConsumer homosexualCountingConsumer = new HomosexualCountingConsumer(SAMPLE_SIZE, onComplete);

        disruptor.handleEventsWith(
                unisexNameConsumer,
                loggedInToday,
                creepyOldMenConsumer,
                fraudConsumer,
                averageAgeEventHandler,
                homosexualCountingConsumer
        );
        return countDownLatch;
    }

}