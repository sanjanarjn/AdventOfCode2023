package org.aoc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day5 {

    public static void main(String[] args) throws IOException {

        List<String> lines = Files.readAllLines(Path.of("/Users/sanjana/Documents/Day5.txt"));
        long closestLocation = getClosestLocationWithRange(lines);
        System.out.println(closestLocation);
    }


    private static long getClosestLocationWithRange(List<String> lines) {

        List<Range> seedRanges = null;
        String[] mapNames = new String[]{
                "seed-to-soil map",
                "soil-to-fertilizer map",
                "fertilizer-to-water map",
                "water-to-light map",
                "light-to-temperature map",
                "temperature-to-humidity map",
                "humidity-to-location map"};

        Map<String, List<Mapping>> mappings = new HashMap<>();
        String currentMap = null;
        for (String line : lines) {
            if (line.trim().isEmpty())
                continue;

            if (line.startsWith("seeds:")) {
                seedRanges = populateSeedRanges(line.replaceFirst("seeds:", ""));
                continue;
            }
            boolean mapHeader = false;
            for (String mapName : mapNames) {
                if (line.startsWith(mapName)) {
                    currentMap = mapName;
                    mapHeader = true;
                    break;
                }
            }
            if (mapHeader)
                continue;

            if (currentMap != null) {
                Mapping mapping = getMapping(line);
                List<Mapping> mappingList = mappings.getOrDefault(currentMap, new ArrayList<>());
                mappingList.add(mapping);
                mappings.put(currentMap, mappingList);
            }
        }
        List<Range> locationRanges = getClosestLocationWithSeedRange(seedRanges, mapNames, 0, mappings);
        long closestLocation = Long.MAX_VALUE;
        for (Range range : locationRanges) {
            if(range.getStart() > 0 )
            closestLocation = Math.min(range.getStart(), closestLocation);
        }
        return closestLocation;

    }

    private static List<Range> populateSeedRanges(String seedString) {

        List<Range> seedRanges = new ArrayList<>();
        seedString = seedString.trim();
        String[] seeds = seedString.split("\\s+");
        for (int i = 0; i <= seeds.length / 2; i = i + 2) {
            seedRanges.add(new Range(Long.valueOf(seeds[i].trim()), Long.valueOf(seeds[i + 1].trim())));
        }
        return seedRanges;
    }

    private static long getClosestLocation(List<String> lines) {

        long closestLocation = -1;
        List<Long> seeds = null;
        String[] mapNames = new String[]{
                "seed-to-soil map",
                "soil-to-fertilizer map",
                "fertilizer-to-water map",
                "water-to-light map",
                "light-to-temperature map",
                "temperature-to-humidity map",
                "humidity-to-location map"};

        Map<String, List<Mapping>> mappings = new HashMap<>();
        String currentMap = null;
        for (String line : lines) {
            if (line.trim().isEmpty())
                continue;

            if (line.startsWith("seeds:")) {
                seeds = populateSeeds(line.replaceFirst("seeds:", ""));
                continue;
            }
            boolean mapHeader = false;
            for (String mapName : mapNames) {
                if (line.startsWith(mapName)) {
                    currentMap = mapName;
                    mapHeader = true;
                    break;
                }
            }
            if (mapHeader)
                continue;

            if (currentMap != null) {
                Mapping mapping = getMapping(line);
                List<Mapping> mappingList = mappings.getOrDefault(currentMap, new ArrayList<>());
                mappingList.add(mapping);
                mappings.put(currentMap, mappingList);
            }
        }
        closestLocation = getClosestLocation(seeds, mapNames, mappings);
        return closestLocation;

    }

    private static List<Range> getClosestLocationWithSeedRange(List<Range> ranges, String[] mapNames, int mapIndex, Map<String, List<Mapping>> mappings) {

        if (mapIndex == mapNames.length) {
            return ranges;
        }
        List<Range> destinationRanges = new ArrayList<>();
        for (Range range : ranges) {
            List<Mapping> mappingList = mappings.get(mapNames[mapIndex]);
            List<Range> destinationRangesForRange = getDestinationRanges(mappingList, range);
            if (destinationRangesForRange.isEmpty())
                destinationRanges.add(range);
            else
                destinationRanges.addAll(destinationRangesForRange);
        }

        List<Range> outputRanges = getClosestLocationWithSeedRange(destinationRanges, mapNames, mapIndex + 1, mappings);
        return outputRanges;
    }

    private static List<Range> getDestinationRanges(List<Mapping> mappingList, Range range) {

        List<Range> destinationRanges = new ArrayList<>();
        for (Mapping mapping : mappingList) {
            Range intersectionSourceRange = getIntersectionSourceRange(range.getStart(), range.getRange(), mapping.getSourceRangeStart(), mapping.getRange());
            if (intersectionSourceRange != null) {
                long offset = intersectionSourceRange.getStart() - mapping.getSourceRangeStart();
                destinationRanges.add(new Range(mapping.getDestinationRangeStart() + offset, intersectionSourceRange.getRange()));
                if(intersectionSourceRange.getStart() > range.getStart()) {
                    Range prefixRange = new Range(range.getStart(), range.getStart() - intersectionSourceRange.getStart());
                    destinationRanges.add(prefixRange);
                }
                if(intersectionSourceRange.getStart() + intersectionSourceRange.getRange() - 1 < range.getStart() + range.getRange() - 1) {
                    long diff =   (range.getStart() + range.getRange()) - (intersectionSourceRange.getStart() + intersectionSourceRange.getRange());
                    Range suffixRange = new Range(intersectionSourceRange.getStart() + intersectionSourceRange.getRange(), diff);
                    destinationRanges.add(suffixRange);
                }
            }

        }
        return destinationRanges;
    }

    private static Range getIntersectionSourceRange(long start1, long range1, long start2, long range2) {

        long end1 = start1 + range1 - 1;
        long end2 = start2 + range2 - 1;

        long intersectionStart = Math.max(start1, start2);
        long intersectionEnd = Math.min(end1, end2);

        if (intersectionStart <= intersectionEnd) {
            return new Range(intersectionStart, intersectionEnd - intersectionStart + 1);
        }
        return null;
    }


    private static long getClosestLocation(List<Long> seeds, String[] mapNames, Map<String, List<Mapping>> mappings) {

        long min = Long.MAX_VALUE;
        for (long seed : seeds) {
            long sourceValue = seed;
            for (String mapName : mapNames) {
                List<Mapping> mappingList = mappings.get(mapName);
                long destinationValue = getDestinationValue(mappingList, sourceValue);
                sourceValue = destinationValue;
            }
            if (sourceValue < min)
                min = sourceValue;
        }
        return min;
    }

    private static long getDestinationValue(List<Mapping> mappingList, long sourceValue) {

        long destinationValue = -1;
        for (Mapping mapping : mappingList) {
            boolean sourceValueInRange = sourceValue >= mapping.getSourceRangeStart() && sourceValue < mapping.getSourceRangeStart() + mapping.getRange();
            if (sourceValueInRange) {
                long offset = sourceValue - mapping.getSourceRangeStart();
                destinationValue = mapping.getDestinationRangeStart() + offset;
                break;
            }
        }
        if (destinationValue == -1) {
            destinationValue = sourceValue;
        }
        return destinationValue;
    }

    private static Mapping getMapping(String line) {
        String[] rangeValues = line.split("\\s+");
        Mapping mapping = new Mapping(Long.valueOf(rangeValues[0].trim()), Long.valueOf(rangeValues[1].trim()), Long.valueOf(rangeValues[2].trim()));
        return mapping;
    }

    private static List<Long> populateSeeds(String seedString) {

        List<Long> seedIds = new ArrayList<>();
        seedString = seedString.trim();
        String[] seeds = seedString.split("\\s+");
        for (String eachSeed : seeds) {
            seedIds.add(Long.valueOf(eachSeed.trim()));
        }
        return seedIds;

    }

}

@Data
@AllArgsConstructor@ToString
class Range {
    private long start;
    private long range;
}

@Data
@AllArgsConstructor
class Mapping {

    private long destinationRangeStart;
    private long sourceRangeStart;
    private long range;
}