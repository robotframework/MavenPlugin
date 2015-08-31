package org.robotframework.mavenplugin.harvesters;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import com.google.common.reflect.ClassPath;

/**
 * Harvests resource (not class) names from the class path given an ant-like pattern (considers '/' replaced with '.' though). 
 */
public class ResourceNameHarvester implements NameHarvester {

    public Set<String> harvest(String antLikePattern) {
        int minPatternIndex = HarvestUtils.calculateMinimumPatternIndex(antLikePattern);
        
        LinkedHashSet<String> result = new LinkedHashSet<String>();
        if (minPatternIndex >= 0) {
            
            try {
        	AntPatternClassPredicate ap = new AntPatternClassPredicate(antLikePattern);
		ClassPath cp = ClassPath.from(this.getClass().getClassLoader());
		for (ClassPath.ResourceInfo ri : cp.getResources()) {
		    String t = ri.getResourceName();
		    if (ap.apply(t)) {
			result.add(t);
		    }
		}
	    } catch (IOException e) {
		//Could not find any!
	    }
        } else {
            //No pattern, add as direct resource to deal with later.
            result.add(antLikePattern);
        }
        return result;
    }
}
