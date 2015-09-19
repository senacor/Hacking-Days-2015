package com.senacor.hackingdays.distributedcache.generate.model;

import java.io.Serializable;
import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Profile implements Serializable {

	private static final long serialVersionUID = 1;

	private final String name;

	private final Gender gender;

	private int age;

	private Location location;

	private RelationShipStatus relationShip;

	private boolean smoker;

	private Seeking seeking;

	private Activity activity;

	private final UUID uuid;

	public Profile(String name, Gender gender) {
		this(name, gender, UUID.randomUUID());
	}

	public Profile(String name, Gender gender, UUID uuid) {
		this.name = name;
		this.gender = gender;
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public Gender getGender() {
		return gender;
	}

	public int getAge() {
		return age;
	}

	public UUID getId() {
		return uuid;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public RelationShipStatus getRelationShip() {
		return relationShip;
	}

	public void setRelationShip(RelationShipStatus relationShip) {
		this.relationShip = relationShip;
	}

	public boolean isSmoker() {
		return smoker;
	}

	public void setSmoker(boolean smoker) {
		this.smoker = smoker;
	}

	public Seeking getSeeking() {
		return seeking;
	}

	public void setSeeking(Seeking seeking) {
		this.seeking = seeking;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	@Override
	public String toString() {
		return "DatingProfile{" + "name='" + name + '\'' + ", gender=" + gender + ", age=" + age + ", location="
				+ location + ", relationShip=" + relationShip + ", smoker=" + smoker + ", seeking=" + seeking
				+ ", activity=" + activity + '}';
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;

		if (!(obj instanceof Profile))
			return false;

		Profile rhs = (Profile) obj;

		return new EqualsBuilder() //
				.append(this.age, rhs.age) //
				.append(this.smoker, rhs.smoker) //
				.append(this.activity, rhs.activity) //
				.append(this.gender, rhs.gender) //
				.append(this.location, rhs.location) //
				.append(this.name, rhs.name) //
				.append(this.relationShip, rhs.relationShip) //
				.append(this.seeking, rhs.seeking) //
				.append(this.uuid, rhs.uuid) //
				.isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder() //
				.append(this.age) //
				.append(this.smoker) //
				.append(this.activity) //
				.append(this.gender) //
				.append(this.location) //
				.append(this.name) //
				.append(this.relationShip) //
				.append(this.seeking) //
				.append(this.uuid) //
				.toHashCode();
	}
}
