package org.aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class Day4 {

    public static void main(String[] args) throws IOException {

        List<String> lines = Files.readAllLines(Path.of("/Users/sanjana/Documents/Day4.txt"));
        int points = getPoints(lines);
        System.out.println(points);
    }


    //Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
    private static int getPoints(List<String> lines) {

        int totalPoints = 0;
        for(String line : lines) {
            int pointsForCard = getPointsForCard(line);
            totalPoints += pointsForCard;
        }
        return totalPoints;
    }

    private static int getPointsForCard(String line) {

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
                points = points == 0 ? 1 : points * 2;
            }
        }
        return points;
    }


}
