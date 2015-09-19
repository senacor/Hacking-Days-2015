package com.senacor.hackingdays.distributedcache.executor;

import static org.junit.Assert.*;

import org.junit.Test;

import com.senacor.hackingdays.distributedcache.generate.ProfileGenerator;
import com.senacor.hackingdays.distributedcache.generate.model.Gender;
import com.senacor.hackingdays.distributedcache.generate.model.Profile;
import com.senacor.hackingdays.distributedcache.generate.model.Range;
import com.senacor.hackingdays.distributedcache.generate.model.Seeking;

public class ProfileMatcherTest {

	@Test
	public void testMatching() {
		Profile firstProfile = ProfileGenerator.newProfile();
		firstProfile.setAge(30);
		Profile secondProfile = ProfileGenerator.newProfile();
		secondProfile.setAge(31);
		Seeking firstSeeking = new Seeking(secondProfile.getGender(), new Range(22, 40));
		firstProfile.setSeeking(firstSeeking);
		Seeking secondSeeking = new Seeking(firstProfile.getGender(), new Range(22, 40));
		secondProfile.setSeeking(secondSeeking);
		assertTrue(ProfileMatcher.matching(firstProfile, secondProfile));
		assertTrue(ProfileMatcher.matching(secondProfile, firstProfile));
	}
	
	@Test
	public void testUnMatchingRange() {
		Profile firstProfile = ProfileGenerator.newProfile();
		firstProfile.setAge(60);
		Profile secondProfile = ProfileGenerator.newProfile();
		secondProfile.setAge(31);
		Seeking firstSeeking = new Seeking(secondProfile.getGender(), new Range(22, 40));
		firstProfile.setSeeking(firstSeeking);
		Seeking secondSeeking = new Seeking(firstProfile.getGender(), new Range(22, 40));
		secondProfile.setSeeking(secondSeeking);
		assertFalse(ProfileMatcher.matching(firstProfile, secondProfile));
		assertFalse(ProfileMatcher.matching(secondProfile, firstProfile));
	}
	
	@Test
	public void testUnMatchingGender() {
		Profile firstProfile = new Profile("Sandy", Gender.Female);
		firstProfile.setAge(30);
		Profile secondProfile = new Profile("Andy", Gender.Male);
		secondProfile.setAge(31);
		Seeking firstSeeking = new Seeking(Gender.Male, new Range(22, 40));
		firstProfile.setSeeking(firstSeeking);
		Seeking secondSeeking = new Seeking(Gender.Male, new Range(22, 40));
		secondProfile.setSeeking(secondSeeking);
		assertFalse(ProfileMatcher.matching(firstProfile, secondProfile));
		assertFalse(ProfileMatcher.matching(secondProfile, firstProfile));
	}
}
