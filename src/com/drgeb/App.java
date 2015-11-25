package com.drgeb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedMap;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.*;

public class App {

    public static void main(String[] args) {
	File file = new File("results.txt");
	ArrayList<LottoResult> results = new ArrayList<LottoResult>();
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
		LottoResult r = new LottoResult.Builder().date(date).drawNumber(drawNumber).numbers(numbers)
			.joker(joker).build();
		results.add(r);
		for (int number : numbers) {
		    LottoNumber lottoNumber = new LottoNumber(number, date, false);
		    lottoNumbers.add(lottoNumber);
		}
		LottoNumber lottoJokerNumber = new LottoNumber(joker, date, true);
		lottoNumbers.add(lottoJokerNumber);
	    }
	    sc.close();
	    mostCommon(results);
	    mostCommonNumbers(lottoNumbers);
	    findPatterns(results);
	    System.out.println(results.size());
	} catch (FileNotFoundException fileNotFoundException) {
	    fileNotFoundException.printStackTrace();
	} catch (IOException ioException) {
	    ioException.printStackTrace();
	}
    }

    private static void mostCommonNumbers(ArrayList<LottoNumber> lottoNumbers) {
	HashMap<Integer, Integer> frequencyMap = new HashMap<Integer, Integer>();
	Comparator<LottoNumber> byNumbers = Comparator.comparing(LottoNumber::getNumber);
	Consumer<? super Integer> action = key -> {
	    Integer value = frequencyMap.get(key);
	    if (value == null) {
		value = Integer.valueOf(1);
		frequencyMap.put(key, value);
	    } else {
		value = value + 1;
		frequencyMap.replace(key, value);
	    }
	};
	lottoNumbers.stream().sorted(byNumbers).map(p -> p.getNumber()).forEach(action);
	
	Comparator<Integer> byValues = Comparator.comparing(frequencyMap.get(key));
	frequencyMap.frequencyMap.keySet().stream().soretd(byValues );
	frequencyMap.frequencyMap.keySet().stream().forEach(key -> System.out.println(key + " " + frequencyMap.get(key)));
    }

    private static void mostCommon(ArrayList<LottoResult> results) {
	HashMap<Integer, Integer> mostCommon = new HashMap<Integer, Integer>();

	// results.parallelStream().iterator().forEachRemaining(action->Arrays.stream(action.getNumbers());

	for (LottoResult result : results) {
	    int[] numbers = result.getNumbers();
	    for (int number : numbers) {
		Integer iNumber = Integer.valueOf(number);
		Integer x = mostCommon.get(iNumber);
		if (x == null) {
		    x = Integer.valueOf(0);
		}
		Integer value = x + 1;
		mostCommon.put(iNumber, value);
	    }
	}

	Set<Integer> set = mostCommon.keySet();
	Integer total1 = 0;
	for (Object key : set.toArray()) {
	    Integer value = mostCommon.get(key);
	    total1 = total1 + value;
	}

	// Using Streams
	Stream<Integer> mstream = mostCommon.values().parallelStream();
	BinaryOperator accumulator = (a, b) -> {
	    Integer x = ((Integer) a).intValue() + ((Integer) b).intValue();
	    return x;
	};
	Integer total2 = mstream.reduce(0, accumulator);
	// TODO: Sort the results of mostCommon from high to low in terms of
	// values!
	// TODO: Compute percentage for each value.
	// TODO: Compute delay between last two values
	// TODO: Compute average delay between all values.
	// TODO: Sort based on the result of frequency times delay the higher
	// the more probable

	// TODO: Display results for top 5
	// TODO: Based on highest frequency.
	// TODO: Based on highest delay.
	// TODO: Based on highest frequency times delay.
    }

    private static void findPatterns(ArrayList<LottoResult> results) {
	// TODO Auto-generated method stub

    }
}
