package com.senacor.hackingdays.serializer;

import akka.actor.ExtendedActorSystem;
import akka.serialization.JSerializer;
import com.senacor.hackingdays.serialization.data.*;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;

public class CapnProtoSerializer extends JSerializer {


    private final ExtendedActorSystem actorSystem;

    public CapnProtoSerializer(ExtendedActorSystem actorSystem) {
        this.actorSystem = actorSystem;
    }

    @Override
    public Object fromBinaryJava(byte[] bytes, Class<?> manifest) {


            if (!CapnProtoProfile.class.getName().equals(manifest.getName())){
                throw  new RuntimeException("no clue how to de-serialize a "+manifest.getName());
            };


            String name = "";
            Gender gender = Gender.Female;
            Profile profile = new Profile(name,gender);

                return profile;

    }

    @Override
    public byte[] toBinary(Object o) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        WritableByteChannel outch = Channels.newChannel(out);
        if (o instanceof Profile){
            Profile profile = (Profile) o;
            org.capnproto.MessageBuilder message =
                    new org.capnproto.MessageBuilder();

            //Stopwatch stopwatch = Stopwatch.createStarted();

            CapnProtoProfile.ProfileStruct.Builder capnProtoProfile =
                    message.initRoot(CapnProtoProfile.ProfileStruct.factory);
            //    System.out.println("initRoot after " + stopwatch.elapsed(TimeUnit.MILLISECONDS));
            Gender gender = profile.getGender();
            capnProtoProfile.setGender(CapnProtoProfile.ProfileStruct.Gender.valueOf(gender.name().toUpperCase()));
            capnProtoProfile.setName(profile.getName());
            capnProtoProfile.setAge(profile.getAge());
            capnProtoProfile.initLocation();
            Location loc = profile.getLocation();
            capnProtoProfile.getLocation().setCity(loc.getCity());
            capnProtoProfile.getLocation().setState(loc.getState());
            capnProtoProfile.getLocation().setZip(loc.getZip());
            capnProtoProfile.setRelationShip(CapnProtoProfile.ProfileStruct.RelationShipStatus.valueOf(profile.getRelationShip().name().toUpperCase()));
            capnProtoProfile.setSmoker(profile.isSmoker());
            Seeking seek = profile.getSeeking();
            capnProtoProfile.initSeeking();
            capnProtoProfile.getSeeking().initAgeRange();
            capnProtoProfile.getSeeking().getAgeRange().setLower(seek.getAgeRange().getLower());
            capnProtoProfile.getSeeking().getAgeRange().setUpper(seek.getAgeRange().getUpper());
            capnProtoProfile.getSeeking().setGender(CapnProtoProfile.ProfileStruct.Gender.valueOf(profile.getGender().name().toUpperCase()));
            Activity act =profile.getActivity();
            capnProtoProfile.initActivity();
            capnProtoProfile.getActivity().initLastLogin();
            capnProtoProfile.getActivity().getLastLogin().setDay(act.getLastLogin().getDay());
            capnProtoProfile.getActivity().getLastLogin().setMonth(act.getLastLogin().getMonth());
            capnProtoProfile.getActivity().getLastLogin().setYear(act.getLastLogin().getYear());
            capnProtoProfile.getActivity().setLoginCount(act.getLoginCount());

            //  System.out.println("filled profile after " + stopwatch.elapsed(TimeUnit.MILLISECONDS));

            try {

                //      System.out.println("before ser "+stopwatch.elapsed(TimeUnit.MILLISECONDS));
                org.capnproto.SerializePacked.writeToUnbuffered(
                        outch, message);

                //      System.out.println("after ser " + stopwatch.elapsed(TimeUnit.MILLISECONDS));
            } catch (IOException e) {
                System.out.println("IOException im Stream:");
                e.printStackTrace();
            }
            //  System.out.println("finished after " + stopwatch.elapsed(TimeUnit.MILLISECONDS));
            //   stopwatch.stop();
            return out.toByteArray();
        }   else {
            throw  new RuntimeException("No clue how to serialize "+o.getClass().getName());
        }
    }

    @Override
    public boolean includeManifest() {
        return true;
    }

    @Override
    public int identifier() {
        return 666;
    }
}
