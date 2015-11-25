package com.drgeb;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.OptionalDouble;

public class LottoResult {
    // Main Key and Value Pairs
    int value;
    ArrayList<LottoNumber> lottoNumbers;

    // Computed Values
    ArrayList<Integer> delayList = null;
    Double averageDelay = 0D;
    int frequencyDelayValue = 0;
    Double averageFrequencyDelayValue = 0D;
    Double maxFrequencyDelayValue = 0D;

    
    /**
     * @return the value
     */
    LottoResult() {
	lottoNumbers = new ArrayList<LottoNumber>();
    }

    public int getValue() {
	return this.value;
    }

    public void computeAll() {
	computeDelayList();
	this.averageDelay = computeAverageDelay();
	this.frequencyDelayValue = computeFrequencyDelayValue();
	this.averageFrequencyDelayValue = computeAverageFrequencyDelayValue();
	this.maxFrequencyDelayValue = computeMaxFrequencyDelayValue();
    }

    /**
     * @return the delayList
     */
    public ArrayList<Integer> getDelayList() {
        return this.delayList;
    }

    /**
     * @return the averageDelay
     */
    public Double getAverageDelay() {
        return this.averageDelay;
    }

    /**
     * @return the frequencyDelayValue
     */
    public int getFrequencyDelayValue() {
        return this.frequencyDelayValue;
    }

    /**
     * @return the averageFrequencyDelayValue
     */
    public Double getAverageFrequencyDelayValue() {
        return this.averageFrequencyDelayValue;
    }

    /**
     * @return the maxFrequencyDelayValue
     */
    public Double getMaxFrequencyDelayValue() {
        return this.maxFrequencyDelayValue;
    }

    /**
     * @return the lottoNumbers
     */
    public ArrayList<LottoNumber> getLottoNumbers() {
	return this.lottoNumbers;
    }

    public void setValue(int value, LottoNumber lottoNumber) {
	this.value = value;
	lottoNumbers.add(lottoNumber);
    }

    private ArrayList<Integer> computeDelayList() {
	ArrayList<Integer> delayList = this.delayList;
	if (delayList == null) {
	    delayList = new ArrayList<Integer>();
	    LocalDate prevDate = LocalDate.now();
	    for (LottoNumber lottoNumber : lottoNumbers) {
		LocalDate currentDate = lottoNumber.getDate();
		int delay = prevDate.compareTo(currentDate);
		prevDate = currentDate;
		delayList.add(Integer.valueOf(delay));
	    }
	    this.delayList = delayList;
	}
	return delayList;
    }

    private Double computeAverageDelay() {
	Double averageDelay = this.averageDelay;
	if (averageDelay == 0D) {
	    ArrayList<Integer> delayList = computeDelayList();
	    OptionalDouble average = delayList.stream().mapToDouble(delay -> delay).average();
	    averageDelay = average.isPresent() ? average.getAsDouble() : 0;
	    this.averageDelay=averageDelay;
	}
	return averageDelay;
    }

    private int computeFrequencyDelayValue() {
	ArrayList<Integer> delayList = computeDelayList();
	int frequencyDelayValue = value * delayList.get(0);
	return frequencyDelayValue;
    }

    private Double computeAverageFrequencyDelayValue() {
	ArrayList<Integer> delayList = computeDelayList();
	Double averageDelay = computeAverageDelay();
	Double averageFrequencyDelayValue = value * averageDelay;
	return averageFrequencyDelayValue;
    }

    private Double computeMaxFrequencyDelayValue() {
	ArrayList<Integer> delayList = computeDelayList();
	OptionalDouble max = delayList.stream().mapToDouble(delay -> delay).max();
	Double maxFrequencyDelayValue = max.isPresent() ? max.getAsDouble() : 0;
	return maxFrequencyDelayValue;
    }
}
