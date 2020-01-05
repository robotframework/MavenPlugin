package org.robotframework.mavenplugin.harvesters;

import com.google.common.reflect.ClassPath;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Harvests resource (not class) names from the class path given an ant-like
 * pattern (considers '/' replaced with '.' though).
 */
public class ClassNameHarvester implements NameHarvester {

    public Set<String> harvest(String antLikePattern) {
        int minPatternIndex = HarvestUtils.calculateMinimumPatternIndex(antLikePattern);

        LinkedHashSet<String> result = new LinkedHashSet<String>();
        if (minPatternIndex >= 0) {
            try {
                AntPatternClassPredicate ap = new AntPatternClassPredicate(antLikePattern);
                ClassPath cp = ClassPath.from(this.getClass().getClassLoader());
                for (ClassPath.ClassInfo ci : cp.getAllClasses()) {
                    String t = ci.getName();
                    if (ap.apply(t)) 
                        result.add(t);
                }
            } catch (IOException e) {
                // Could not find any!
            }
        } else {
            // No pattern, add as direct resource to deal with later.
            result.add(antLikePattern);
        }
        return result;
    }
}
