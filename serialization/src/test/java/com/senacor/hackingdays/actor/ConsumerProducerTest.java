package com.senacor.hackingdays.actor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.google.common.base.Stopwatch;
import com.senacor.hackingdays.serialization.data.generate.ProfileGenerator;
import com.senacor.hackingdays.serialization.data.writer.XMLProfileWriter;
import org.junit.Ignore;
import org.junit.Test;
import scala.concurrent.Await;
import scala.concurrent.Future;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class ConsumerProducerTest {


    public static final int COUNT = 1;


    @Test
    public void sendMessages() throws Exception {
        ActorSystem actorSystem = ActorSystem.create("producer-consumer-actorsystem");
        ActorRef consumer = actorSystem.actorOf(Props.create(ConsumerActor.class, () -> new ConsumerActor()), "consumer");
        ActorRef producer = actorSystem.actorOf(Props.create(ProducerActor.class, () -> new ProducerActor(consumer)), "producer");

        Timeout timeout = Timeout.apply(25, TimeUnit.SECONDS);

        Stopwatch stopwatch = Stopwatch.createStarted();
        Future<Object> ask = Patterns.ask(producer, new GenerateMessages(COUNT), timeout);
        Await.result(ask, timeout.duration());
        stopwatch.stop();
        actorSystem.shutdown();
        actorSystem.awaitTermination();
        System.err.println("Sending " + COUNT + " dating profiles took " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " millis");
    }

    @Test
    @Ignore
    public void writeXmlFile() throws Exception {

        try(XMLProfileWriter writer = new XMLProfileWriter(new File("src/main/resources/database.xml"))) {
            ProfileGenerator generator = ProfileGenerator.newInstance(1_000_000);
            generator.stream().forEach(writer::write);

        }
    }

}