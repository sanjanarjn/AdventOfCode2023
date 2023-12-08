package org.aoc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day7 {

    private static CustomAlphabetComparator customAlphabetComparator = new CustomAlphabetComparator("AKQJT98765432");

    public static void main(String[] args) throws IOException {

        List<String> lines = Files.readAllLines(Path.of("/Users/sanjana/Documents/AoC/Day7.txt"));
        long winnings = getTotalWinnings(lines);
        System.out.println(winnings);
    }

    private static long getTotalWinnings(List<String> lines) {
        List<Hand> hands = new ArrayList<>();
        for (String line : lines) {
            String[] handDetails = line.split("\\s+");
            hands.add(new Hand(handDetails[0].trim(), Integer.valueOf(handDetails[1].trim())));
        }

        Comparator<Hand> handComparator = new HandComparator();
        Collections.sort(hands, handComparator.reversed());

        for (Hand hand : hands) {
            System.out.println(hand.cards + " " + hand.bid);
        }
        long winnings = 0;
        for (int i = 0; i < hands.size(); i++) {
            winnings += (i + 1) * hands.get(i).bid;
        }
        return winnings;

    }
}

class HandComparator implements Comparator<Hand> {

    CustomAlphabetComparator customAlphabetComparator = new CustomAlphabetComparator("AKQT98765432J");

    @Override
    public int compare(Hand hand1, Hand hand2) {
        int typeCompare = Integer.compare(hand1.type.ordinal(), hand2.type.ordinal());
        if (typeCompare == 0) {
            return customAlphabetComparator.compare(hand1.cards, hand2.cards);
        }
        return typeCompare;
    }
}

enum HandType {
    FIVE_OF_A_KIND, FOUR_OF_A_KIND, FULL_HOUSE, THREE_OF_A_KIND, TWO_PAIR, ONE_PAIR, HIGH_CARD;
}

@NoArgsConstructor
@Data
class Hand {

    String cards;
    int bid;
    HandType type;

    public Hand(String cards, int bid) {
        this.cards = cards;
        this.bid = bid;
        setHandType();
    }

    private void setHandType() {

        Map<Character, Integer> charCount = new HashMap<>();
        for (char c : cards.toCharArray()) {
            charCount.put(c, charCount.getOrDefault(c, 0) + 1);
        }
        handleJokers(charCount);
        if (fiveOfAKind(charCount)) {
            type = HandType.FIVE_OF_A_KIND;
        } else if (fourOfAKind(charCount)) {
            type = HandType.FOUR_OF_A_KIND;
        } else if (fullHouse(charCount)) {
            type = HandType.FULL_HOUSE;
        } else if (threeOfAKind(charCount)) {
            type = HandType.THREE_OF_A_KIND;
        } else if (twoPair(charCount)) {
            type = HandType.TWO_PAIR;
        } else if (onePair(charCount)) {
            type = HandType.ONE_PAIR;
        } else if (highCard(charCount)) {
            type = HandType.HIGH_CARD;
        }
    }

    private void handleJokers(Map<Character, Integer> charCount) {

        if (!charCount.containsKey('J')) {
            return;
        }
        int jCount = charCount.get('J');
        charCount.remove('J');

        int largest = Integer.MIN_VALUE;
        char moreFrequentChar = 'J';
        for (char c : charCount.keySet()) {
            if (charCount.get(c) > largest) {
                largest = charCount.get(c);
                moreFrequentChar = c;
            }
        }
        charCount.put(moreFrequentChar, charCount.getOrDefault(moreFrequentChar, 0) + jCount);
    }

    private boolean highCard(Map<Character, Integer> charCount) {
        Collection<Integer> counts = charCount.values();
        return counts.size() == 5;
    }

    private boolean onePair(Map<Character, Integer> charCount) {
        Collection<Integer> counts = charCount.values();
        return counts.size() == 4;
    }

    private boolean twoPair(Map<Character, Integer> charCount) {
        Collection<Integer> counts = charCount.values();
        return counts.size() == 3 && counts.contains(2) && counts.contains(1);
    }

    private boolean threeOfAKind(Map<Character, Integer> charCount) {
        Collection<Integer> counts = charCount.values();
        return counts.contains(3) && counts.contains(1) && counts.contains(1);
    }

    private boolean fullHouse(Map<Character, Integer> charCount) {
        Collection<Integer> counts = charCount.values();
        return counts.contains(3) && counts.contains(2);
    }

    private boolean fiveOfAKind(Map<Character, Integer> charCount) {
        return charCount.size() == 1;
    }

    private boolean fourOfAKind(Map<Character, Integer> charCount) {
        Collection<Integer> counts = charCount.values();
        return counts.contains(4) && counts.contains(1);
    }
}

class CustomAlphabetComparator implements Comparator<String> {
    private String customAlphabet;

    public CustomAlphabetComparator(String customAlphabet) {
        this.customAlphabet = customAlphabet;
    }

    @Override
    public int compare(String str1, String str2) {
        int minLength = Math.min(str1.length(), str2.length());

        for (int i = 0; i < minLength; i++) {
            char char1 = str1.charAt(i);
            char char2 = str2.charAt(i);

            int index1 = customAlphabet.indexOf(char1);
            int index2 = customAlphabet.indexOf(char2);

            if (index1 != index2) {
                return Integer.compare(index1, index2);
            }
        }

        return Integer.compare(str1.length(), str2.length());
    }
}
