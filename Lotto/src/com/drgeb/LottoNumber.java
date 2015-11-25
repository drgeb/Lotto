package com.drgeb;

import java.time.LocalDate;

public class LottoNumber {
    int number=0;
    LocalDate date;
    int countOccurance=0;
    int average=0;
    int stdDev=0;
    int delay;
    boolean isJoker=false;
    private int drawNumber;
    
    LottoNumber(int number, int drawNumber,LocalDate date,boolean isJoker){
	this.number=number;
	this.date=date;
	this.isJoker=isJoker;
	this.drawNumber=drawNumber;
    }
    
    /**
     * @return the drawNumber
     */
    public int getDrawNumber() {
        return this.drawNumber;
    }

    /**
     * @return the number
     */
    public int getNumber() {
        return this.number;
    }

    /**
     * @return the date
     */
    public LocalDate getDate() {
        return this.date;
    }

    /**
     * @return the countOccurance
     */
    public int getCountOccurance() {
        return this.countOccurance;
    }

    /**
     * @return the average
     */
    public int getAverage() {
        return this.average;
    }

    /**
     * @return the stdDev
     */
    public int getStdDev() {
        return this.stdDev;
    }

    /**
     * @return the delay
     */
    public int getDelay() {
        return this.delay;
    }

    /**
     * @return the isJoker
     */
    public boolean isJoker() {
        return this.isJoker;
    }

    void computeAverage() {
	
    }
    
    void computeDelay(LocalDate dateLast,LocalDate datePrev ){
	delay= dateLast.compareTo(datePrev);
    }
}
