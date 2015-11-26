package com.drgeb;

import java.util.ArrayList;

public class DrawingResult {

    // Pattern Codes
    ArrayList<Integer> patternCodes;
    String patternCodeList;
    String sortedPatternCodeList;
    DrawingResult(ArrayList<LottoNumber> numbers){
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
	patternCodes.stream().forEach(patternCode -> {
	    String s =patternCode.toString();
	    code.append(s);
	});
	patternCodeList = code.toString();
    }

    /**
     * @return the patternCodes
     */
    public ArrayList<Integer> getPatternCodes() {
        return this.patternCodes;
    }
    /**
     * @param patternCodes the patternCodes to set
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
     * @param patternCodeList the patternCodeList to set
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
     * @param sortedPatternCodeList the sortedPatternCodeList to set
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
