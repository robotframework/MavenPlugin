package org.robotframework.mavenplugin.harvesters;

import java.util.List;

public interface NameHarvester {
	public List<String> harvest(String pattern);
}
