package org.robotframework.mavenplugin.harvesters;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Test;

public class ClassNameHarvesterTest {

    @Test
    public void testHarvestSimple() throws Exception {
        //prepare data
        String pattern = "java.util.List";
        
        //do the test
        NameHarvester harv = new ClassNameHarvester();
        Set<String> result = harv.harvest(pattern);
        
        //checks
        assertEquals(1, result.size());
        String r = result.iterator().next();
        assertEquals(pattern, r);
    }

    @Test
    public void testHarvestSimplePattern() throws Exception {
        //prepare data
        String pattern = "org.**.*ClassNameHarvester";
        
        //do the test
        NameHarvester harv = new ClassNameHarvester();
        Set<String> result = harv.harvest(pattern);
        
        //checks
        assertEquals(1, result.size());
        String r = result.iterator().next();
        assertEquals("org.robotframework.mavenplugin.harvesters.ClassNameHarvester", r);
    }
}
