package com.senacor.hackingdays.serialization.data.generate;

import akka.util.Timeout;
import com.google.common.base.Stopwatch;
import com.senacor.hackingdays.serialization.data.Profile;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by hmarginean on 19/09/15.
 */
public class ParallelProfileGenerator {
    private int threadCount;
    private int durationSec;
    private Queue<Profile> profiles;
    private ProfileGenerator profileGenerator;
    private volatile int countProfiles = 0;

    public ParallelProfileGenerator(int threadCount, int durationSec) {
        this.threadCount = threadCount;
        this.durationSec = durationSec;
        profiles = new ConcurrentLinkedQueue<Profile>();
    }

    public void generate() {

        Runnable task = () -> {
            String name = Thread.currentThread().getName();
            System.out.println("Thread " + name + " started");

            while( true ){
                ProfileGenerator.newInstance(100);
                countProfiles+= 100;
            }
        };

        List<Thread> threads = new ArrayList<>();
        for(int i = 0 ; i < threadCount; i++){
            threads.add(startThread(task));
        }

        try {
            Thread.sleep(durationSec * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        threads.stream().forEach(thread -> {
            thread.interrupt();
        });

    }

    private Thread startThread(Runnable task){
        Thread thread = new Thread(task);
        thread.start();
        return thread;

    }

    public void showProfiles(){
        profiles.stream().forEach(System.out::println);
        System.out.println("Count " + countProfiles);
    }




}
