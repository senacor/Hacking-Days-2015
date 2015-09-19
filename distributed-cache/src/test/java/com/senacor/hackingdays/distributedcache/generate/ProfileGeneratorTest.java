package com.senacor.hackingdays.distributedcache.generate;

import org.junit.Test;

/**
 * @author Andreas Keefer
 */
public class ProfileGeneratorTest {

    @Test
    public void testGenerate() throws Exception {
        ProfileGenerator generator = ProfileGenerator.newInstance(2);
        generator.stream().forEach(System.out::println);
    }
}