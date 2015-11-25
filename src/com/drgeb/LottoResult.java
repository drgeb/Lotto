package com.drgeb;

import java.time.LocalDate;

public class LottoResult {
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
    public int[] getNumbers() {
        return this.numbers;
    }

    /**
     * @return the joker
     */
    public int getJoker() {
        return this.joker;
    }

    LocalDate date;
    int drawNumber;
    int[] numbers;
    int joker;

    public static class Builder {
	private LocalDate date;
	private int drawNumber;
	private int[] numbers;
	private int joker;

	public Builder date(LocalDate date) {
	    this.date = date;
	    return this;
	}

	public Builder drawNumber(int drawNumber) {
	    this.drawNumber = drawNumber;
	    return this;
	}

	public Builder numbers(int[] numbers) {
	    this.numbers = numbers;
	    return this;
	}

	public Builder joker(int joker) {
	    this.joker = joker;
	    return this;
	}

	public LottoResult build() {
	    return new LottoResult(this);
	}
    }

    private LottoResult(Builder builder) {
	this.date = builder.date;
	this.drawNumber = builder.drawNumber;
	this.numbers = builder.numbers;
	this.joker = builder.joker;
    }
    
    
}
