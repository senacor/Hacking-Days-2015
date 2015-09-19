package com.senacor.hackingdays.distributedcache.serializer.kryo.sryll;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.senacor.hackingdays.distributedcache.generate.model.*;
import com.senacor.hackingdays.distributedcache.serializer.kryo.*;

import java.util.BitSet;

public class ProfileSerializer extends Serializer<Profile> {

    @Override
    public void write(Kryo kryo, Output output, Profile profile) {
        BitSet opts = new BitSet(6);
        if (profile.isSmoker()) opts.set(0);
        if (profile.getGender() != null) opts.set(1);
        if (profile.getActivity() != null) opts.set(2);
        if (profile.getLocation() != null) opts.set(3);
        if (profile.getRelationShip() != null) opts.set(4);
        if (profile.getSeeking() != null) opts.set(5);

        output.writeByte(opts.toByteArray()[0]);
        output.writeString(profile.getName());
        if (profile.getGender() != null) kryo.writeObject(output, profile.getGender(), GenderSerializer.INSTANCE);
        if (profile.getActivity() != null) kryo.writeObject(output, profile.getActivity(), ActivitySerializer.INSTANCE);
        output.writeByte(profile.getAge());
        if (profile.getLocation() != null) kryo.writeObject(output, profile.getLocation(), LocationSerializer.INSTANCE);
        if (profile.getRelationShip() != null) kryo.writeObject(output, profile.getRelationShip());
        if (profile.getSeeking() != null) kryo.writeObject(output, profile.getSeeking(), SeekingSerializer.INSTANCE);

    }

    @Override
    public Profile read(Kryo kryo, Input input, Class<Profile> type) {

        BitSet opts = BitSet.valueOf(new byte[]{input.readByte()});

        Profile profile = new Profile(input.readString(), opts.get(1) ? kryo.readObject(input, Gender.class, GenderSerializer.INSTANCE) : null);
        if (opts.get(2)) profile.setActivity(kryo.readObject(input, Activity.class, ActivitySerializer.INSTANCE));
        profile.setAge(input.readByte());
        if (opts.get(3)) profile.setLocation(kryo.readObject(input, Location.class, LocationSerializer.INSTANCE));
        if (opts.get(4)) profile.setRelationShip(kryo.readObject(input, RelationShipStatus.class));
        if (opts.get(5)) profile.setSeeking(kryo.readObject(input, Seeking.class, SeekingSerializer.INSTANCE));
        profile.setSmoker(opts.get(0));
        return profile;
    }

}
