package com.drgeb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class DrawingResult {

    // Pattern Codes
    ArrayList<Integer> patternCodes;
    String patternCodeList;
    String sortedPatternCodeList;

    DrawingResult(ArrayList<LottoNumber> numbers) {
	patternCodes = new ArrayList<Integer>();
	setPatternCode(numbers);
	setPatternCodeList();
	setSortedPatternCodeList();
    }

    private void setPatternCode(ArrayList<LottoNumber> numbers) {
	numbers.stream().forEach(number -> {
	    int ipatternCode = number.getNumber() / 10;
	    patternCodes.add(ipatternCode);
	});
    }

    private void setPatternCodeList() {
	StringBuffer code = new StringBuffer();
	LinkedHashMap <Integer,Integer> pattern=new LinkedHashMap<Integer, Integer>();
	patternCodes.stream().forEach(patternCode -> {
	    String s = patternCode.toString();
	    code.append(s);
	    /* Determine patterns in the sorted Unique Templates */
	    /* First analysis just make the initail counts */
	    Integer patternCodeCount=pattern.get(patternCode);
	    if (patternCodeCount==null)
		patternCodeCount=1;
	    else
		patternCodeCount=patternCodeCount+1;
	    pattern.put(patternCode,patternCodeCount);
	    System.out.print(patternCode);
	    
	});
	System.out.println(pattern);	
	patternCodeList = code.toString();
	/* Next anaysis count how many Singles are, Doubles are and triplets for each Drawing */
	Consumer<? super Integer> action=null;
	pattern.keySet().stream().forEachOrdered(action);
	/* Finally for all Drawings summarize the number of Triplets number of Doubles and number of Singles and for each one compute the percentage based on total number of drawings */
	
    }

    /**
     * @return the patternCodes
     */
    public ArrayList<Integer> getPatternCodes() {
	return this.patternCodes;
    }

    /**
     * @param patternCodes
     *            the patternCodes to set
     */
    public void setPatternCodes(ArrayList<Integer> patternCodes) {
	this.patternCodes = patternCodes;
    }

    /**
     * @return the patternCodeList
     */
    public String getPatternCodeList() {
	return this.patternCodeList;
    }

    /**
     * @param patternCodeList
     *            the patternCodeList to set
     */
    public void setPatternCodeList(String patternCodeList) {
	this.patternCodeList = patternCodeList;
    }

    /**
     * @return the sortedPatternCodeList
     */
    public String getSortedPatternCodeList() {
	return this.sortedPatternCodeList;
    }

    /**
     * @param sortedPatternCodeList
     *            the sortedPatternCodeList to set
     */
    public void setSortedPatternCodeList(String sortedPatternCodeList) {
	this.sortedPatternCodeList = sortedPatternCodeList;
    }

    private void setSortedPatternCodeList() {
	StringBuffer code = new StringBuffer();
	patternCodes.stream().sorted().forEach(patternCode -> {
	    code.append(patternCode.toString());
	});
	sortedPatternCodeList = code.toString();
    }
}
