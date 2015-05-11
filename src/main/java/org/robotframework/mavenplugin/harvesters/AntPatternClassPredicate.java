package org.robotframework.mavenplugin.harvesters;

import java.io.File;

import org.apache.maven.it.util.DirectoryScanner;

import com.google.common.base.Predicate;

/**
 * Checks matching in an ant-like pattern.
 */
public class AntPatternClassPredicate implements Predicate<String> {
	private final String pattern;
	
	public AntPatternClassPredicate(String aPattern) {
		pattern = aPattern.replace('.', File.separatorChar);
	}
	
	public boolean apply(String target) {
		String compatibleTarget;
		if (target.endsWith(".class")) {
			int classSuffixIndex = target.lastIndexOf(".class");
			compatibleTarget = target.substring(0, classSuffixIndex);
		} else {
			compatibleTarget = target;
		}
		
		compatibleTarget = compatibleTarget.replace('.', File.separatorChar);
		return DirectoryScanner.match(pattern, compatibleTarget);
	}
}

