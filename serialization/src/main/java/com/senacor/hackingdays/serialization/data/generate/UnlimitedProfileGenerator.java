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

  private final static NameSupplier femaleNames = NameSupplier.forGender(Female);
  private final static NameSupplier maleNames = NameSupplier.forGender(Male);
  private final static NameSupplier transgenderNames = NameSupplier.forGender(Disambiguous);
  private final static LocationSupplier locationSupplier = LocationSupplier.newInstance();
  private final static Random random = new Random();
  private static final Instant NOW = Instant.now();

  private final Supplier<Integer> ageFunction;
  private final Supplier<Gender> genderFunction;
  private final Function<Gender, String> nameFunction;

  public static UnlimitedProfileGenerator newInstance() {
    return new Builder().build();
  }
  private UnlimitedProfileGenerator(Supplier<Integer> ageFunction, Supplier<Gender> genderFunction, Function<Gender, String> nameFunction, Supplier<Activity> activityFunction, Supplier<RelationShipStatus> relationShipStatusFunction,
                           long seed) {
    this.ageFunction = ageFunction;
    this.genderFunction = genderFunction;
    this.nameFunction = nameFunction;
    random.setSeed(seed);
  }

  public Profile generateProfile() {
    Gender gender = genderFunction.get();
    Profile profile = new Profile(nameFunction.apply(gender), gender);
    profile.setAge(ageFunction.get());
    profile.setLocation(locationSupplier.get());
    profile.setRelationShip(randomRelationShipStatus());
    profile.setSmoker(random.nextBoolean());
    profile.setSeeking(randomSeeking());
    profile.setActivity(randomActivity());
    return profile;
  }

  private static Profile initialProfile(Gender gender) {
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

    private Supplier<Integer> ageFunction = UnlimitedProfileGenerator::randomAge;
    private Supplier<Gender> genderFunction = UnlimitedProfileGenerator::randomGender;
    private Function<Gender, String> nameFunction = defaultNameFunction();
    private Supplier<RelationShipStatus> relationShipStatusFunction = UnlimitedProfileGenerator::randomRelationShipStatus;
    private Supplier<Activity> activityFunction = UnlimitedProfileGenerator::randomActivity;
    private long seed = 42L;

    public Builder() {
    }

    public Builder withAge(Supplier<Integer> ageFunction) {
      this.ageFunction = ageFunction;
      return this;
    }


    public Builder withGender(Supplier<Gender> genderFunction) {
      this.genderFunction = genderFunction;
      return this;
    }
    public Builder withName(Function<Gender, String> nameFunction) {
      this.nameFunction = nameFunction;
      return this;
    }

    public Builder withRelationShipStatus(Supplier<RelationShipStatus> relationShipStatusFunction) {
      this.relationShipStatusFunction = relationShipStatusFunction;
      return this;
    }

    public Builder withActivity(Supplier<Activity> activityFunction) {
      this.activityFunction = activityFunction;
      return this;
    }

    public Builder withSeed(long seed) {
      this.seed = seed;
      return this;
    }

    public UnlimitedProfileGenerator build() {
      return new UnlimitedProfileGenerator(ageFunction, genderFunction, nameFunction, activityFunction, relationShipStatusFunction, seed);
    }

  }
  private static Function<Gender, String> defaultNameFunction() {
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

  private static int randomAge() {
    return random.nextInt(Range.MAX_AGE - Range.MIN_AGE) + Range.MIN_AGE;
  }

  private static Gender randomGender() {
    int i = random.nextInt(100);
    return i < 45 ? Gender.Male : i > 95 ? Gender.Disambiguous : Female;

  }

  private static Activity randomActivity() {
    int frequency = random.nextInt(10);
    long toEpochMilli = NOW.minus(random.nextInt(1_000_000), ChronoUnit.MINUTES).toEpochMilli();
    Date lastLogin = new Date(toEpochMilli);
    return new Activity(lastLogin, frequency);
  }

  private static Seeking randomSeeking() {
    int bound1 = randomAge();
    int bound2 = randomAge();
    return new Seeking(randomGender(), new Range(min(bound1, bound2), max(bound1, bound2)));
  }

  private static RelationShipStatus randomRelationShipStatus() {
    int i = random.nextInt(100);
    return i < 35 ? Divorced : i > 65 ? RelationShipStatus.Maried : RelationShipStatus.Single;
  }
}
