package com.senacor.hackingdays.serialization.data.generate;


import com.senacor.hackingdays.serialization.thirftdata.*;

import static com.senacor.hackingdays.serialization.thirftdata.Gender.Disambiguous;
import static com.senacor.hackingdays.serialization.thirftdata.Gender.Female;
import static com.senacor.hackingdays.serialization.thirftdata.Gender.Male;

import static com.senacor.hackingdays.serialization.thirftdata.RelationShipStatus.Divorced;
import static com.senacor.hackingdays.serialization.thirftdata.RelationShipStatus.Single;
import static com.senacor.hackingdays.serialization.thirftdata.RelationShipStatus.Maried;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


import static java.lang.Integer.max;
import static java.lang.Integer.min;

public class ProfileGenerator implements Iterable<Profile> {

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

    private ProfileGenerator(int size, Supplier<Integer> ageFunction, Supplier<Gender> genderFunction, Function<Gender, String> nameFunction, Supplier<Activity> activityFunction, Supplier<RelationShipStatus> bla) {
        this.sampleSize = size;
        this.ageFunction = ageFunction;
        this.genderFunction = genderFunction;
        this.nameFunction = nameFunction;
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
        Profile profile = new Profile();
        profile.setName(nameFunction.apply(gender));
        profile.setGender(gender);

        profile.setAge(ageFunction.get());
        profile.setLocation(locationSupplier.get());
        profile.setRelationShip(randomRelationShipStatus());
        profile.setSmoker(random.nextBoolean());
        profile.setSeeking(randomSeeking());
        profile.setActivity(randomActivity());
        return profile;
    }

    private static Profile createProfile(String name, Gender gender) {
      Profile profile = new Profile();
      profile.setName(name);
      profile.setGender(gender);
      return profile;
    }


    private static Profile initialProfile(Gender gender) {
        switch (gender.getValue()) {
            case 0:
                return createProfile(maleNames.get(), gender);
            case 1:
                return createProfile(femaleNames.get(), gender);
            case 2:
                return createProfile(transgenderNames.get(), gender);
            default:
                throw new AssertionError();
        }
    }

    public static class Builder {

        private Supplier<Integer> ageFunction = ProfileGenerator::randomAge;
        private Supplier<Gender> genderFunction = ProfileGenerator::randomGender;
        private Function<Gender, String> nameFunction = defaultNameFunction();
        private Supplier<RelationShipStatus> relationShipStatusFunction = ProfileGenerator::randomRelationShipStatus;
        private Supplier<Activity> activityFunction = ProfileGenerator::randomActivity;
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

        public ProfileGenerator build() {
            return new ProfileGenerator(size, ageFunction, genderFunction, nameFunction, activityFunction, relationShipStatusFunction);
        }

    }
    private static final  Function<Gender, String> defaultNameFunction() {
        return gender -> {
            switch (gender.getValue()) {
                case 0:
                    return maleNames.get();
                case 1:
                    return femaleNames.get();
                case 2:
                    return transgenderNames.get();
                default:
                    throw new AssertionError();
            }
        };
    }

    private static int randomAge() {
        return random.nextInt(RangeConstants.MAX_AGE - RangeConstants.MIN_AGE) + RangeConstants.MIN_AGE;
    }

    private static Gender randomGender() {
        int i = random.nextInt(100);
        return i < 45 ? Gender.Male : i > 95 ? Gender.Disambiguous : Female;

    }

    private static Activity createActivity(Date lastLogin, int frequency) {
      Activity activity = new Activity();
      activity.setLastLoginTimestamp(lastLogin.getTime());
      activity.setLoginCount(frequency);

      return activity;
    }

    private static Activity randomActivity() {
        int frequency = random.nextInt(10);
        long toEpochMilli = NOW.minus(random.nextInt(1_000_000), ChronoUnit.MINUTES).toEpochMilli();
        Date lastLogin = new Date(toEpochMilli);
        return createActivity(lastLogin, frequency);
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
