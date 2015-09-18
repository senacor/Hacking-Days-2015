package com.senacor.hackingdays.remoting;

import static com.senacor.hackingdays.config.ConfigHelper.createConfig;
import static com.senacor.hackingdays.remoting.ProducerProfile.createProfile;
import static com.senacor.hackingdays.remoting.ProducerProfile.createProtoBufProfile;
import static com.senacor.hackingdays.remoting.ProducerProfile.createThriftProfile;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.serialization.JavaSerializer;
import akka.util.Timeout;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.senacor.hackingdays.actor.GenerateMessages;
import com.senacor.hackingdays.actor.ProducerActor;
import com.senacor.hackingdays.serializer.CapnProtoOptimizedSerializer;
import com.senacor.hackingdays.serializer.CapnProtoSerializer;
import com.senacor.hackingdays.serializer.DeflatingHessian2Serializer;
import com.senacor.hackingdays.serializer.FastSerializer;
import com.senacor.hackingdays.serializer.GsonSerializer;
import com.senacor.hackingdays.serializer.GsonSerializer2;
import com.senacor.hackingdays.serializer.Hessian2Serializer;
import com.senacor.hackingdays.serializer.JacksonSerializer;
import com.senacor.hackingdays.serializer.JsonIoSerializer;
import com.senacor.hackingdays.serializer.KryoSerializer;
import com.senacor.hackingdays.serializer.ProtoBufSerilalizer;
import com.senacor.hackingdays.serializer.ReflectionKryoSerializer;
import com.senacor.hackingdays.serializer.UnsafeSerializer;
import com.senacor.hackingdays.serializer.XStreamXMLSerializer;
import com.senacor.hackingdays.serializer.thrift.ThriftSerializerTBinary;
import com.senacor.hackingdays.serializer.thrift.ThriftSerializerTTuple;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

public class ProducerLauncher {

    //TODO edit IP here
    private static final String CONSUMER_IP = "172.16.73.211";
    //    private static final String CONSUMER_IP = "169.254.17.33";
    private static final int COUNT = 100_000;
    private static final List<ProducerProfile> producerProfiles = Lists
        .newArrayList(createProfile("java", JavaSerializer.class.getName(), "2552"),
            createProfile("kryo", KryoSerializer.class.getName(), "2553"),
            createProfile("jackson", JacksonSerializer.class.getName(), "2554"),
            createProfile("gson", GsonSerializer.class.getName(), "2555"),
            createProfile("gson2", GsonSerializer2.class.getName(), "2556"),
            createProfile("xml", XStreamXMLSerializer.class.getName(), "2557"),
            createProfile("json-io", JsonIoSerializer.class.getName(), "2558"),
            createProfile("fast-ser", FastSerializer.class.getName(), "2559"),
            createProfile("hessian2", Hessian2Serializer.class.getName(), "2560"),
            createProfile("unsafe", UnsafeSerializer.class.getName(), "2561"),
            createProfile("refl-kryo", ReflectionKryoSerializer.class.getName(), "2562"),
            createProfile("defl-hessian2", DeflatingHessian2Serializer.class.getName(), "2563"),
            createProfile("capn-proto", CapnProtoSerializer.class.getName(), "2564"),
            createProfile("capn-proto-opt", CapnProtoOptimizedSerializer.class.getName(), "2565"),
            createProtoBufProfile("proto", ProtoBufSerilalizer.class.getName(), "2566"),
            createThriftProfile("thrift-bin", ThriftSerializerTBinary.class.getName(), "2567"),
            createThriftProfile("thrift-tuple", ThriftSerializerTTuple.class.getName(), "2568"));
    private static Set<Result> resultSet = new TreeSet<>();

    public static void main(String[] args) throws Exception {
        producerProfiles.forEach(ProducerLauncher::sendForSerializer);
        long totalTime = resultSet.stream().collect(Collectors.summarizingLong(Result::getTime)).getSum();
        resultSet.forEach(result -> System.out
            .printf("%s -> %d milliseconds (~%.1f%% of the total time)", result.getName(), result.getTime(), (result.getTime() * 100d / totalTime)));
    }

    private static void sendForSerializer(ProducerProfile producerProfile) {
        ActorSystem actorSystem = null;
        try {
            actorSystem = ActorSystem.create("producer-actorsystem",
                createConfig(producerProfile.serializerName, producerProfile.fqcn, "application-remote.conf")); // 1833 Millis

            ActorRef remoteConsumer = findRemoteConsumer(actorSystem, producerProfile.port);

            ActorRef producer = actorSystem.actorOf(Props.create(ProducerActor.class, () -> new ProducerActor(remoteConsumer)), "producer");
            System.out.println("Startet producer " + producer);

            sendDataAndWaitForCompletion(producer, producerProfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (actorSystem != null) {
                actorSystem.shutdown();
                actorSystem.awaitTermination(Duration.create(3, TimeUnit.SECONDS));
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static ActorRef findRemoteConsumer(ActorSystem actorSystem, String port) throws Exception {
        ActorSelection actorSelection = actorSystem.actorSelection("akka.tcp://consumer-actorsystem@" + CONSUMER_IP + ":" + port + "/user/consumer");
        Future<ActorRef> consumerFuture = actorSelection.resolveOne(Timeout.apply(2, TimeUnit.SECONDS));
        return Await.result(consumerFuture, Duration.create(2, TimeUnit.SECONDS));
    }

    private static void sendDataAndWaitForCompletion(ActorRef producer, ProducerProfile producerProfile) throws Exception {
        System.err.println(String.format("Sending %s dating profiles with %s now.", COUNT, producerProfile.serializerName));
        Timeout timeout = Timeout.apply(90, TimeUnit.SECONDS);
        Stopwatch stopwatch = Stopwatch.createStarted();
        Future<Object> ask = Patterns.ask(producer, new GenerateMessages(COUNT, producerProfile.profileClass), timeout);
        Await.result(ask, timeout.duration());
        stopwatch.stop();
        long elapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        resultSet.add(new Result(elapsed, producerProfile.serializerName));
        System.err.println(String.format("Sending %s dating profiles with %s took %s millis.", COUNT, producerProfile.serializerName, elapsed));
    }

}
