package com.senacor.hackingdays.serialization.data.generate;

import static com.senacor.hackingdays.serialization.data.Gender.Disambiguous;
import static com.senacor.hackingdays.serialization.data.Gender.Female;
import static com.senacor.hackingdays.serialization.data.Gender.Male;
import static com.senacor.hackingdays.serialization.data.proto.ProfileProtos.RelationShipStatus.Divorced;
import static com.senacor.hackingdays.serialization.data.proto.ProfileProtos.RelationShipStatus.Maried;
import static com.senacor.hackingdays.serialization.data.proto.ProfileProtos.RelationShipStatus.Single;
import static java.lang.Integer.max;
import static java.lang.Integer.min;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.senacor.hackingdays.serialization.data.Gender;
import com.senacor.hackingdays.serialization.data.Profile;
import com.senacor.hackingdays.serialization.data.Range;
import com.senacor.hackingdays.serialization.data.proto.ProfileProtos;

public class ProfileProtoGenerator implements Iterable<ProfileProtos.Profile>, DataGenerator {

    private final static NameSupplier femaleNames = NameSupplier.forGender(Female);
    private final static NameSupplier maleNames = NameSupplier.forGender(Male);
    private final static NameSupplier transgenderNames = NameSupplier.forGender(Disambiguous);
    private final static LocationProtoSupplier locationSupplier = LocationProtoSupplier.newInstance();
    private final static Random random = new Random();
    private static final Instant NOW = Instant.now();

    private final int sampleSize;
    private final Supplier<Integer> ageFunction;
    private final Supplier<ProfileProtos.Gender> genderFunction;
    private final Function<ProfileProtos.Gender, String> nameFunction;

    public static ProfileProtoGenerator newInstance(int sampleSize) {
        return new Builder(sampleSize).build();
    }

    public static ProfileProtos.Profile newProfile() {
        return newInstance(1).generateProfile();
    }

    private ProfileProtoGenerator(int size,
                                  Supplier<Integer> ageFunction,
                                  Supplier<ProfileProtos.Gender> genderFunction,
                                  Function<ProfileProtos.Gender, String> nameFunction,
                                  Supplier<ProfileProtos.Activity> activityFunction,
                                  Supplier<ProfileProtos.RelationShipStatus> relationShipStatusFunction,
                                  long seed) {
        this.sampleSize = size;
        this.ageFunction = ageFunction;
        this.genderFunction = genderFunction;
        this.nameFunction = nameFunction;
        random.setSeed(seed);
    }

    public Stream<ProfileProtos.Profile> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    public Iterator<ProfileProtos.Profile> iterator() {
        return new Iterator<ProfileProtos.Profile>() {
            int count;

            public boolean hasNext() {
                return count < sampleSize;
            }

            public ProfileProtos.Profile next() {
                ProfileProtos.Profile profile = generateProfile();
                count++;
                return profile;
            }
        };
    }

    private ProfileProtos.Profile generateProfile() {
        ProfileProtos.Gender gender = genderFunction.get();
        ProfileProtos.Profile profile = ProfileProtos.Profile.newBuilder()
                .setName(nameFunction.apply(gender))
                .setGender(gender)
                .setAge(ageFunction.get())

                .setLocation(locationSupplier.get())
                .setRelationShip(randomRelationShipStatus())
                .setSmoker(random.nextBoolean())
                .setSeeking(randomSeeking())
                .setActivity(randomActivity())
                .build();
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
      iterator().forEachRemaining(o -> consumer.accept(o));
    }

    public static class Builder {

        private Supplier<Integer> ageFunction = ProfileProtoGenerator::randomAge;
        private Supplier<ProfileProtos.Gender> genderFunction = ProfileProtoGenerator::randomGender;
        private Function<ProfileProtos.Gender, String> nameFunction = defaultNameFunction();
        private Supplier<ProfileProtos.RelationShipStatus> relationShipStatusFunction = ProfileProtoGenerator::randomRelationShipStatus;
        private Supplier<ProfileProtos.Activity> activityFunction = ProfileProtoGenerator::randomActivity;
        private long seed;
        private final int size;

        public Builder(int sampleSize) {
            this.size = sampleSize;
        }

        public Builder withAge(Supplier<Integer> ageFunction) {
            this.ageFunction = ageFunction;
            return this;
        }


        public Builder withGender(Supplier<ProfileProtos.Gender> genderFunction) {
            this.genderFunction = genderFunction;
            return this;
        }

        public Builder withName(Function<ProfileProtos.Gender, String> nameFunction) {
            this.nameFunction = nameFunction;
            return this;
        }

        public Builder withRelationShipStatus(Supplier<ProfileProtos.RelationShipStatus> relationShipStatusFunction) {
            this.relationShipStatusFunction = relationShipStatusFunction;
            return this;
        }

        public Builder withActivity(Supplier<ProfileProtos.Activity> activityFunction) {
            this.activityFunction = activityFunction;
            return this;
        }

        public Builder setSeed(long seed) {
            this.seed = seed;
            return this;
        }

        public ProfileProtoGenerator build() {
            return new ProfileProtoGenerator(size, ageFunction, genderFunction, nameFunction, activityFunction, relationShipStatusFunction, seed);
        }

    }

    private static Function<ProfileProtos.Gender, String> defaultNameFunction() {
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

    private static ProfileProtos.Gender randomGender() {
        int i = random.nextInt(100);
        return i < 45 ? ProfileProtos.Gender.Male : i > 95 ? ProfileProtos.Gender.Disambiguous : ProfileProtos.Gender.Female;

    }

    private static ProfileProtos.Activity randomActivity() {
        int frequency = random.nextInt(10);
        long toEpochMilli = NOW.minus(random.nextInt(1_000_000), ChronoUnit.MINUTES).toEpochMilli();
        //Date lastLogin = new Date(toEpochMilli);

        return ProfileProtos.Activity.newBuilder()
                .setLastLogin(toEpochMilli)
                .setLoginCount(frequency)
                .build();
    }

    private static ProfileProtos.Seeking randomSeeking() {
        int bound1 = randomAge();
        int bound2 = randomAge();
        return ProfileProtos.Seeking.newBuilder()
                .setGender(randomGender())
                .setAgeRange(ProfileProtos.Range.newBuilder().setLower(min(bound1, bound2)).setUpper(max(bound1, bound2)))
                .build();
    }

    private static ProfileProtos.RelationShipStatus randomRelationShipStatus() {
        int i = random.nextInt(100);
        return i < 35 ? Divorced : i > 65 ? Maried : Single;
    }
}
