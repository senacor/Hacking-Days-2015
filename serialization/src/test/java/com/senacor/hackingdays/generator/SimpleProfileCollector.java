package com.senacor.hackingdays.generator;

import com.senacor.hackingdays.serialization.data.Profile;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created with IntelliJ IDEA.
 * User: lrscholz
 * Date: 19/09/15
 * Time: 10:13
 * To change this template use File | Settings | File Templates.
 */
public class SimpleProfileCollector {
  private volatile int count = 0;
  private final static int LIMIT = 100;

  private Deque<Profile> collected = new LinkedList<>();

  public int getCount() {
    return count;
  }

  public Deque<Profile> getCollected() {
    return collected;
  }


  public  void collect(Profile profile) {
    count++;
  }

  public synchronized void xxcollect(Profile profile) {

    collected.addLast(profile);

    if (collected.size() > LIMIT) {
      collected.removeFirst();
    }

    count++;
  }
}
