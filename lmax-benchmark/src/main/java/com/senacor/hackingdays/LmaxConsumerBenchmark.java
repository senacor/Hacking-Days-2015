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
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import com.google.common.base.Stopwatch;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.senacor.hackingdays.lmax.generate.ProfileGenerator;
import com.senacor.hackingdays.lmax.lmax.AverageAgeConsumer;
import com.senacor.hackingdays.lmax.lmax.CompletableConsumer;
import com.senacor.hackingdays.lmax.lmax.CreepyOldMenConsumer;
import com.senacor.hackingdays.lmax.lmax.DisruptorEnvelope;
import com.senacor.hackingdays.lmax.lmax.HomosexualCountingConsumer;
import com.senacor.hackingdays.lmax.lmax.KeepInRamConsumer;
import com.senacor.hackingdays.lmax.lmax.LoggedInTodayConsumer;
import com.senacor.hackingdays.lmax.lmax.MatchMakingConsumer;
import com.senacor.hackingdays.lmax.lmax.UnisexNameConsumer;
import com.senacor.hackingdays.lmax.lmax.fraudrule.RuleBasedFraudDetector;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Warmup(iterations = 2, time = 10, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 15, timeUnit = TimeUnit.SECONDS)
@Fork(2)
@Threads(1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class LmaxConsumerBenchmark {

  public static final int POOL_SIZE = 12;

  @State(Scope.Benchmark)
  public static class TestState {

    ProfileGenerator generator;
    ExecutorService executor;
    Stopwatch stopwatch;
    CountDownLatch countDownLatch;

    Disruptor<DisruptorEnvelope> disruptor;

    @Param({
        // "com.lmax.disruptor.BlockingWaitStrategy",
        "com.lmax.disruptor.YieldingWaitStrategy"
        // ,"com.lmax.disruptor.BusySpinWaitStrategy"
        , "com.lmax.disruptor.SleepingWaitStrategy"
    })
    private String disruptorWaitStratConfig;

    @Param({ "500000" })
    public int SAMPLE_SIZE;

    @Param({ "2048", "1024" })
    public int RING_BUFFER_SIZE;

    private ProducerType producerType = ProducerType.SINGLE;

    public TestState() {
      executor = Executors.newCachedThreadPool();
      stopwatch = Stopwatch.createUnstarted();
    }

    private ProfileGenerator setupGenerator() {
      return new ProfileGenerator.Builder(SAMPLE_SIZE).build();

    }

    @Setup(Level.Invocation)
    public void foo() throws Exception {
      System.out.println("--- instantiate " + disruptorWaitStratConfig);
      WaitStrategy waitStrategy = (WaitStrategy) Class.forName(disruptorWaitStratConfig).newInstance();

      disruptor = new Disruptor<>(DisruptorEnvelope::new, RING_BUFFER_SIZE, executor,
          producerType, waitStrategy);
      countDownLatch = registerConsumers(disruptor);
      // Start the Disruptor, starts all threads running
      disruptor.start();

      generator = setupGenerator();

      stopwatch.reset();
      stopwatch.start();
    }

    @TearDown(Level.Invocation)
    public void tearDown() {
      stopwatch.stop();
      disruptor.shutdown();

      System.out.println(
          "--- processing took " + stopwatch);
    }

    @TearDown(Level.Trial)
    public void shutdownThreadPool() {
      executor.shutdown();
    }

    @SuppressWarnings("unchecked")
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
          keepInRamConsumer);
      return countDownLatch;
    }

  }

  @Benchmark
  public void testMethod(TestState state, Blackhole hole) throws Exception {
    // Get the ring buffer from the Disruptor to be used for publishing.
    RingBuffer<DisruptorEnvelope> ringBuffer = state.disruptor.getRingBuffer();

    state.generator.forEach(prof -> ringBuffer.publishEvent(
        (envelope, sequence, profile) -> envelope.setProfile(profile),
        prof));

    // wait for termination
    state.countDownLatch.await();

  }

}
