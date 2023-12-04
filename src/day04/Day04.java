package day04;

import java.util.ArrayList;
import java.util.List;

import template.AdventOfCodeTemplate;

public class Day04 extends AdventOfCodeTemplate {

    private class ScratchCard {

        int           number;
        List<Integer> winningNumbers = new ArrayList<>();
        List<Integer> myNumbers      = new ArrayList<>();
        int           copies         = 1;
    }

    public static void main(String[] args) {
        new Day04().solve();
    }

    /** List of objects created by parsing the input */
    private List<ScratchCard> cards = new ArrayList<>();

    @Override
    public void readInput() {

        // Read the input until a blank line is encountered
        while (true) {

            String input = scanner.nextLine();

            if (input.isBlank()) {
                break;
            }

            ScratchCard card = new ScratchCard();
            cards.add(card);

            String[] strings = input.split(":");
            card.number = Integer.valueOf(strings[0].substring(4).trim());

            String[] numberStrings  = strings[1].trim().split(" ");

            boolean  winningNumbers = true;

            for (String s : numberStrings) {

                if (s.isBlank()) {
                    continue;
                }

                if (s.equals("|")) {
                    winningNumbers = false;
                    continue;
                }

                if (winningNumbers) {
                    card.winningNumbers.add(Integer.valueOf(s));
                }
                else {
                    card.myNumbers.add(Integer.valueOf(s));
                }
            }
        }
    }

    @Override
    public void solvePart1() {

        int total = 0;

        for (ScratchCard card : cards) {
            total += points(card);
        }

        System.out.println("Total " + total);
    }

    @Override
    public void solvePart2() {

        // Calculate the number of copies of each card

        for (int i = 0; i < cards.size(); i++) {

            ScratchCard card    = cards.get(i);

            int         matches = matchingNumbers(card);

            for (int j = i + 1; j <= Math.min(cards.size() - 1, i + matches); j++) {

                cards.get(j).copies += card.copies;
            }
        }

        // Count the total number of cards

        int total = 0;

        for (ScratchCard card : cards) {
            total += card.copies;
        }

        System.out.println("Total " + total);
    }

    private int points(ScratchCard card) {

        int points = 0;

        for (Integer winningNumber : card.winningNumbers) {

            if (card.myNumbers.contains(winningNumber)) {
                if (points == 0) {
                    points = 1;
                }
                else {
                    points *= 2;
                }
            }
        }

        return points;
    }

    private int matchingNumbers(ScratchCard card) {

        int matches = 0;

        for (Integer winningNumber : card.winningNumbers) {

            if (card.myNumbers.contains(winningNumber)) {
                matches++;
            }
        }

        return matches;
    }

}
