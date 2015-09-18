package com.senacor.hackingdays.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import com.senacor.hackingdays.serialization.data.generate.DataGenerator;
import com.senacor.hackingdays.serialization.data.generate.ProfileGenerator;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

import java.lang.reflect.InvocationTargetException;

public class ProducerActor extends AbstractActor {

    private final ActorRef consumer;

    public ProducerActor(ActorRef consumer) {
        this.consumer = consumer;
        receive(messageHandler());
    }

    private PartialFunction<Object, BoxedUnit> messageHandler() {
        return ReceiveBuilder
                .match(GenerateMessages.class, msg -> sendMessagesToConsumer(msg.getCount(), msg.getGenerator()))
                .build();
    }

    private void sendMessagesToConsumer(int count, Class<DataGenerator> generatorClazz) {
        ActorRef collector = context().actorOf(AckCollector.props(count, sender()), "collector");

        if (generatorClazz != null) {
          try {
            DataGenerator generator = (DataGenerator) generatorClazz.getMethod("newInstance", Integer.TYPE).invoke(null, count);
            generator.doEach(count, profile -> consumer.tell(profile, collector));
          } catch (IllegalAccessException|NoSuchMethodException|InvocationTargetException e) {
            throw new RuntimeException(e);
          }
        } else {
          ProfileGenerator.newInstance(count).stream().forEach(profile -> consumer.tell(profile, collector));
        }
    }


    private final static class AckCollector extends AbstractActor {

        private final LoggingAdapter logger = Logging.getLogger(context().system().eventStream(), this);

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
//            if (acknowledged % 100 == 0) {
//                logger.info(String.format("acked %s profiles", acknowledged));
//            }
            if (acknowledged == count) {
                launcher.tell("completed", launcher);
            }
        }

        public static Props props(int count, ActorRef launcher) {
            return Props.create(AckCollector.class, () -> new AckCollector(count, launcher));
        }
    }
}
