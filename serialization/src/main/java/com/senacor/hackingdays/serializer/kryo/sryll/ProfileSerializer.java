package com.senacor.hackingdays.serializer.kryo.sryll;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.senacor.hackingdays.serialization.data.Activity;
import com.senacor.hackingdays.serialization.data.Gender;
import com.senacor.hackingdays.serialization.data.Location;
import com.senacor.hackingdays.serialization.data.Profile;
import com.senacor.hackingdays.serialization.data.RelationShipStatus;
import com.senacor.hackingdays.serialization.data.Seeking;
import com.senacor.hackingdays.serializer.ActivitySerializer;
import com.senacor.hackingdays.serializer.GenderSerializer;
import com.senacor.hackingdays.serializer.LocationSerializer;
import com.senacor.hackingdays.serializer.SeekingSerializer;

public class ProfileSerializer extends Serializer<Profile> {

  @Override
  public void write(Kryo kryo, Output output, Profile profile) {
    output.writeString(profile.getName());
    kryo.writeObjectOrNull(output, profile.getGender(), GenderSerializer.INSTANCE);
    kryo.writeObjectOrNull(output, profile.getActivity(), ActivitySerializer.INSTANCE);
    output.writeInt(profile.getAge());
    kryo.writeObjectOrNull(output, profile.getLocation(), LocationSerializer.INSTANCE);
    kryo.writeObjectOrNull(output, profile.getRelationShip(), RelationShipStatus.class);
    kryo.writeObjectOrNull(output, profile.getSeeking(), SeekingSerializer.INSTANCE);
    output.writeBoolean(profile.isSmoker());

  }

  @Override
  public Profile read(Kryo kryo, Input input, Class<Profile> type) {
    Profile profile = new Profile(input.readString(), kryo.readObjectOrNull(input, Gender.class, GenderSerializer.INSTANCE));
    profile.setActivity(kryo.readObjectOrNull(input, Activity.class, ActivitySerializer.INSTANCE));
    profile.setAge(input.readInt());
    profile.setLocation(kryo.readObjectOrNull(input, Location.class, LocationSerializer.INSTANCE));
    profile.setRelationShip(kryo.readObjectOrNull(input, RelationShipStatus.class));
    profile.setSeeking(kryo.readObjectOrNull(input, Seeking.class, SeekingSerializer.INSTANCE));
    profile.setSmoker(input.readBoolean());
    return profile;
  }

}
