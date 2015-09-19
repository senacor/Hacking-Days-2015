package com.senacor.hackingdays.distributedcache.generate.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Seeking implements Serializable {

	private static final long serialVersionUID = 1L;
	private final Gender gender;
	private final Range ageRange;

	public Seeking(Gender gender, Range ageRange) {
		this.gender = gender;
		this.ageRange = ageRange;
	}

	public Gender getGender() {
		return gender;
	}

	public Range getAgeRange() {
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
		if (!(obj instanceof Seeking))
			return false;

		Seeking rhs = (Seeking) obj;

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
