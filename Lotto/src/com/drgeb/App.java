package com.drgeb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Pattern;

//http://www.lotorainbow.com.br/en/default.asp
/*
//http://www.vnutz.com/megamillions_lottery/most_frequent_numbers
// Strategies:
	1.Most Frequent Numbers
	2.Hot Wining Numbers
	3.Cold Winning Numbers
	4.Winning Pairs,
	5.Winning Triples.
	6.Winning Quadruples.
	*/

public class App {

    public static void main(String[] args) {
	File file = new File("results.txt");
	ArrayList<LottoNumber> allLottonNumbers = new ArrayList<LottoNumber>();
	ArrayList<Drawing> drawings = new ArrayList<Drawing>();
	ArrayList<DrawingResult> drawingResults = new ArrayList<DrawingResult>();

	try (FileInputStream fileInputStream = new FileInputStream(file)) {
	    Scanner sc = new Scanner(fileInputStream);
	    Pattern pattern = Pattern.compile("\t");
	    Pattern numPattern = Pattern.compile("-");
	    while (sc.hasNext()) {
		String input = sc.nextLine();
		String[] matcher = pattern.split(input);
		LocalDate date = LocalDate.parse(matcher[0], DateTimeFormatter.ofPattern("d'-'MMM'-'yy"));
		int drawNumber = Integer.valueOf(matcher[1]);
		LottoNumber joker = new LottoNumber(Integer.valueOf(matcher[3]), drawNumber, date, true);
		int[] numbers = numPattern.splitAsStream(matcher[2]).mapToInt(i -> Integer.valueOf(i)).toArray();
		ArrayList<LottoNumber> dNumbers = new ArrayList<LottoNumber>();
		for (int number : numbers) {
		    LottoNumber lottoNumber = new LottoNumber(number, drawNumber, date, false);
		    allLottonNumbers.add(lottoNumber);
		    dNumbers.add(lottoNumber);
		}
		allLottonNumbers.add(joker);
		Drawing drawing = new Drawing.Builder().date(date).drawNumber(drawNumber).numbers(dNumbers).joker(joker)
			.build();
		drawings.add(drawing);
		DrawingResult drawingResult = drawing.getDrawingResult();
		drawingResults.add(drawingResult);
	    }
	    
	    sc.close();
	    DrawingResultAnalysis drawingResultAnalysis = new DrawingResultAnalysis(drawingResults);
	    drawingResultAnalysis.analyze();
	    
	    TreeMap<Integer, LottoResult> frequencyMap = analyze(allLottonNumbers);
/*
	    frequencyMap.keySet().stream().forEach(key -> {
		LottoResult lottoResult = frequencyMap.get(key);
		Integer value = lottoResult.getValue();
		;
		ArrayList<Integer> delayArrayList = lottoResult.getDelayList();
		double averageDelay = lottoResult.getAverageDelay();
		double frequencyDelayValue = lottoResult.getFrequencyDelayValue();
		double averageFrequencyDelayValue = lottoResult.getAverageFrequencyDelayValue();
		double maxFrequencyDelayValue = lottoResult.getMaxFrequencyDelayValue();
		String listString = delayArrayList.stream().map(r -> r.toString()).collect(Collectors.joining(", "));

		System.out.println(key + "\t" + value + " \t" + averageDelay + "\t" + frequencyDelayValue + "\t"
			+ averageFrequencyDelayValue + "\t" + maxFrequencyDelayValue + "\t " + listString);

	    });
*/
	    TreeMap<Integer, LottoResult> sortedMap1 = sortByValues((TreeMap<Integer, LottoResult>) frequencyMap);
	    TreeMap<Integer, LottoResult> sortedMap2 = sortByAverageDelay((TreeMap<Integer, LottoResult>) frequencyMap);
	    TreeMap<Integer, LottoResult> sortedMap3 = sortByFrequencyDelayValue(
		    (TreeMap<Integer, LottoResult>) frequencyMap);
	    TreeMap<Integer, LottoResult> sortedMap4 = sortByAverageFrequencyDelayValue(
		    (TreeMap<Integer, LottoResult>) frequencyMap);
	    TreeMap<Integer, LottoResult> sortedMap5 = sortByMaxFrequencyDelayValue(
		    (TreeMap<Integer, LottoResult>) frequencyMap);

	    sortedMap3.keySet().stream().forEach(key -> {
		LottoResult lr = sortedMap3.get(key);
		System.out.println(key + "\t" + lr.getValue());
	    });

	} catch (FileNotFoundException fileNotFoundException) {
	    fileNotFoundException.printStackTrace();
	} catch (IOException ioException) {
	    ioException.printStackTrace();
	}
    }

    private static Comparator<? super LottoResult> ValueComparator() {
	// TODO Auto-generated method stub
	return null;
    }

    private static TreeMap<Integer, LottoResult> analyze(ArrayList<LottoNumber> lottoNumbers) {
	TreeMap<Integer, LottoResult> frequencyMap = new TreeMap<Integer, LottoResult>();

	for (LottoNumber lottoNumber : lottoNumbers) {
	    Integer key = Integer.valueOf(lottoNumber.getNumber());
	    LottoResult r = frequencyMap.get(key);
	    if (r == null) {
		r = new LottoResult();
		r.setValue(Integer.valueOf(1), lottoNumber);
		frequencyMap.put(key, r);
	    } else {
		r.setValue(r.getValue() + 1, lottoNumber);
		frequencyMap.put(key, r);
	    }
	}
	frequencyMap.keySet().stream().forEach(key -> {
	    frequencyMap.get(key).computeAll();
	});
	return frequencyMap;
    }

    public static <K, V extends Comparable<V>> TreeMap<Integer, LottoResult> sortByValues(
	    final TreeMap<Integer, LottoResult> frequencyMap) {
	Comparator<K> valueComparator = new Comparator<K>() {
	    public int compare(K k1, K k2) {
		int compare = (frequencyMap.get(k2)).getValue() - (frequencyMap.get(k1)).getValue();
		return compare;
	    }
	};
	TreeMap<Integer, LottoResult> sortedByValues = new TreeMap<Integer, LottoResult>(
		(Comparator<? super Integer>) valueComparator);
	sortedByValues.putAll(frequencyMap);
	return sortedByValues;
    }

    public static <K, V extends Comparable<V>> TreeMap<Integer, LottoResult> sortByMaxFrequencyDelayValue(
	    final TreeMap<Integer, LottoResult> frequencyMap) {
	Comparator<K> valueComparator = new Comparator<K>() {
	    public int compare(K k1, K k2) {
		LottoResult l1 = frequencyMap.get(k1);
		LottoResult l2 = frequencyMap.get(k2);
		int compare = l1.getMaxFrequencyDelayValue().compareTo(l2.getMaxFrequencyDelayValue());
		return compare;
	    }
	};
	TreeMap<Integer, LottoResult> sortedByValues = new TreeMap<Integer, LottoResult>(
		(Comparator<? super Integer>) valueComparator);
	sortedByValues.putAll(frequencyMap);
	return sortedByValues;
    }

    public static <K, V extends Comparable<V>> TreeMap<Integer, LottoResult> sortByAverageFrequencyDelayValue(
	    final TreeMap<Integer, LottoResult> frequencyMap) {
	Comparator<K> valueComparator = new Comparator<K>() {
	    public int compare(K k1, K k2) {
		LottoResult l1 = frequencyMap.get(k1);
		LottoResult l2 = frequencyMap.get(k2);
		int compare = l1.getAverageFrequencyDelayValue().compareTo(l2.getAverageFrequencyDelayValue());
		return compare;
	    }
	};
	TreeMap<Integer, LottoResult> sortedByValues = new TreeMap<Integer, LottoResult>(
		(Comparator<? super Integer>) valueComparator);
	sortedByValues.putAll(frequencyMap);
	return sortedByValues;
    }

    public static <K, V extends Comparable<V>> TreeMap<Integer, LottoResult> sortByFrequencyDelayValue(
	    final TreeMap<Integer, LottoResult> frequencyMap) {
	Comparator<K> valueComparator = new Comparator<K>() {
	    public int compare(K k1, K k2) {
		LottoResult l1 = frequencyMap.get(k1);
		LottoResult l2 = frequencyMap.get(k2);
		int compare = l1.getFrequencyDelayValue() - l2.getFrequencyDelayValue();
		return compare;
	    }
	};
	TreeMap<Integer, LottoResult> sortedByValues = new TreeMap<Integer, LottoResult>(
		(Comparator<? super Integer>) valueComparator);
	sortedByValues.putAll(frequencyMap);
	return sortedByValues;
    }

    public static <K, V extends Comparable<V>> TreeMap<Integer, LottoResult> sortByAverageDelay(
	    final TreeMap<Integer, LottoResult> frequencyMap) {
	Comparator<K> valueComparator = new Comparator<K>() {
	    public int compare(K k1, K k2) {
		LottoResult l1 = frequencyMap.get(k1);
		LottoResult l2 = frequencyMap.get(k2);
		int compare = l1.getAverageDelay().compareTo(l2.getAverageDelay());
		return compare;
	    }
	};
	TreeMap<Integer, LottoResult> sortedByValues = new TreeMap<Integer, LottoResult>(
		(Comparator<? super Integer>) valueComparator);
	sortedByValues.putAll(frequencyMap);
	return sortedByValues;
    }

}