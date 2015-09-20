package com.senacor.hackingdays.distributedcache.generate;

import static com.senacor.hackingdays.distributedcache.generate.model.Gender.Disambiguous;
import static com.senacor.hackingdays.distributedcache.generate.model.Gender.Female;
import static com.senacor.hackingdays.distributedcache.generate.model.Gender.Male;
import static java.lang.Math.max;
import static java.lang.Math.min;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.senacor.hackingdays.distributedcache.generate.model.Range;
import com.senacor.hackingdays.distributedcache.generate.model2.Activity2;
import com.senacor.hackingdays.distributedcache.generate.model2.Gender2;
import com.senacor.hackingdays.distributedcache.generate.model2.Profile2;
import com.senacor.hackingdays.distributedcache.generate.model2.Range2;
import com.senacor.hackingdays.distributedcache.generate.model2.RelationShipStatus2;
import com.senacor.hackingdays.distributedcache.generate.model2.Seeking2;

public class Profile2Generator implements Iterable<Profile2> {

    private final static NameSupplier femaleNames = NameSupplier.forGender(Female);
    private final static NameSupplier maleNames = NameSupplier.forGender(Male);
    private final static NameSupplier transgenderNames = NameSupplier.forGender(Disambiguous);
    private final static Location2Supplier locationSupplier = Location2Supplier.newInstance();
    private final static Random random = new Random();
    private static final Instant NOW = Instant.now();

    private final int sampleSize;
    private final Supplier<Integer> ageFunction;
    private final Supplier<Gender2> genderFunction;
    private final Function<Gender2, String> nameFunction;

    public static Profile2Generator newInstance(int sampleSize) {
        return new Builder(sampleSize).build();
    }

    public static Profile2 newProfile() {
        return newInstance(1).generateProfile();
    }

    private Profile2Generator(int size, Supplier<Integer> ageFunction, Supplier<Gender2> genderFunction, Function<Gender2, String> nameFunction,
        Supplier<Activity2> activityFunction, Supplier<RelationShipStatus2> relationShipStatusFunction, long seed) {
        this.sampleSize = size;
        this.ageFunction = ageFunction;
        this.genderFunction = genderFunction;
        this.nameFunction = nameFunction;
        random.setSeed(seed);
    }

    public Stream<Profile2> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    public Iterator<Profile2> iterator() {
        return new Iterator<Profile2>() {
            int count;

            public boolean hasNext() {
                return count < sampleSize;
            }

            public Profile2 next() {
                Profile2 profile = generateProfile();
                count++;
                return profile;
            }
        };
    }

    private Profile2 generateProfile() {
        Gender2 gender = genderFunction.get();
        Profile2 profile = new Profile2(nameFunction.apply(gender), gender);
        profile.setAge(ageFunction.get());
        profile.setLocation(locationSupplier.get());
        profile.setRelationShip(randomRelationShipStatus());
        profile.setSmoker(random.nextBoolean());
        profile.setSeeking(randomSeeking());
        profile.setActivity(randomActivity());
        return profile;
    }

    public static class Builder {

        private Supplier<Integer> ageFunction = Profile2Generator::randomAge;
        private Supplier<Gender2> genderFunction = Profile2Generator::randomGender;
        private Function<Gender2, String> nameFunction = defaultNameFunction();
        private Supplier<RelationShipStatus2> relationShipStatusFunction = Profile2Generator::randomRelationShipStatus;
        private Supplier<Activity2> activityFunction = Profile2Generator::randomActivity;
        private long seed = 42L;
        private final int size;

        public Builder(int sampleSize) {
            this.size = sampleSize;
        }

        public Builder withAge(Supplier<Integer> ageFunction) {
            this.ageFunction = ageFunction;
            return this;
        }


        public Builder withGender(Supplier<Gender2> genderFunction) {
            this.genderFunction = genderFunction;
            return this;
        }

        public Builder withName(Function<Gender2, String> nameFunction) {
            this.nameFunction = nameFunction;
            return this;
        }

        public Builder withRelationShipStatus(Supplier<RelationShipStatus2> relationShipStatusFunction) {
            this.relationShipStatusFunction = relationShipStatusFunction;
            return this;
        }

        public Builder withActivity(Supplier<Activity2> activityFunction) {
            this.activityFunction = activityFunction;
            return this;
        }

        public Builder withSeed(long seed) {
            this.seed = seed;
            return this;
        }

        public Profile2Generator build() {
            return new Profile2Generator(size, ageFunction, genderFunction, nameFunction, activityFunction, relationShipStatusFunction, seed);
        }

    }

    private static Function<Gender2, String> defaultNameFunction() {
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

    private static Gender2 randomGender() {
        int i = random.nextInt(100);
        return i < 45 ? Gender2.Male : i > 95 ? Gender2.Disambiguous : Gender2.Female;

    }

    private static Activity2 randomActivity() {
        int frequency = random.nextInt(10);
        long toEpochMilli = NOW.minus(random.nextInt(1_000_000), ChronoUnit.MINUTES).toEpochMilli();
        Date lastLogin = new Date(toEpochMilli);
        return new Activity2(lastLogin, frequency);
    }

    private static Seeking2 randomSeeking() {
        int bound1 = randomAge();
        int bound2 = randomAge();
        return new Seeking2(randomGender(), new Range2(min(bound1, bound2), max(bound1, bound2)));
    }

    private static RelationShipStatus2 randomRelationShipStatus() {
        int i = random.nextInt(100);
        return i < 35 ? RelationShipStatus2.Divorced : i > 65 ? RelationShipStatus2.Maried : RelationShipStatus2.Single;
    }
}
