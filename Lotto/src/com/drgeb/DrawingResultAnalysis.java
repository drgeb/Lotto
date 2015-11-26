package com.drgeb;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
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

    public static <K> Map<String, Long> sortByUniquePatternCount(final Map<String, Long> uniquePatternCounts) {

	Comparator<K> valueComparator = new Comparator<K>() {
	    @Override
	    public int compare(K o1, K o2) {
		Long l1 = uniquePatternCounts.get(o1);
		Long l2 = uniquePatternCounts.get(o2);
		int compare = l1.compareTo(l2);
		System.out.println(o1+"\t"+o2+"\t"+l1+"\t"+l2);
		return compare;
	    }
	};
	Map<String, Long> sortedByValues = new TreeMap<String, Long>((Comparator<? super String>) valueComparator);
	sortedByValues.putAll(uniquePatternCounts);
	return sortedByValues;
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
		.collect(Collectors.groupingBy(e -> e.getPatternCodeList(), Collectors.counting()));
	this.uniquePatternCounts = counts;
	this.sortedUniquePatternCount= sortByUniquePatternCount(this.uniquePatternCounts);
	System.out.println("Unique Pattern Count");
	printUniquePatterCountResults();
	System.out.println("Sorted Unique Pattern Count");
	printSortedUniquePatterCountResults();
    }

    public void printUniquePatterCountResults() {
	uniquePatternCounts.keySet().stream().forEach(key -> {
	    System.out.println(key + "\t" + uniquePatternCounts.get(key));
	});
    }

    public void printSortedUniquePatterCountResults() {
	sortedUniquePatternCount.keySet().stream().forEach(key -> {
	    System.out.println(key + "\t" + sortedUniquePatternCount.get(key));
	});
    }
}
