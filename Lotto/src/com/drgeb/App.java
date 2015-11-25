package com.drgeb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class App {

    public static void main(String[] args) {
	File file = new File("results.txt");
	ArrayList<LottoNumber> lottoNumbers = new ArrayList<LottoNumber>();

	try (FileInputStream fileInputStream = new FileInputStream(file)) {
	    Scanner sc = new Scanner(fileInputStream);
	    Pattern pattern = Pattern.compile("\t");
	    Pattern numPattern = Pattern.compile("-");
	    while (sc.hasNext()) {
		String input = sc.nextLine();
		String[] matcher = pattern.split(input);
		LocalDate date = LocalDate.parse(matcher[0], DateTimeFormatter.ofPattern("d'-'MMM'-'yy"));
		int drawNumber = Integer.valueOf(matcher[1]);
		int joker = Integer.valueOf(matcher[3]);
		int[] numbers = numPattern.splitAsStream(matcher[2]).mapToInt(i -> Integer.valueOf(i)).toArray();
		for (int number : numbers) {
		    LottoNumber lottoNumber = new LottoNumber(number, drawNumber, date, false);
		    lottoNumbers.add(lottoNumber);
		}
		LottoNumber lottoJokerNumber = new LottoNumber(joker, drawNumber, date, true);
		lottoNumbers.add(lottoJokerNumber);
	    }
	    sc.close();
	    TreeMap<Integer, LottoResult> frequencyMap = analyze(lottoNumbers);

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
		System.out.println(
			key + "\t" + value + " \t" + averageDelay + "\t" + frequencyDelayValue + "\t"
				+ averageFrequencyDelayValue + "\t" + maxFrequencyDelayValue + "\t " + listString);
	    });

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

    // public static <Integer, LottoResult extends Comparable<LottoResult>>
    // TreeMap sortByValues(final Map<java.lang.Integer, com.drgeb.LottoResult>
    // frequencyMap) {
    // Comparator<Integer> valueComparator = new Comparator<Integer>() {
    // public int compare(Integer k1, Integer k2) {
    // int compare = ((com.drgeb.LottoResult) frequencyMap.get(k2)).getValue() -
    // ((com.drgeb.LottoResult) frequencyMap.get(k1)).getValue();
    // if (compare == 0) return 1;
    // else return compare;
    // }
    // };
    // TreeMap<Integer, LottoResult> sortedByValues = new TreeMap<Integer,
    // LottoResult>(valueComparator);
    // sortedByValues.putAll((TreeMap<? extends Integer, ? extends LottoResult>)
    // frequencyMap);
    // return sortedByValues;
    // }
    // }

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
		int compare = l1.getFrequencyDelayValue()-l2.getFrequencyDelayValue();
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