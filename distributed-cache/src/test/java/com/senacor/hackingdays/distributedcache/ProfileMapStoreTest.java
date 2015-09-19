package com.senacor.hackingdays.distributedcache;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.senacor.hackingdays.distributedcache.generate.model.Activity;
import com.senacor.hackingdays.distributedcache.generate.model.Gender;
import com.senacor.hackingdays.distributedcache.generate.model.Location;
import com.senacor.hackingdays.distributedcache.generate.model.Profile;
import com.senacor.hackingdays.distributedcache.generate.model.Range;
import com.senacor.hackingdays.distributedcache.generate.model.RelationShipStatus;
import com.senacor.hackingdays.distributedcache.generate.model.Seeking;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Alasdair Collinson, Senacor Technologies AG
 */
public class ProfileMapStoreTest {

    @Before
    public void setup() {
    }

    @Test
    public void storeAndRetrieveProfile() throws IOException {
        Profile inputProfile = createProfile("Hans Wurst", Gender.Male);

        Profile loadedProfile;
        try (ProfileMapStore mapStore = new ProfileMapStore()) {
            mapStore.store(inputProfile.getId(), inputProfile);

            loadedProfile = mapStore.load(inputProfile.getId());
        }
        Assert.assertEquals(inputProfile.getName(), loadedProfile.getName());
        Assert.assertEquals(inputProfile.getGender(), loadedProfile.getGender());
        Assert.assertEquals(inputProfile.getId(), loadedProfile.getId());
    }

    @Test
    public void storeAndRetrieveProfiles() throws IOException {
        Profile profile1 = createProfile("Hans Wurst", Gender.Male);
        Profile profile2 = createProfile("Brat Wurst", Gender.Disambiguous);
        Profile profile3 = createProfile("Leber Wurst", Gender.Female);
        Map<UUID, Profile> inputProfiles = Maps.newHashMap();
        inputProfiles.put(profile1.getId(), profile1);
        inputProfiles.put(profile2.getId(), profile2);
        inputProfiles.put(profile3.getId(), profile3);

        Map<UUID, Profile> loadedProfiles;
        try (ProfileMapStore mapStore = new ProfileMapStore()) {
            mapStore.storeAll(inputProfiles);

            loadedProfiles = mapStore.loadAll(Lists.newArrayList(profile1.getId(), profile2.getId(), profile3.getId()));
        }
        for(Map.Entry<UUID, Profile> profileEntry : inputProfiles.entrySet()) {
            Profile loadedProfile = loadedProfiles.get(profileEntry.getKey());
            Assert.assertEquals(profileEntry.getValue().getName(), loadedProfile.getName());
            Assert.assertEquals(profileEntry.getValue().getGender(), loadedProfile.getGender());
            Assert.assertEquals(profileEntry.getValue().getId(), loadedProfile.getId());
        }
    }

    @Test
    public void storeAndDeleteProfile() throws IOException {
        Profile profile = createProfile("Hans Wurst", Gender.Male);

        Profile loadedProfile;
        try (ProfileMapStore mapStore = new ProfileMapStore()) {
            mapStore.store(profile.getId(), profile);

            mapStore.delete(profile.getId());

            loadedProfile = mapStore.load(profile.getId());
        }
        Assert.assertNull(loadedProfile);
    }

    @Test
    public void storeAndDeleteProfiles() throws IOException {
        Profile profile1 = createProfile("Hans Wurst", Gender.Male);
        Profile profile2 = createProfile("Brat Wurst", Gender.Disambiguous);
        Profile profile3 = createProfile("Leber Wurst", Gender.Female);
        Map<UUID, Profile> inputProfiles = Maps.newHashMap();
        inputProfiles.put(profile1.getId(), profile1);
        inputProfiles.put(profile2.getId(), profile2);
        inputProfiles.put(profile3.getId(), profile3);

        Map<UUID, Profile> loadedProfiles;
        try (ProfileMapStore mapStore = new ProfileMapStore()) {
            mapStore.storeAll(inputProfiles);

            ArrayList<UUID> ids = Lists.newArrayList(profile1.getId(), profile2.getId(), profile3.getId());
            mapStore.deleteAll(ids);

            loadedProfiles = mapStore.loadAll(ids);
        }
        Assert.assertTrue(loadedProfiles.isEmpty());
    }

    @Test
    public void storeProfilesAndgetAllKeys() throws IOException {
        Profile profile1 = createProfile("Hans Wurst", Gender.Male);
        Profile profile2 = createProfile("Brat Wurst", Gender.Disambiguous);
        Profile profile3 = createProfile("Leber Wurst", Gender.Female);
        Map<UUID, Profile> inputProfiles = Maps.newHashMap();
        inputProfiles.put(profile1.getId(), profile1);
        inputProfiles.put(profile2.getId(), profile2);
        inputProfiles.put(profile3.getId(), profile3);
        ArrayList<UUID> ids = Lists.newArrayList(profile1.getId(), profile2.getId(), profile3.getId());

        Iterable<UUID> keys;
        try (ProfileMapStore mapStore = new ProfileMapStore()) {
            mapStore.storeAll(inputProfiles);

            keys = mapStore.loadAllKeys();
        }
        Assert.assertThat(ids, new ContainsMatcher(keys));
    }

    private Profile createProfile(String name, Gender gender) {
        Profile profile = new Profile(name, gender);
        Activity activity = new Activity(new Date(), 8);
        profile.setActivity(activity);
        profile.setAge(30);
        Location location = new Location("State", "City", "zip");
        profile.setLocation(location);
        profile.setRelationShip(RelationShipStatus.Single);
        Seeking seeking = new Seeking(Gender.Disambiguous, new Range(25, 35));
        profile.setSeeking(seeking);
        profile.setSmoker(false);
        return profile;
    }

    private static class ContainsMatcher extends BaseMatcher<Iterable<UUID>> {
        private final Collection<UUID> compareAgainst;

        public ContainsMatcher(Iterable<UUID> compareAgainst) {
            this.compareAgainst = Lists.newArrayList(compareAgainst);
        }

        @Override
        public boolean matches(Object items) {
            if(!(items instanceof Iterable)) {
                return false;
            }
            boolean result = true;
            for(Object item : (Iterable) items) {
                result &= compareAgainst.contains(item);
            }
            return result;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("Eine Collection enthält ein Element");
        }
    }
}