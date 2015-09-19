package com.senacor.hackingdays.remoting;

import com.senacor.hackingdays.serialization.data.Profile;
import com.senacor.hackingdays.serialization.data.proto.ProfileProtos;

/**
 * @author Alasdair Collinson, Senacor Technologies AG
 */
public class ProducerProfile {
    final String serializerName;
    final String fqcn;
    final String port;
    final Class<?> profileClass;

    public ProducerProfile(String serializerName, String fqcn, String port, Class<?> profileClass) {
        this.serializerName = serializerName;
        this.fqcn = fqcn;
        this.port = port;
        this.profileClass = profileClass;
    }

    public String getSerializerName() {
        return serializerName;
    }

    public String getFqcn() {
        return fqcn;
    }

    public String getPort() {
        return port;
    }

    public Class<?> getProfileClass() {
        return profileClass;
    }

    public static ProducerProfile createProfile(String serializerName, String fqcn, String port) {
        return new ProducerProfile(serializerName, fqcn, port, Profile.class);
    }

    public static ProducerProfile createProtoBufProfile(String serializerName, String fqcn, String port) {
        return new ProducerProfile(serializerName, fqcn, port, ProfileProtos.Profile.class);
    }

    public static ProducerProfile createThriftProfile(String serializerName, String fqcn, String port) {
        return new ProducerProfile(serializerName, fqcn, port, com.senacor.hackingdays.serialization.data.thrift.Profile.class);
    }
}
