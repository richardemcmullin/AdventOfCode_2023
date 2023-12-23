package day12;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import template.AdventOfCodeTemplate;

public class Day12 extends AdventOfCodeTemplate {

    public static void main(String[] args) {
        new Day12().solve();
    }

    private class ConditionRecord {
        String        springs;
        List<Integer> damagedSizes = new ArrayList<>();

        ConditionRecord(String input) {
            String[] strings = input.split(" ");
            springs = strings[0];
            strings = strings[1].trim().split(",");
            for (String s : strings) {
                damagedSizes.add(Integer.valueOf(s));
            }
        }

        ConditionRecord(ConditionRecord cr) {
            this.springs = cr.springs;
            for (Integer damagedSize : cr.damagedSizes) {
                this.damagedSizes.add(damagedSize);
            }
        }

        @Override
        public String toString() {
            return springs + " " + damagedSizes;
        }
    }

    /** List of objects created by parsing the input */
    private List<ConditionRecord> conditionRecords = new ArrayList<>();
    private Map<Integer, Long>    cache            = new HashMap<>();

    @Override
    public void readInput() {

        // Read the input until a blank line is encountered
        while (true) {

            String input = scanner.nextLine();

            if (input.isBlank()) {
                break;
            }

            conditionRecords.add(new ConditionRecord(input));
        }
    }

    @Override
    public void solvePart1() {

        int total = 0;

        for (ConditionRecord cr : conditionRecords) {
            cache.clear();
            total += getPatternCount(cr);
        }

        System.out.println("Total " + total);
    }

    @Override
    public void solvePart2() {

        long total = 0;

        for (ConditionRecord cr : conditionRecords) {

            ConditionRecord copyFiveTimes = new ConditionRecord(cr);
            for (int i = 0; i < 4; i++) {
                copyFiveTimes.springs += "?" + cr.springs;
                copyFiveTimes.damagedSizes.addAll(cr.damagedSizes);
            }

            cache.clear();
            total += getPatternCount(copyFiveTimes);
        }

        System.out.println("Total " + total);
    }

    private long getPatternCount(ConditionRecord cr) {

        if (cr.damagedSizes.isEmpty()) {
            if (cr.springs.contains("#")) {
                return 0;
            }
            return 1;
        }

        // See if this pattern is in the cache
        // Cache by number of elements and length of the remaining string
        int pattern = cr.damagedSizes.size() * 1000 + cr.springs.length();
        if (cache.containsKey(pattern)) {
            return cache.get(pattern);
        }


        long            totalPatterns = 0;

        ConditionRecord subCr         = new ConditionRecord(cr);

        int             curDamageSize = subCr.damagedSizes.remove(0);

        // Calculate the minimum size required for the remaining elements
        int             minSpringSize = subCr.damagedSizes.size();
        for (int damageSize : subCr.damagedSizes) {
            minSpringSize += damageSize;
        }

        // Check all locations up to the minimum length required for the remaining elements.
        int maxDamageRecordPosition = cr.springs.length() - minSpringSize - curDamageSize;

        for (int i = 0; i <= maxDamageRecordPosition; i++) {

            // Determine if the damage record can go at this location.

            // There cannot be a damaged spring before this location
            if (i > 0) {
                if (cr.springs.charAt(i - 1) == '#') {
                    // Cache before returning
                    cache.put(pattern, totalPatterns);
                    return totalPatterns;
                }
            }

            // None of the springs can be active in this position in order
            // to place the damage record
            boolean validPosition = true;

            for (int j = i; j < i + curDamageSize; j++) {
                if (cr.springs.charAt(j) == '.') {
                    validPosition = false;
                    break;
                }
            }

            if (!validPosition) {
                continue;
            }

            // If this is not the end of the record, then the next position cannot be a
            // damaged spring
            if (i + curDamageSize < cr.springs.length()) {
                if (cr.springs.charAt(i + curDamageSize) == '#') {
                    continue;
                }
            }

            // Substring the input and then count the sub patterns.
            subCr.springs  = cr.springs.substring(
                Math.min(cr.springs.length(), i + curDamageSize + 1));

            totalPatterns += getPatternCount(subCr);
        }

        // Cache the pattern total before returning
        cache.put(pattern, totalPatterns);

        return totalPatterns;
    }

}
