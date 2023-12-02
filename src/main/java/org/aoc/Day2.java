package org.aoc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;

public class Day2 {

    public static void main(String[] args) throws IOException {

        List<String> lines = Files.readAllLines(Path.of("/Users/sanjana/Documents/Day2.txt"));
        //int sum = getPossibleGameSum(lines, new GameConfig(14,13,12));
        int sum = sumOfPowerOfCubes(lines);
        System.out.println(sum);
    }

    //Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
    private static int getPossibleGameSum(List<String> lines, GameConfig gameConfig) {

        int possibleGameSum = 0;
        for(String line: lines) {
            Game game = getGameFromRecord(line);
            boolean impossibleGame =
                    game.getBlueCubes() > gameConfig.getBlueCubes() ||
                    game.getGreenCubes() > gameConfig.getGreenCubes() ||
                    game.getRedCubes() > gameConfig.getRedCubes();
            if(!impossibleGame)
                possibleGameSum += game.getId();
        }
        return possibleGameSum;
    }

    private static int sumOfPowerOfCubes(List<String> lines) {
        int sumOfPower = 0;
        for(String line: lines) {
            Game game = getGameFromRecord(line);
            sumOfPower += game.getGreenCubes() * game.getBlueCubes() * game.getRedCubes();
        }
        return sumOfPower;
    }

    private static Game getGameFromRecord(String record) {
        Game game = new Game();

        String[] gameDetails = record.split(Pattern.quote(":"));
        String gameId = gameDetails[0].replace("Game", "").trim();
        game.setId(Integer.valueOf(gameId));

        String[] gameSets = gameDetails[1].split(Pattern.quote(";"));
        for(String eachSet: gameSets) {
            String[] cubes = eachSet.split(Pattern.quote(","));
            for(String eachCube:  cubes) {
                CubeCount cubeCount = getCubeCountFromPattern(eachCube);
                switch (cubeCount.getColor()) {
                    case "blue" -> game.setBlueCubes(Math.max(game.getBlueCubes(), cubeCount.getCount()));
                    case "green" -> game.setGreenCubes(Math.max(game.getGreenCubes(), cubeCount.getCount()));
                    case "red" -> game.setRedCubes(Math.max(game.getRedCubes(), cubeCount.getCount()));
                }
            }
        }
        return game;
    }

    private static CubeCount getCubeCountFromPattern(String eachCube) {
        eachCube = eachCube.trim();
        String[] cubeDetails = eachCube.split(Pattern.quote(" "));
        CubeCount cubeCount = new CubeCount();
        cubeCount.setColor(cubeDetails[1].trim());
        cubeCount.setCount(Integer.valueOf(cubeDetails[0].trim()));
        return cubeCount;
    }
}

@AllArgsConstructor
@NoArgsConstructor
@Data
class GameConfig {
    int blueCubes;
    int greenCubes;
    int redCubes;
}

@Data
@NoArgsConstructor
class CubeCount {
    String color;
    int count;
}

@AllArgsConstructor
@NoArgsConstructor
@Data
class Game {
    int id;
    int blueCubes;
    int greenCubes;
    int redCubes;
}