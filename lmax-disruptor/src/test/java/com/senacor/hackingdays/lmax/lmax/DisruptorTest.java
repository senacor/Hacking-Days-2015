package com.senacor.hackingdays.lmax.lmax;

import com.google.common.base.Stopwatch;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.LiteBlockingWaitStrategy;
import com.lmax.disruptor.PhasedBackoffWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.TimeoutBlockingWaitStrategy;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
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


    public static final int SAMPLE_SIZE = 1_000_000;

    @ClassRule
    public static final ResultCollector resultCollector = new ResultCollector();
    public static final int POOL_SIZE = 12;
    public static final int RING_BUFFER_SIZE = 1024 * 2 * 2;

    @Test
    @Parameters(method = "disruptorParams")
    public void testDisruptor(ProducerType producerType, WaitStrategy waitStrategy) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(POOL_SIZE);
        Disruptor<DisruptorEnvelope> disruptor = new Disruptor<>(DisruptorEnvelope::new, RING_BUFFER_SIZE, executor, producerType, waitStrategy);
        CountDownLatch countDownLatch = registerConsumers(disruptor);

        // Start the Disruptor, starts all threads running
        disruptor.start();

        // Get the ring buffer from the Disruptor to be used for publishing.
        RingBuffer<DisruptorEnvelope> ringBuffer = disruptor.getRingBuffer();

        ProfileGenerator generator = setupGenerator();

        Stopwatch stopwatch = Stopwatch.createStarted();
        generator.forEach(prof -> ringBuffer.publishEvent(
                        (envelope, sequence, profile) -> envelope.setProfile(profile),
                        prof)
        );

        // wait for termination
        countDownLatch.await();
        stopwatch.stop();
        disruptor.shutdown();
        executor.shutdown();
        resultCollector.addResult(producerType, waitStrategy, stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    @SuppressWarnings("unusedDeclaration")
    private Object[] disruptorParams() {
        return $(
                $(ProducerType.MULTI, new BlockingWaitStrategy()),        // warm up
                $(ProducerType.MULTI, new BlockingWaitStrategy()),
                $(ProducerType.SINGLE, new YieldingWaitStrategy()),
                $(ProducerType.SINGLE, new BlockingWaitStrategy()),
                $(ProducerType.SINGLE, new BusySpinWaitStrategy()),
                $(ProducerType.SINGLE, new PhasedBackoffWaitStrategy(100, 100, TimeUnit.MILLISECONDS, new YieldingWaitStrategy())),
                $(ProducerType.SINGLE, new LiteBlockingWaitStrategy()),
                $(ProducerType.SINGLE, new TimeoutBlockingWaitStrategy(100, TimeUnit.MILLISECONDS)),
                $(ProducerType.SINGLE, new SleepingWaitStrategy())
        );
    }

    private ProfileGenerator setupGenerator() {
        return new ProfileGenerator.Builder(SAMPLE_SIZE)
                .build();
    }

    private CountDownLatch registerConsumers(Disruptor<DisruptorEnvelope> disruptor) {
        // Connect the handler
        CountDownLatch countDownLatch = new CountDownLatch(8);

        Runnable onComplete = () -> countDownLatch.countDown();
        CompletableConsumer unisexNameConsumer = new UnisexNameConsumer(SAMPLE_SIZE, onComplete);
        CompletableConsumer loggedInToday = new LoggedInTodayConsumer(SAMPLE_SIZE, onComplete);
        CompletableConsumer creepyOldMenConsumer = new CreepyOldMenConsumer(SAMPLE_SIZE, onComplete);
        CompletableConsumer averageAgeEventHandler = new AverageAgeConsumer(SAMPLE_SIZE, onComplete);
        CompletableConsumer fraudConsumer = new RuleBasedFraudDetector(SAMPLE_SIZE, onComplete);
        CompletableConsumer homosexualCountingConsumer = new HomosexualCountingConsumer(SAMPLE_SIZE, onComplete);
        CompletableConsumer matchMakingConsumer = new MatchMakingConsumer(SAMPLE_SIZE, onComplete);
        CompletableConsumer keepInRamConsumer = new KeepInRamConsumer(SAMPLE_SIZE, onComplete);

        disruptor.handleEventsWith(
                unisexNameConsumer,
                loggedInToday,
                creepyOldMenConsumer,
                fraudConsumer,
                averageAgeEventHandler,
                homosexualCountingConsumer,
                matchMakingConsumer,
                keepInRamConsumer
        );
        return countDownLatch;
    }

}