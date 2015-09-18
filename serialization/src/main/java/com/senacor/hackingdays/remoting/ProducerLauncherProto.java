package com.senacor.hackingdays.remoting;

import static com.senacor.hackingdays.config.ConfigHelper.createConfig;

import java.util.concurrent.TimeUnit;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.google.common.base.Stopwatch;
import com.senacor.hackingdays.actor.GenerateMessages;
import com.senacor.hackingdays.actor.ProducerActorProto;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

public class ProducerLauncherProto {

    private static final int COUNT = 100_000;
    //TODO edit IP here
    private static final String CONSUMER_IP = "172.16.73.211";
    private static final String CONSUMER_PORT = "2552";

    public static void main(String[] args) throws Exception {
        ActorSystem actorSystem = ActorSystem.create("producer-actorsystem",
            createConfig("proto", "com.senacor.hackingdays.serializer.ProtoBufSerilalizer", "application-remote.conf"));

        ActorRef remoteConsumer = findRemoteConsumer(actorSystem);

        ActorRef producer = actorSystem.actorOf(Props.create(ProducerActorProto.class, () -> new ProducerActorProto(remoteConsumer)), "producer");
        System.out.println("Started producer " + producer);

        sendDataAndWaitForCompletion(producer);
    }

    private static void sendDataAndWaitForCompletion(ActorRef producer) throws Exception {
        Timeout timeout = Timeout.apply(50, TimeUnit.SECONDS);
        Stopwatch stopwatch = Stopwatch.createStarted();
        Future<Object> ask = Patterns.ask(producer, new GenerateMessages(COUNT), timeout);
        Await.result(ask, timeout.duration());
        stopwatch.stop();
        System.err
            .println(String.format("Sending %s dating profiles with %s took %s millis.", COUNT, "java", stopwatch.elapsed(TimeUnit.MILLISECONDS)));
    }

    private static ActorRef findRemoteConsumer(ActorSystem actorSystem) throws Exception {
        ActorSelection actorSelection =
            actorSystem.actorSelection("akka.tcp://consumer-actorsystem@" + CONSUMER_IP + ":" + CONSUMER_PORT + "/user/consumer");
        Future<ActorRef> consumerFuture = actorSelection.resolveOne(Timeout.apply(2, TimeUnit.SECONDS));
        return Await.result(consumerFuture, Duration.create(2, TimeUnit.SECONDS));
    }
}
