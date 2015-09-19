package com.senacor.hackingdays.serialization.data;

import com.senacor.hackingdays.serialization.data.unsafe.BufferTooSmallException;
import com.senacor.hackingdays.serialization.data.unsafe.SizeAware;
import com.senacor.hackingdays.serialization.data.unsafe.UnsafeMemory;
import com.senacor.hackingdays.serialization.data.unsafe.UnsafeSerializable;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;
import java.util.function.Predicate;

import static com.senacor.hackingdays.serialization.data.unsafe.UnsafeMemory.sizeOf;

public class Profile implements Serializable, UnsafeSerializable, SizeAware {

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
    return "DatingProfile{"
           + "name='" + name + '\''
           + ", gender=" + gender
           + ", age=" + age
           + ", location=" + location
           + ", relationShip=" + relationShip
           + ", smoker=" + smoker
           + ", seeking=" + seeking
           + ", activity=" + activity
           + '}';
  }

  @Override
  public void serializeUnsafe(UnsafeMemory memory) throws BufferTooSmallException {

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

  /**
   * Erzeugt ein Predicate, das genau dann true liefert, wenn das übergebene Profil zu den eigenen Vorstellungen
   * passt.
   *
   * @return
   */
  public Predicate<Profile> matcher() {
    final Gender seeking = this.seeking.getGender();
    final int ageLow = this.seeking.getAgeRange().getLower();
    final int ageHigh = this.seeking.getAgeRange().getUpper();
    return (Profile other) -> other.getGender() == seeking
                              && other.getAge() <= ageHigh
                              && other.getAge() >= ageLow;
  }

  /**
   * Prüft, ob ein gegebenes anderes Profil zu den eignen Suchkriterien passt;
   *
   * @return
   */
  public boolean match(Profile other) {
    final Range range = seeking.getAgeRange();
    return other.getGender() == this.seeking.getGender()
           && other.age <= range.getUpper()
           && other.age >= range.getUpper();
  }

  /**
   * Erzeugt ein Predicate, das genau dann true liefert, wenn beide Profile zueinander passen.
   *
   * @return
   */
  public Predicate<Profile> perfectMatcher() {
    final Gender sGender = this.seeking.getGender();
    final int ageLow = this.seeking.getAgeRange().getLower();
    final int ageHigh = this.seeking.getAgeRange().getUpper();
    return (Profile other) -> {
      return other.getGender() == sGender
             && other.getAge() <= ageHigh
             && other.getAge() >= ageLow
             && other.match(this);
    };
  }

  /**
   * Prüft, ob beide Profile zueinander passen;
   *
   * @param other
   * @return
   */
  public boolean perfectMatch(Profile other) {
    final Range range = seeking.getAgeRange();
    return other.getGender() == this.seeking.getGender()
           && other.age <= range.getUpper()
           && other.age >= range.getUpper()
           && other.match(this);
  }

  @Override
  public long getDeepSize() {
    // Includes references to objects and native member variables of profile
    final long profileShallowSize = getShallowSize();
    final long profileNameSize = sizeOf(name);
    // gender is just a reference to an enum (singleton instance). It takes some space but once only, not for each profile.
    // the reference to gender is already accounted for by profileShallowSize
    // age is native int, already accounted for
    final long profileLocationSize = location.getDeepSize();
    // relationship is enum, already accounted for
    // smoker is native boolean, already accounted for
    final long profileSeekingSize = seeking.getDeepSize();
    final long profileActivitySize = activity.getDeepSize();
    return profileShallowSize + profileNameSize + profileLocationSize + profileSeekingSize + profileActivitySize;
  }

  @Override
  public long getShallowSize() {
    return UnsafeMemory.sizeOf(this);
  }
}
