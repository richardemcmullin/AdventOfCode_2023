package day09;

import java.util.ArrayList;
import java.util.List;

import template.AdventOfCodeTemplate;

public class Day09 extends AdventOfCodeTemplate {

    public static void main(String[] args) {
        new Day09().solve();
    }

    private class ValueHistory {

        List<List<Integer>> valuesSequences = new ArrayList<>();

        ValueHistory(String input) {

            String[]      inputStrings = input.split(" ");

            List<Integer> values       = new ArrayList<>();
            valuesSequences.add(values);

            for (String s : inputStrings) {
                values.add(Integer.valueOf(s));
            }

            while (!isAllZeroes(values)) {

                List<Integer> nextValues = new ArrayList<>();
                valuesSequences.add(nextValues);

                for (int i = 0; i < values.size() - 1; i++) {
                    nextValues.add(values.get(i + 1) - values.get(i));
                }

                values = nextValues;
            }
        }

        public int getNextValue() {

            int nextValue = 0;
            for (int i = valuesSequences.size() - 1; i >= 0; i--) {

                List<Integer> nextValues = valuesSequences.get(i);

                nextValue += nextValues.get(nextValues.size() - 1);
            }
            return nextValue;
        }

        public int getPreviousValue() {

            int prevValue = 0;

            for (int i = valuesSequences.size() - 1; i >= 0; i--) {

                List<Integer> nextValues = valuesSequences.get(i);

                prevValue = nextValues.get(0) - prevValue;
            }
            return prevValue;
        }

        private boolean isAllZeroes(List<Integer> values) {

            for (int value : values) {
                if (value != 0) {
                    return false;
                }
            }

            return true;
        }
    }

    /** List of objects created by parsing the input */
    private List<ValueHistory> valueHistories = new ArrayList<>();

    @Override
    public void readInput() {

        // Read the input until a blank line is encountered
        while (true) {

            String input = scanner.nextLine();

            if (input.isBlank()) {
                break;
            }

            valueHistories.add(new ValueHistory(input));
        }
    }

    @Override
    public void solvePart1() {

        int total = 0;

        for (ValueHistory valueHistory : valueHistories) {
            total += valueHistory.getNextValue();
        }

        System.out.println("Total " + total);
    }

    @Override
    public void solvePart2() {

        int total = 0;

        for (ValueHistory valueHistory : valueHistories) {
            total += valueHistory.getPreviousValue();
        }

        System.out.println("Total " + total);
    }

}
