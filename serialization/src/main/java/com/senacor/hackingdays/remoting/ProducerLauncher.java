package com.senacor.hackingdays.remoting;

import static com.senacor.hackingdays.config.ConfigHelper.createConfig;

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
import akka.util.Timeout;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.senacor.hackingdays.actor.GenerateMessages;
import com.senacor.hackingdays.actor.ProducerActor;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

public class ProducerLauncher {

    //TODO edit IP here
//    private static final String CONSUMER_IP = "172.16.73.211";
    private static final String CONSUMER_IP = "169.254.17.33";
    private static final int COUNT = 100_000;
    private static final List<ProducerProfile> producerProfiles = Lists
        .newArrayList(new ProducerProfile("java", "akka.serialization.JavaSerializer", "2552"),
            new ProducerProfile("kryo", "com.senacor.hackingdays.serializer.KryoSerializer", "2553"),
            new ProducerProfile("jackson", "com.senacor.hackingdays.serializer.JacksonSerializer", "2554"),
            //            new ProducerProfile("gson", "com.senacor.hackingdays.serializer.GsonSerializer", "2555"),
            new ProducerProfile("gson2", "com.senacor.hackingdays.serializer.GsonSerializer2", "2556"),
            new ProducerProfile("xml", "com.senacor.hackingdays.serializer.XStreamXMLSerializer", "2557"),
            //            new ProducerProfile("json-io", "com.senacor.hackingdays.serializer.JsonIoSerializer", "2558"),
            new ProducerProfile("fast-ser", "com.senacor.hackingdays.serializer.FastSerializer", "2559"),
            new ProducerProfile("unsafe", "com.senacor.hackingdays.serializer.UnsafeSerializer", "2561"),
            new ProducerProfile("refl-kryo", "com.senacor.hackingdays.serializer.ReflectionKryoSerializer", "2562"),
            //            new ProducerProfile("jaxb", "com.senacor.hackingdays.serializer.JAXBSerializer", "2563")
            new ProducerProfile("capn-proto", "com.senacor.hackingdays.serializer.CapnProtoSerializer", "2564"),
            new ProducerProfile("capn-proto-opt", "com.senacor.hackingdays.serializer.CapnProtoOptimizedSerializer", "2565"));
    private static Set<Result> resultSet = new TreeSet<>();

    public static void main(String[] args) throws Exception {
        producerProfiles.forEach(ProducerLauncher::sendForSerializer);
        long totalTime = resultSet.stream().collect(Collectors.summarizingLong(result -> result.time)).getSum();
        resultSet.forEach(result -> System.out
            .println(result.name + " -> " + result.time + " milliseconds (~" + (result.time * 100L / totalTime) + "% of the total time)"));
    }

    private static void sendForSerializer(ProducerProfile producerProfile) {
        ActorSystem actorSystem = null;
        try {
            actorSystem = ActorSystem.create("producer-actorsystem",
                createConfig(producerProfile.serializerName, producerProfile.fqcn, "application-remote.conf")); // 1833 Millis

            ActorRef remoteConsumer = findRemoteConsumer(actorSystem, producerProfile.port);

            ActorRef producer = actorSystem.actorOf(Props.create(ProducerActor.class, () -> new ProducerActor(remoteConsumer)), "producer");
            System.out.println("Startet producer " + producer);

            sendDataAndWaitForCompletion(producer, producerProfile.serializerName);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (actorSystem != null) {
                actorSystem.shutdown();
                actorSystem.awaitTermination();
            }
        }
    }

    private static ActorRef findRemoteConsumer(ActorSystem actorSystem, String port) throws Exception {
        ActorSelection actorSelection = actorSystem.actorSelection("akka.tcp://consumer-actorsystem@" + CONSUMER_IP + ":" + port + "/user/consumer");
        Future<ActorRef> consumerFuture = actorSelection.resolveOne(Timeout.apply(2, TimeUnit.SECONDS));
        return Await.result(consumerFuture, Duration.create(2, TimeUnit.SECONDS));
    }

    private static void sendDataAndWaitForCompletion(ActorRef producer, String serializerName) throws Exception {
        Timeout timeout = Timeout.apply(90, TimeUnit.SECONDS);
        Stopwatch stopwatch = Stopwatch.createStarted();
        Future<Object> ask = Patterns.ask(producer, new GenerateMessages(COUNT), timeout);
        Await.result(ask, timeout.duration());
        stopwatch.stop();
        long elapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        resultSet.add(new Result(elapsed, serializerName));
        System.err.println(String.format("Sending %s dating profiles with %s took %s millis.", COUNT, serializerName, elapsed));
    }

    private static class ProducerProfile {
        String serializerName;
        String fqcn;
        String port;

        public ProducerProfile(String serializerName, String fqcn, String port) {
            this.serializerName = serializerName;
            this.fqcn = fqcn;
            this.port = port;
        }
    }

    private static class Result implements Comparable<Result> {
        private long time;
        private String name;

        public Result(long time, String name) {
            this.time = time;
            this.name = name;
        }

        @Override
        public int compareTo(Result o) {
            return time > o.time ? 1 : -1;
        }
    }
}
