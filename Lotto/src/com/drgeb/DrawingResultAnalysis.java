package com.drgeb;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class DrawingResultAnalysis {
    private ArrayList<DrawingResult> drawingResults;
    private Map<String, Long> uniquePatternCounts;

    private Map<String, Long> repeatingPatternCounts;
    private Map<String, Long> sortedUniquePatternCount;

    /**
     * @return the drawingResults
     */
    public ArrayList<DrawingResult> getDrawingResults() {
	return this.drawingResults;
    }

    /**
     * @return the uniquePatternCounts
     */
    public Map<String, Long> getUniquePatternCounts() {
	return this.uniquePatternCounts;
    }

    /**
     * @return the repeatingPatternCounts
     */
    public Map<String, Long> getRepeatingPatternCounts() {
	return this.repeatingPatternCounts;
    }

    // TODO Compute Winning Pairs Winning Triplets Winning Quadruplets and their
    // corresponding delays

    public DrawingResultAnalysis(ArrayList<DrawingResult> drawingResults) {
	this.drawingResults = drawingResults;
    }

    public void analyze() {
	Map<String, Long> counts = drawingResults.stream()
		.collect(Collectors.groupingBy(DrawingResult::getPatternCodeList, Collectors.counting()));
	this.uniquePatternCounts = new TreeMap<String, Long>(counts);
	
        sortedUniquePatternCount=(LinkedHashMap) 	uniquePatternCounts.entrySet().stream()
    	    .sorted(Map.Entry.comparingByValue((v1,v2)->v2.compareTo(v1)))
    	    .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (v1,v2)->v1, LinkedHashMap::new));
        
    }

    public void printUniquePatterCountResults() {
	uniquePatternCounts.keySet().stream().forEach(key -> {
	    System.out.println(key + "\t" + uniquePatternCounts.get(key));
	});
    }

    public void printSortedUniquePatterCountResults() {
	if (sortedUniquePatternCount != null) {
	    sortedUniquePatternCount.keySet().stream().forEach(key -> {
		System.out.println(key + "\t" + sortedUniquePatternCount.get(key));
	    });
	}
    }
}
