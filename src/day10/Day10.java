package day10;

import java.util.ArrayList;
import java.util.List;

import template.AdventOfCodeTemplate;

public class Day10 extends AdventOfCodeTemplate {

    public static void main(String[] args) {
        new Day10().solve();
    }

    private enum Direction {
        E, W, N, S
    }

    private class Position {
        int       row, col, dist;
        Direction direction;

        @Override
        public String toString() {
            return '[' + row + ',' + col + "] " + direction + ' ' + dist;
        }
    }

    private char[][] map              = null;
    private Position startingPosition = new Position();
    private int      maxRow           = -1;
    private int      maxCol           = -1;

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

            maxRow = inputs.size();
            maxCol = inputs.get(0).length();

            map    = new char[maxRow][maxCol];

            for (int row = 0; row < maxRow; row++) {

                map[row] = inputs.get(row).toCharArray();

                int startingCol = inputs.get(row).indexOf('S');
                if (startingCol >= 0) {
                    startingPosition.row = row;
                    startingPosition.col = startingCol;
                }
            }
        }
    }

    @Override
    public void solvePart1() {

        List<Position> positions = new ArrayList<>();

        // Find the two directions for the first step from the starting position.
        if (isValidDirection(Direction.N, startingPosition.row, startingPosition.col)) {

            Position position = new Position();
            position.row       = startingPosition.row;
            position.col       = startingPosition.col;
            position.direction = Direction.N;
            position.dist      = 0;

            positions.add(position);
        }

        if (isValidDirection(Direction.S, startingPosition.row, startingPosition.col)) {

            Position position = new Position();
            position.row       = startingPosition.row;
            position.col       = startingPosition.col;
            position.direction = Direction.S;
            position.dist      = 0;

            positions.add(position);
        }

        if (isValidDirection(Direction.E, startingPosition.row, startingPosition.col)) {

            Position position = new Position();
            position.row       = startingPosition.row;
            position.col       = startingPosition.col;
            position.direction = Direction.E;
            position.dist      = 0;

            positions.add(position);
        }

        if (isValidDirection(Direction.W, startingPosition.row, startingPosition.col)) {

            Position position = new Position();
            position.row       = startingPosition.row;
            position.col       = startingPosition.col;
            position.direction = Direction.W;
            position.dist      = 0;

            positions.add(position);
        }

        if (positions.size() != 2) {
            System.out.println("Cannot find 2 starting directions");
            return;
        }

        Position position0 = positions.get(0);
        Position position1 = positions.get(1);

        // take a step in each starting direction to get off the S
        step(position0);
        step(position1);

        while (!(position0.row == position1.row && position0.col == position1.col)) {
            // take a step in each direction
            step(position0);
            step(position1);
        }

        System.out.println("Total " + position0.dist);
    }

    private void step(Position position) {

        // Take a step in the current direction
        switch (position.direction) {
        case N:
            position.row--;
            break;
        case S:
            position.row++;
            break;
        case E:
            position.col++;
            break;
        case W:
            position.col--;
            break;
        default:
            break;
        }

        // Set the new direction based on the new symbol
        switch (map[position.row][position.col]) {

        case '|':
        case '-':
            // keep going in the same direction
            break;

        case 'F':
            if (position.direction == Direction.N) {
                position.direction = Direction.E;
            }
            else {
                position.direction = Direction.S;
            }
            break;

        case '7':
            if (position.direction == Direction.N) {
                position.direction = Direction.W;
            }
            else {
                position.direction = Direction.S;
            }
            break;

        case 'L':
            if (position.direction == Direction.S) {
                position.direction = Direction.E;
            }
            else {
                position.direction = Direction.N;
            }
            break;

        case 'J':
            if (position.direction == Direction.S) {
                position.direction = Direction.W;
            }
            else {
                position.direction = Direction.N;
            }
            break;
        default:
            System.out.println("Invalid position " + position.row + ' ' + position.col + ' '
                + map[position.row][position.col]);
        }

        // Increment the step count
        position.dist++;
    }

    private boolean isValidDirection(Direction d, int row, int col) {

        char c = ' ';

        switch (d) {

        case N:
            if (row == 0) {
                return false;
            }
            c = map[row - 1][col];
            if (c == '|' || c == 'F' || c == '7') {
                return true;
            }
            return false;

        case S:
            if (row == map.length - 1) {
                return false;
            }
            c = map[row + 1][col];
            if (c == '|' || c == 'L' || c == 'J') {
                return true;
            }
            return false;

        case E:
            if (col == map[0].length - 1) {
                return false;
            }
            c = map[row][col + 1];
            if (c == '-' || c == 'J' || c == '7') {
                return true;
            }
            return false;

        case W:
            if (col == 0) {
                return false;
            }
            c = map[row][col - 1];
            if (c == '-' || c == 'F' || c == 'L') {
                return true;
            }
            return false;

        default:
            return false;
        }
    }

    @Override
    public void solvePart2() {

        System.out.println("Not yet implemented");
    }



}
