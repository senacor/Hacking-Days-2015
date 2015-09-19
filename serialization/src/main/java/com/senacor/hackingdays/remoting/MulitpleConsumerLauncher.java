package com.senacor.hackingdays.remoting;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.senacor.hackingdays.actor.ConsumerActor;
import com.senacor.hackingdays.serializer.DeflatingHessian2Serializer;
import com.senacor.hackingdays.serializer.ProtoBufSerilalizer;
import com.senacor.hackingdays.serializer.thrift.ThriftSerializerTBinary;
import com.senacor.hackingdays.serializer.thrift.ThriftSerializerTTuple;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.stream.Stream;

import static com.senacor.hackingdays.config.ConfigHelper.createConfig;

public class MulitpleConsumerLauncher {


    public static void main(String[] args) {
        Stream.of(
                new ConfigOptions("java", "akka.serialization.JavaSerializer", "2552"),
                new ConfigOptions("kryo", "com.senacor.hackingdays.serializer.KryoSerializer", "2553"),
                new ConfigOptions("jackson", "com.senacor.hackingdays.serializer.JacksonSerializer", "2554"),
                new ConfigOptions("gson", "com.senacor.hackingdays.serializer.GsonSerializer", "2555"),
                new ConfigOptions("gson2", "com.senacor.hackingdays.serializer.GsonSerializer2", "2556"),
                new ConfigOptions("xml", "com.senacor.hackingdays.serializer.XStreamXMLSerializer", "2557"),
                new ConfigOptions("json-io", "com.senacor.hackingdays.serializer.JsonIoSerializer", "2558"),
                new ConfigOptions("fast-ser", "com.senacor.hackingdays.serializer.FastSerializer", "2559"),
                new ConfigOptions("hessian2", "com.senacor.hackingdays.serializer.Hessian2Serializer", "2560"),
                new ConfigOptions("unsafe", "com.senacor.hackingdays.serializer.UnsafeSerializer", "2561"),
                new ConfigOptions("refl-kryo", "com.senacor.hackingdays.serializer.ReflectionKryoSerializer", "2562"),
                new ConfigOptions("defl-hessian2", DeflatingHessian2Serializer.class.getName(), "2563"),
                new ConfigOptions("capn-proto", "com.senacor.hackingdays.serializer.CapnProtoSerializer", "2564"),
                new ConfigOptions("capn-proto-opt", "com.senacor.hackingdays.serializer.CapnProtoOptimizedSerializer", "2565"),
                new ConfigOptions("proto", ProtoBufSerilalizer.class.getName(), "2566"),
                new ConfigOptions("thrift-bin", ThriftSerializerTBinary.class.getName(), "2567"),
                new ConfigOptions("thrift-tuple", ThriftSerializerTTuple.class.getName(), "2568")
        ).forEach(triple -> {

            Config config = createConfig(triple.name, triple.fqcn, "application-remote.conf");
            Config mergedConfig = overridePort(triple.port).withFallback(config);
            ActorSystem actorSystem = ActorSystem.create("consumer-actorsystem", mergedConfig);

            ActorRef consumer = actorSystem.actorOf(Props.create(ConsumerActor.class, ConsumerActor::new), "consumer");
            System.out.println("Started consumer " + consumer);

        });
    }

    private static Config overridePort(String port) {
        return ConfigFactory.parseString("akka.remote.netty.tcp.port = " + port);
    }

    private static class ConfigOptions {
        private final String name;
        private final String fqcn;
        private final String port;

        public ConfigOptions(String name, String fqcn, String port) {
            this.name = name;
            this.fqcn = fqcn;
            this.port = port;
        }
    }
}
