package com.senacor.hackingdays.serializer;

import akka.actor.ExtendedActorSystem;
import akka.serialization.JSerializer;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.UncheckedIOException;

public class JacksonSerializer extends JSerializer {


    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ExtendedActorSystem actorSystem;

    public JacksonSerializer(ExtendedActorSystem actorSystem) {
        this.actorSystem = actorSystem;
    }

    @Override
    public Object fromBinaryJava(byte[] bytes, Class<?> manifest) {
        try {
            return objectMapper.readValue(bytes, manifest);
        } catch (IOException e) {
            actorSystem.log().error("Error during serialization: {}", e);
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public byte[] toBinary(Object o) {
        try {
            return objectMapper.writeValueAsBytes(o);
        } catch (IOException e) {
            actorSystem.log().error("Error during serialization: {}", e);
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public boolean includeManifest() {
        return true;
    }

    @Override
    public int identifier() {
        return 666;
    }
}
