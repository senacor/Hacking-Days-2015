package com.senacor.hackingdays;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import com.google.common.base.Stopwatch;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.senacor.hackingdays.lmax.generate.ProfileGenerator;
import com.senacor.hackingdays.lmax.lmax.AverageAgeConsumer;
import com.senacor.hackingdays.lmax.lmax.CompletableConsumer;
import com.senacor.hackingdays.lmax.lmax.CreepyOldMenConsumer;
import com.senacor.hackingdays.lmax.lmax.DisruptorEnvelope;
import com.senacor.hackingdays.lmax.lmax.HomosexualCountingConsumer;
import com.senacor.hackingdays.lmax.lmax.LoggedInTodayConsumer;
import com.senacor.hackingdays.lmax.lmax.MatchMakingConsumer;
import com.senacor.hackingdays.lmax.lmax.UnisexNameConsumer;
import com.senacor.hackingdays.lmax.lmax.fraudrule.RuleBasedFraudDetector;

@Warmup(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS)
@Fork(5)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class LmaxConsumerBenchmark {

  public static final int POOL_SIZE = 12;
  public static final int RING_BUFFER_SIZE = 1024 * 2 * 2;
  public static final int SAMPLE_SIZE = 1_000_000;


  @State(Scope.Benchmark)
  public static class TestState {

    ProfileGenerator generator;
    ExecutorService executor;
    Stopwatch stopwatch;
    CountDownLatch countDownLatch;

    Disruptor<DisruptorEnvelope> disruptor;

    public TestState() {
    }

    private ProfileGenerator setupGenerator() {
      return new ProfileGenerator.Builder(SAMPLE_SIZE).build();

    }

    @Setup(Level.Iteration)
    public void foo() {
      executor = Executors.newFixedThreadPool(POOL_SIZE);

      generator = setupGenerator();

      stopwatch = Stopwatch.createUnstarted();
      stopwatch.start();
    }

    @TearDown(Level.Iteration)
    public void tearDown() {
      stopwatch.stop();

      executor.shutdown();

    }

  }

  @Benchmark
  public void testMethod(TestState state, Blackhole hole) throws Exception {
    Disruptor<DisruptorEnvelope> disruptor = state.disruptor;

    disruptor = new Disruptor<>(DisruptorEnvelope::new, RING_BUFFER_SIZE, state.executor,
        ProducerType.MULTI, new BlockingWaitStrategy());
    state.countDownLatch = registerConsumers(disruptor);

    // Start the Disruptor, starts all threads running
    disruptor.start();

    // Get the ring buffer from the Disruptor to be used for publishing.
    RingBuffer<DisruptorEnvelope> ringBuffer = disruptor.getRingBuffer();

    ProfileGenerator generator = state.generator;

    generator.forEach(prof -> ringBuffer.publishEvent(
        (envelope, sequence, profile) -> envelope.setProfile(profile),
        prof));

    // wait for termination
    state.countDownLatch.await();
    disruptor.shutdown();

  }


  private CountDownLatch registerConsumers(Disruptor<DisruptorEnvelope> disruptor) {
    // Connect the handler
    CountDownLatch countDownLatch = new CountDownLatch(7);

    Runnable onComplete = () -> countDownLatch.countDown();
    CompletableConsumer unisexNameConsumer = new UnisexNameConsumer(SAMPLE_SIZE, onComplete);
    CompletableConsumer loggedInToday = new LoggedInTodayConsumer(SAMPLE_SIZE, onComplete);
    CompletableConsumer creepyOldMenConsumer = new CreepyOldMenConsumer(SAMPLE_SIZE, onComplete);
    CompletableConsumer averageAgeEventHandler = new AverageAgeConsumer(SAMPLE_SIZE, onComplete);
    CompletableConsumer fraudConsumer = new RuleBasedFraudDetector(SAMPLE_SIZE, onComplete);
    CompletableConsumer homosexualCountingConsumer = new HomosexualCountingConsumer(SAMPLE_SIZE, onComplete);
    CompletableConsumer matchMakingConsumer = new MatchMakingConsumer(SAMPLE_SIZE, onComplete);

    disruptor.handleEventsWith(
        unisexNameConsumer,
        loggedInToday,
        creepyOldMenConsumer,
        fraudConsumer,
        averageAgeEventHandler,
        homosexualCountingConsumer,
        matchMakingConsumer);
    return countDownLatch;
  }

}
