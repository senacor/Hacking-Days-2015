package com.senacor.hackingdays.distributedcache.executor;

import com.senacor.hackingdays.distributedcache.generate.model.Profile;
import com.senacor.hackingdays.distributedcache.generate.model.Seeking;

public class ProfileMatcher {

	public static boolean matching(Profile first, Profile second) {
		return firstLikesSecond(first, second) && firstLikesSecond(second, first);
	}
	
	private static boolean firstLikesSecond(Profile first, Profile second) {
		Seeking firstSeeking = first.getSeeking();
		if (firstSeeking == null) 
			return false;
		if (!firstSeeking.getGender().equals(second.getGender()))
			return false;
		return firstSeeking.getAgeRange().getLower() <= second.getAge() && 
				second.getAge() <= firstSeeking.getAgeRange().getUpper();
	}
}
