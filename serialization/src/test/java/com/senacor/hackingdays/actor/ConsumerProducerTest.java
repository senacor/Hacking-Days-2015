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
import static com.senacor.hackingdays.config.ConfigHelper.createConfig;
import com.senacor.hackingdays.serialization.data.Activity;
import com.senacor.hackingdays.serialization.data.CompactedProfile;
import com.senacor.hackingdays.serialization.data.Gender;
import static com.senacor.hackingdays.serialization.data.Gender.Female;
import com.senacor.hackingdays.serialization.data.Location;
import com.senacor.hackingdays.serialization.data.Profile;
import com.senacor.hackingdays.serialization.data.Seeking;
import com.senacor.hackingdays.serialization.data.generate.CompactedProfileGenerator;
import com.senacor.hackingdays.serialization.data.generate.NameSupplier;
import com.senacor.hackingdays.serialization.data.generate.ProfileGenerator;
import com.senacor.hackingdays.serialization.data.generate.ProfileGeneratorThrift;
import com.senacor.hackingdays.serialization.data.generate.ProfileProtoGenerator;
import com.senacor.hackingdays.serialization.data.proto.ProfileProtos;
import com.senacor.hackingdays.serializer.ProtoBufSerilalizer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import junitparams.JUnitParamsRunner;
import static junitparams.JUnitParamsRunner.$;
import junitparams.Parameters;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import org.junit.Assert;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import scala.concurrent.Await;
import scala.concurrent.Future;

@RunWith(JUnitParamsRunner.class)
public class ConsumerProducerTest {

    public static final int COUNT = 40_000_000;

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

    @Test
    public void checkMemory() throws Exception {
        System.gc();

        List profiles = Arrays.asList(ProfileGenerator.newInstance(COUNT).stream().toArray());
        System.out.println(COUNT + " records generated");

        System.out.println("(verification - count: " + profiles.stream().count() + ")");

        System.gc();

        Stopwatch stopwatch = Stopwatch.createStarted();
        Optional oldest = profiles.stream().max((p1, p2) -> {
            return ((Profile) p1).getAge() - ((Profile) p2).getAge();
        });
        stopwatch.stop();
        System.out.println("elapsed: " + stopwatch.elapsed(TimeUnit.MILLISECONDS));

        stopwatch = Stopwatch.createStarted();
        Optional youngest = profiles.stream().min((p1, p2) -> {
            return ((Profile) p1).getAge() - ((Profile) p2).getAge();
        });
        stopwatch.stop();
        System.out.println("elapsed: " + stopwatch.elapsed(TimeUnit.MILLISECONDS));

        System.gc();
        System.out.println("heap: " + Runtime.getRuntime().freeMemory());

        NameSupplier femaleNames = NameSupplier.forGender(Female);
        int max = 1000;
        stopwatch = Stopwatch.createStarted();
        while (max-- > 0) {
            String name = femaleNames.get();
            long num = profiles.parallelStream().filter(p -> {
                return ((Profile)p).getName().contains(name);
            }).count();
            // System.out.println(name+" -> "+num);
        }
        stopwatch.stop();
        System.out.println("elapsed (for all): " + stopwatch.elapsed(TimeUnit.MILLISECONDS));

        System.gc();
    }

    @Test
    public void checkCompactedMemory() throws Exception {
        System.gc();

        List profiles = Arrays.asList(CompactedProfileGenerator.newInstance(COUNT).stream().toArray());
        System.out.println(COUNT+" records generated");

        System.out.println("(verification - count: "+profiles.stream().count()+")");

        System.gc();

        Stopwatch stopwatch = Stopwatch.createStarted();
        Optional oldest = profiles.stream().max((p1, p2) -> {
            return ((Profile) p1).getAge() - ((Profile) p2).getAge();
        });
        stopwatch.stop();
        System.out.println("elapsed: " + stopwatch.elapsed(TimeUnit.MILLISECONDS));

        stopwatch = Stopwatch.createStarted();
        Optional youngest = profiles.stream().min((p1, p2) -> {
            return ((Profile) p1).getAge() - ((Profile) p2).getAge();
        });
        stopwatch.stop();
        System.out.println("elapsed: " + stopwatch.elapsed(TimeUnit.MILLISECONDS));

        System.gc();
        System.out.println("heap: " + Runtime.getRuntime().freeMemory());

        NameSupplier femaleNames = NameSupplier.forGender(Female);
        int max = 1000;
        stopwatch = Stopwatch.createStarted();
        while (max-- > 0) {
            String name = femaleNames.get();
            long num = profiles.parallelStream().filter(p -> {
                return ((Profile)p).getName().contains(name);
            }).count();
            // System.out.println(name+" -> "+num);
        }
        stopwatch.stop();
        System.out.println("elapsed (for all): " + stopwatch.elapsed(TimeUnit.MILLISECONDS));

        System.gc();

        System.out.println("avg size: " + profiles.stream().mapToInt(p -> {return ((CompactedProfile)p).getSize();}).average().getAsDouble());
    }

    public <T> void findInCompactedMemory(Predicate<? super T> predicate) throws Exception {
        System.gc();
        System.out.println("vor find (false) - heap: " + Runtime.getRuntime().freeMemory());

        Stopwatch stopwatch = Stopwatch.createStarted();
        long count = Arrays.<T>stream((T[])profiles).filter(predicate).count();
        stopwatch.stop();

        System.out.println("elapsed: " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms found: " + count);
        System.out.println("nach find:- heap: " + Runtime.getRuntime().freeMemory());
        System.gc();
        System.out.println("heap: " + Runtime.getRuntime().freeMemory());

    }

    Object[] profiles;

    @Test
    public void findMatches() throws Exception {
        System.gc();
        System.out.println("vor produce- heap: " + Runtime.getRuntime().freeMemory());

        profiles = CompactedProfileGenerator.newInstance(COUNT).stream().toArray();

        System.out.println(COUNT+" records generated");
        System.out.println("(verification - count: "+profiles.length+")");
        assertThat(profiles.length, is(COUNT));
        System.out.println("nach  produce- heap: " + Runtime.getRuntime().freeMemory());

        findInCompactedMemory((t) -> {
            return true;
        });

        CompactedProfile selected = (CompactedProfile) profiles[COUNT/2];

        assertThat(selected.getAge(), is(not(0)));
        assertThat(selected.getGender(), is(not(nullValue())));
        assertThat(selected.getSeekingAgeUpper(), is(not(0)));
        assertThat(selected.getGender(), is(not(nullValue())));

        Gender gender = selected.getSeekingGender();
        int ageLow = selected.getSeekingAgeLower();
        int ageHigh = selected.getSeekingAgeUpper();


//        findInCompactedMemory((t) -> {
//            int age = t.getAge();
//            return seeking.getGender() == t.getGender()
//                 && ageLow <= age && age <= ageHigh;
//        });

        System.out.println("******* selected::match ");
        this.<CompactedProfile>findInCompactedMemory(selected::match);

        System.out.println("******* selected::matcher ");
        findInCompactedMemory(selected.compactMatcher());

        System.out.println("******* selected::perfectMatch ");
        this.<CompactedProfile>findInCompactedMemory(selected::perfectMatch);

        System.out.println("******* selected::perfectMatcher ");
        findInCompactedMemory(selected.perfectCompactMatcher());

    }
}