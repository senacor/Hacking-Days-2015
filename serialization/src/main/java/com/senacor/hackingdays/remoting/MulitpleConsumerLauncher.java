package com.senacor.hackingdays.remoting;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.senacor.hackingdays.actor.ConsumerActor;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import scala.remote;

import java.util.stream.Stream;

import static com.senacor.hackingdays.config.ConfigHelper.createConfig;

public class MulitpleConsumerLauncher {


    public static void main(String[] args) {
        Stream.of(
                new ProducerProfile("java", "akka.serialization.JavaSerializer", "2552"),
                new ProducerProfile("kryo", "com.senacor.hackingdays.serializer.KryoSerializer", "2553"),
                new ProducerProfile("jackson", "com.senacor.hackingdays.serializer.JacksonSerializer", "2554"),
                new ProducerProfile("gson", "com.senacor.hackingdays.serializer.GsonSerializer", "2555"),
                new ProducerProfile("gson2", "com.senacor.hackingdays.serializer.GsonSerializer2", "2556"),
                new ProducerProfile("xml", "com.senacor.hackingdays.serializer.XStreamXMLSerializer", "2557"),
                new ProducerProfile("json-io", "com.senacor.hackingdays.serializer.JsonIoSerializer", "2558"),
                new ProducerProfile("fast-ser", "com.senacor.hackingdays.serializer.FastSerializer", "2559"),
                new ProducerProfile("kryo", "com.senacor.hackingdays.serializer.KryoSerializer", "2560"),
                new ProducerProfile("unsafe", "com.senacor.hackingdays.serializer.UnsafeSerializer", "2561"),
                new ProducerProfile("refl-kryo", "com.senacor.hackingdays.serializer.ReflectionKryoSerializer", "2562"),
                new ProducerProfile("capn-proto", "com.senacor.hackingdays.serializer.CapnProtoSerializer", "2564"),
                new ProducerProfile("capn-proto-opt", "com.senacor.hackingdays.serializer.CapnProtoOptimizedSerializer", "2565"),
                new ProducerProfile("proto", "com.senacor.hackingdays.serializer.ProtoBufSerilalizer", "2566")
        ).forEach(triple -> {

            Config config = createConfig(triple.name, triple.fqcn, "application-remote.conf");
            Config mergedConfig = overridePort(triple.port).withFallback(config);
            ActorSystem actorSystem = ActorSystem.create("consumer-actorsystem", mergedConfig);

            ActorRef consumer = actorSystem.actorOf(Props.create(ConsumerActor.class, () -> new ConsumerActor()), "consumer");
            System.out.println("Startet consumer " + consumer);

        });
    }

    private static Config overridePort(String port) {
        return ConfigFactory.parseString("akka.remote.netty.tcp.port = " + port);
    }


    private static class ProducerProfile {
        private final String name;
        private final String fqcn;
        private final String port;

        public ProducerProfile(String name, String fqcn, String port) {
            this.name = name;
            this.fqcn = fqcn;
            this.port = port;
        }
    }
}