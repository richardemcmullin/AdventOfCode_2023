package day05;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import template.AdventOfCodeTemplate;

public class Day05 extends AdventOfCodeTemplate {

    private class SourceMap {

        String      title;
        List<Range> ranges = new ArrayList<>();

    }

    private class Range {
        long source, dest, length;
    }

    public static void main(String[] args) {
        new Day05().solve();
    }

    /** List of objects created by parsing the input */
    private List<SourceMap> sourceMaps = new ArrayList<>();

    private List<Long>      seeds      = new ArrayList<>();

    @Override
    public void readInput() {

        // get the seeds
        String[] seedStrings = scanner.nextLine().split(" ");

        for (int i = 1; i < seedStrings.length; i++) {
            seeds.add(Long.valueOf(seedStrings[i]));
        }

        // Skip the blank line
        scanner.nextLine();

        // Read the input until a blank line is encountered
        while (true) {

            // Source Name
            String input = scanner.nextLine();

            if (input.isBlank()) {
                break;
            }

            SourceMap sourceMap = new SourceMap();
            sourceMaps.add(sourceMap);

            sourceMap.title = input;

            // Read the input until a blank line is encountered
            while (true) {

                input = scanner.nextLine();

                if (input.isBlank()) {
                    break;
                }

                String[] rangeString = input.split(" ");

                Range    range       = new Range();
                sourceMap.ranges.add(range);

                range.dest   = Long.valueOf(rangeString[0]);
                range.source = Long.valueOf(rangeString[1]);
                range.length = Long.valueOf(rangeString[2]);

            }

            // Sort the ranges by source
            sourceMap.ranges.sort(new Comparator<Range>() {

                @Override
                public int compare(Range r0, Range r1) {
                    if (r0.source - r1.source == 0) {
                        return 0;
                    }
                    if (r0.source - r1.source < 0) {
                        return -1;
                    }
                    return 1;
                }
            });
        }
    }

    @Override
    public void solvePart1() {

        List<Long> outputs = new ArrayList<>();

        for (Long seed : seeds) {

            long input  = seed;
            long output = 0;

            for (SourceMap sourceMap : sourceMaps) {

                // Calculate the output from this map
                output = input;

                for (Range range : sourceMap.ranges) {

                    // Ranges are sorted in ascending order. Look for the
                    // first end of range that is higher than the input.
                    if (input > range.source + range.length - 1) {
                        continue;
                    }

                    // If the source also higher than the input then we are done.
                    if (range.source > input) {
                        break;
                    }

                    output = range.dest + input - range.source;
                }
                input = output;
            }

            outputs.add(output);
        }

        System.out.println("Locations " + outputs.toString());

        System.out.println("Min " + Collections.min(outputs));
    }

    @Override
    public void solvePart2() {

        System.out.println("Not yet implemented");
    }

}
