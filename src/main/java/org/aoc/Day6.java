package org.aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day6 {

    public static void main(String[] args) throws IOException {

        List<String> lines = Files.readAllLines(Path.of("/Users/sanjana/Documents/AoC/Day6.txt"));
        long totalWays = findTotalNumWaysToBeatPartTwo(lines);
        System.out.println(totalWays);
    }

    private static long findTotalNumWaysToBeatPartTwo(List<String> lines) {

        String timeLine = lines.get(0).replaceFirst("Time:", "").trim();
        String distanceLine = lines.get(1).replaceFirst("Distance:", "").trim();

        timeLine = timeLine.replaceAll("\\s+", "");
        distanceLine = distanceLine.replaceAll("\\s+", "");

        long numWays = getNumwaysForRace(Long.valueOf(timeLine), Long.valueOf(distanceLine));
        return numWays;
    }

    private static long findTotalNumWaysToBeat(List<String> lines) {

        String timeLine = lines.get(0).replaceFirst("Time:", "").trim();
        String distanceLine = lines.get(1).replaceFirst("Distance:", "").trim();

        String[] timeStrings = timeLine.split("\\s+");
        String[] distanceStrings = distanceLine.split("\\s+");

        long totalNumWays = 0;
        for (int i = 0; i < timeStrings.length; i++) {
            long numWays = getNumwaysForRace(Integer.valueOf(timeStrings[i]), Integer.valueOf(distanceStrings[i]));
            totalNumWays = totalNumWays == 0 ? numWays : totalNumWays * numWays;
        }
        return totalNumWays;
    }

    // 1, 2, 3, 4, 5
    private static long getNumwaysForRace(long time, long distance) {
        boolean oddTime = time % 2 != 0;

        long numWays = 0;
        long midPoint = time / 2;
        for (long i = midPoint; i >= 0; i--) {
            long possibleDistance = (time - i) * i;
            if (possibleDistance <= distance)
                break;

            if (i == midPoint) {
                numWays += oddTime ? 2 : 1;
            } else {
                numWays += 2;
            }
        }
        return numWays;
    }
}
