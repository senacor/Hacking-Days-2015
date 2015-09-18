package com.senacor.hackingdays.actor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.google.common.base.Stopwatch;
import com.senacor.hackingdays.serialization.data.generate.ProfileGenerator;
import com.senacor.hackingdays.serialization.data.writer.XMLProfileWriter;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import scala.concurrent.Await;
import scala.concurrent.Future;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static junitparams.JUnitParamsRunner.$;

@RunWith(JUnitParamsRunner.class)
public class ConsumerProducerTest {


    public static final int COUNT = 100_000;


    @Test
    @Parameters(method = "serializers")
    public void sendMessages(String serializerName, String fqcn) throws Exception {
        ActorSystem actorSystem = ActorSystem.create("producer-consumer-actorsystem", overrideConfig(serializerName, fqcn));
        ActorRef consumer = actorSystem.actorOf(Props.create(ConsumerActor.class, () -> new ConsumerActor()), "consumer");
        ActorRef producer = actorSystem.actorOf(Props.create(ProducerActor.class, () -> new ProducerActor(consumer)), "producer");

        Timeout timeout = Timeout.apply(25, TimeUnit.SECONDS);

        Stopwatch stopwatch = Stopwatch.createStarted();
        Future<Object> ask = Patterns.ask(producer, new GenerateMessages(COUNT), timeout);
        Await.result(ask, timeout.duration());
        stopwatch.stop();
        actorSystem.shutdown();
        actorSystem.awaitTermination();
        System.err.println(String.format("Sending %s dating profiles with %s took %s millis.", COUNT, serializerName, stopwatch.elapsed(TimeUnit.MILLISECONDS)));
    }

    private Config overrideConfig(String serializerName, String fqcn) {
        String configSnippet = String.format("akka {\n" +
                "  actor {\n" +
                "    serializers {\n" +
                "      %s = \"%s\"\n" +
                "    }\n" +
                "\n" +
                "    serialization-bindings {\n" +
                "      \"com.senacor.hackingdays.serialization.data.Profile\" = %s\n" +
                "    }\n" +
                "  }\n" +
                "}", serializerName, fqcn, serializerName);

        Config overrides = ConfigFactory.parseString(configSnippet);
        return overrides.withFallback(ConfigFactory.load());
    }

    @SuppressWarnings("unusedDeclaration")
    static Object[] serializers() {
        return $(
                $("java", "akka.serialization.JavaSerializer"),
                $("json", "com.senacor.hackingdays.serializer.JacksonSerializer")
        );
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