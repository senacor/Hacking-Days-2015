package com.senacor.hackingdays.serialization.data.generate;

import static com.senacor.hackingdays.serialization.data.Gender.Disambiguous;
import static com.senacor.hackingdays.serialization.data.Gender.Female;
import static com.senacor.hackingdays.serialization.data.Gender.Male;
import static com.senacor.hackingdays.serialization.data.RelationShipStatus.Divorced;
import static java.lang.Integer.max;
import static java.lang.Integer.min;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.senacor.hackingdays.serialization.data.Activity;
import com.senacor.hackingdays.serialization.data.Gender;
import com.senacor.hackingdays.serialization.data.Profile;
import com.senacor.hackingdays.serialization.data.Range;
import com.senacor.hackingdays.serialization.data.RelationShipStatus;
import com.senacor.hackingdays.serialization.data.Seeking;

public class UnlimitedProfileGenerator {

  private final NameSupplier femaleNames = NameSupplier.forGender(Female);
  private final NameSupplier maleNames = NameSupplier.forGender(Male);
  private final NameSupplier transgenderNames = NameSupplier.forGender(Disambiguous);
  private final LocationSupplier locationSupplier = LocationSupplier.newInstance();
  private final Random random = new Random();
  private static final Instant NOW = Instant.now();

  public static UnlimitedProfileGenerator newInstance() {
    return new Builder().build();
  }
  private UnlimitedProfileGenerator(long seed) {
    random.setSeed(seed);
  }

  public Profile generateProfile() {
    Gender gender = randomGender();
    Profile profile = new Profile(defaultNameFunction().apply(gender), gender);
    profile.setAge(randomAge());
    profile.setLocation(locationSupplier.get());
    profile.setRelationShip(randomRelationShipStatus());
    profile.setSmoker(random.nextBoolean());
    profile.setSeeking(randomSeeking());
    profile.setActivity(randomActivity());
    return profile;
  }

  private Profile initialProfile(Gender gender) {
    switch (gender) {
      case Male:
        return new Profile(maleNames.get(), gender);
      case Female:
        return new Profile(femaleNames.get(), gender);
      case Disambiguous:
        return new Profile(transgenderNames.get(), gender);
      default:
        throw new AssertionError();
    }
  }

  public static class Builder {

    private long seed = 42L;

    public Builder() {
    }

    public Builder withSeed(long seed) {
      this.seed = seed;
      return this;
    }

    public UnlimitedProfileGenerator build() {
      return new UnlimitedProfileGenerator(seed);
    }

  }
  private Function<Gender, String> defaultNameFunction() {
    return gender -> {
      switch (gender) {
        case Male:
          return maleNames.get();
        case Female:
          return femaleNames.get();
        case Disambiguous:
          return transgenderNames.get();
        default:
          throw new AssertionError();
      }
    };
  }

  private int randomAge() {
    return random.nextInt(Range.MAX_AGE - Range.MIN_AGE) + Range.MIN_AGE;
  }

  private Gender randomGender() {
    int i = random.nextInt(100);
    return i < 45 ? Gender.Male : i > 95 ? Gender.Disambiguous : Female;

  }

  private Activity randomActivity() {
    int frequency = random.nextInt(10);
    long toEpochMilli = NOW.minus(random.nextInt(1_000_000), ChronoUnit.MINUTES).toEpochMilli();
    Date lastLogin = new Date(toEpochMilli);
    return new Activity(lastLogin, frequency);
  }

  private Seeking randomSeeking() {
    int bound1 = randomAge();
    int bound2 = randomAge();
    return new Seeking(randomGender(), new Range(min(bound1, bound2), max(bound1, bound2)));
  }

  private RelationShipStatus randomRelationShipStatus() {
    int i = random.nextInt(100);
    return i < 35 ? Divorced : i > 65 ? RelationShipStatus.Maried : RelationShipStatus.Single;
  }
}
