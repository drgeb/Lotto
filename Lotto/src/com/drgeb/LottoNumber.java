package com.drgeb;

import java.time.LocalDate;
/*
 * This class just contains the data as pure as possible from the datafile.
 * It is basically a holding bin for a number keeping track of the date, draw#, and if it was selected as a Joker or not
 */
public class LottoNumber {
    int number=0;
    LocalDate date;

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
     * @return the isJoker
     */
    public boolean isJoker() {
        return this.isJoker;
    }
}
