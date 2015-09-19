package com.senacor.hackingdays.generator;

import com.senacor.hackingdays.serialization.data.Profile;
import com.senacor.hackingdays.serialization.data.generate.ProfileGenerator;
import com.senacor.hackingdays.serialization.data.generate.UnlimitedProfileGenerator;
import junitparams.Parameters;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created with IntelliJ IDEA.
 * User: lrscholz
 * Date: 19/09/15
 * Time: 10:10
 * To change this template use File | Settings | File Templates.
 */
public class TestGeneratorSimple {

  @Rule
  public TestName name = new TestName();

  private final static int DURACTION = 2000;
  private final static int LIMIT = 1_000_000_000;

  private final ProfileGenerator lpg = ProfileGenerator.newInstance(LIMIT);
  private final UnlimitedProfileGenerator upg = UnlimitedProfileGenerator.newInstance();

  @Test
  public void generateSerialUnlimited() {
    SimpleProfileCollector collector = new SimpleProfileCollector();

    long start = System.currentTimeMillis();

    while(true) {
      long now = System.currentTimeMillis();

      if (start < now - DURACTION) {
        break;
      }

      Profile profile = upg.generateProfile();
      collector.collect(profile);
    }

    String text = MessageFormat.format("{0} : count = {1}, in collector = {2}", name.getMethodName(), collector.getCount(), collector.getCollected().size());
    System.out.println(text);
  }

  @Test
   public void generateSerialLimited() {
    SimpleProfileCollector collector = new SimpleProfileCollector();

    long start = System.currentTimeMillis();
    Iterator<Profile> iterator = lpg.iterator();

    while(true) {
      long now = System.currentTimeMillis();

      if (start < now - DURACTION) {
        break;
      }

      if (!iterator.hasNext()) {
        break;
      }

      Profile profile = iterator.next();
      collector.collect(profile);
    }

    String text = MessageFormat.format("{0} : count = {1}, in collector = {2}", name.getMethodName(), collector.getCount(), collector.getCollected().size());
    System.out.println(text);

  }

  @Test
  public void generateSerialLimitedWithThread() {
    SimpleProfileCollector collector = new SimpleProfileCollector();

    final Iterator<Profile> iterator = lpg.iterator();

    final Object signal = new Object();

    Thread thread = new Thread() {
      @Override
      public void run() {

        while(!this.isInterrupted()) {
          Profile profile = iterator.next();
          collector.collect(profile);
        }
      }
    };

    thread.start();

    try {
      Thread.sleep(DURACTION);
    } catch (InterruptedException e) {

    }

    thread.interrupt();

    String text = MessageFormat.format("{0} : count = {1}, in collector = {2}", name.getMethodName(), collector.getCount(), collector.getCollected().size());
    System.out.println(text);
  }

  private static class RunnerThread extends Thread {

    SimpleProfileCollector collector;
    final Iterator<Profile> iterator;

    public RunnerThread(ProfileGenerator pg, SimpleProfileCollector sc) {
      this.collector = sc;
      iterator = pg.iterator();
    }

    @Override
    public void run() {

      while(!this.isInterrupted()) {
        Profile profile = iterator.next();
        collector.collect(profile);
      }
    }
  };

  private static class NewPGRunnerThread extends Thread {

    SimpleProfileCollector collector;

    public NewPGRunnerThread(SimpleProfileCollector sc) {
      this.collector = sc;
    }

    @Override
    public void run() {

      while(!this.isInterrupted()) {
        ProfileGenerator lpg = ProfileGenerator.newInstance(1);

        Profile profile = lpg.iterator().next();
        collector.collect(profile);
      }
    }
  };

  @Test
    public void generateSerialLimitedWithMultipleThreadsAndPG() {

    for (int size = 1; size < 4; size++) {

      SimpleProfileCollector collector = new SimpleProfileCollector();

      ProfileGenerator upg = ProfileGenerator.newInstance(LIMIT);

      String text = MessageFormat.format("start {0} : count = {1}, in collector = {2}, size = {3}", name.getMethodName(), collector.getCount(), collector.getCollected().size(), size);
      System.out.println(text);


      final Object signal = new Object();

      List<RunnerThread> threads = new ArrayList<>();
      for(int i = 0; i < size; i++) {
        threads.add(new RunnerThread(upg, collector));
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

  @Test
  public void generateSerialLimitedWithMultipleThreadsAndNewPG() {

    for (int size = 1; size < 4; size++) {

      SimpleProfileCollector collector = new SimpleProfileCollector();


      String text = MessageFormat.format("start {0} : count = {1}, in collector = {2}, size = {3}", name.getMethodName(), collector.getCount(), collector.getCollected().size(), size);
      System.out.println(text);


      final Object signal = new Object();

      List<NewPGRunnerThread> threads = new ArrayList<>();
      for(int i = 0; i < size; i++) {
        threads.add(new NewPGRunnerThread(collector));
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

  @Test
  public void generateSerialLimitedWithMultipleThreads() {

    for (int size = 1; size < 4; size++) {

      SimpleProfileCollector collector = new SimpleProfileCollector();

      String text = MessageFormat.format("start {0} : count = {1}, in collector = {2}, size = {3}", name.getMethodName(), collector.getCount(), collector.getCollected().size(), size);
      System.out.println(text);


      final Iterator<Profile> iterator = lpg.iterator();

      final Object signal = new Object();

      List<RunnerThread> threads = new ArrayList<>();
      for(int i = 0; i < size; i++) {
        threads.add(new RunnerThread(lpg, collector));
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

  @Test
  public void generateSerialWithStream() {
    SimpleProfileCollector collector = new SimpleProfileCollector();

    final Object signal = new Object();

    Thread thread = new Thread() {
      @Override
      public void run() {
        lpg.stream().forEach(profile -> {
          if (this.isInterrupted()) {
            throw new RuntimeException();
          }
          collector.collect(profile);
        });
      }
    };

    thread.start();

    try {
      Thread.sleep(DURACTION);
    } catch (InterruptedException e) {

    }

    thread.interrupt();

    String text = MessageFormat.format("{0} : count = {1}, in collector = {2}", name.getMethodName(), collector.getCount(), collector.getCollected().size());
    System.out.println(text);

    try {
      Thread.sleep(DURACTION);
    } catch (InterruptedException e) {

    }

    text = MessageFormat.format("{0} : count = {1}, in collector = {2}", name.getMethodName(), collector.getCount(), collector.getCollected().size());
    System.out.println(text);
  }

  @Test
  public void generateSerialWithParralelStream() {
    SimpleProfileCollector collector = new SimpleProfileCollector();

    final Object signal = new Object();

    Thread thread = new Thread() {
      @Override
      public void run() {
        lpg.stream().parallel().forEach(profile -> {
          if (this.isInterrupted()) {
            throw new RuntimeException();
          }
          collector.collect(profile);
        });
      }
    };

    thread.start();

    try {
      Thread.sleep(DURACTION);
    } catch (InterruptedException e) {

    }

    thread.interrupt();

    String text = MessageFormat.format("{0} : count = {1}, in collector = {2}", name.getMethodName(), collector.getCount(), collector.getCollected().size());
    System.out.println(text);

    try {
      Thread.sleep(DURACTION);
    } catch (InterruptedException e) {

    }

    text = MessageFormat.format("{0} : count = {1}, in collector = {2}", name.getMethodName(), collector.getCount(), collector.getCollected().size());
    System.out.println(text);
  }
}
