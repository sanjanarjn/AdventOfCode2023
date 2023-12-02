package org.aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day1 {

    public static void main(String[] args) throws IOException {

        List<String> lines = Files.readAllLines(Path.of("/Users/sanjana/Documents/Day1.txt"));
        int sum = getCalibrationValueAdvanced(lines);
        System.out.println(sum);
    }

    private static int getCalibrationValue(List<String> input) {

        int sum = 0;
        for (String line : input) {
            sum += getSumForLine(line);
        }
        return sum;
    }

    private static int getSumForLine(String line) {

        int firstNumber = -1;
        int lastNumber = -1;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (Character.isDigit(c)) {
                if (firstNumber < 0) {
                    firstNumber = Character.getNumericValue(c);
                    lastNumber = Character.getNumericValue(c);
                } else {
                    lastNumber = Character.getNumericValue(c);
                }
            }
        }
        System.out.print("   --->   " + firstNumber + "   --->   " + lastNumber);
        int sum = (firstNumber * 10) + lastNumber;
        return sum;
    }


    private static int getCalibrationValueAdvanced(List<String> input) {

        int i = 0;
        int sum = 0;
        for (String line : input) {

            String replacedLine = replaceNumbers(line);

            System.out.print(++i+ ". "+ line + "  --->  " + replacedLine);
            int lineSum = getSumForLine(replacedLine);
            sum = sum + lineSum;
            System.out.println("   --->   " + lineSum + "   --->   " + sum);



        }
        return sum;
    }

    private static String replaceNumbers(String line) {

        line = replaceFirstNumber(line);
        line = replaceLastNumber(line);

        return line;
    }


    static Map<String, Integer> wordsToNumbers = new HashMap<>();

    static {
        wordsToNumbers.put("one", 1);
        wordsToNumbers.put("two", 2);
        wordsToNumbers.put("three", 3);
        wordsToNumbers.put("four", 4);
        wordsToNumbers.put("five", 5);
        wordsToNumbers.put("six", 6);
        wordsToNumbers.put("seven", 7);
        wordsToNumbers.put("eight", 8);
        wordsToNumbers.put("nine", 9);


    }


    private static String replaceFirstNumber(String line) {

        int firstNumberIndex = Integer.MAX_VALUE;
        String firstNumber = null;
        for (String key : wordsToNumbers.keySet()) {
            int index = line.indexOf(key);
            if (index >= 0 && index < firstNumberIndex) {
                firstNumberIndex = index;
                firstNumber = key;
            }
        }
        if (firstNumber != null) {
            String firstNumberCut = firstNumber.substring(0, firstNumber.length() - 1);
            return line.replaceFirst(firstNumberCut, String.valueOf(wordsToNumbers.get(firstNumber)));
        }
        return line;
    }

    private static String replaceLastNumber(String line) {
        String replacedLine = line;

        int lastNumberIndex = Integer.MIN_VALUE;
        String lastNumber = null;
        for (String key : wordsToNumbers.keySet()) {
            int index = line.lastIndexOf(key);
            if (index > lastNumberIndex) {
                lastNumberIndex = index;
                lastNumber = key;
            }
        }
        if (lastNumberIndex >= 0)
            replacedLine = line.substring(0, lastNumberIndex) + wordsToNumbers.get(lastNumber) + line.substring(lastNumberIndex + lastNumber.length());
        return replacedLine;
    }
}

