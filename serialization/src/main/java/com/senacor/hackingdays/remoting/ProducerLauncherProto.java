package com.senacor.hackingdays.remoting;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.google.common.base.Stopwatch;
import com.senacor.hackingdays.actor.GenerateMessages;
import com.senacor.hackingdays.actor.ProducerActor;
import com.senacor.hackingdays.actor.ProducerActorProto;
import scala.concurrent.Await;
import scala.concurrent.Future;

import java.util.concurrent.TimeUnit;

import static com.senacor.hackingdays.config.ConfigHelper.createConfig;

public class ProducerLauncherProto {

    private static final int COUNT = 1000;

    public static void main(String[] args) throws Exception {
        ActorSystem actorSystem = ActorSystem.create("producer-actorsystem-protobuf", createConfig("java", "akka.serialization.JavaSerializer"));

        //TODO edit IP here
        ActorRef remoteConsumer = actorSystem.actorFor("akka.tcp://consumer-actorsystem-protobuf@172.16.236.207:2553/user/consumer");

        ActorRef producer = actorSystem.actorOf(Props.create(ProducerActorProto.class, () -> new ProducerActorProto(remoteConsumer)), "producer");
        System.out.println("Startet producer " + producer);


        sendDataAndWaitForCompletion(producer);
    }

    private static void sendDataAndWaitForCompletion(ActorRef producer) throws Exception {
        Timeout timeout = Timeout.apply(50, TimeUnit.SECONDS);
        Stopwatch stopwatch = Stopwatch.createStarted();
        Future<Object> ask = Patterns.ask(producer, new GenerateMessages(COUNT), timeout);
        Await.result(ask, timeout.duration());
        stopwatch.stop();
        System.err.println(String.format("Sending %s dating profiles with %s took %s millis.", COUNT, "java", stopwatch.elapsed(TimeUnit.MILLISECONDS)));
    }
}
