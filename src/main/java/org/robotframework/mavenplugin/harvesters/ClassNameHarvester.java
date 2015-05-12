package org.robotframework.mavenplugin.harvesters;

import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

/**
 * Harvests resource (not class) names from the class path given an ant-like pattern (considers '/' replaced with '.' though). 
 */
public class ClassNameHarvester implements NameHarvester {

	public Set<String> harvest(String antLikePattern) {
		int indexOfStar = antLikePattern.indexOf('*');
		int indexOfQuestionMark = antLikePattern.indexOf('?');
		int minPatternIndex;
    	if (indexOfStar >= 0) {
    		if (indexOfQuestionMark >= 0) {
    			minPatternIndex = Math.min(indexOfStar, indexOfQuestionMark);
    		} else {
    			minPatternIndex = indexOfStar;
    		}
    	} else {
    		if (indexOfQuestionMark >= 0) {
    			minPatternIndex = indexOfQuestionMark;
    		} else {
    			minPatternIndex = -1;
    		}
    	}
    	
    	LinkedHashSet<String> result = new LinkedHashSet<String>();
    	if (minPatternIndex >= 0) {
    		Set<URL> classpathURLs = ClasspathHelper.forClassLoader();
    		classpathURLs.addAll(ClasspathHelper.forJavaClassPath());
			Reflections refs = new Reflections(new ConfigurationBuilder()
					.setUrls(classpathURLs)
					.setScanners(new SubTypesScanner(false /* do not exclude top level classes */))
					.filterInputsBy(new AntPatternClassPredicate(antLikePattern)));
			Set<Class<? extends Object>> r = refs.getSubTypesOf(Object.class);
			for (Class<? extends Object> c : r) {
				result.add(c.getCanonicalName());
			}
    	} else {
    		//No pattern, add as direct resource to deal with later.
    		result.add(antLikePattern);
    	}
		return result;
	}
}

