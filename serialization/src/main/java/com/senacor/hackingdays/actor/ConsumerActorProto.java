package com.senacor.hackingdays.actor;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import com.senacor.hackingdays.serialization.data.Profile;
import com.senacor.hackingdays.serialization.data.proto.ProfileProtos;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

public class ConsumerActorProto extends AbstractActor {

    private final LoggingAdapter logger = Logging.getLogger(context().system().eventStream(), this);

    private int receivedCount;

    public ConsumerActorProto() {
        receive(messageHandler());
    }

    private PartialFunction<Object, BoxedUnit> messageHandler() {
        return ReceiveBuilder
                .match(ProfileProtos.Profile.class, profile -> ack(profile))
                .build();
    }

    private void ack(ProfileProtos.Profile profile) {
        receivedCount++;
//        if (receivedCount % 100 == 0) {
//            logger.info(String.format("received profile # %s for %s", receivedCount, profile.getName()));
//        }
        sender().tell("Received", self());
    }

}
