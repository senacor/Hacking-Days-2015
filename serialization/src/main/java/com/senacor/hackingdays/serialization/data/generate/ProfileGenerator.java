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

public class ProfileGenerator implements Iterable<Profile>, DataGenerator {

    private final static NameSupplier femaleNames = NameSupplier.forGender(Female);
    private final static NameSupplier maleNames = NameSupplier.forGender(Male);
    private final static NameSupplier transgenderNames = NameSupplier.forGender(Disambiguous);
    private final static LocationSupplier locationSupplier = LocationSupplier.newInstance();
    private final static Random random = new Random();
    private static final Instant NOW = Instant.now();

    private final int sampleSize;
    private final Supplier<Integer> ageFunction;
    private final Supplier<Gender> genderFunction;
    private final Function<Gender, String> nameFunction;

    public static ProfileGenerator newInstance(int sampleSize) {
        return new Builder(sampleSize).build();
    }

    public static Profile newProfile() {
        return newInstance(1).generateProfile();
    }

    private ProfileGenerator(int size, Supplier<Integer> ageFunction, Supplier<Gender> genderFunction, Function<Gender, String> nameFunction, Supplier<Activity> activityFunction, Supplier<RelationShipStatus> relationShipStatusFunction,
        long seed) {
        this.sampleSize = size;
        this.ageFunction = ageFunction;
        this.genderFunction = genderFunction;
        this.nameFunction = nameFunction;
        random.setSeed(seed);
    }

    public Stream<Profile> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    public Iterator<Profile> iterator() {
        return new Iterator<Profile>() {
            int count;

            public boolean hasNext() {
                return count < sampleSize;
            }

            public Profile next() {
                Profile profile = generateProfile();
                count++;
                return profile;
            }
        };
    }

    private Profile generateProfile() {
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

  @Override
  public void doEach(int size, Consumer consumer) {
    new Builder(size).build().iterator().forEachRemaining(o -> consumer.accept(o));
  }

  public static class Builder {

        private Supplier<Integer> ageFunction = ProfileGenerator::randomAge;
        private Supplier<Gender> genderFunction = ProfileGenerator::randomGender;
        private Function<Gender, String> nameFunction = defaultNameFunction();
        private Supplier<RelationShipStatus> relationShipStatusFunction = ProfileGenerator::randomRelationShipStatus;
        private Supplier<Activity> activityFunction = ProfileGenerator::randomActivity;
        private long seed = 42L;
        private final int size;

        public Builder(int sampleSize) {
            this.size = sampleSize;
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

        public ProfileGenerator build() {
            return new ProfileGenerator(size, ageFunction, genderFunction, nameFunction, activityFunction, relationShipStatusFunction, seed);
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
