package com.senacor.hackingdays.distributedcache;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.util.StringUtil;
import com.senacor.hackingdays.distributedcache.generate.Profile2Generator;
import com.senacor.hackingdays.distributedcache.generate.ProfileGenerator;
import com.senacor.hackingdays.distributedcache.generate.model.Gender;
import com.senacor.hackingdays.distributedcache.generate.model.Profile;
import com.senacor.hackingdays.distributedcache.generate.model2.Profile2;

public class Datenpumpe {

    private static int COUNT = 0;
    private static Stopwatch sw = Stopwatch.createUnstarted();

    public static void main(String[] args) {
//        pumpeProfile();
        pumpeProfile2();
    }

    private static void pumpeProfile() {
        String defaultMap = "profileSenke";
        Integer defaultCount = 10_000;

        HazelcastInstance client = HazelcastClient.newHazelcastClient();

        try {
            Thread.sleep(500);

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("In welche Map? (leer fuer '" + defaultMap + "'): ");
            String s = br.readLine();
            if (StringUtil.isNullOrEmpty(s)) {
                s = defaultMap;
            }
            System.out.println("Wieviele? (leer fuer " + defaultCount + "):");
            try {
                String n = br.readLine();
                Integer nt = Integer.valueOf(n, 10);
                if (nt != null && nt >= 0) {
                    defaultCount = nt;
                }
            } catch (Exception e) {
                //whatever
            }
            System.out.println("OK, " + defaultCount + " nach " + s + ", geht los...");
            System.out.flush();

            IMap<String, Profile> profiles = client.getMap(s);

            sw.start();
            ProfileGenerator.newInstance(defaultCount).stream().parallel().forEach(profile -> {
                if (COUNT % 100 == 0) {
                    System.out.println("Habe bis jetzt " + COUNT + " Saetze nach " + sw.elapsed(TimeUnit.MILLISECONDS) + "ms geschrieben.");
                    System.out.println("Die naechste ID ist " + profile.getId().toString() + " (" + profile.getName() + ")");
                    System.out.flush();
                }
                COUNT++;
                profiles.set(profile.getId().toString(), profile);
            });
            sw.stop();
            System.out.println("fertig.");
            System.out.println("Habe " + COUNT + " Saetze in " + sw.elapsed(TimeUnit.MILLISECONDS) + "ms geschrieben.");
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void pumpeProfile2() {
        String defaultMap = "profile2Senke";
        Integer defaultCount = 10_000;

        HazelcastInstance client = HazelcastClient.newHazelcastClient();

        try {
            Thread.sleep(500);

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("In welche Map? (leer fuer '" + defaultMap + "'): ");
            String s = br.readLine();
            if (StringUtil.isNullOrEmpty(s)) {
                s = defaultMap;
            }
            System.out.println("Wieviele? (leer fuer " + defaultCount + "):");
            try {
                String n = br.readLine();
                Integer nt = Integer.valueOf(n, 10);
                if (nt != null && nt >= 0) {
                    defaultCount = nt;
                }
            } catch (Exception e) {
                //whatever
            }
            System.out.println("OK, " + defaultCount + " nach " + s + ", geht los...");
            System.out.flush();

            IMap<String, Profile2> profiles = client.getMap(s);

            sw.start();
            Profile2Generator.newInstance(defaultCount).stream().parallel().forEach(profile -> {
                if (COUNT % 100 == 0) {
                    System.out.println("Habe bis jetzt " + COUNT + " Saetze nach " + sw.elapsed(TimeUnit.MILLISECONDS) + "ms geschrieben.");
                    System.out.println("Die naechste ID ist " + profile.getId().toString() + " (" + profile.getName() + ")");
                    System.out.flush();
                }
                COUNT++;
                profiles.set(profile.getId().toString(), profile);
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
