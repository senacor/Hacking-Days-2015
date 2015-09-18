package com.senacor.hackingdays.remoting;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.senacor.hackingdays.actor.ConsumerActor;
import com.senacor.hackingdays.actor.ConsumerActorProto;

import static com.senacor.hackingdays.config.ConfigHelper.createConfig;

public class ConsumerLauncherProto {


    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("consumer-actorsystem", createConfig("java", "akka.serialization.JavaSerializer"));
        ActorRef consumer = actorSystem.actorOf(Props.create(ConsumerActorProto.class, () -> new ConsumerActorProto()), "consumer");
        System.out.println("Startet consumer " + consumer);
        //actorSystem.shutdown(); akka.tcp://consumer-actorsystem@172.16.236.207:2553/user/consumer
    }
}
