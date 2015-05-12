package org.robotframework.mavenplugin.harvesters;

import java.util.Set;

public interface NameHarvester {
	public Set<String> harvest(String pattern);
}
