package com.senacor.hackingdays.serializer.jsonio;

import java.io.IOException;
import java.util.LinkedList;

import com.cedarsoftware.util.io.JsonObject;
import com.cedarsoftware.util.io.JsonReader.JsonClassReader;
import com.senacor.hackingdays.serialization.data.Range;

public class RangeReader implements JsonClassReader {

	@Override
	public Object read(Object jOb, LinkedList<JsonObject<String, Object>> stack) throws IOException {
		if (jOb instanceof Range) {
			return jOb;
		}

		JsonObject jObj = (JsonObject) jOb;

		if (!jObj.containsKey("lower")) {
			throw new IOException("Range missing 'lower' field");
		}

		if (!jObj.containsKey("upper")) {
			throw new IOException("Range missing 'upper' field");
		}

		jObj.setTarget(new Range(((Number) jObj.get("lower")).intValue(), ((Number) jObj.get("upper")).intValue()));
		return jObj.getTarget();
	}

}
