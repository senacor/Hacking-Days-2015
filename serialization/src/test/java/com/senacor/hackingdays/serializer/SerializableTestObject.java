package com.senacor.hackingdays.serializer;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;

public class SerializableTestObject implements Serializable {

	private static final long serialVersionUID = 1L;
	private final String eins;
	private final String zwei;
	private final boolean bla;

	public SerializableTestObject(String eins, String zwei, boolean bla) {
		this.eins = eins;
		this.zwei = zwei;
		this.bla = bla;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;

		if (!(obj instanceof SerializableTestObject)) {
			return false;
		}

		SerializableTestObject o = (SerializableTestObject) obj;

		return new EqualsBuilder().append(this.bla, o.bla).append(this.eins, o.eins).append(this.zwei, o.zwei)
				.isEquals();
	}
}