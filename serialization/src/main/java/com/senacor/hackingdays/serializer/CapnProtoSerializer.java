package com.senacor.hackingdays.serializer;

import akka.actor.ExtendedActorSystem;
import akka.serialization.JSerializer;
import com.senacor.hackingdays.serialization.data.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Date;

public class CapnProtoSerializer extends JSerializer {

    private String toCamel(String in) {
        String out = in.toLowerCase();
        out = String.valueOf(in.charAt(0)) + out.substring(1);
        return out;
    }


    private final ExtendedActorSystem actorSystem;

    public CapnProtoSerializer(ExtendedActorSystem actorSystem) {
        this.actorSystem = actorSystem;
    }

    @Override
    public Object fromBinaryJava(byte[] bytes, Class<?> manifest) {

        if (!Profile.class.getName().equals(manifest.getName())) {
            throw new RuntimeException("no clue how to de-serialize a " + manifest.getName());
        }
        org.capnproto.MessageReader message =
                null;
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            ReadableByteChannel inch = Channels.newChannel(in);
            message = org.capnproto.SerializePacked.readFromUnbuffered(inch);


            CapnProtoProfile.ProfileStruct.Reader capnProfile =
                    message.getRoot(CapnProtoProfile.ProfileStruct.factory);

            Profile profile = new Profile(capnProfile.getName().toString(), Gender.valueOf(toCamel(capnProfile.getGender().name())));
            Range seekingRange = new Range(capnProfile.getSeeking().getAgeRange().getLower(),capnProfile.getSeeking().getAgeRange().getUpper());
            Seeking seek = new Seeking(Gender.valueOf(toCamel(capnProfile.getSeeking().getGender().name())),seekingRange);
            profile.setSeeking(seek);
            profile.setRelationShip(RelationShipStatus.valueOf(toCamel(capnProfile.getRelationShip().name())));
            Location loc = new Location(capnProfile.getLocation().getState().toString(),capnProfile.getLocation().getCity().toString(),capnProfile.getLocation().getZip().toString());

            profile.setLocation(loc);

              profile.setAge(capnProfile.getAge());
            Date lastlog = new Date(capnProfile.getActivity().getLastLogin());
            Activity act = new Activity(lastlog,capnProfile.getActivity().getLoginCount());
            profile.setActivity(act);


            return profile;
        } catch (IOException e) {
            System.out.println("IOException im fromBinaryJava Stream:");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] toBinary(Object o) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        WritableByteChannel outch = Channels.newChannel(out);
        if (o instanceof Profile) {
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
            Activity act = profile.getActivity();
            capnProtoProfile.initActivity();
            capnProtoProfile.getActivity().setLastLogin(profile.getActivity().getLastLogin().getTime());
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
        } else {
            throw new RuntimeException("No clue how to serialize " + o.getClass().getName());
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
