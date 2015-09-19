package com.senacor.hackingdays.parralelgenerator;

import com.senacor.hackingdays.serialization.data.Profile;
import com.senacor.hackingdays.serialization.data.generate.ProfileGenerator;
import com.senacor.hackingdays.serialization.data.generate.UnlimitedProfileGenerator;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lrscholz
 * Date: 19/09/15
 * Time: 13:19
 * To change this template use File | Settings | File Templates.
 */
public class ParallelGenerator {
  @Rule
  public TestName name = new TestName();

  private final static int DURACTION = 2000;
  private final static int LIMIT = 1_000_000_000;

  private static class RunnerThread extends Thread {

    ProfileCollector collector;
    UnlimitedProfileGenerator generator;

    public RunnerThread(UnlimitedProfileGenerator generator, ProfileCollector collector) {
      this.collector = collector;
      this.generator = generator;
    }

    @Override
    public void run() {

      while(!this.isInterrupted()) {
        Profile profile = generator.generateProfile();
        collector.collect(profile);
      }
    }
  };

  @Test
  public void generateSerialLimitedWithMultipleThreads() {

    int size = 4;

    ProfileCollector collector = new ProfileCollector();

    String text = MessageFormat.format("start {0} : count = {1}, in collector = {2}, size = {3}", name.getMethodName(), collector.getCount(), collector.getCollected().size(), size);
    System.out.println(text);


    final Object signal = new Object();

    List<RunnerThread> threads = new ArrayList<>();
    for(int i = 0; i < size; i++) {
      UnlimitedProfileGenerator pg = UnlimitedProfileGenerator.newInstance();
      threads.add(new RunnerThread(pg, collector));
    }

    threads.forEach(t -> t.start());

    try {
      Thread.sleep(DURACTION);
    } catch (InterruptedException e) {

    }

    threads.forEach(t -> t.interrupt());

    text = MessageFormat.format("{0} : count = {1}, in collector = {2}, size = {3}", name.getMethodName(), collector.getCount(), collector.getCollected().size(), size);
    System.out.println(text);

    try {
      Thread.sleep(DURACTION);
    } catch (InterruptedException e) {

    }

    text = MessageFormat.format("end {0} : count = {1}, in collector = {2}, size = {3}", name.getMethodName(), collector.getCount(), collector.getCollected().size(), size);
    System.out.println(text);
  }
}
