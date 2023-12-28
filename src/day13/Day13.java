package day13;

import java.util.ArrayList;
import java.util.List;

import template.AdventOfCodeTemplate;

public class Day13 extends AdventOfCodeTemplate {

    public static void main(String[] args) {
        new Day13().solve();
    }

    private class Pattern {

        final char[][] pattern;

        boolean        horizontalReflection = false;
        double         reflectionLine       = -1;

        Pattern(List<String> patternStrings) {

            pattern = new char[patternStrings.size()][patternStrings.get(0).length()];

            for (int row = 0; row < patternStrings.size(); row++) {
                pattern[row] = patternStrings.get(row).toCharArray();
            }
        }

        Pattern(Pattern p) {

            this.pattern = new char[p.pattern.length][p.pattern[0].length];

            for (int row = 0; row < pattern.length; row++) {
                for (int col = 0; col < pattern[0].length; col++) {
                    this.pattern[row][col] = p.pattern[row][col];
                }
            }

        }

        @Override
        public String toString() {

            StringBuilder sb = new StringBuilder();

            for (int row = 0; row < pattern.length; row++) {
                for (int col = 0; col < pattern[0].length; col++) {
                    sb.append(this.pattern[row][col]);
                }
                sb.append("\n");
            }
            return sb.toString();
        }
    }

    /** List of objects created by parsing the input */
    private List<Pattern> patterns = new ArrayList<>();

    @Override
    public void readInput() {

        // Read the input until a double blank line is encountered
        while (true) {

            String input = scanner.nextLine();

            if (input.isBlank()) {
                break;
            }

            List<String> patternStrings = new ArrayList<>();

            patternStrings.add(input);

            while (true) {

                input = scanner.nextLine();

                if (input.isBlank()) {
                    break;
                }

                patternStrings.add(input);
            }

            patterns.add(new Pattern(patternStrings));
        }
    }

    @Override
    public void solvePart1() {

        int total = 0;

        for (Pattern pattern : patterns) {

            findReflectionLine(pattern);

            if (pattern.horizontalReflection) {
                total += 100 * (int) (pattern.reflectionLine + 0.5);
            }
            else {
                total += (int) (pattern.reflectionLine + 0.5);
            }
        }

        System.out.println("Total " + total);
    }

    private double findReflectionLine(Pattern pattern) {
        return findReflectionLine(pattern, 0, false);
    }

    private double findReflectionLine(Pattern pattern, double ignoreReflection, boolean ignoreDirection) {

        // Look for a horizontal reflection line.
        for (int row1 = 0; row1 < pattern.pattern.length - 1; row1++) {

            int     row2            = row1 + 1;

            boolean reflectionFound = false;

            if (isRowReflection(pattern, row1, row2)) {
                reflectionFound = true;
            }

            if (reflectionFound) {

                pattern.reflectionLine       = (row1 + row2) / 2.0d;
                pattern.horizontalReflection = true;

                if (pattern.horizontalReflection == ignoreDirection
                    && pattern.reflectionLine == ignoreReflection) {
                    continue;
                }

                return pattern.reflectionLine;
            }
        }

        // Look for a vertical reflection line.
        for (int col1 = 0; col1 < pattern.pattern[0].length - 1; col1++) {

            int     col2            = col1 + 1;

            boolean reflectionFound = false;

            if (isColReflection(pattern, col1, col2)) {
                reflectionFound = true;
            }

            if (reflectionFound) {

                pattern.reflectionLine       = (col1 + col2) / 2.0d;
                pattern.horizontalReflection = false;

                if (pattern.horizontalReflection == ignoreDirection
                    && pattern.reflectionLine == ignoreReflection) {
                    continue;
                }

                return pattern.reflectionLine;
            }
        }

        return -1;
    }

    private boolean isRowReflection(Pattern pattern, int row1, int row2) {

        while (row1 >= 0 && row2 < pattern.pattern.length) {

            // Determine if the rows are equal
            char[] row1Chars = pattern.pattern[row1];
            char[] row2Chars = pattern.pattern[row2];

            for (int col = 0; col < pattern.pattern[0].length; col++) {
                if (row1Chars[col] != row2Chars[col]) {
                    return false;
                }
            }
            row1--;
            row2++;
        }
        return true;
    }

    private boolean isColReflection(Pattern pattern, int col1, int col2) {

        while (col1 >= 0 && col2 < pattern.pattern[0].length) {

            for (int row = 0; row < pattern.pattern.length; row++) {
                if (pattern.pattern[row][col1] != pattern.pattern[row][col2]) {
                    return false;
                }
            }
            col1--;
            col2++;
        }
        return true;
    }

    @Override
    public void solvePart2() {

        int total = 0;

        for (Pattern pattern : patterns) {

            Pattern smudgePattern      = new Pattern(pattern);

            boolean newReflectionFound = false;

            for (int row = 0; row < smudgePattern.pattern.length; row++) {

                for (int col = 0; col < smudgePattern.pattern[0].length; col++) {

                    char temp = smudgePattern.pattern[row][col];

                    // Change the one character, save it for later
                    if (temp == '#') {
                        smudgePattern.pattern[row][col] = '.';
                    }
                    else {
                        smudgePattern.pattern[row][col] = '#';
                    }

                    // Find a new reflection line, ignoring the current pattern reflection
                    double reflectionLine = findReflectionLine(smudgePattern, pattern.reflectionLine,
                        pattern.horizontalReflection);

                    // If the reflection is not found, restore the character that
                    // was changed and continue trying smudges one by one.
                    if (reflectionLine < 0) {
                        smudgePattern.pattern[row][col] = temp;
                        continue;
                    }

                    newReflectionFound = true;
                    break;
                }

                if (newReflectionFound) {
                    break;
                }
            }

            if (!newReflectionFound) {
                System.out.println("No new reflection found for pattern");
            }

            if (smudgePattern.horizontalReflection) {
                total += 100 * (int) (smudgePattern.reflectionLine + 0.5);
            }
            else {
                total += (int) (smudgePattern.reflectionLine + 0.5);
            }
        }

        System.out.println("Total " + total);
    }

}
