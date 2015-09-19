package com.senacor.hackingdays.distributedcache;

import com.google.common.base.Stopwatch;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.SerializerConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.security.UsernamePasswordCredentials;
import com.hazelcast.util.StringUtil;
import com.senacor.hackingdays.distributedcache.generate.ProfileGenerator;
import com.senacor.hackingdays.distributedcache.generate.model.Gender;
import com.senacor.hackingdays.distributedcache.generate.model.Profile;
import com.senacor.hackingdays.distributedcache.serializer.KryoProfileStreamSerializer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Datenpumpe {

    private static int COUNT = 0;
    private static Stopwatch sw = Stopwatch.createUnstarted();

    public static void main(String[] args) {


        String defaultMap = "profileSenke";
        Integer defaultCount = 10_000;


        ClientConfig clientConfig = new ClientConfig();
        clientConfig.getSerializationConfig().getSerializerConfigs().add(
                new SerializerConfig().
                        setTypeClass(Profile.class).
                        setImplementation(new KryoProfileStreamSerializer()));
        clientConfig.setCredentials(new UsernamePasswordCredentials("sinalco", "sinalco"));
        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);

        try {
            Thread.sleep(500);

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("In welche Map? (leer fuer '" + defaultMap + "'): ");
            String s = br.readLine();
            if (StringUtil.isNullOrEmpty(s)) s = defaultMap;
            System.out.println("Wieviele? (leer fuer " + defaultCount + "):");
            try {
                String n = br.readLine();
                Integer nt = Integer.valueOf(n, 10);
                if (nt != null && nt >= 0) defaultCount = nt;
            } catch (Exception e) {
                //whatever
            }
            System.out.println("OK, " + defaultCount + " nach " + s + ", geht los...");
            System.out.flush();

            Map<String, Profile> profiles = client.getMap(s);

            sw.start();
            ProfileGenerator.newInstance(defaultCount).stream().forEach(profile -> {
                if (COUNT % 100 == 0) {
                    System.out.println("Habe bis jetzt " + COUNT + " Saetze nach " + sw.elapsed(TimeUnit.MILLISECONDS) + "ms geschrieben.");
                    System.out.flush();
                }
                COUNT++;
                profiles.put(profile.getId().toString(), profile);
            });
            sw.stop();
            System.out.println("fertig.");
            System.out.println("Habe " + COUNT + " Saetze in " + sw.elapsed(TimeUnit.MILLISECONDS) + "ms geschrieben.");
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private static String toString(Gender gender) {
        switch (gender) {
            case Male:
                return "männlich";
            case Female:
                return "weiblich";
            case Disambiguous:
                return "uneindeutig";
            default:
                return "unbekannt";
        }
    }
}
