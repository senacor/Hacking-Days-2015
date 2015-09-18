package com.senacor.hackingdays.actor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.serialization.JavaSerializer;
import akka.serialization.SerializationExtension;
import akka.serialization.Serializer;
import akka.util.Timeout;
import com.google.common.base.Stopwatch;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import com.senacor.hackingdays.serialization.data.Activity;
import com.senacor.hackingdays.serialization.data.Location;
import com.senacor.hackingdays.serialization.data.Profile;
import com.senacor.hackingdays.serialization.data.Seeking;
import com.senacor.hackingdays.serialization.data.generate.ProfileGenerator;
import com.senacor.hackingdays.serialization.data.generate.ProfileGeneratorThrift;
import com.senacor.hackingdays.serialization.data.generate.ProfileProtoGenerator;
import com.senacor.hackingdays.serialization.data.proto.ProfileProtos;
import com.senacor.hackingdays.serializer.ProtoBufSerilalizer;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import scala.concurrent.Await;
import scala.concurrent.Future;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import static com.senacor.hackingdays.config.ConfigHelper.createConfig;
import static junitparams.JUnitParamsRunner.$;

@RunWith(JUnitParamsRunner.class)
public class ConsumerProducerTest {

    public static final int COUNT = 10_000;

    private static boolean isSerializer(Class<?> cls) {
        return Serializer.class.isAssignableFrom(cls);
    }

    @Test
    @Parameters(method = "serializers")
    public void sendMessages(String serializerName, String fqcn, Class<?> profileClass) throws Exception {
        ActorSystem actorSystem = ActorSystem.create("producer-consumer-actorsystem", createConfig(serializerName, fqcn));
        ActorRef consumer = actorSystem.actorOf(Props.create(ConsumerActor.class, () -> new ConsumerActor()), "consumer");
        ActorRef producer = actorSystem.actorOf(Props.create(ProducerActor.class, () -> new ProducerActor(consumer)), "producer");

        Timeout timeout = Timeout.apply(25, TimeUnit.SECONDS);

        Stopwatch stopwatch = Stopwatch.createStarted();
        Future<Object> ask = Patterns.ask(producer, new GenerateMessages(COUNT, profileClass), timeout);
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
    @Parameters(method = "serializers")
    public void calculateObjectSize(String serializerName, String fqcn, Class<?> profileClass) throws Exception {
        ActorSystem actorSystem = ActorSystem.create("producer-consumer-actorsystem", createConfig(serializerName, fqcn));

        final Object p;
        if (ProfileProtos.Profile.class.equals(profileClass)) {
            p = ProfileProtoGenerator.newProfile();
        } else if (com.senacor.hackingdays.serialization.data.thrift.Profile.class.equals(profileClass)) {
            p = ProfileGeneratorThrift.newProfile();
        } else {
            p = ProfileGenerator.newProfile();
        }

        int length = SerializationExtension.get(actorSystem).serializerFor(profileClass).toBinary(p).length;
        Thread.sleep(200);
        shutdown(actorSystem);

        System.err.println(String.format("Serializing a Profile with %25s weights %4s bytes.", serializerName, length));
    }

    @Test
    @Parameters(method = "serializers")
    public void assertFields(String serializerName, String fqcn, Class<?> profileClass) throws Exception {
        ActorSystem actorSystem = ActorSystem.create("producer-consumer-actorsystem", createConfig(serializerName, fqcn));

        Serializer serializer = SerializationExtension.get(actorSystem).serializerFor(com.senacor.hackingdays.serialization.data.thrift.Profile.class);
        System.out.println("serializer=" + serializer.getClass().getName());

        if (ProfileProtos.Profile.class.equals(profileClass)) {
            ProfileProtos.Profile input = ProfileProtoGenerator.newProfile();
            ProfileProtos.Profile output = (ProfileProtos.Profile) serializer.fromBinary(serializer.toBinary(input), ProfileProtos.Profile.class);

            shutdown(actorSystem);

            Assert.assertNotNull(output);
            ProfileProtos.Activity activity = output.getActivity();
            Assert.assertNotNull(activity);
            ProfileProtos.Location location = output.getLocation();
            Assert.assertNotNull(location);
            ProfileProtos.Seeking seeking = output.getSeeking();
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
        } else if (com.senacor.hackingdays.serialization.data.thrift.Profile.class.equals(profileClass)) {
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
        } else {
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
    }

    static Object[] serializers() throws IOException {

        final List<Class<ProtoBufSerilalizer>> testExceptions = new ArrayList<>();

        Set<ClassInfo> classInfos = ClassPath.from(Serializer.class.getClassLoader())
                .getTopLevelClasses("com.senacor.hackingdays.serializer");
        Set<ClassInfo> classInfosThrift = ClassPath.from(Serializer.class.getClassLoader())
                .getTopLevelClasses("com.senacor.hackingdays.serializer.thrift");

        Set<Object[]> resultSet = new TreeSet<>((o1, o2) -> {
            return o1[0].toString().compareTo(o2[0].toString());
        });

        resultSet.add($(JavaSerializer.class.getSimpleName(), JavaSerializer.class.getCanonicalName(), Profile.class));

        for (ClassInfo info : classInfos) {
            Class<?> clazz = info.load();
            if (isSerializer(clazz) && !testExceptions.contains(clazz)) {
                Class<?> generatedClass = Profile.class;
                if (ProtoBufSerilalizer.class.isAssignableFrom(clazz)) {
                    generatedClass = ProfileProtos.Profile.class;
                }
                resultSet.add($(clazz.getSimpleName(), clazz.getCanonicalName(), generatedClass));
            }
        }
        for (ClassInfo info : classInfosThrift) {
            Class<?> clazz = info.load();
            if (isSerializer(clazz) && !testExceptions.contains(clazz)) {
                resultSet.add($(clazz.getSimpleName(), clazz.getCanonicalName(), com.senacor.hackingdays.serialization.data.thrift.Profile.class));
            }
        }

        return resultSet.toArray();
    }
}