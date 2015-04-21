package org.robotframework.mavenplugin.harvesters;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Test;

public class ClassNameHarvesterTest {

    public static class A {

    }

    public static class B extends A {

    }

    @Test
    public void testHarvestSimple() throws Exception {
        // prepare data
        String pattern = "java.util.List";

        // do the test
        NameHarvester harv = new ClassNameHarvester();
        Set<String> result = harv.harvest(pattern);

        // checks
        assertEquals(1, result.size());
        String r = result.iterator().next();
        assertEquals(pattern, r);
    }

    @Test
    public void testHarvestSimplePattern() throws Exception {
        // prepare data
        String pattern = "org.**.*ClassNameHarvester";

        // do the test
        NameHarvester harv = new ClassNameHarvester();
        Set<String> result = harv.harvest(pattern);

        // checks
        assertEquals(1, result.size());
        String r = result.iterator().next();
        assertEquals("org.robotframework.mavenplugin.harvesters.ClassNameHarvester", r);
    }

    @Test
    public void testHarvestSimplePattern1() throws Exception {
        // prepare data
        String pattern = "org.**.B";

        // do the test
        NameHarvester harv = new ClassNameHarvester();
        Set<String> result = harv.harvest(pattern);

        // checks
        assertEquals(1, result.size());
        String r = result.iterator().next();
        assertEquals("org.robotframework.mavenplugin.harvesters.B", r);
    }

    @Test
    public void testHarvestSimplePattern2() throws Exception {
        // prepare data
        String pattern = "org.**.A";

        // do the test
        NameHarvester harv = new ClassNameHarvester();
        Set<String> result = harv.harvest(pattern);

        // checks
        assertEquals(1, result.size());
        String r = result.iterator().next();
        assertEquals("org.robotframework.mavenplugin.harvesters.A", r);
    }

    @Test
    public void testHarvestSimplePatternWithInners() throws Exception {
        // prepare data
        String pattern = "org.**.ClassNameHarvesterTest$A";

        // do the test
        NameHarvester harv = new ClassNameHarvester();
        Set<String> result = harv.harvest(pattern);

        // checks
        assertEquals(1, result.size());
        String r = result.iterator().next();
        assertEquals("org.robotframework.mavenplugin.harvesters.ClassNameHarvesterTest$A", r);
    }
}
