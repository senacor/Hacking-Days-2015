package com.senacor.hackingdays.serialization.data.generate;

import org.junit.Test;

/**
 * Created by hmarginean on 19/09/15.
 */
public class ParallelProfileGeneratorTest{

    @Test
    public void testGenerate(){
        ParallelProfileGenerator parallelProfileGenerator = new ParallelProfileGenerator(4,2);
        parallelProfileGenerator.generate();
        parallelProfileGenerator.showProfiles();
    }
}
