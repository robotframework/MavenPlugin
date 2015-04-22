package org.robotframework.mavenplugin.harvesters;

import static org.junit.Assert.*;

import org.junit.Test;

public class HarvestUtilsTest {

	@Test
	public void testExtractName() throws Exception {
		
		assertEquals("Blabla", HarvestUtils.extractName("com.xxx.Blabla"));
		assertEquals("Blabla", HarvestUtils.extractName("Blabla.java"));
		assertEquals("Blabla", HarvestUtils.extractName("c:\\xxx\\Blabla.java"));
		assertEquals("Blabla", HarvestUtils.extractName("/xxx/Blabla.java"));
	}

	@Test
	public void testGenerateIdName() throws Exception {
		assertEquals("b_c_d_e", HarvestUtils.generateIdName("b/c\\d.e"));
	}

	@Test
	public void testExtractExtension() throws Exception {
		assertEquals(".java", HarvestUtils.extractExtension("Blabla.java"));
		assertEquals("", HarvestUtils.extractExtension("Blabla"));
		assertEquals(".java", HarvestUtils.extractExtension(".java"));
	}

}
