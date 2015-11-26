package com.drgeb;

import java.time.LocalDate;
import java.util.ArrayList;

public class Drawing {
    LocalDate date;
    private int drawNumber;
    ArrayList<LottoNumber> numbers;
    LottoNumber joker;
    private DrawingResult drawingResult;
    
    /**
     * @return the drawingResult
     */
    public DrawingResult getDrawingResult() {
        return this.drawingResult;
    }

    /**
     * @return the date
     */
    public LocalDate getDate() {
	return this.date;
    }

    /**
     * @return the drawNumber
     */
    public int getDrawNumber() {
	return this.drawNumber;
    }

    /**
     * @return the numbers
     */
    public ArrayList<LottoNumber> getNumbers() {
	return this.numbers;
    }

    /**
     * @return the joker
     */
    public LottoNumber getJoker() {
	return this.joker;
    }

    public static class Builder {
	private LocalDate date;
	private int drawNumber;
	private ArrayList<LottoNumber> numbers;
	private LottoNumber joker;

	public Builder date(LocalDate date) {
	    this.date = date;
	    return this;
	}

	public Builder drawNumber(int drawNumber) {
	    this.drawNumber = drawNumber;
	    return this;
	}

	public Builder numbers(ArrayList<LottoNumber> numbers) {
	    this.numbers = numbers;
	    return this;
	}

	public Builder joker(LottoNumber joker) {
	    this.joker = joker;
	    return this;
	}

	public Drawing build() {
	    return new Drawing(this);
	}
    }

    private Drawing(Builder builder) {
	this.date = builder.date;
	this.drawNumber = builder.drawNumber;
	this.numbers = builder.numbers;
	this.joker = builder.joker;
	this.drawingResult=new DrawingResult(numbers);
    }
}
