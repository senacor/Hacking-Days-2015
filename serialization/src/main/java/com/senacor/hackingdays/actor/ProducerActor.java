package com.senacor.hackingdays.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import com.senacor.hackingdays.serialization.data.generate.ProfileGenerator;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

public class ProducerActor extends AbstractActor {


    private final ActorRef consumer;
    private int acknowledged;

    public ProducerActor(ActorRef consumer) {
        this.consumer = consumer;
        receive(messageHandler());
    }

    private PartialFunction<Object, BoxedUnit> messageHandler() {
        return ReceiveBuilder
                .match(GenerateMessages.class, msg -> sendMessagesToConsumer(msg.getCount()))
                .build();
    }

    private void sendMessagesToConsumer(int count) {
        ActorRef collector = context().actorOf(AckCollector.props(count, sender()), "collector");
        new ProfileGenerator(count).stream().forEach(profile -> consumer.tell(profile, collector));
    }



    private final static class AckCollector extends AbstractActor {

        private final int count;
        private final ActorRef launcher;
        private int acknowledged;

        public AckCollector(int count, ActorRef launcher) {
            this.count = count;
            this.launcher = launcher;
            receive(messageHandler());
        }
        private PartialFunction<Object, BoxedUnit> messageHandler() {
            return ReceiveBuilder
                    .matchEquals("Received", msg -> checkForCompletion())
                    .build();
        }

        private void checkForCompletion() {
            acknowledged++;
            if (acknowledged == count) {
                launcher.tell("completed", launcher);
            }
        }

        public static Props props(int count, ActorRef launcher) {
            return Props.create(AckCollector.class, () -> new AckCollector(count, launcher));
        }
    }
}
