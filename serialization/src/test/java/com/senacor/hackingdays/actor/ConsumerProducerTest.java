package com.senacor.hackingdays.actor;

import static com.senacor.hackingdays.config.ConfigHelper.*;
import static junitparams.JUnitParamsRunner.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import com.senacor.hackingdays.serialization.data.generate.ProfileGeneratorThrift;
import com.senacor.hackingdays.serialization.data.Activity;
import com.senacor.hackingdays.serialization.data.Location;
import com.senacor.hackingdays.serialization.data.Seeking;
import com.senacor.hackingdays.serialization.data.generate.ProfileProtoGenerator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.base.Stopwatch;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import com.senacor.hackingdays.serialization.data.Profile;
import com.senacor.hackingdays.serialization.data.generate.ProfileGenerator;
import com.senacor.hackingdays.serializer.ProtoBufSerilalizer;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.serialization.JavaSerializer;
import akka.serialization.SerializationExtension;
import akka.serialization.Serializer;
import akka.util.Timeout;
import com.google.common.base.Stopwatch;
import com.senacor.hackingdays.serialization.data.Profile;
import com.senacor.hackingdays.serialization.data.generate.ProfileGenerator;
import com.senacor.hackingdays.serialization.data.generate.ProfileProtoGenerator;
import com.senacor.hackingdays.serialization.data.proto.ProfileProtos;
import com.senacor.hackingdays.serialization.data.writer.XMLProfileWriter;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import scala.concurrent.Await;
import scala.concurrent.Future;

@RunWith(JUnitParamsRunner.class)
public class ConsumerProducerTest {

    public static final int COUNT = 100_000;
    
	private static boolean isSerializer(Class<?> cls) {
		return Serializer.class.isAssignableFrom(cls);
	}

    @Test
    @Parameters(method = "serializers")
    public void sendMessages(String serializerName, String fqcn) throws Exception {
        ActorSystem actorSystem = ActorSystem.create("producer-consumer-actorsystem", createConfig(serializerName, fqcn));
        ActorRef consumer = actorSystem.actorOf(Props.create(ConsumerActor.class, () -> new ConsumerActor()), "consumer");
        ActorRef producer = actorSystem.actorOf(Props.create(ProducerActor.class, () -> new ProducerActor(consumer)), "producer");

        Timeout timeout = Timeout.apply(25, TimeUnit.SECONDS);

        Stopwatch stopwatch = Stopwatch.createStarted();
        Future<Object> ask = Patterns.ask(producer, new GenerateMessages(COUNT), timeout);
        Await.result(ask, timeout.duration());
        stopwatch.stop();
        shutdown(actorSystem);
        System.err.println(
        String.format("Sending %s dating profiles with %25s took %4s millis.", COUNT, serializerName,
            stopwatch.elapsed(TimeUnit.MILLISECONDS)));
    }

    private void shutdown(ActorSystem actorSystem) {
        actorSystem.shutdown();
        actorSystem.awaitTermination();
    }

    @Test
    @Parameters(method = "serializerProtoBuf")
    public void sendMessagesProtoBuf(String serializerName, String fqcn) throws Exception {
        ActorSystem actorSystem = ActorSystem.create("producer-consumer-actorsystem-actorsystem", createConfig(serializerName, fqcn));
        ActorRef consumer = actorSystem.actorOf(Props.create(ConsumerActor.class, () -> new ConsumerActor()), "consumer");
        ActorRef producer = actorSystem.actorOf(Props.create(ProducerActor.class, () -> new ProducerActor(consumer)), "producer");

        Timeout timeout = Timeout.apply(25, TimeUnit.SECONDS);

        Stopwatch stopwatch = Stopwatch.createStarted();
        Future<Object> ask = Patterns.ask(producer, new GenerateMessages(COUNT, ProfileProtoGenerator.class), timeout);
        Await.result(ask, timeout.duration());
        stopwatch.stop();
        shutdown(actorSystem);
    System.err.println(String.format("Sending %s dating profiles with %25s took %4s millis.", COUNT, serializerName,
        stopwatch.elapsed(TimeUnit.MILLISECONDS)));
    }

  @Test
  @Parameters(method = "serializerThrift")
  public void sendMessagesThrift(String serializerName, String fqcn) throws Exception {
    ActorSystem actorSystem = ActorSystem.create("producer-consumer-actorsystem-actorsystem", createConfig(serializerName, fqcn));
    ActorRef consumer = actorSystem.actorOf(Props.create(ConsumerActor.class, () -> new ConsumerActor()), "consumer");
    ActorRef producer = actorSystem.actorOf(Props.create(ProducerActor.class, () -> new ProducerActor(consumer)), "producer");

    Timeout timeout = Timeout.apply(25, TimeUnit.SECONDS);

    Stopwatch stopwatch = Stopwatch.createStarted();
    Future<Object> ask = Patterns.ask(producer, new GenerateMessages(COUNT, ProfileGeneratorThrift.class), timeout);
    Await.result(ask, timeout.duration());
    stopwatch.stop();
    shutdown(actorSystem);
    System.err.println(String.format("Sending %s dating profiles with %25s took %4s millis.", COUNT, serializerName,
        stopwatch.elapsed(TimeUnit.MILLISECONDS)));
  }

    @Test
    @Parameters(method = "serializers")
    public void calculateObjectSize(String serializerName, String fqcn) throws Exception {
      ActorSystem actorSystem = ActorSystem.create("producer-consumer-actorsystem", createConfig(serializerName, fqcn));

      Profile p = ProfileGenerator.newProfile();
      int length = SerializationExtension.get(actorSystem).serializerFor(Profile.class).toBinary(p).length;
      Thread.sleep(200);
      shutdown(actorSystem);

      System.err.println(String.format("Serializing a Profile with %25s weights %4s bytes.", serializerName, length));
    }

    @Test
    @Parameters(method = "serializerThrift")
    public void calculateObjectSizeThrift(String serializerName, String fqcn) throws Exception {
      ActorSystem actorSystem = ActorSystem.create("producer-consumer-actorsystem", createConfig(serializerName, fqcn));

      com.senacor.hackingdays.serialization.data.thrift.Profile p = ProfileGeneratorThrift.newProfile();
      int length = SerializationExtension.get(actorSystem).serializerFor(com.senacor.hackingdays.serialization.data.thrift.Profile.class).toBinary(p).length;
      Thread.sleep(200);
      shutdown(actorSystem);

      System.err.println(String.format("Serializing a Profile with %25s weights %4s bytes.", serializerName, length));
    }

    @Test
    @Parameters(method = "serializerProtoBuf")
    public void calculateObjectSizeProtoBuf(String serializerName, String fqcn) throws Exception {
        ActorSystem actorSystem = ActorSystem.create("producer-consumer-actorsystem", createConfig(serializerName, fqcn));

        ProfileProtos.Profile p = ProfileProtoGenerator.newInstance(1).iterator().next();
        int length = SerializationExtension.get(actorSystem).serializerFor(ProfileProtos.Profile.class).toBinary(p).length;
        Thread.sleep(200);
        shutdown(actorSystem);

        System.err.println(String.format("Serializing a Profile with %s took %s bytes.", serializerName, length));
    }

    @Test
    @Parameters(method = "serializers")
    public void assertFields(String serializerName, String fqcn) throws Exception {
        ActorSystem actorSystem = ActorSystem.create("producer-consumer-actorsystem", createConfig(serializerName, fqcn));

        Serializer serializer = SerializationExtension.get(actorSystem).serializerFor(com.senacor.hackingdays.serialization.data.thrift.Profile.class);

        Profile input = ProfileGenerator.newProfile();
        Profile output = (Profile) serializer.fromBinary(serializer.toBinary(input), Profile.class);

        shutdown(actorSystem);

        Assert.assertNotNull(output);
        Activity activity = output.getActivity();
        Assert.assertNotNull(activity);
        Location location = output.getLocation();
        Assert.assertNotNull(location);
        Seeking seeking = output.getSeeking();
        Assert.assertNotNull(seeking);
        Assert.assertNotNull(output.getName());

        Assert.assertEquals(input.getActivity().getLastLogin(), activity.getLastLogin());
        Assert.assertEquals(input.getActivity().getLoginCount(), activity.getLoginCount());
        Assert.assertEquals(input.getAge(), output.getAge());
        Assert.assertEquals(input.getGender(), output.getGender());
        Assert.assertEquals(input.getLocation().getCity(), location.getCity());
        Assert.assertEquals(input.getLocation().getState(), location.getState());
        Assert.assertEquals(input.getLocation().getZip(), location.getZip());
        Assert.assertEquals(input.getName(), output.getName());
        Assert.assertEquals(input.getRelationShip(), output.getRelationShip());
        Assert.assertEquals(input.getSeeking().getAgeRange().getLower(), seeking.getAgeRange().getLower());
        Assert.assertEquals(input.getSeeking().getAgeRange().getUpper(), seeking.getAgeRange().getUpper());
        Assert.assertEquals(input.getSeeking().getGender(), seeking.getGender());
    }

  @Test
  @Parameters(method = "serializerThrift")
  public void assertFieldsThrift(String serializerName, String fqcn) throws Exception {
    ActorSystem actorSystem = ActorSystem.create("producer-consumer-actorsystem", createConfig(serializerName, fqcn));

    Serializer serializer = SerializationExtension.get(actorSystem).serializerFor(Profile.class);

    com.senacor.hackingdays.serialization.data.thrift.Profile input = ProfileGeneratorThrift.newProfile();
    com.senacor.hackingdays.serialization.data.thrift.Profile output =
            (com.senacor.hackingdays.serialization.data.thrift.Profile) serializer.fromBinary(serializer.toBinary(input), com.senacor.hackingdays.serialization.data.thrift.Profile.class);

    shutdown(actorSystem);

    Assert.assertNotNull(output);
    Assert.assertNotNull(output.getActivity());
    Assert.assertNotNull(output.getLocation());
    Assert.assertNotNull(output.getSeeking());

    Assert.assertEquals(input.getActivity().getLastLoginTimestamp(), output.getActivity().getLastLoginTimestamp());
    Assert.assertEquals(input.getActivity().getLoginCount(), output.getActivity().getLoginCount());
    Assert.assertEquals(input.getAge(), output.getAge());
    Assert.assertEquals(input.getGender(), output.getGender());
    Assert.assertEquals(input.getLocation().getCity(), output.getLocation().getCity());
    Assert.assertEquals(input.getLocation().getState(), output.getLocation().getState());
    Assert.assertEquals(input.getLocation().getZip(), output.getLocation().getZip());
    Assert.assertEquals(input.getName(), output.getName());
    Assert.assertEquals(input.getRelationShip(), output.getRelationShip());
    Assert.assertEquals(input.getSeeking().getAgeRange().getLower(), output.getSeeking().getAgeRange().getLower());
    Assert.assertEquals(input.getSeeking().getAgeRange().getUpper(), output.getSeeking().getAgeRange().getUpper());
    Assert.assertEquals(input.getSeeking().getGender(), output.getSeeking().getGender());
  }

	static Object[] serializers() throws IOException {

		final List<Class<ProtoBufSerilalizer>> testExceptions = Arrays.asList(ProtoBufSerilalizer.class);

		Set<ClassInfo> classInfos = ClassPath.from(Serializer.class.getClassLoader())
				.getTopLevelClasses("com.senacor.hackingdays.serializer");
		Set<Object[]> resultSet = new TreeSet<>(new Comparator<Object[]>() {

			@Override
			public int compare(Object[] o1, Object[] o2) {
				return o1[0].toString().compareTo(o2[0].toString()) ;
			}
			
		});

		resultSet.add($(JavaSerializer.class.getSimpleName(), JavaSerializer.class.getCanonicalName()));

		for (ClassInfo info : classInfos) {
			Class<?> clazz = info.load();
			if (isSerializer(clazz) && !testExceptions.contains(clazz))
				resultSet.add($(clazz.getSimpleName(), clazz.getCanonicalName()));
		}
		
		return resultSet.toArray();
		
	}
	
    @SuppressWarnings("unusedDeclaration")
    static Object[] serializerProtoBuf() {
        return $(
                $("protoBuf", "com.senacor.hackingdays.serializer.ProtoBufSerilalizer")
        );
    }

    @SuppressWarnings("unusedDeclaration")
    static Object[] serializerThrift() {
      return $(
              $("thrifttuple", "com.senacor.hackingdays.serializer.thrift.ThriftSerializerTTuple"),
              $("thriftbinary", "com.senacor.hackingdays.serializer.thrift.ThriftSerializerTBinary"),
              $("thriftcompact", "com.senacor.hackingdays.serializer.thrift.ThriftSerializerTCompact"),
              $("thriftjson", "com.senacor.hackingdays.serializer.thrift.ThriftSerializerTJSON"),
              /*$("thriftsimplejson", "com.senacor.hackingdays.serializer.thrift.ThriftSerializerTSimpleJSON"), has problems */
              $("thrifttuple", "com.senacor.hackingdays.serializer.thrift.ThriftSerializerTTuple")
      );
    }


}