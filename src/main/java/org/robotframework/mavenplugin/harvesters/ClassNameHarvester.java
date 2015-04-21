package org.robotframework.mavenplugin.harvesters;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

/**
 * Harvests resource (not class) names from the class path given an ant-like pattern (considers '/' replaced with '.' though). 
 */
public class ClassNameHarvester implements NameHarvester {

	public List<String> harvest(String antLikePattern) {
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
    	
    	ArrayList<String> result = new ArrayList<String>();
    	if (minPatternIndex >= 0) {
			Reflections refs = new Reflections(
					new SubTypesScanner(false /* do not exclude top level classes */), 
					new AntPatternClassPredicate(antLikePattern),
					Thread.currentThread().getContextClassLoader(),
					Reflections.class.getClassLoader());
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
