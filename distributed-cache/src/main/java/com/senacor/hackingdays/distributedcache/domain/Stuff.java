package com.senacor.hackingdays.distributedcache.domain;

public class Stuff {
	private int id;
	private String name;

	public Stuff(String name) {
		this.name = name;
	}

	public Stuff(Integer id, String name) {
		this(name);
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return id + ": " + name;
	}
}
