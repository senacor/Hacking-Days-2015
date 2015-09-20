package com.senacor.hackingdays.distributedcache.generate.model2;

import java.io.Serializable;
import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Profile2 implements Serializable {

	private static final long serialVersionUID = 2;

	private final String name;

	private final Gender2 gender;

	private int age;

	private Location2 location;

	private RelationShipStatus2 relationShip;

	private boolean smoker;

	private Seeking2 seeking;

	private Activity2 activity;

	private final UUID uuid;

	public Profile2(String name, Gender2 gender) {
		this(name, gender, UUID.randomUUID());
	}

	public Profile2(String name, Gender2 gender, UUID uuid) {
		this.name = name;
		this.gender = gender;
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public Gender2 getGender() {
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

	public Location2 getLocation() {
		return location;
	}

	public void setLocation(Location2 location) {
		this.location = location;
	}

	public RelationShipStatus2 getRelationShip() {
		return relationShip;
	}

	public void setRelationShip(RelationShipStatus2 relationShip) {
		this.relationShip = relationShip;
	}

	public boolean isSmoker() {
		return smoker;
	}

	public void setSmoker(boolean smoker) {
		this.smoker = smoker;
	}

	public Seeking2 getSeeking() {
		return seeking;
	}

	public void setSeeking(Seeking2 seeking) {
		this.seeking = seeking;
	}

	public Activity2 getActivity() {
		return activity;
	}

	public void setActivity(Activity2 activity) {
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

		if (!(obj instanceof Profile2))
			return false;

		Profile2 rhs = (Profile2) obj;

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
