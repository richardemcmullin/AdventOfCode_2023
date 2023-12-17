package day11;

import java.util.ArrayList;
import java.util.List;

import template.AdventOfCodeTemplate;

public class Day11 extends AdventOfCodeTemplate {

    public static void main(String[] args) {
        new Day11().solve();
    }


    private class Galaxy {

        int row, col;

        Galaxy(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public String toString() {
            return "G(" + row + "," + col + ")";
        }
    }

    /** List of objects created by parsing the input */
    private List<Galaxy>  galaxies  = new ArrayList<>();

    private List<Integer> emptyRows = new ArrayList<>();
    private List<Integer> emptyCols = new ArrayList<>();

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

        char[][] image = new char[inputs.size()][inputs.get(0).length()];

        for (int row = 0; row < inputs.size(); row++) {
            image[row] = inputs.get(row).toCharArray();
        }

        // Find all the galaxies and empty rows
        for (int row = 0; row < image.length; row++) {

            boolean emptyRow = true;

            for (int col = 0; col < image[0].length; col++) {
                if (image[row][col] == '#') {
                    Galaxy galaxy = new Galaxy(row, col);
                    galaxies.add(galaxy);
                    emptyRow = false;
                }
            }
            if (emptyRow) {
                emptyRows.add(row);
            }
        }

        // Find all of the empty columns
        for (int col = 0; col < image[0].length; col++) {

            boolean emptyCol = true;

            for (int row = 0; row < image.length; row++) {

                if (image[row][col] == '#') {
                    emptyCol = false;
                    break;
                }
            }
            if (emptyCol) {
                emptyCols.add(col);
            }
        }

        System.out.println("Galaxies " + galaxies);
        System.out.println("EmptyRows " + emptyRows);
        System.out.println("EmptyCols " + emptyCols);
    }

    @Override
    public void solvePart1() {

        int total = 0;

        for (int i = 0; i < galaxies.size() - 1; i++) {

            for (int j = i + 1; j < galaxies.size(); j++) {

                total += getDistance(galaxies.get(i), galaxies.get(j), 2);
            }
        }

        System.out.println("Total " + total);
    }

    @Override
    public void solvePart2() {

        long total = 0;

        for (int i = 0; i < galaxies.size() - 1; i++) {

            for (int j = i + 1; j < galaxies.size(); j++) {

                total += getDistance(galaxies.get(i), galaxies.get(j), 1000000);
            }
        }

        System.out.println("Total " + total);
    }

    private long getDistance(Galaxy galaxy1, Galaxy galaxy2, long emptySpaceSize) {

        int emptyRowCount = 0;
        for (int row : emptyRows) {
            if (row > Math.min(galaxy1.row, galaxy2.row) && row < Math.max(galaxy1.row, galaxy2.row)) {
                emptyRowCount++;
            }
        }
        int emptyColCount = 0;
        for (int col : emptyCols) {
            if (col > Math.min(galaxy1.col, galaxy2.col) && col < Math.max(galaxy1.col, galaxy2.col)) {
                emptyColCount++;
            }
        }

        return Math.abs(galaxy1.row - galaxy2.row) + (emptyRowCount * (emptySpaceSize - 1))
            + Math.abs(galaxy1.col - galaxy2.col) + (emptyColCount * (emptySpaceSize - 1));
    }

}
