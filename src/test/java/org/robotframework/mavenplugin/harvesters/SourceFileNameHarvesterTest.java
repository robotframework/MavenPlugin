package org.robotframework.mavenplugin.harvesters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.maven.it.util.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SourceFileNameHarvesterTest {
	
	public volatile File workingDirectory = new File(new File(System.getProperty("user.dir")), SourceFileNameHarvesterTest.class.getCanonicalName());
	
	@Before
	public void setup() throws IOException {
		if (workingDirectory.isDirectory()) {
			FileUtils.deleteDirectory(workingDirectory);
		} else {
			if (workingDirectory.isFile()) {
				assertTrue(workingDirectory.delete());
			} else {
				assertTrue(workingDirectory.mkdir());
			}
		}
	}

	@After
	public void teardown() throws IOException {
		if (workingDirectory.isDirectory()) {
			FileUtils.deleteDirectory(workingDirectory);
		} else {
			if (workingDirectory.isFile()) {
				workingDirectory.delete();
			}
		}
	}
	
	@Test
	public void testHarvestFilesSimple() throws Exception {
		//create some files
		File testFile1 = new File(workingDirectory, "bla.java");
		assertTrue(testFile1.createNewFile());
		String pattern = workingDirectory.getAbsolutePath() + File.separator + "*.java";
		
		//do the test
		NameHarvester h = new SourceFileNameHarvester(workingDirectory);
		Set<String> result = h.harvest(pattern);
		
		//Checks
		assertEquals(1, result.size());
		String t = result.iterator().next();
		assertEquals(testFile1.getAbsolutePath(), t);
	}

	@Test
	public void testHarvestFilesSeveralButStillSimplerPattern() throws Exception {
		//create some files
		File testFile1 = new File(workingDirectory, "bla.java");
		assertTrue(testFile1.createNewFile());
		File testFile2 = new File(workingDirectory, "deeper" + File.separator + "bla.java");
		assertTrue(testFile2.getParentFile().mkdirs());
		assertTrue(testFile2.createNewFile());
		
		String pattern = workingDirectory.getAbsolutePath() + File.separator + "*.java";
		
		//do the test
		NameHarvester h = new SourceFileNameHarvester(workingDirectory);
		Set<String> result = h.harvest(pattern);
		
		//Checks
		assertEquals(1, result.size());
		String t = result.iterator().next();
		assertEquals(testFile1.getAbsolutePath(), t);
	}

	@Test
	public void testHarvestFilesSeveralRecursivePattern() throws Exception {
		//create some files
		File testFile1 = new File(workingDirectory, "bla.java");
		assertTrue(testFile1.createNewFile());
		File testFile2 = new File(workingDirectory, "deeper" + File.separator + "deeper_still" + File.separator + "bla.java");
		assertTrue(testFile2.getParentFile().mkdirs());
		assertTrue(testFile2.createNewFile());
		
		String pattern = workingDirectory.getAbsolutePath() + File.separator + "**" + File.separator + "*.java";
		
		//do the test
		NameHarvester h = new SourceFileNameHarvester(workingDirectory);
		Set<String> result = h.harvest(pattern);
		
		//Checks
		assertEquals(2, result.size());
		
		HashSet<String> tr = new HashSet<String>(result);
		assertTrue(tr.contains(testFile1.getAbsolutePath()));
		assertTrue(tr.contains(testFile2.getAbsolutePath()));
		
	}

	@Test
	public void testHarvestFilesSeveralRecursivePatternSlash() throws Exception {
		//create some files
		File testFile1 = new File(workingDirectory, "bla.java");
		assertTrue(testFile1.createNewFile());
		File testFile2 = new File(workingDirectory, "deeper" + File.separator + "deeper_still" + File.separator + "bla.java");
		assertTrue(testFile2.getParentFile().mkdirs());
		assertTrue(testFile2.createNewFile());
		
		String pattern = workingDirectory.getAbsolutePath() + File.separator + "**" + File.separator + "*.java";
		
		//do the test
		NameHarvester h = new SourceFileNameHarvester(workingDirectory);
		Set<String> result = h.harvest(pattern);
		
		//Checks
		assertEquals(2, result.size());
		
		HashSet<String> tr = new HashSet<String>(result);
		assertTrue(tr.contains(testFile1.getAbsolutePath()));
		assertTrue(tr.contains(testFile2.getAbsolutePath()));
		
	}

	@Test
	public void testHarvestFilesSeveralRecursivePatternSlashRelativePattern() throws Exception {

		//create some files
		File testFile1 = new File(workingDirectory, "bla.java");
		assertTrue(testFile1.createNewFile());
		File testFile2 = new File(workingDirectory,  "deeper" + File.separator + "deeper_still" + File.separator + "bla.java");
		assertTrue(testFile2.getParentFile().mkdirs());
		assertTrue(testFile2.createNewFile());
		
		String pattern = "deeper" + File.separator + "**" + File.separator + "*.java";
		
		//do the test
		NameHarvester h = new SourceFileNameHarvester(workingDirectory);
		Set<String> result = h.harvest(pattern);
		
		//Checks
		assertEquals(1, result.size());
		
		HashSet<String> tr = new HashSet<String>(result);
		assertTrue(tr.contains(testFile2.getAbsolutePath()));
	}
}

