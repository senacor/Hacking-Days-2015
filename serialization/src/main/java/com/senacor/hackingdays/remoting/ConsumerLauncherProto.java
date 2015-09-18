package com.senacor.hackingdays.remoting;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.senacor.hackingdays.actor.ConsumerActor;
import com.senacor.hackingdays.actor.ConsumerActorProto;
import scala.remote;

import static com.senacor.hackingdays.config.ConfigHelper.createConfig;

public class ConsumerLauncherProto {


    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("consumer-actorsystem-protobuf", createConfig("proto", "com.senacor.hackingdays.serializer.ProtoBufSerilalizer", "application-remote.conf"));
        ActorRef consumer = actorSystem.actorOf(Props.create(ConsumerActorProto.class, () -> new ConsumerActorProto()), "consumer");
        System.out.println("Started consumer " + consumer);
    }
}
