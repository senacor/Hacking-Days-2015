/*
 * Project       MKP
 * Copyright (c) 2009,2010,2011 DP IT Services GmbH
 *
 * All rights reserved.
 *
 * $Rev: $ 
 * $Date: $ 
 * $Author: $ 
 */
package com.senacor.hackingdays.lmax.lmax;

import com.senacor.hackingdays.lmax.generate.model.Profile;
import com.senacor.hackingdays.serialization.data.Activity;
import com.senacor.hackingdays.serialization.data.Gender;
import com.senacor.hackingdays.serialization.data.Location;
import com.senacor.hackingdays.serialization.data.Range;
import com.senacor.hackingdays.serialization.data.RelationShipStatus;
import com.senacor.hackingdays.serialization.data.Seeking;
import com.senacor.hackingdays.serialization.data.memorymanager.BuddyMemoryManager;
import com.senacor.hackingdays.serialization.data.memorymanager.MMOutOfMemoryException;
import com.senacor.hackingdays.serializer.UnsafeSerializer;

import java.util.Date;

/**
 * @author ccharles
 * @version $LastChangedVersion$
 */
public class KeepInRamConsumer extends CompletableConsumer {

  // 128 bytes minimum block size, 65.536 byte poolsize
  private static final long BLOCK_SIZE = 128;

  private static final long POOL_SIZE = (int) Math.pow(2,27);

  private BuddyMemoryManager memoryManager;

  private UnsafeSerializer unsafeSerializer;

  private long savedProfiles;

  private long discardedProfiles;

  public KeepInRamConsumer(int expectedMessages, Runnable onComplete) {
    super(expectedMessages, onComplete);
    memoryManager = new BuddyMemoryManager(BLOCK_SIZE, POOL_SIZE);
    unsafeSerializer = new UnsafeSerializer();
  }

  @Override
  protected void onComplete() {
    System.out.println("Saved profiles in pool of " + POOL_SIZE + ": " + savedProfiles);
    System.out.println("Discarded profiles due to Out of Memory: " + discardedProfiles);
  }

  @Override
  protected void processEvent(Profile profile, long sequence, boolean endOfBatch) {

    final com.senacor.hackingdays.serialization.data.Profile translatedProfile = translateProfile(profile);

    final byte[] serializedProfile = unsafeSerializer.toBinary(translatedProfile);

    try {
      final long address = memoryManager.malloc(serializedProfile.length);
      memoryManager.put(address, serializedProfile);
      savedProfiles++;
    } catch (MMOutOfMemoryException e) {
      discardedProfiles++;
    }

  }

  private com.senacor.hackingdays.serialization.data.Profile translateProfile(Profile profile) {

    final com.senacor.hackingdays.serialization.data.Profile translatedProfile =
            new com.senacor.hackingdays.serialization.data.Profile(profile.getName(),
                                                                   Gender.valueOf(profile.getGender().name()));
    translatedProfile.setAge(profile.getAge());
    translatedProfile.setLocation(new Location(profile.getLocation().getState(), profile.getLocation().getCity(),
                                               profile.getLocation().getZip()));
    translatedProfile.setRelationShip(RelationShipStatus.valueOf(profile.getRelationShip().name()));
    translatedProfile.setSmoker(profile.isSmoker());
    translatedProfile.setAge(profile.getAge());
    final Range range = new Range(profile.getSeeking().getAgeRange().getLower(), profile.getSeeking().getAgeRange().getUpper());
    //translatedProfile.setSeeking(new Seeking(Gender.valueOf(profile.getSeeking().getGender().name()),range));
    translatedProfile.setSeeking(new Seeking(Gender.Female,range));
    translatedProfile.setActivity(new Activity(new Date(profile.getActivity().getLastLogin().getTime()),profile.getActivity().getLoginCount()));
    return translatedProfile;
  }
}
