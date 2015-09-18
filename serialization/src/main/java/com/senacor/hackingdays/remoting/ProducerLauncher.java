package com.senacor.hackingdays.remoting;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.google.common.base.Stopwatch;
import com.senacor.hackingdays.actor.ConsumerActor;
import com.senacor.hackingdays.actor.GenerateMessages;
import com.senacor.hackingdays.actor.ProducerActor;
import scala.concurrent.Await;
import scala.concurrent.Future;

import java.util.concurrent.TimeUnit;

import static com.senacor.hackingdays.config.ConfigHelper.createConfig;

public class ProducerLauncher {

    private static final int COUNT = 100;

    public static void main(String[] args) throws Exception {
        ActorSystem actorSystem = ActorSystem.create("producer-actorsystem", createConfig("java", "akka.serialization.JavaSerializer"));

        //TODO edit IP here
        ActorRef remoteConsumer = actorSystem.actorFor("akka.tcp://consumer-actorsystem@10.0.0.1:2553/user/consumer");

        ActorRef producer = actorSystem.actorOf(Props.create(ProducerActor.class, () -> new ProducerActor(remoteConsumer)), "producer");
        System.out.println("Startet producer " + producer);


        sendDataAndWaitForCompletion(actorSystem, producer);
        actorSystem.shutdown();
    }

    private static void sendDataAndWaitForCompletion(ActorSystem actorSystem, ActorRef producer) throws Exception {
        Timeout timeout = Timeout.apply(25, TimeUnit.SECONDS);
        Stopwatch stopwatch = Stopwatch.createStarted();
        Future<Object> ask = Patterns.ask(producer, new GenerateMessages(COUNT), timeout);
        Await.result(ask, timeout.duration());
        stopwatch.stop();
        actorSystem.shutdown();
        actorSystem.awaitTermination();
        System.err.println(String.format("Sending %s dating profiles with %s took %s millis.", COUNT, "java", stopwatch.elapsed(TimeUnit.MILLISECONDS)));
    }
}
