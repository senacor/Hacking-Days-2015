package com.senacor.hackingdays.actor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.google.common.base.Stopwatch;
import scala.concurrent.Await;
import scala.concurrent.Future;

import java.util.concurrent.TimeUnit;

public class Launcher {


    public static final int COUNT = 100000;

    public static void main(String[] args) throws Exception {
        ActorSystem actorSystem = ActorSystem.create("producer-consumer-actorsystem");
        ActorRef consumer = actorSystem.actorOf(Props.create(ConsumerActor.class, () -> new ConsumerActor()), "consumer");
        ActorRef producer = actorSystem.actorOf(Props.create(ProducerActor.class, () -> new ProducerActor(consumer)));

        Timeout timeout = Timeout.apply(10, TimeUnit.SECONDS);

        Stopwatch stopwatch = Stopwatch.createStarted();
        Future<Object> ask = Patterns.ask(producer, new GenerateMessages(COUNT), timeout);
        Await.result(ask, timeout.duration());
        stopwatch.stop();
        System.out.println("Sending " + COUNT + " dating profiles took " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " millis");
    }
}
