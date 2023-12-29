package day15;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import template.AdventOfCodeTemplate;

public class Day15 extends AdventOfCodeTemplate {

    public static void main(String[] args) {
        new Day15().solve();
    }

    private class Lens {

        String label;
        int    focalLength;

        @Override
        public String toString() {
            return "[" + label + " " + focalLength + "]";
        }
    }

    /** List of objects created by parsing the input */
    private char[][] initializationSteps;

    @Override
    public void readInput() {

        // Read the input until a blank line is encountered
        while (true) {

            String input = scanner.nextLine();

            if (input.isBlank()) {
                break;
            }

            String[] steps = input.split(",");

            initializationSteps = new char[steps.length][];

            int i = 0;
            for (String step : steps) {
                initializationSteps[i] = step.toCharArray();
                i++;
            }
        }
    }

    @Override
    public void solvePart1() {

        int total = 0;

        for (char[] initializationStep : initializationSteps) {

            int hash = getHash(initializationStep);

//            System.out.println(hash);
            total += hash;
        }

        System.out.println("Total " + total);
    }

    @Override
    public void solvePart2() {

        List<List<Lens>> boxes = new ArrayList<>();

        for (int i = 0; i < 256; i++) {
            boxes.add(new ArrayList<Lens>());
        }

        for (char[] initializationStep : initializationSteps) {

            String  label        = "";
            boolean addOperation = true;
            int     focalLength  = 0;

            int     index        = -1;

            // Parse the label and the operation
            while (true) {

                index++;

                if (initializationStep[index] == '=') {
                    break;
                }

                if (initializationStep[index] == '-') {
                    addOperation = false;
                    break;
                }

                label += initializationStep[index];
            }

            // The box index is the hash of the label
            int boxIndex = getHash(label.toCharArray());

            // If it is not an add operation, remove the lens from the box
            if (!addOperation) {

                Iterator<Lens> lensIterator = boxes.get(boxIndex).iterator();

                while (lensIterator.hasNext()) {
                    Lens lens = lensIterator.next();
                    if (lens.label.equals(label)) {
                        lensIterator.remove();
                        break;
                    }
                }
                continue; // Done with remove, next step
            }

            index++;

            // On an add, parse the focal length
            while (index < initializationStep.length) {
                focalLength *= 10;
                focalLength += initializationStep[index] - '0';
                index++;
            }

            // Find an existing lens in the box
            int        existingIndex = -1;

            List<Lens> boxLenses     = boxes.get(boxIndex);

            for (int i = 0; i < boxLenses.size(); i++) {

                if (boxLenses.get(i).label.equals(label)) {
                    existingIndex = i;
                    break;
                }
            }

            // Add any new lenses to the end of the list
            if (existingIndex < 0) {

                Lens lens = new Lens();
                lens.label       = label;
                lens.focalLength = focalLength;
                boxLenses.add(lens);
            }
            else {

                // Replace the existing lens focal length
                Lens lens = boxLenses.get(existingIndex);
                lens.focalLength = focalLength;
            }
        }

        // Calculate the focusing power
        long total = 0;

        for (int box = 0; box < boxes.size(); box++) {

            List<Lens> boxLenses = boxes.get(box);

            // System.out.println("box " + box + ": " + boxLenses);

            for (int i = 0; i < boxLenses.size(); i++) {

                total += (box + 1) * (i + 1) * boxLenses.get(i).focalLength;
            }
        }

        System.out.println("Total " + total);
    }

    private int getHash(char[] chars) {

        int hash = 0;
        for (char c : chars) {
            if (c == '\n' || c == '\r') {
                continue;
            }

            hash += c;

            hash *= 17;

            hash %= 256;
        }
        return hash;
    }

}
