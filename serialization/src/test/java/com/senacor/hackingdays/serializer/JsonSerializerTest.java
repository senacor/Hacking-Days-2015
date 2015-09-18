package com.senacor.hackingdays.serializer;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class JsonSerializerTest {
	
	public class Zeug {
		private final String eins;
		private final String zwei;
		private final boolean bla;
		
		public Zeug(String eins, String zwei, boolean bla) {
			this.eins=eins;
			this.zwei=zwei;
			this.bla=bla;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj == this)
				return true;
			
			if (!(obj instanceof Zeug)) {
				return false;
			}
			
			Zeug o = (Zeug) obj; 
			
			return new EqualsBuilder().append(this.bla, o.bla).append(this.eins, o.eins).append(this.zwei,  o.zwei).isEquals();
		}
	}

	@Test
	public void testSerialization() {
		JsonSerializer serializer = new JsonSerializer();
		Zeug zeug = new Zeug("a", "b", false);
		byte[] serialized = serializer.toBinary(zeug);
		assertThat(serialized, is(notNullValue()));
		Zeug deserialized = (Zeug) serializer.fromBinary(serialized, Zeug.class);
		assertThat(deserialized, is(equalTo(zeug)));
	}

}
