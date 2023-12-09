package org.aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day9 {

    public static void main(String[] args) throws IOException {

        List<String> lines = Files.readAllLines(Path.of("/Users/sanjana/Documents/AoC/Day9.txt"));
        long nextValueSum = getNextValueSum(lines);
        System.out.println(nextValueSum);
    }

    private static long getNextValueSum(List<String> lines) {

        long sum = 0;
        for(String line: lines) {
            List<Long> numbers = getNumbers(line);
            sum += getNextValuePartTwo(numbers);
        }
        return sum;
    }


    private static long getNextValuePartTwo(List<Long> numbers) {

        long nextValue = 0;
        if(numbers.stream().allMatch(num -> num == 0))
            return nextValue;

        List<Long> nextNums = new ArrayList<>();
        for(int i = 0; i < numbers.size() - 1; i++) {
            nextNums.add(numbers.get(i + 1) - numbers.get(i));
        }
        long smallNextValue = getNextValuePartTwo(nextNums);
        return numbers.get(0) - smallNextValue;
    }

    private static long getNextValue(List<Long> numbers) {

        long nextValue = 0;
        if(numbers.stream().allMatch(num -> num == 0))
            return nextValue;

        List<Long> nextNums = new ArrayList<>();
        for(int i = 0; i < numbers.size() - 1; i++) {
            nextNums.add(numbers.get(i + 1) - numbers.get(i));
        }
        long smallNextValue = getNextValue(nextNums);
        return numbers.get(numbers.size() - 1) + smallNextValue;
    }

    private static List<Long> getNumbers(String line) {

        List<Long> nums = new ArrayList<>();
        String[] numStrings = line.trim().split("\\s+");
        for(String eachNum: numStrings) {
            nums.add(Long.valueOf(eachNum.trim()));
        }
        return nums;
    }
}
