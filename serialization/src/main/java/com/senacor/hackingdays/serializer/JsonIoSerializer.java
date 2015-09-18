package com.senacor.hackingdays.serializer;

import static com.cedarsoftware.util.io.JsonReader.addReader;
import static com.cedarsoftware.util.io.JsonReader.jsonToJava;
import static com.cedarsoftware.util.io.JsonWriter.objectToJson;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.senacor.hackingdays.serialization.data.Range;
import com.senacor.hackingdays.serializer.jsonio.RangeReader;

import akka.serialization.JSerializer;

public class JsonIoSerializer extends JSerializer {
	/** Logger for this class */
	private static final Logger LOGGER = LoggerFactory.getLogger(JsonIoSerializer.class);

	public JsonIoSerializer() {
		addReader(Range.class, new RangeReader());
	}

	@Override
    public Object fromBinaryJava(byte[] bytes, Class<?> manifest) {
    	try {
			return jsonToJava(new String(bytes, UTF_8));

    	} catch (IOException e) {
			LOGGER.error("Exception during serialization", e);
			return null;
		}
    }

    @Override
    public byte[] toBinary(Object o) {
    	try {
			return objectToJson(o).getBytes(UTF_8);

    	} catch (IOException e) {
			LOGGER.error("Exception during deserialization", e);
			return null;
		}
    }

    @Override
    public int identifier() {
        return 101;
    }

    @Override
    public boolean includeManifest() {
        return false;
    }
}
