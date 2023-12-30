package day16;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import template.AdventOfCodeTemplate;

public class Day16 extends AdventOfCodeTemplate {

    public static void main(String[] args) {
        new Day16().solve();
    }

    private enum Direction {
        N, S, E, W
    };

    private class Beam {
        int       row, col;
        Direction dir;

        Beam(int row, int col, Direction dir) {
            this.row = row;
            this.col = col;
            this.dir = dir;
        }

        boolean step() {

            switch (dir) {
            case N:
                if (row == 0) {
                    return false;
                }
                else {
                    row--;
                    return true;
                }

            case S:
                if (row == maxRow - 1) {
                    return false;
                }
                else {
                    row++;
                    return true;
                }

            case E:
                if (col == maxCol - 1) {
                    return false;
                }
                else {
                    col++;
                    return true;
                }
            case W:
                if (col == 0) {
                    return false;
                }
                else {
                    col--;
                    return true;
                }
            }
            return false;
        }
    }

    private class Tile {

        boolean[] beamActivations = new boolean[4];
        char      tileType;

        Tile(char tileType) {
            this.tileType = tileType;
            reset();
        }

        @Override
        public String toString() {
            return String.valueOf(tileType);
        }

        void reset() {
            for (int i = 0; i < 4; i++) {
                beamActivations[i] = false;
            }
        }

        boolean isActivated() {
            for (int i = 0; i < 4; i++) {
                if (beamActivations[i]) {
                    return true;
                }
            }
            return false;
        }

        boolean isActivated(Direction dir) {
            return beamActivations[dir.ordinal()];
        }

        void setActivated(Direction dir) {
            beamActivations[dir.ordinal()] = true;
        }
    }

    /** List of objects created by parsing the input */
    private Tile[][] grid;

    int              maxRow = 0, maxCol = 0;

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

        maxRow = inputs.size();
        maxCol = inputs.get(0).length();

        grid   = new Tile[maxRow][maxCol];

        for (int row = 0; row < maxRow; row++) {

            String input = inputs.get(row);

            for (int col = 0; col < maxCol; col++) {

                grid[row][col] = new Tile(input.charAt(col));
            }
        }

        for (int row = 0; row < maxRow; row++) {
            System.out.println();
            for (int col = 0; col < maxCol; col++) {
                System.out.print(grid[row][col].tileType);
            }
        }
    }

    @Override
    public void solvePart1() {

        int         total = 0;

        Deque<Beam> beams = new ArrayDeque<>();

        // Initial beam is at 0,0 heading east.
        beams.push(new Beam(0, -1, Direction.E));

        total = getActivatedTileCount(beams);

        System.out.println("Total " + total);
    }

    @Override
    public void solvePart2() {

        int         maxEnergy = 0;

        Deque<Beam> beams     = new ArrayDeque<>();

        // Enter the grid from the north and south sides to
        // determine the max energy
        for (int col = 0; col < maxCol; col++) {

            beams.clear();
            beams.add(new Beam(-1, col, Direction.S));

            int energy = getActivatedTileCount(beams);

            maxEnergy = Math.max(maxEnergy, energy);

            beams.clear();
            beams.add(new Beam(maxRow, col, Direction.N));

            energy    = getActivatedTileCount(beams);

            maxEnergy = Math.max(maxEnergy, energy);

        }

        // Enter the grid from the east and west sides to
        // determine the max energy
        for (int row = 0; row < maxRow; row++) {

            beams.clear();
            beams.add(new Beam(row, -1, Direction.E));

            int energy = getActivatedTileCount(beams);

            maxEnergy = Math.max(maxEnergy, energy);

            beams.clear();
            beams.add(new Beam(row, maxCol, Direction.W));

            energy    = getActivatedTileCount(beams);

            maxEnergy = Math.max(maxEnergy, energy);

        }

        System.out.println("Max energy " + maxEnergy);
    }

    private int getActivatedTileCount(Deque<Beam> beams) {

        // Reset the grid before starting
        for (int row = 0; row < maxRow; row++) {
            for (int col = 0; col < maxCol; col++) {
                grid[row][col].reset();
            }
        }

        while (!beams.isEmpty()) {

            Beam beam = beams.pop();

            // Follow this beam to the end
            while (true) {

                // Follow the path of the beam.
                // If the beam runs off the map, then this beam is done.
                if (!beam.step()) {
                    break;
                }

                // get the new tile
                Tile tile = grid[beam.row][beam.col];

                // If the tile is already active from this direction, then
                // we are done.
                if (tile.isActivated(beam.dir)) {
                    break;
                }

                tile.setActivated(beam.dir);

                switch (tile.tileType) {

                case '|': // Splitter
                    if (beam.dir == Direction.E || beam.dir == Direction.W) {
                        // Split this beam by adding a south going beam
                        Beam splitBeam = new Beam(beam.row, beam.col, Direction.N);
                        beams.push(splitBeam);
                        beam.dir = Direction.S;
                    }
                    break;

                case '-': // Splitter
                    if (beam.dir == Direction.N || beam.dir == Direction.S) {
                        // Split this beam by adding a east going beam
                        Beam splitBeam = new Beam(beam.row, beam.col, Direction.E);
                        beams.push(splitBeam);
                        beam.dir = Direction.W;
                    }
                    break;

                case '/': // Mirror
                    switch (beam.dir) {
                    case N:
                        beam.dir = Direction.E;
                        break;
                    case S:
                        beam.dir = Direction.W;
                        break;
                    case E:
                        beam.dir = Direction.N;
                        break;
                    case W:
                        beam.dir = Direction.S;
                        break;
                    }
                    break;

                case '\\': // Mirror
                    switch (beam.dir) {
                    case N:
                        beam.dir = Direction.W;
                        break;
                    case S:
                        beam.dir = Direction.E;
                        break;
                    case E:
                        beam.dir = Direction.S;
                        break;
                    case W:
                        beam.dir = Direction.N;
                        break;
                    }
                    break;

                default: // Regular tile
                    break;

                }
            }
        }


        // Count activated tiles
        int activatedTiles = 0;

        for (int row = 0; row < maxRow; row++) {
            for (int col = 0; col < maxCol; col++) {
                if (grid[row][col].isActivated()) {
                    activatedTiles++;
                }
            }
        }
        return activatedTiles;
    }

}
