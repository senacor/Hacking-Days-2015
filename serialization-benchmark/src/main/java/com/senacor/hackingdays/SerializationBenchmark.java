/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.senacor.hackingdays;

import static com.senacor.hackingdays.config.ConfigHelper.createConfig;
import static com.senacor.hackingdays.remoting.ProducerProfile.createProfile;
import static com.senacor.hackingdays.remoting.ProducerProfile.createProtoBufProfile;
import static com.senacor.hackingdays.remoting.ProducerProfile.createThriftProfile;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.serialization.JavaSerializer;
import akka.util.Timeout;
import com.google.common.collect.Maps;
import com.senacor.hackingdays.actor.GenerateMessages;
import com.senacor.hackingdays.actor.ProducerActor;
import com.senacor.hackingdays.remoting.ProducerProfile;
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
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

@Warmup(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS)
@Fork(5)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
// TODO Vieles hiervon lässt sich aus der ProducerLauncher.java und hier zusammenführeng
public class SerializationBenchmark {

    //TODO edit IP here
    private static final String CONSUMER_IP = "172.16.73.211";
    //    private static final String CONSUMER_IP = "169.254.17.33";

    private static final int COUNT = 100_000;

    @Benchmark
    public void runSerializerTest(SerializatorState state, Blackhole blackhole) throws Exception {
        sendForSerializer(state.producerProfiles.get(state.profileName), blackhole);
    }

    private static void sendForSerializer(ProducerProfile producerProfile, Blackhole blackhole) {
        ActorSystem actorSystem = null;
        try {
            actorSystem = ActorSystem.create("producer-actorsystem",
                createConfig(producerProfile.getSerializerName(), producerProfile.getFqcn(), "application-remote.conf"));

            ActorRef remoteConsumer = findRemoteConsumer(actorSystem, producerProfile.getPort());

            ActorRef producer = actorSystem.actorOf(Props.create(ProducerActor.class, () -> new ProducerActor(remoteConsumer)), "producer");
            System.out.println("Startet producer " + producer);

            sendDataAndWaitForCompletion(producer, producerProfile, blackhole);
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

    private static void sendDataAndWaitForCompletion(ActorRef producer, ProducerProfile producerProfile, Blackhole blackhole) throws Exception {
        Timeout timeout = Timeout.apply(90, TimeUnit.SECONDS);
        Future<Object> ask = Patterns.ask(producer, new GenerateMessages(COUNT, producerProfile.getProfileClass()), timeout);
        blackhole.consume(Await.result(ask, timeout.duration()));
    }

    @State(Scope.Benchmark)
    public static class SerializatorState {
        private final Map<String, ProducerProfile> producerProfiles = Maps.newHashMap();

        @Param({"java", "kryo", "jackson", "gson", "gson2", "xml", "json-io", "fast-ser",
            //            "hessian2",
            "unsafe", "refl-kryo",
            //            "defl-hessian2",
            "capn-proto", "capn-proto-optimized", "proto", "thrift-bin", "thirft-tuple"})
        public String profileName;

        public SerializatorState() {
            producerProfiles.put("java", createProfile("java", JavaSerializer.class.getName(), "2552"));
            producerProfiles.put("kryo", createProfile("kryo", KryoSerializer.class.getName(), "2553"));
            producerProfiles.put("jackson", createProfile("jackson", JacksonSerializer.class.getName(), "2554"));
            producerProfiles.put("gson", createProfile("gson", GsonSerializer.class.getName(), "2555"));
            producerProfiles.put("gson2", createProfile("gson2", GsonSerializer2.class.getName(), "2556"));
            producerProfiles.put("xml", createProfile("xml", XStreamXMLSerializer.class.getName(), "2557"));
            producerProfiles.put("json-io", createProfile("json-io", JsonIoSerializer.class.getName(), "2558"));
            producerProfiles.put("fast-ser", createProfile("fast-ser", FastSerializer.class.getName(), "2559"));
            producerProfiles.put("hessian2", createProfile("hessian2", Hessian2Serializer.class.getName(), "2560"));
            producerProfiles.put("unsafe", createProfile("unsafe", UnsafeSerializer.class.getName(), "2561"));
            producerProfiles.put("refl-kryo", createProfile("refl-kryo", ReflectionKryoSerializer.class.getName(), "2562"));
            producerProfiles.put("defl-hessian2", createProfile("defl-hessian2", DeflatingHessian2Serializer.class.getName(), "2563"));
            producerProfiles.put("capn-proto", createProfile("capn-proto", CapnProtoSerializer.class.getName(), "2564"));
            producerProfiles.put("capn-proto-opt", createProfile("capn-proto-opt", CapnProtoOptimizedSerializer.class.getName(), "2565"));
            producerProfiles.put("proto", createProtoBufProfile("proto", ProtoBufSerilalizer.class.getName(), "2566"));
            producerProfiles.put("thrift-bin", createThriftProfile("thrift-bin", ThriftSerializerTBinary.class.getName(), "2567"));
            producerProfiles.put("thrift-tuple", createThriftProfile("thrift-tuple", ThriftSerializerTTuple.class.getName(), "2568"));
        }
    }
}
