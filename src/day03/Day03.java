package day03;

import java.util.ArrayList;
import java.util.List;

import template.AdventOfCodeTemplate;

public class Day03 extends AdventOfCodeTemplate {

    public static void main(String[] args) {
        new Day03().solve();
    }

    class Coordinate {
        int row, col;
    }

    /** List of objects created by parsing the input */
    private List<String> inputs = new ArrayList<>();

    private char[][]     schematic;

    int                  maxRow = 0;
    int                  maxCol = 0;

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

        maxRow    = inputs.get(0).length();
        maxCol    = inputs.size();

        schematic = new char[maxRow][maxCol];

        int row = 0;
        for (String input : inputs) {
            schematic[row] = input.toCharArray();
            row++;
        }
    }

    @Override
    public void solvePart1() {

        int total = 0;

        for (int row = 0; row < maxRow; row++) {

            int     value          = 0;
            boolean adjacentSymbol = false;

            for (int col = 0; col < maxCol; col++) {

                char c = schematic[row][col];

                // If this is a number, then add it to the current number
                // and continue;
                if (c >= '0' && c <= '9') {
                    value = value * 10 + c - '0';

                    if (!adjacentSymbol && hasAdjacentSymbol(row, col)) {
                        adjacentSymbol = true;
                    }
                }
                else {
                    // Not a number,
                    // Add any values found to the total
                    if (adjacentSymbol) {
                        total += value;
                    }
                    adjacentSymbol = false;
                    value          = 0;
                }
            }

            // Check the end of the line for a number
            if (adjacentSymbol) {
                total += value;
            }
        }

        System.out.println("Total " + total);
    }

    @Override
    public void solvePart2() {

        int              total = 0;

        List<Coordinate> gears = new ArrayList<>();

        // get all of the gears
        for (int row = 0; row < maxRow; row++) {

            for (int col = 0; col < maxCol; col++) {

                if (schematic[row][col] == '*') {

                    Coordinate gear = new Coordinate();
                    gear.row = row;
                    gear.col = col;

                    gears.add(gear);
                }
            }
        }

        // For each gear, determine the gear ratio.

        for (Coordinate gear : gears) {

            total += getGearRatio(gear);
        }

        System.out.println("Total " + total);
    }

    private boolean hasAdjacentSymbol(int testRow, int testCol) {

        // Check all rows and cols adjacent to the test row, col
        // that are within the bounds of the schematic

        for (int row = Math.max(0, testRow - 1); row <= Math.min(testRow + 1, maxRow - 1); row++) {

            for (int col = Math.max(0, testCol - 1); col <= Math.min(testCol + 1, maxCol - 1); col++) {

                char c = schematic[row][col];

                // A symbol is any value that is not a number or a period.
                if (c == '.') {
                    continue;
                }

                if (c >= '0' && c <= '9') {
                    continue;
                }

                // Adjacent symbol found
                return true;
            }
        }

        // No adjacent symbol was found
        return false;
    }

    private int getGearRatio(Coordinate gear) {

        List<Integer> gearValues = new ArrayList<>();

        // look up
        if (gear.row > 0) {

            // If the center is a digit, then there is one number above.
            int value = getNumber(gear.row - 1, gear.col);

            if (value != 0) {
                gearValues.add(value);
            }
            else {
                // there could be two gears - one to the left and right of the center
                if (gear.col > 0) {
                    value = getNumber(gear.row - 1, gear.col - 1);
                    if (value != 0) {
                        gearValues.add(value);
                    }
                }
                if (gear.col < maxCol - 1) {
                    value = getNumber(gear.row - 1, gear.col + 1);
                    if (value != 0) {
                        gearValues.add(value);
                    }
                }
            }
        }

        // look down
        if (gear.row < maxRow - 1) {

            // If the center is a digit, then there is one number above.
            int value = getNumber(gear.row + 1, gear.col);

            if (value != 0) {
                gearValues.add(value);
            }
            else {
                // there could be two gears - one to the left and right of the center
                if (gear.col > 0) {
                    value = getNumber(gear.row + 1, gear.col - 1);
                    if (value != 0) {
                        gearValues.add(value);
                    }
                }
                if (gear.col < maxCol - 1) {
                    value = getNumber(gear.row + 1, gear.col + 1);
                    if (value != 0) {
                        gearValues.add(value);
                    }
                }
            }
        }

        // left
        if (gear.col > 0) {

            int value = getNumber(gear.row, gear.col - 1);

            if (value != 0) {
                gearValues.add(value);
            }
        }

        // right
        if (gear.col < maxCol - 1) {

            int value = getNumber(gear.row, gear.col + 1);

            if (value != 0) {
                gearValues.add(value);
            }
        }

        // If there are exactly two gears found, return the gear ratio
        if (gearValues.size() == 2) {
            return gearValues.get(0) * gearValues.get(1);
        }

        return 0;
    }

    private int getNumber(int row, int col) {

        // If the character is not a number, then there is nothing to do.
        char c = schematic[row][col];

        if (c < '0' || c > '9') {
            return 0;
        }

        // get the starting column of the number
        int startCol = col;

        while (startCol > 0) {

            char prevChar = schematic[row][startCol - 1];

            if (prevChar < '0' || prevChar > '9') {
                break;
            }

            startCol--;
        }

        // get the ending column of the number
        int endCol = col;

        while (endCol < maxCol - 1) {

            char nextChar = schematic[row][endCol + 1];

            if (nextChar < '0' || nextChar > '9') {
                break;
            }

            endCol++;
        }

        return Integer.valueOf(
            String.valueOf(schematic[row], startCol, endCol - startCol + 1));
    }
}
