package org.aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class Day3 {


    public static void main(String[] args) throws IOException {

        List<String> lines = Files.readAllLines(Path.of("/Users/sanjana/Documents/Day3.txt"));
        //int sum = getPartNumberSum(lines);
        int sum = getGearRatioSum(lines);
        System.out.println(sum);
    }

    private static int getGearRatioSum(List<String> lines) {

        int sum = 0;

        Map<String, List<Integer>> gearPartNumbers = new HashMap<>();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String prevLine = i > 0 ? lines.get(i - 1) : null;
            String nextLine = i < lines.size() - 1 ? lines.get(i + 1) : null;

            getGearRationSum(i, line, prevLine, nextLine, gearPartNumbers);

        }
        for(String gearIndex : gearPartNumbers.keySet()) {
            List<Integer> partNumbers = gearPartNumbers.get(gearIndex);
            if(partNumbers != null && partNumbers.size() == 2) {
                sum += partNumbers.get(0) * partNumbers.get(1);
            }
        }
        return sum;
    }

    private static int getPartNumberSum(List<String> lines) {

        int sum = 0;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String prevLine = i > 0 ? lines.get(i - 1) : null;
            String nextLine = i < lines.size() - 1 ? lines.get(i + 1) : null;

            sum += getPartNumberSumForLine(line, prevLine, nextLine);
        }
        return sum;
    }

    private static void getGearRationSum(int lineIndex, String line, String prevLine, String nextLine, Map<String, List<Integer>> gearPartNumbers) {

        int currentNumber = 0;
        String starSymbolIndex = null;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (Character.isDigit(c)) {
                currentNumber = currentNumber * 10 + Character.getNumericValue(c);
                if(currentNumber == 755 || currentNumber == 598)
                    System.out.println();
                starSymbolIndex = starSymbolIndex == null ? getStarSymbolIndex(lineIndex,  i, line, prevLine, nextLine) : starSymbolIndex;
            }
            else {
                if(starSymbolIndex != null) {
                    List<Integer> partNumbers = gearPartNumbers.getOrDefault(starSymbolIndex, new ArrayList<>());
                    partNumbers.add(currentNumber);
                    gearPartNumbers.put(starSymbolIndex, partNumbers);
                }
                starSymbolIndex = null;
                currentNumber = 0;
            }
        }
        if(starSymbolIndex != null) {
            List<Integer> partNumbers = gearPartNumbers.getOrDefault(starSymbolIndex, new ArrayList<>());
            partNumbers.add(currentNumber);
            gearPartNumbers.put(starSymbolIndex, partNumbers);
        }
    }

    private static String getStarSymbolIndex(int lineIndex, int i, String line, String prevLine, String nextLine) {

        int starSymbolIndex = symbolNearbyInSameLine(i, line, isStarSymbol);
        if(starSymbolIndex != -1) {
            return lineIndex + "_" + starSymbolIndex;
        }

        if(prevLine != null) {
            starSymbolIndex = symbolNearbyInOtherLine(i, prevLine, isStarSymbol);
            if(starSymbolIndex != -1) {
                return (lineIndex - 1) + "_" + starSymbolIndex;
            }
        }

        if(nextLine != null) {
            starSymbolIndex = symbolNearbyInOtherLine(i, nextLine, isStarSymbol);
            if(starSymbolIndex != -1) {
                return (lineIndex + 1) + "_" + starSymbolIndex;
            }
        }
        return null;
    }

    private static int getPartNumberSumForLine(String line, String prevLine, String nextLine) {

        int sum = 0;

        int currentNumber = 0;
        int nearbySymbolIndex = -1;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (Character.isDigit(c)) {
                currentNumber = currentNumber * 10 + Character.getNumericValue(c);

                if (nearbySymbolIndex == -1)
                    nearbySymbolIndex = isSymbolNearby(i, line, prevLine, nextLine, isAnySymbol);
            }
            else
            {

                if(nearbySymbolIndex != -1)
                    sum += currentNumber;

                nearbySymbolIndex = -1;
                currentNumber = 0;
            }
        }

        if(nearbySymbolIndex != -1)
            sum += currentNumber;

        return sum;
    }



    private static int isSymbolNearby(int i, String currLine, String prevLine, String nextLine, Predicate<Character> symbolPredicate) {

        int symbolIndex = symbolNearbyInSameLine(i, currLine, symbolPredicate);

        if (symbolIndex == -1 && prevLine != null) {
            symbolIndex = symbolNearbyInOtherLine(i, prevLine, symbolPredicate);
        }

        if (symbolIndex == -1 && nextLine != null) {
            symbolIndex = symbolNearbyInOtherLine(i, nextLine, symbolPredicate);
        }
        return symbolIndex;
    }

    private static int symbolNearbyInSameLine(int i, String currLine, Predicate<Character> symbolPredicate) {

        int symbolIndex = -1;
        if (i > 0) {
            symbolIndex = symbolPredicate.test(currLine.charAt(i - 1)) ? i - 1 : symbolIndex;
        }

        if (symbolIndex == -1 && i < currLine.length() - 1) {
            symbolIndex = symbolPredicate.test(currLine.charAt(i + 1)) ? i + 1 : symbolIndex;
        }
        return symbolIndex;
    }

    private static int symbolNearbyInOtherLine(int i, String otherLine, Predicate<Character> symbolPredicate) {

        int symbolIndex = -1;
        if (i > 0) {
            symbolIndex = symbolPredicate.test(otherLine.charAt(i - 1)) ? i - 1 : symbolIndex;
        }

        if (symbolIndex == -1) {
            symbolIndex = symbolPredicate.test(otherLine.charAt(i)) ? i : symbolIndex;
        }

        if (symbolIndex == -1 && i < otherLine.length() - 1) {
            symbolIndex = symbolPredicate.test(otherLine.charAt(i + 1)) ? i + 1 : symbolIndex;
        }
        return symbolIndex;
    }

    private static Predicate<Character> isAnySymbol = c -> !Character.isDigit(c) && c != '.';
    private static Predicate<Character> isStarSymbol = c -> c =='*';


}
