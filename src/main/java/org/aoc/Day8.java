package org.aoc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day8 {

    public static void main(String[] args) throws IOException {

        List<String> lines = Files.readAllLines(Path.of("/Users/sanjana/Documents/AoC/Day8.txt"));
        int steps = getNumStepsPartTwo(lines);
        System.out.println(steps);
    }

    private static int getNumStepsPartTwo(List<String> lines) {

        String instruction = "";
        String regex = "([1-9A-Z]{3}) = \\(([1-9A-Z]{3})\\, ([1-9A-Z]{3})\\)";
        Pattern pattern = Pattern.compile(regex);
        Map<String, NextNode> nodeMap = new HashMap<>();

        List<String> startNodes = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {

            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }
            if (i == 0) {
                instruction = line;
                continue;
            }
            List<String> matchedGroups = getMatchedGroups(pattern, line);
            if (matchedGroups.size() == 3) {
                String node = matchedGroups.get(0);
                if (node.endsWith("A"))
                    startNodes.add(node);
                nodeMap.put(node, new NextNode(matchedGroups.get(1), matchedGroups.get(2)));
            }
        }

        return getNumSteps(instruction, startNodes, nodeMap);
    }

    private static int getNumSteps(String instruction, List<String> startNodes, Map<String, NextNode> nodeMap) {

        List<Integer> steps = new ArrayList<>();
        for (String eachStartNode : startNodes) {
            int numSteps = getNumSteps(instruction, eachStartNode, nodeMap);
            steps.add(numSteps);

        }
        System.out.println(calculateLCM(steps));
        System.out.println(steps);
        return 0;
    }

    private static BigInteger calculateLCM(List<Integer> numbers) {
        BigInteger lcm = BigInteger.ONE;

        for (int number : numbers) {
            lcm = calculateLCM(lcm, BigInteger.valueOf(number));
        }

        return lcm;
    }

    private static BigInteger calculateLCM(BigInteger a, BigInteger b) {
        BigInteger product = a.multiply(b);
        BigInteger gcd = a.gcd(b);

        return product.divide(gcd);
    }


    private static int getNumSteps(List<String> lines) {

        String instruction = "";
        String regex = "([1-9A-Z]{3}) = \\(([1-9A-Z]{3})\\, ([1-9A-Z]{3})\\)";
        Pattern pattern = Pattern.compile(regex);
        Map<String, NextNode> nodeMap = new HashMap<>();
        for (int i = 0; i < lines.size(); i++) {

            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }
            if (i == 0) {
                instruction = line;
                continue;
            }
            List<String> matchedGroups = getMatchedGroups(pattern, line);
            if (matchedGroups.size() == 3)
                nodeMap.put(matchedGroups.get(0), new NextNode(matchedGroups.get(1), matchedGroups.get(2)));
        }

        return getNumSteps(instruction, "AAA", nodeMap);
    }

    private static int getNumSteps(String instruction, String currentNode, Map<String, NextNode> nodeMap) {

        int steps = 0;
        int directionIndex = 0;
        while (true) {
            char nextDirection = instruction.charAt(directionIndex);
            if (currentNode.endsWith("Z"))
                break;

            NextNode nextNode = nodeMap.get(currentNode);
            if (nextDirection == 'L') {
                currentNode = nextNode.getLeft();
            } else {
                currentNode = nextNode.getRight();
            }
            directionIndex = (directionIndex + 1) % instruction.length();
            steps++;
        }
        return steps;
    }

    private static List<String> getMatchedGroups(Pattern pattern, String line) {
        Matcher matcher = pattern.matcher(line);
        List<String> matchedGroups = new ArrayList<>();
        if (matcher.matches()) {
            matchedGroups.add(matcher.group(1));
            matchedGroups.add(matcher.group(2));
            matchedGroups.add(matcher.group(3));
        }
        return matchedGroups;
    }

}

@Data
@AllArgsConstructor
@NoArgsConstructor
class NextNode {
    String left;
    String right;
}