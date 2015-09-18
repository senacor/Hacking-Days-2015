package com.senacor.hackingdays.serialization.data.generate;

import com.senacor.hackingdays.serialization.data.Activity;
import com.senacor.hackingdays.serialization.data.Gender;
import com.senacor.hackingdays.serialization.data.Profile;
import com.senacor.hackingdays.serialization.data.Range;
import com.senacor.hackingdays.serialization.data.RelationShipStatus;
import com.senacor.hackingdays.serialization.data.Seeking;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.senacor.hackingdays.serialization.data.Gender.Disambiguous;
import static com.senacor.hackingdays.serialization.data.Gender.Female;
import static com.senacor.hackingdays.serialization.data.Gender.Male;
import static com.senacor.hackingdays.serialization.data.RelationShipStatus.Divorced;
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

    public ProfileGenerator(int sampleSize) {
        this.sampleSize = sampleSize;
    }

    public static Profile newInstance() {
        return newProfile();
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
                Profile profile = newProfile();
                count++;
                return profile;
            }
        };
    }

    private static Profile newProfile() {
        Profile profile = initialProfile(randomGender());
        profile.setAge(randomAge());
        profile.setLocation(locationSupplier.get());
        profile.setRelationShip(randomRelationShipStatus());
        profile.setSmoker(random.nextBoolean());
        profile.setSeeking(randomSeeking());
        profile.setActivity(randomActivity());
        return profile;
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

    private static int randomAge() {
        return random.nextInt(Range.MAX_AGE - Range.MIN_AGE) + Range.MIN_AGE;
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

    private static String nextMale() {
        return null;
    }

    private static Gender randomGender() {
        int i = random.nextInt(100);
        return i < 45 ? Gender.Male : i > 95 ? Gender.Disambiguous : Female;

    }


}
