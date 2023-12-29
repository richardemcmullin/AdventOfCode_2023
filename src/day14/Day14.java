package day14;

import java.util.ArrayList;
import java.util.List;

import template.AdventOfCodeTemplate;

public class Day14 extends AdventOfCodeTemplate {

    public static void main(String[] args) {
        new Day14().solve();
    }

    /** List of objects created by parsing the input */
    private char[][] platform;

    private enum Direction {
        NORTH, WEST, SOUTH, EAST
    };

    @Override
    public void readInput() {

        List<String> inputs = new ArrayList<>();

        // Read the input until a blank line is encountered
        while (true) {

            String input = scanner.nextLine();

            if (input.isBlank()) {
                break;
            }

            inputs.add(input);
        }

        platform = new char[inputs.size()][inputs.get(0).length()];

        for (int row = 0; row < inputs.size(); row++) {
            platform[row] = inputs.get(row).toCharArray();
        }
    }

    @Override
    public void solvePart1() {

        // Tilt the platform north.
        char[][] tiltedPlatform = tilt(platform, Direction.NORTH);

        System.out.println(getPlatformString(tiltedPlatform));

        System.out.println("Total " + getLoad(tiltedPlatform));
    }

    @Override
    public void solvePart2() {

        /*
         * Part 2 involves a bit of a cheat. Instead of detecting the pattern
         * repeats, just print of a list of numbers from 100,000 and look for
         * the repetitionLength
         * (1,000,000,000 - 100,000) % repetitionLength = index into printed numbers.
         *
         * FIXME: Ideas for how to solve this. Keep a list of all patterns solved so
         * far, and when a repeat position is found, then the period is known and all
         * the current point is known. Calculate and print the answer.
         */
        // Tilt the platform north.
        char[][] tiltedPlatform = spin(platform);
        for (int i = 0; i < 1000000000 - 1; i++) {

            // Print out the 10 spins after 100000.
            if (i + 1 >= 100000 && i + 1 <= 100030) {
                System.out.println((i + 1) + ": " + getLoad(tiltedPlatform));
            }

            if (i + 1 > 100030) {
                break;
            }
//            System.out.println(i + 1);
//            System.out.println(getPlatformString(tiltedPlatform));

            tiltedPlatform = spin(tiltedPlatform);

        }

//        System.out.println("3");
//        System.out.println(getPlatformString(tiltedPlatform));

        System.out.println("Total " + getLoad(tiltedPlatform));
    }



    private char[][] spin(char[][] platform) {

        char[][] tiltedPlatform = tilt(platform, Direction.NORTH);
        tiltedPlatform = tilt(tiltedPlatform, Direction.WEST);
        tiltedPlatform = tilt(tiltedPlatform, Direction.SOUTH);
        tiltedPlatform = tilt(tiltedPlatform, Direction.EAST);

        return tiltedPlatform;
    }

    private String getPlatformString(char[][] platform) {

        StringBuilder sb = new StringBuilder();

        for (char[] rowChars : platform) {
            sb.append(rowChars).append("\n");
        }
        return sb.toString();
    }

    private char[][] tilt(char[][] platform, Direction direction) {

        // Initialize an empty platform
        char[][] tiltedPlatform = new char[platform.length][platform[0].length];

        for (int row = 0; row < tiltedPlatform.length; row++) {
            for (int col = 0; col < tiltedPlatform[0].length; col++) {
                tiltedPlatform[row][col] = '.';
            }
        }


        int[] next;

        switch (direction) {

        case NORTH:
            next = new int[platform[0].length];

            for (int col = 0; col < tiltedPlatform[0].length; col++) {
                next[col] = 0;
            }

            for (int row = 0; row < tiltedPlatform.length; row++) {

                for (int col = 0; col < tiltedPlatform[0].length; col++) {

                    // Roll any rocks to the next available row
                    if (platform[row][col] == 'O') {
                        tiltedPlatform[next[col]][col] = 'O';
                        next[col]++;
                    }

                    if (platform[row][col] == '#') {
                        tiltedPlatform[row][col] = '#';
                        next[col]                = row + 1;
                    }
                }
            }
            break;

        case SOUTH:

            next = new int[platform[0].length];

            for (int col = 0; col < tiltedPlatform[0].length; col++) {
                next[col] = platform.length - 1;
            }

            for (int row = platform.length - 1; row >= 0; row--) {

                for (int col = 0; col < tiltedPlatform[0].length; col++) {

                    // Roll any rocks to the next available row
                    if (platform[row][col] == 'O') {
                        tiltedPlatform[next[col]][col] = 'O';
                        next[col]--;
                    }

                    if (platform[row][col] == '#') {
                        tiltedPlatform[row][col] = '#';
                        next[col]                = row - 1;
                    }
                }
            }
            break;

        case WEST:

            next = new int[platform.length];

            for (int row = 0; row < tiltedPlatform.length; row++) {
                next[row] = 0;
            }

            for (int col = 0; col < platform[0].length; col++) {

                for (int row = 0; row < tiltedPlatform.length; row++) {

                    // Roll any rocks to the next available col
                    if (platform[row][col] == 'O') {
                        tiltedPlatform[row][next[row]] = 'O';
                        next[row]++;
                    }

                    if (platform[row][col] == '#') {
                        tiltedPlatform[row][col] = '#';
                        next[row]                = col + 1;
                    }
                }
            }
            break;

        case EAST:
            next = new int[platform.length];

            for (int row = 0; row < tiltedPlatform.length; row++) {
                next[row] = tiltedPlatform[0].length - 1;
            }

            for (int col = tiltedPlatform[0].length - 1; col >= 0; col--) {

                for (int row = 0; row < tiltedPlatform.length; row++) {

                    // Roll any rocks to the next available col
                    if (platform[row][col] == 'O') {
                        tiltedPlatform[row][next[row]] = 'O';
                        next[row]--;
                    }

                    if (platform[row][col] == '#') {
                        tiltedPlatform[row][col] = '#';
                        next[row]                = col - 1;
                    }
                }
            }
            break;
        }

        return tiltedPlatform;

    }

    private double getLoad(char[][] platform) {

        double load = 0;

        for (int row = 0; row < platform.length; row++) {

            for (int col = 0; col < platform[0].length; col++) {

                if (platform[row][col] == 'O') {
                    load += (platform.length - row);
                }
            }
        }

        return load;
    }

}
