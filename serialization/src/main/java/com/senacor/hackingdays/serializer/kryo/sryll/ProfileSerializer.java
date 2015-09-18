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

public class ProfileSerializer extends Serializer<Profile> {

  @Override
  public void write(Kryo kryo, Output output, Profile profile) {
    output.writeString(profile.getName());
    kryo.writeObjectOrNull(output, profile.getGender(), Gender.class);
    kryo.writeObjectOrNull(output, profile.getActivity(), Activity.class);
    output.writeInt(profile.getAge());
    kryo.writeObjectOrNull(output, profile.getLocation(), Location.class);
    kryo.writeObjectOrNull(output, profile.getRelationShip(), RelationShipStatus.class);
    kryo.writeObjectOrNull(output, profile.getSeeking(), Seeking.class);
    output.writeBoolean(profile.isSmoker());
    
  }

  @Override
  public Profile read(Kryo kryo, Input input, Class<Profile> type) {
    Profile profile = new Profile(input.readString(), kryo.readObjectOrNull(input, Gender.class));
    profile.setActivity(kryo.readObjectOrNull(input, Activity.class));
    profile.setAge(input.readInt());
    profile.setLocation(kryo.readObjectOrNull(input, Location.class));
    profile.setRelationShip(kryo.readObjectOrNull(input, RelationShipStatus.class));
    profile.setSeeking(kryo.readObjectOrNull(input, Seeking.class));
    profile.setSmoker(input.readBoolean());
    return profile;
  }

}
