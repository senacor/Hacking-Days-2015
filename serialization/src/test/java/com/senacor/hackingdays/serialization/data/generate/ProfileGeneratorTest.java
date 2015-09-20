package com.senacor.hackingdays.serialization.data.generate;

import com.senacor.hackingdays.serialization.data.CompactedProfile;
import com.senacor.hackingdays.serialization.data.Gender;
import com.senacor.hackingdays.serialization.data.Location;
import com.senacor.hackingdays.serialization.data.RelationShipStatus;
import org.junit.Test;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;

public class ProfileGeneratorTest {

  private static final Unsafe unsafe;

  static {
    try {
      Field field = Unsafe.class.getDeclaredField("theUnsafe");
      field.setAccessible(true);
      unsafe = (Unsafe) field.get(null);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }


  @Test
  public void thatProfilesAreGenerated() {
    ProfileGenerator generator = ProfileGenerator.newInstance(100);
    generator.stream().forEach(System.out::println);
  }

  @Test
  public void testCompactedProfile() {
    String name = "Bibi Blocksberg";
    String state = "BY";
    String zip = "90410";
    String city = "Nuernberg";

    CompactedProfile cp = new CompactedProfile(name, new Location(state, city, zip));
    assertEquals(name, cp.getName());
    assertEquals(state, cp.getCompactedLocation().getState());
    assertEquals(zip, cp.getCompactedLocation().getZip());
    assertEquals(city, cp.getCompactedLocation().getCity());

    Gender gender = Gender.Disambiguous;
    cp.setGender(gender);
    assertEquals(gender, cp.getGender());

    gender = Gender.Female;
    cp.setGender(gender);
    assertEquals(gender, cp.getGender());

    gender = Gender.Male;
    cp.setGender(gender);
    assertEquals(gender, cp.getGender());

    RelationShipStatus rss = RelationShipStatus.Single;
    cp.setRelationShipStatus(rss);
    assertEquals(rss, cp.getRelationShipStatus());

    rss = RelationShipStatus.Maried;
    cp.setRelationShipStatus(rss);
    assertEquals(rss, cp.getRelationShipStatus());

    rss = RelationShipStatus.Divorced;
    cp.setRelationShipStatus(rss);
    assertEquals(rss, cp.getRelationShipStatus());

    boolean smoker = false;
    cp.setSmoker(smoker);
    assertEquals(smoker, cp.isSmoker());

    smoker = true;
    cp.setSmoker(smoker);
    assertEquals(smoker, cp.isSmoker());

    int age = 34;
    cp.setAge(age);
    assertEquals(age, cp.getAge());

    // Seeking

    gender = Gender.Disambiguous;
    cp.getCompactedSeeking().setGender(gender);
    assertEquals(gender, cp.getCompactedSeeking().getGender());

    gender = Gender.Female;
    cp.getCompactedSeeking().setGender(gender);
    assertEquals(gender, cp.getCompactedSeeking().getGender());

    gender = Gender.Male;
    cp.getCompactedSeeking().setGender(gender);
    assertEquals(gender, cp.getCompactedSeeking().getGender());

    age = 27;
    cp.getCompactedSeeking().getCompactedRange().setLower(age);
    assertEquals(age, cp.getCompactedSeeking().getCompactedRange().getLower());
    age = 64;
    cp.getCompactedSeeking().getCompactedRange().setUpper(age);
    assertEquals(age, cp.getCompactedSeeking().getCompactedRange().getUpper());

    // Activity

    int loginCount = 2;
    cp.getCompactedActivity().setLoginCount(loginCount);
    assertEquals(loginCount, cp.getCompactedActivity().getLoginCount());

    loginCount = 7;
    cp.getCompactedActivity().setLoginCount(loginCount);
    assertEquals(loginCount, cp.getCompactedActivity().getLoginCount());

    loginCount = 9;
    cp.getCompactedActivity().setLoginCount(loginCount);
    assertEquals(loginCount, cp.getCompactedActivity().getLoginCount());

  }

}