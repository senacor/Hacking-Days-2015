package com.senacor.hackingdays.serialization.data.generate;

import org.junit.Test;

public class ProfileGeneratorTest {


    @Test
    public void thatProfilesAreGenerated() {


        ProfileGenerator generator = ProfileGenerator.newInstance(100);
        generator.stream().forEach(System.out::println);

    }

}