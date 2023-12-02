package day01;

import java.util.ArrayList;
import java.util.List;

import template.AdventOfCodeTemplate;

public class Day01 extends AdventOfCodeTemplate {

    public static void main(String[] args) {
        new Day01().solve();
    }

    /** List of objects created by parsing the input */
    private List<String> inputs = new ArrayList<>();

    @Override
    public void readInput() {

        // Read the input until a blank line is encountered
        while (true) {

            String input = scanner.nextLine();

            if (input.isBlank()) {
                break;
            }

            inputs.add(input);
        }
    }

    @Override
    public void solvePart1() {

        int total = 0;

        for (String calibrationLine : inputs) {

            char[] calibrationChars = calibrationLine.toCharArray();

            int    firstValue       = 0;

            for (int i = 0; i < calibrationChars.length; i++) {

                int value = getValueAt(i, calibrationChars);

                if (value >= 0) {
                    firstValue = value;
                    break;
                }
            }

            int lastValue = 0;

            for (int i = calibrationChars.length - 1; i >= 0; i--) {

                int value = getValueAt(i, calibrationChars);

                if (value >= 0) {
                    lastValue = value;
                    break;
                }
            }

            total += firstValue * 10 + lastValue;
        }

        System.out.println("Total " + total);
    }

    @Override
    public void solvePart2() {

        int total = 0;

        for (String calibrationLine : inputs) {

            char[] calibrationChars = calibrationLine.toCharArray();

            int    firstValue       = 0;

            for (int i = 0; i < calibrationChars.length; i++) {

                int value = getValueAfter(i, calibrationChars);

                if (value >= 0) {
                    firstValue = value;
                    break;
                }
            }

            int lastValue = 0;

            for (int i = calibrationChars.length - 1; i >= 0; i--) {

                int value = getValueAfter(i, calibrationChars);

                if (value >= 0) {
                    lastValue = value;
                    break;
                }
            }

            total += firstValue * 10 + lastValue;
        }

        System.out.println("Total " + total);
    }

    private int getValueAt(int index, char[] chars) {

        char c = chars[index];

        if (c >= '0' && c <= '9') {
            return c - '0';
        }

        return -1;
    }

    private int getValueAfter(int index, char[] chars) {

        char c = chars[index];

        if (c >= '0' && c <= '9') {
            return c - '0';
        }

        // If the character is not a number, then look for a full word

        String s = String.valueOf(chars, index, chars.length - index);

        if (s.startsWith("zero")) {
            return 0;
        }
        if (s.startsWith("one")) {
            return 1;
        }
        if (s.startsWith("two")) {
            return 2;
        }
        if (s.startsWith("three")) {
            return 3;
        }
        if (s.startsWith("four")) {
            return 4;
        }
        if (s.startsWith("five")) {
            return 5;
        }
        if (s.startsWith("six")) {
            return 6;
        }
        if (s.startsWith("seven")) {
            return 7;
        }
        if (s.startsWith("eight")) {
            return 8;
        }
        if (s.startsWith("nine")) {
            return 9;
        }

        return -1;
    }
}

