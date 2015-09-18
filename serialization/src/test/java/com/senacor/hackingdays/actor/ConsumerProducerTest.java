package com.senacor.hackingdays.actor;

import static com.senacor.hackingdays.config.ConfigHelper.*;
import static junitparams.JUnitParamsRunner.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.base.Stopwatch;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import com.senacor.hackingdays.serialization.data.Profile;
import com.senacor.hackingdays.serialization.data.generate.ProfileGenerator;
import com.senacor.hackingdays.serialization.data.writer.XMLProfileWriter;
import com.senacor.hackingdays.serializer.ProtoBufSerilalizer;
import com.senacor.hackingdays.serializer.SerializerTest;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.serialization.JavaSerializer;
import akka.serialization.SerializationExtension;
import akka.serialization.Serializer;
import akka.util.Timeout;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import scala.concurrent.Await;
import scala.concurrent.Future;

@RunWith(JUnitParamsRunner.class)
public class ConsumerProducerTest {

	public static final int COUNT = 1_000_000;

	private static boolean isSerializer(Class<?> cls) {
		return Serializer.class.isAssignableFrom(cls);
	}

	@Test
	@Parameters(method = "serializers")
	public void sendMessages(String serializerName, String fqcn) throws Exception {
		ActorSystem actorSystem = ActorSystem.create("producer-consumer-actorsystem",
				createConfig(serializerName, fqcn));
		ActorRef consumer = actorSystem.actorOf(Props.create(ConsumerActor.class, () -> new ConsumerActor()),
				"consumer");
		ActorRef producer = actorSystem.actorOf(Props.create(ProducerActor.class, () -> new ProducerActor(consumer)),
				"producer");

		Timeout timeout = Timeout.apply(25, TimeUnit.SECONDS);

		Stopwatch stopwatch = Stopwatch.createStarted();
		Future<Object> ask = Patterns.ask(producer, new GenerateMessages(COUNT), timeout);
		Await.result(ask, timeout.duration());
		stopwatch.stop();
		actorSystem.shutdown();
		actorSystem.awaitTermination();
		System.err.println(String.format("Sending %s dating profiles with %s took %s millis.", COUNT, serializerName,
				stopwatch.elapsed(TimeUnit.MILLISECONDS)));
	}

	@Test
	@Parameters(method = "serializerProtoBuf")
	public void sendMessagesProtoBuf(String serializerName, String fqcn) throws Exception {
		ActorSystem actorSystem = ActorSystem.create("producer-consumer-actorsystem-protobuf",
				createConfig(serializerName, fqcn));
		ActorRef consumer = actorSystem.actorOf(Props.create(ConsumerActorProto.class, () -> new ConsumerActorProto()),
				"consumer");
		ActorRef producer = actorSystem
				.actorOf(Props.create(ProducerActorProto.class, () -> new ProducerActorProto(consumer)), "producer");

		Timeout timeout = Timeout.apply(25, TimeUnit.SECONDS);

		Stopwatch stopwatch = Stopwatch.createStarted();
		Future<Object> ask = Patterns.ask(producer, new GenerateMessages(COUNT), timeout);
		Await.result(ask, timeout.duration());
		stopwatch.stop();
		actorSystem.shutdown();
		actorSystem.awaitTermination();
		System.err.println(String.format("Sending %s dating profiles with %s took %s millis.", COUNT, serializerName,
				stopwatch.elapsed(TimeUnit.MILLISECONDS)));
	}

	@Test
	@Parameters(method = "serializers")
	public void calculateObjectSize(String serializerName, String fqcn) throws Exception {
		ActorSystem actorSystem = ActorSystem.create("producer-consumer-actorsystem",
				createConfig(serializerName, fqcn));

		Profile p = ProfileGenerator.newInstance(1).iterator().next();
		int length = SerializationExtension.get(actorSystem).serializerFor(Profile.class).toBinary(p).length;
		Thread.sleep(1000);
		actorSystem.shutdown();
		actorSystem.awaitTermination();

		System.err.println(String.format("Serializing a Profile with %s took %s bytes.", serializerName, length));
	}

	static Object[] serializers() throws IOException {

		final List<Class<?>> testExceptions = Arrays.asList(ProtoBufSerilalizer.class);

		Set<ClassInfo> classInfos = ClassPath.from(Serializer.class.getClassLoader())
				.getTopLevelClasses(SerializerTest.class.getPackage().getName());
		Set<Object[]> resultSet = new HashSet<Object[]>();

		resultSet.add($(JavaSerializer.class.getSimpleName(), JavaSerializer.class.getCanonicalName()));

		for (ClassInfo info : classInfos) {
			Class<?> clazz = info.load();
			if (isSerializer(clazz) && !testExceptions.contains(clazz))
				resultSet.add($(clazz.getSimpleName(), clazz.getCanonicalName()));
		}
		
		return resultSet.toArray();
		
	}
	// private Config overrideConfig(String serializerName, String fqcn) {
	// String configSnippet = String.format("akka {\n" +
	// " actor {\n" +
	// " serializers {\n" +
	// " %s = \"%s\"\n" +
	// " }\n" +
	// "\n" +
	// " serialization-bindings {\n" +
	// " \"com.senacor.hackingdays.serialization.data.Profile\" = %s\n" +
	// " }\n" +
	// " }\n" +
	// "}", serializerName, fqcn, serializerName);
	//
	// Config overrides = ConfigFactory.parseString(configSnippet);
	// return overrides.withFallback(ConfigFactory.load());
	// }

	@SuppressWarnings("unusedDeclaration")
	static Object[] serializerProtoBuf() {
		return $($("protoBuf", "com.senacor.hackingdays.serializer.ProtoBufSerilalizer"));
	}

	@Test
	@Ignore
	public void writeXmlFile() throws Exception {

		try (XMLProfileWriter writer = new XMLProfileWriter(new File("src/main/resources/database.xml"))) {
			ProfileGenerator generator = ProfileGenerator.newInstance(1_000_000);
			generator.stream().forEach(writer::write);

		}
	}

}