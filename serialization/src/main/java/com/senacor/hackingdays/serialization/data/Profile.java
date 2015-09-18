package com.senacor.hackingdays.serialization.data;

import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

public class Profile implements Serializable, UnsafeSerializable {

  private static final long serialVersionUID = 1;

  private final String name;

  private final Gender gender;

  private int age;

  private Location location;

  private RelationShipStatus relationShip;

  private boolean smoker;

  private Seeking seeking;

  private Activity activity;

  public Profile(
          @JsonProperty("name") String name,
          @JsonProperty("gender") Gender gender) {
    this.name = name;
    this.gender = gender;
  }

  public String getName() {
    return name;
  }

  public Gender getGender() {
    return gender;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public RelationShipStatus getRelationShip() {
    return relationShip;
  }

  public void setRelationShip(RelationShipStatus relationShip) {
    this.relationShip = relationShip;
  }

  public boolean isSmoker() {
    return smoker;
  }

  public void setSmoker(boolean smoker) {
    this.smoker = smoker;
  }

  public Seeking getSeeking() {
    return seeking;
  }

  public void setSeeking(Seeking seeking) {
    this.seeking = seeking;
  }

  public Activity getActivity() {
    return activity;
  }

  public void setActivity(Activity activity) {
    this.activity = activity;
  }

  @Override
  public String toString() {
    return "DatingProfile{" +
           "name='" + name + '\'' +
           ", gender=" + gender +
           ", age=" + age +
           ", location=" + location +
           ", relationShip=" + relationShip +
           ", smoker=" + smoker +
           ", seeking=" + seeking +
           ", activity=" + activity +
           '}';
  }

  @Override
  public void serializeUnsafe(UnsafeMemory memory) {

    memory.putByteArray(name.getBytes());
    gender.serializeUnsafe(memory);
    memory.putInt(age);
    location.serializeUnsafe(memory);
    relationShip.serializeUnsafe(memory);
    memory.putBoolean(smoker);
    seeking.serializeUnsafe(memory);
    activity.serializeUnsafe(memory);

  }

  public static Profile deserializeUnsafe(UnsafeMemory memory) {
    final String name = new String(memory.getByteArray());
    final Gender gender = Gender.deserializeUnsafe(memory);
    final int age = memory.getInt();
    final Location location = Location.deserializeUnsafe(memory);
    final RelationShipStatus relationShip = RelationShipStatus.deserializeUnsafe(memory);
    final boolean smoker = memory.getBoolean();
    final Seeking seeking = Seeking.deserializeUnsafe(memory);
    final Activity activity = Activity.deserializeUnsafe(memory);

    final Profile profile = new Profile(name, gender);
    profile.setAge(age);
    profile.setLocation(location);
    profile.setRelationShip(relationShip);
    profile.setSmoker(smoker);
    profile.setSeeking(seeking);
    profile.setActivity(activity);
    return profile;
  }
}
