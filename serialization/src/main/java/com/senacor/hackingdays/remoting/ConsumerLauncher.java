package com.senacor.hackingdays.remoting;

import akka.actor.ActorRef;
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

public class ConsumerLauncher {


    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("consumer-actorsystem", createConfig("java", "akka.serialization.JavaSerializer"));
        ActorRef consumer = actorSystem.actorOf(Props.create(ConsumerActor.class, () -> new ConsumerActor()), "consumer");
        System.out.println("Startet consumer " + consumer);
        actorSystem.shutdown();
    }
}
