package org.robotframework.mavenplugin.harvesters;

import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

/**
 * Harvests resource (not class) names from the class path given an ant-like pattern (considers '/' replaced with '.' though). 
 */
public class ResourceNameHarvester implements NameHarvester {

    public Set<String> harvest(String antLikePattern) {
        int minPatternIndex = HarvestUtils.calculateMinimumPatternIndex(antLikePattern);
        
        LinkedHashSet<String> result = new LinkedHashSet<String>();
        if (minPatternIndex >= 0) {
            Set<URL> classpathURLs = ClasspathHelper.forClassLoader();
            classpathURLs.addAll(ClasspathHelper.forJavaClassPath());
            Reflections refs = new Reflections(new ConfigurationBuilder()
                            .setUrls(classpathURLs)
                            .setScanners(new ResourcesScanner()));
            Set<String> r = refs.getResources(new AntPatternClassPredicate(antLikePattern));
            if (!r.isEmpty()) {
                result.addAll(r);
            } else {
                //Do nothing, we have a pattern with '?' or '*' that did not yield results.
            }
        } else {
            //No pattern, add as direct resource to deal with later.
            result.add(antLikePattern);
        }
        return result;
    }
}
