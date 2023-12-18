package day07;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import template.AdventOfCodeTemplate;

public class Day07 extends AdventOfCodeTemplate {

    public static void main(String[] args) {
        new Day07().solve();
    }

    private enum HandValue {
        HIGH_CARD, ONE_PAIR, TWO_PAIR, THREE_OF_A_KIND, FULL_HOUSE, FOUR_OF_A_KIND, FIVE_OF_A_KIND
    }

    private class Hand {

        final HandValue handValue;
        final HandValue handValueWild;
        final String    hand;
        final int       bid;

        Hand(String input) {

            String[] inputStrings = input.split(" ");

            hand          = inputStrings[0];
            bid           = Integer.valueOf(inputStrings[1]);
            handValue     = getHandValue(hand, false);
            handValueWild = getHandValue(hand, true);

        }
    }

    /** List of objects created by parsing the input */
    private List<Hand> hands = new ArrayList<>();

    @Override
    public void readInput() {

        // Read the input until a blank line is encountered
        while (true) {

            String input = scanner.nextLine();

            if (input.isBlank()) {
                break;
            }

            hands.add(new Hand(input));
        }
    }

    @Override
    public void solvePart1() {

        int total = 0;

        // Sort the hands based on hand value and highest first card
        hands.sort(new Comparator<Hand>() {
            @Override
            public int compare(Hand h0, Hand h1) {

                // Compare the strength of the hands
                if (h0.handValue.ordinal() != h1.handValue.ordinal()) {
                    return h0.handValue.ordinal() - h1.handValue.ordinal();
                }

                for (int i = 0; i < 5; i++) {

                    int value0 = getCardValue(h0.hand.charAt(i));
                    int value1 = getCardValue(h1.hand.charAt(i));

                    if (value0 == value1) {
                        continue;
                    }

                    return value0 - value1;
                }
                return 0;
            }
        });

        int order = 0;
        for (Hand hand : hands) {

            order++;

            total += order * hand.bid;
        }

        System.out.println("Total " + total);
    }

    @Override
    public void solvePart2() {

        int total = 0;

        // Sort the hands based on hand value and highest first card
        hands.sort(new Comparator<Hand>() {
            @Override
            public int compare(Hand h0, Hand h1) {

                // Compare the strength of the hands
                if (h0.handValueWild.ordinal() != h1.handValueWild.ordinal()) {
                    return h0.handValueWild.ordinal() - h1.handValueWild.ordinal();
                }

                for (int i = 0; i < 5; i++) {

                    int value0 = getCardValue(h0.hand.charAt(i), true);
                    int value1 = getCardValue(h1.hand.charAt(i), true);

                    if (value0 == value1) {
                        continue;
                    }

                    return value0 - value1;
                }
                return 0;
            }
        });

        int order = 0;
        for (Hand hand : hands) {

            order++;

            total += order * hand.bid;
        }

        System.out.println("Total " + total);
    }

    private HandValue getHandValue(String hand, boolean jacksWild) {

        Map<Character, Integer> handMap = new HashMap<>();

        for (char c : hand.toCharArray()) {
            if (handMap.containsKey(c)) {
                handMap.put(c, handMap.get(c) + 1);
            }
            else {
                handMap.put(c, 1);
            }
        }

        if (jacksWild) {

            // find the jacks
            Entry<Character, Integer> jacks = null;

            for (Entry<Character, Integer> entry : handMap.entrySet()) {

                if (entry.getKey() == 'J') {
                    jacks = entry;
                    break;
                }
            }

            if (jacks != null) {

                // Remove the jacks entry
                handMap.remove(jacks.getKey());

                // If there were 5 jacks, then return 5 of a kind
                if (handMap.isEmpty()) {
                    return HandValue.FIVE_OF_A_KIND;
                }

                // Find the entry with most of a kind
                Entry<Character, Integer> mostOfAKind = null;

                for (Entry<Character, Integer> entry : handMap.entrySet()) {

                    if (mostOfAKind == null) {
                        mostOfAKind = entry;
                    }
                    else {
                        if (mostOfAKind.getValue() < entry.getValue()) {
                            mostOfAKind = entry;
                        }
                    }
                }

                // Add the jacks to the most of a kind
                handMap.put(mostOfAKind.getKey(), mostOfAKind.getValue() + jacks.getValue());
            }
        }

        switch (handMap.size()) {

        case 5:
            return HandValue.HIGH_CARD;

        case 4:
            return HandValue.ONE_PAIR;

        case 3:
            // Could be 3 of a kind, or two pair
            for (Integer count : handMap.values()) {
                if (count == 3) {
                    return HandValue.THREE_OF_A_KIND;
                }
            }
            // If it is not three of a kind, then two pair
            return HandValue.TWO_PAIR;

        case 2:
            // Could be 4 of a kind or a full house
            for (Integer count : handMap.values()) {
                if (count == 4) {
                    return HandValue.FOUR_OF_A_KIND;
                }
            }
            // If it is not three of a kind, then two pair
            return HandValue.FULL_HOUSE;

        case 1:
            return HandValue.FIVE_OF_A_KIND;

        default:
            System.out.println("Unknown hand value " + hand);
            return HandValue.HIGH_CARD;
        }
    }

    private int getCardValue(char c) {
        return getCardValue(c, false);
    }

    private int getCardValue(char c, boolean jacksWild) {

        if (c >= '2' && c <= '9') {
            return c - '0';
        }

        switch (c) {
        case 'T':
            return 10;
        case 'J':
            if (jacksWild) {
                return 0;
            }
            return 11;
        case 'Q':
            return 12;
        case 'K':
            return 13;
        case 'A':
            return 14;
        default:
            System.out.println("Unknown card (" + c + ")");
            return 0;
        }
    }
}
