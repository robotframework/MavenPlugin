package org.robotframework.mavenplugin.harvesters;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.it.util.DirectoryScanner;

/**
 * Harvests file names, supports ant-like patterns, the same understood by Maven 'includes'. 
 */
public class SourceFileNameHarvester implements NameHarvester {
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
    		
    	
    	int lastSlashBeforePatternSymbol = antLikePattern.lastIndexOf('/', minPatternIndex);
    	int lastBackslashBeforePatternSymbol = antLikePattern.lastIndexOf('\\', minPatternIndex);
    	
    	int maxSlashIndex = Math.max(lastSlashBeforePatternSymbol, lastBackslashBeforePatternSymbol);
    	
    	//Parse out the baseDirectory and pattern parts.
    	String baseDirectory;
    	String patternString;
    	if (maxSlashIndex > 0) {
    		baseDirectory = antLikePattern.substring(0, maxSlashIndex + 1);
    		if (maxSlashIndex + 1 >= antLikePattern.length()) {
    			patternString = "";
    		} else {
    			patternString = antLikePattern.substring(maxSlashIndex + 1);
    		}
    	} else {
    		baseDirectory = "";
    		patternString = antLikePattern;
    	}
    		
   		//pattern that we need to expand.
   		DirectoryScanner scanner = new DirectoryScanner();
   		scanner.setBasedir(baseDirectory);
   		scanner.setCaseSensitive(true);
   		scanner.setIncludes(new String[] { patternString });
   		
   		scanner.scan();
   		
   		String[] includedFiles = scanner.getIncludedFiles();
   		ArrayList<String> result = new ArrayList<String>();
   		File bDir = scanner.getBasedir();
   		for (String iF : includedFiles) {
   			File tmp = new File(bDir, iF);
   			result.add(tmp.getAbsolutePath());
   		}
		return result;
	}
}
