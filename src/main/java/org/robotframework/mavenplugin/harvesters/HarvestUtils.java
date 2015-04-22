package org.robotframework.mavenplugin.harvesters;

/**
 * Some utilities for processing harvested names. 
 */
public class HarvestUtils {
	
	public static final String JAVA_FILE_EXT = ".java";
	
	/**
	 * Extracts the name component.
	 * 
	 * @param harvestedName
	 * @return
	 */
	public static String extractName(String harvestedName) {
		String result;
		
    	int indexOfSlash = harvestedName.lastIndexOf('/');
    	int indexOfBackSlash = harvestedName.lastIndexOf('\\');
    	if (indexOfSlash >=0 || indexOfBackSlash >= 0) {
    		//we deal with a file path
    		int index = Math.max(indexOfSlash, indexOfBackSlash);
    		if (index + 1 == harvestedName.length()) {
    			result = "";
    		} else {
    			if (harvestedName.endsWith(JAVA_FILE_EXT)) {
    				//Get name minus path and extension.
    				result = harvestedName.substring(index + 1, harvestedName.length() - JAVA_FILE_EXT.length());
    			} else {
    				//Get name minus path.
    				result = harvestedName.substring(index + 1);
    			}
    		}
    	} else {
    		if (harvestedName.endsWith(JAVA_FILE_EXT)) {
    			//The name of the file minus the extension.
    			result = harvestedName.substring(0, harvestedName.length() - JAVA_FILE_EXT.length());
    		} else {
    			//Dealing with a class, so use the name.
    			int indexOfDot = harvestedName.lastIndexOf('.');
    			if (indexOfDot + 1 == harvestedName.length()) {
    				result = "";
    			} else {
    				result = harvestedName.substring(indexOfDot + 1);
    			}
    		}
    	}
		return result;
	}
	
	/**
	 * Prepares an id name from a full path or fully qualified file, by replacing various chars with '_'.
	 *  
	 * @param harvestedName
	 * @return
	 */
	public static String generateIdName(String harvestedName) {
		return harvestedName.replaceAll("/|\\.|\\\\", "_");
	}
	
	/**
	 * Extracts from the filename what could serve as extension.
	 * 
	 * @param name
	 * @return
	 */
	public static String extractExtension(String filename) {
		String result;
		int indexOfDot = filename.lastIndexOf('.');
		if (indexOfDot >=0 ) {
			result = filename.substring(indexOfDot);
		} else {
			result = "";
		}
		return result;
	}
}
