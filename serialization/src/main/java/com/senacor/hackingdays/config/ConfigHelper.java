package com.senacor.hackingdays.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class ConfigHelper {


    public static Config createConfig(String serializerName, String fqcn) {
        return createConfig(serializerName, fqcn, "application.conf");
    }


    public static Config createConfig(String serializerName, String fqcn, String confName) {
        String configSnippet = String.format("akka {\n" +
                "  actor {\n" +
                "    serializers {\n" +
                "      %s = \"%s\"\n" +
                "    }\n" +
                "\n" +
                "    serialization-bindings {\n" +
                "      \"com.senacor.hackingdays.serialization.data.Profile\" = %s\n" +
                "      \"com.senacor.hackingdays.serialization.data.thrift.Profile\" = %s\n" +
                "      \"com.google.protobuf.GeneratedMessage\" = %s\n" +
                "    }\n" +
                "  }\n" +
                "}", serializerName, fqcn, serializerName, serializerName, serializerName);

        Config overrides = ConfigFactory.parseString(configSnippet);
        return overrides.withFallback(ConfigFactory.load(confName));
    }
}
