package org.aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;

public class Day4 {

    public static void main(String[] args) throws IOException {

        List<String> lines = Files.readAllLines(Path.of("/Users/sanjana/Documents/Day4.txt"));
        int points = getPointsPartTwo(lines);
        System.out.println(points);
    }


    //Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
    private static int getPoints(List<String> lines) {

        int totalPoints = 0;
        for(String line : lines) {
            int pointsForCard = getPointsForCard(line, partOnePointUpdator);
            totalPoints += pointsForCard;
        }
        return totalPoints;
    }

    private static int getPointsPartTwo(List<String> lines) {

        Map<Integer, Integer> copyCount = new HashMap<>();
        int totalPoints = 0;
        int cardNumber = 1;


        for(String line : lines) {

            int currCardCopyCount = copyCount.getOrDefault(cardNumber, 1);
            int pointsForCard = getPointsForCard(line, partTwoPointUpdator);
            incrementNextCardCopyCount(pointsForCard, cardNumber, currCardCopyCount, copyCount);
            cardNumber++;
        }

        for(int i = 1; i < cardNumber; i++) {
            totalPoints += copyCount.getOrDefault(i, 1);
        }
        return totalPoints;
    }

    private static void incrementNextCardCopyCount(int pointsForCard, int cardNumber, int currCardCopyCount, Map<Integer, Integer> copyCount) {
        for(int i = 1; i <= pointsForCard; i++) {
            int nextCardNumber = cardNumber + i;
            int nextCardCopyCount = copyCount.getOrDefault(nextCardNumber, 1);
            copyCount.put(nextCardNumber, nextCardCopyCount + currCardCopyCount);
        }
    }

    private static int getPointsForCard(String line, Function<Integer, Integer> pointUpdator) {

        String[] cardDetails = line.split(Pattern.quote(":"));
        String[] pointsSplit = cardDetails[1].split(Pattern.quote("|"));

        String winningNumberString = pointsSplit[0].trim();
        String yourNumberString = pointsSplit[1].trim();

        String[] winningNumbers = winningNumberString.split("\\s+");
        String[] yourNumbers = yourNumberString.split("\\s+");

        Set<String> winningNumSet = new HashSet<>();
        for(String eachWinningNum : winningNumbers) {
            winningNumSet.add(eachWinningNum);
        }

        int points = 0;
        for(String yourNum: yourNumbers) {
            if(winningNumSet.contains(yourNum)) {
                points = pointUpdator.apply(points);
            }
        }
        return points;
    }

    private static Function<Integer, Integer> partOnePointUpdator = points -> points == 0 ? 1 : points * 2;
    private static Function<Integer, Integer> partTwoPointUpdator = points -> points + 1;

}
