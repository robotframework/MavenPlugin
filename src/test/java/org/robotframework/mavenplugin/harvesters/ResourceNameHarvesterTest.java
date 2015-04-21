package org.robotframework.mavenplugin.harvesters;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.robotframework.mavenplugin.harvesters.ResourceNameHarvester;

public class ResourceNameHarvesterTest {

	@Test
	public void testHarvestResourcesSimpleNonMatching() throws Exception {
		//prepare data
		String pattern = "xxx.py";
		
		//do the test
		NameHarvester harv = new ResourceNameHarvester();
		List<String> result = harv.harvest(pattern);
		
		//checks
		assertEquals(1, result.size());
		String r = result.iterator().next();
		assertEquals(pattern, r);
	}

	@Test
		public void testHarvestPythonResourcesLargeMatch() throws Exception {
			//prepare data
			String pattern = "*.py";
			
			//do the test
			NameHarvester harv = new ResourceNameHarvester();
			List<String> result = harv.harvest(pattern);
			
			//checks
			assertTrue(result.size() > 1);
		}
}
