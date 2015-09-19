package com.senacor.hackingdays.lmax.generate;


import com.senacor.hackingdays.lmax.generate.model.Activity;
import com.senacor.hackingdays.lmax.generate.model.Gender;
import com.senacor.hackingdays.lmax.generate.model.Profile;
import com.senacor.hackingdays.lmax.generate.model.RelationShipStatus;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.senacor.hackingdays.lmax.generate.model.Gender.Female;

public class ProfileGenerator implements Iterable<Profile> {

    private final static Random random = new Random();
    private static final Instant NOW = Instant.now();

    private final int sampleSize;
    private final AgeSupplier ageFunction;
    private final Supplier<Gender> genderFunction;
    private final NameSupplier nameFunction;
    private final Supplier<RelationShipStatus> relationShipStatusFunction;
    private final Supplier<Activity> activityFunction;
    private final LocationSupplier locationSupplier;
    private final SeekingSupplier seekingSupplier;


    public ProfileGenerator(int size,
                            AgeSupplier ageFunction,
                            Supplier<Gender> genderFunction,
                            NameSupplier nameFunction,
                            Supplier<RelationShipStatus> relationShipStatusFunction,
                            Supplier<Activity> activityFunction,
                            LocationSupplier locationSupplier,
                            SeekingSupplier seekingSupplier) {
        this.sampleSize = size;
        this.ageFunction = ageFunction;
        this.genderFunction = genderFunction;
        this.nameFunction = nameFunction;
        this.relationShipStatusFunction = relationShipStatusFunction;
        this.activityFunction = activityFunction;
        this.locationSupplier = locationSupplier;

        this.seekingSupplier = seekingSupplier;
    }

    public static ProfileGenerator newInstance(int sampleSize) {
        return new Builder(sampleSize).build();
    }

    public static Profile newProfile() {
        return newInstance(1).generateProfile();
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
        profile.setAge(ageFunction.apply(gender));
        profile.setLocation(locationSupplier.get());
        profile.setRelationShip(relationShipStatusFunction.get());
        profile.setSmoker(random.nextBoolean());
        profile.setSeeking(seekingSupplier.apply(gender));
        profile.setActivity(activityFunction.get());
        return profile;
    }

    public static class Builder {

        private final int size;
        private AgeSupplier ageFunction = AgeSupplier.randomAge();
        private Supplier<Gender> genderFunction = ProfileGenerator::randomGender;
        private NameSupplier nameFunction = new DefaultNameSupplier();
        private Supplier<RelationShipStatus> relationShipStatusFunction = ProfileGenerator::randomRelationShipStatus;
        private Supplier<Activity> activityFunction = ProfileGenerator::randomActivity;
        private LocationSupplier locationSupplier = new DefaultLocationSupplier();
        private SeekingSupplier seekingSupplier = new DefaultSeekingSupplier();

        public Builder(int sampleSize) {
            this.size = sampleSize;
        }

        public Builder withAge(AgeSupplier ageFunction) {
            this.ageFunction = ageFunction;
            return this;
        }


        public Builder withGender(Supplier<Gender> genderFunction) {
            this.genderFunction = genderFunction;
            return this;
        }

        public Builder withName(NameSupplier nameFunction) {
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

        public Builder withActivity(LocationSupplier locationSupplier) {
            this.locationSupplier = locationSupplier;
            return this;
        }

        public Builder withASeeking(SeekingSupplier seekingSupplier) {
            this.seekingSupplier = seekingSupplier;
            return this;
        }

        public ProfileGenerator build() {
            return new ProfileGenerator(
                    size,
                    ageFunction,
                    genderFunction,
                    nameFunction,
                    relationShipStatusFunction,
                    activityFunction,
                    locationSupplier,
                    seekingSupplier);
        }

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

    private static RelationShipStatus randomRelationShipStatus() {
        int i = random.nextInt(100);
        return i < 35 ? RelationShipStatus.Divorced : i > 65 ? RelationShipStatus.Maried : RelationShipStatus.Single;
    }
}
