package com.senacor.hackingdays.distributedcache.generate.model2;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Seeking2 implements Serializable {

	private static final long serialVersionUID = 1L;
	private final Gender2 gender;
	private final Range2 ageRange;

	public Seeking2(Gender2 gender, Range2 ageRange) {
		this.gender = gender;
		this.ageRange = ageRange;
	}

	public Gender2 getGender() {
		return gender;
	}

	public Range2 getAgeRange() {
		return ageRange;
	}

	@Override
	public String toString() {
		return "Seeking{" + "gender=" + gender + ", ageRange=" + ageRange + '}';
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Seeking2))
			return false;

		Seeking2 rhs = (Seeking2) obj;

		return new EqualsBuilder() //
				.append(this.ageRange, rhs.ageRange) //
				.append(this.gender, rhs.gender) //
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder() //
				.append(this.ageRange) //
				.append(this.gender) //
				.toHashCode();
	}

}
