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
    kryo.writeObject(output, profile.getGender());
    kryo.writeObject(output, profile.getActivity());
    output.writeInt(profile.getAge());
    kryo.writeObject(output, profile.getLocation());
    kryo.writeObject(output, profile.getRelationShip());
    kryo.writeObject(output, profile.getSeeking());
    output.writeBoolean(profile.isSmoker());
    
  }

  @Override
  public Profile read(Kryo kryo, Input input, Class<Profile> type) {
    Profile profile = new Profile(input.readString(), kryo.readObject(input, Gender.class));
    profile.setActivity(kryo.readObject(input, Activity.class));
    profile.setAge(input.readInt());
    profile.setLocation(kryo.readObject(input, Location.class));
    profile.setRelationShip(kryo.readObject(input, RelationShipStatus.class));
    profile.setSeeking(kryo.readObject(input, Seeking.class));
    profile.setSmoker(input.readBoolean());
    return profile;
  }

}
