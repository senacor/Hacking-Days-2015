package com.senacor.hackingdays.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class ConfigHelper {


    public static Config createConfig(String serializerName, String fqcn) {
        String configSnippet = String.format("akka {\n" +
                "  actor {\n" +
                "    serializers {\n" +
                "      %s = \"%s\"\n" +
                "    }\n" +
                "\n" +
                "    serialization-bindings {\n" +
                "      \"com.senacor.hackingdays.serialization.data.Profile\" = %s\n" +
                "    }\n" +
                "  }\n" +
                "}", serializerName, fqcn, serializerName);

        Config overrides = ConfigFactory.parseString(configSnippet);
        return overrides.withFallback(ConfigFactory.load());
    }
}
