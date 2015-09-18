package com.senacor.hackingdays.remoting;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.senacor.hackingdays.actor.ConsumerActor;

import static com.senacor.hackingdays.config.ConfigHelper.createConfig;

public class ConsumerLauncher {


    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("consumer-actorsystem", createConfig("java", "akka.serialization.JavaSerializer", "application-remote.conf"));
        ActorRef consumer = actorSystem.actorOf(Props.create(ConsumerActor.class, () -> new ConsumerActor()), "consumer");
        System.out.println("Startet consumer " + consumer);
    }
}
