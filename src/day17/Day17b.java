package day17;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import template.AdventOfCodeTemplate;

public class Day17b extends AdventOfCodeTemplate {

    static final int MIN_STEP_COUNT  = 4;
    static final int MAX_STEP_COUNT  = 10;
    static final int STEP_ARRAY_SIZE = MAX_STEP_COUNT - MIN_STEP_COUNT + 1;

    public static void main(String[] args) {
        new Day17b().solve();
    }

    private enum Direction {
        S, E, W, N
    }

    static final int DIRECTIONS = Direction.values().length;

    private class Block {

        final int entryCost;
        final int row, col;

        // Shortest paths by entry direction and steps
        Path[][]  shortestPaths = new Path[DIRECTIONS][STEP_ARRAY_SIZE];

        Block(int row, int col, int entryCost) {
            this.row       = row;
            this.col       = col;
            this.entryCost = entryCost;
            reset();
        }

        void reset() {
            for (int dir = 0; dir < DIRECTIONS; dir++) {
                for (int step = 0; step < STEP_ARRAY_SIZE; step++) {
                    shortestPaths[dir][step] = null;
                }
            }
        }

        public void addPath(Path path) {

            int dir       = path.curDir.ordinal();
            int stepIndex = path.curDirStepCount - MIN_STEP_COUNT;

            // Replace all values at higher number of steps with this value
            // if the heat loss is larger at a higher number of steps
            for (int i = stepIndex; i < STEP_ARRAY_SIZE; i++) {

                if (shortestPaths[dir][i] == null
                    || path.heatLoss < shortestPaths[dir][i].heatLoss) {
                    shortestPaths[dir][i] = path;
                }
            }
        }

        public boolean isNewShortestPath(Path path) {

            int dir       = path.curDir.ordinal();
            int stepIndex = path.curDirStepCount - MIN_STEP_COUNT;

            if (shortestPaths[dir][stepIndex] == null) {
                return true;
            }

            return path.heatLoss < shortestPaths[dir][stepIndex].heatLoss;

        }
    }

    private class Path {

        int         heatLoss        = 0;

        Direction   curDir          = null;
        int         curDirStepCount = 0;

        List<Block> blocks          = new ArrayList<>();

        Path() {
        }

        Path(Path p) {
            this.curDir          = p.curDir;
            this.curDirStepCount = p.curDirStepCount;
            this.heatLoss        = p.heatLoss;
            this.blocks.addAll(p.blocks);
        }

        Path step(Direction dir) {

            // Check against the max steps in any direction
            if (dir == curDir && curDirStepCount >= MAX_STEP_COUNT) {
                return null;
            }

            // Stepping back is not allowed
            if (dir == Direction.N && curDir == Direction.S
                || dir == Direction.S && curDir == Direction.N
                || dir == Direction.E && curDir == Direction.W
                || dir == Direction.W && curDir == Direction.E) {
                return null;
            }

            Block curBlock = getCurBlock();

            // Cannot step off the map
            // Steps of 1 are allowed in the current direction, or steps of
            // 4 are allowed in a new direction;
            int   stepSize = 1;
            if (dir != curDir) {
                stepSize = MIN_STEP_COUNT;
            }

            if (dir == Direction.N && curBlock.row < stepSize
                || dir == Direction.S && curBlock.row >= rows - stepSize
                || dir == Direction.E && curBlock.col >= cols - stepSize
                || dir == Direction.W && curBlock.col < stepSize) {
                return null;
            }

            // Copy this path and add the next blocks
            Path newPath = new Path(this);

            int  nextRow = curBlock.row;
            int  nextCol = curBlock.col;

            for (int i = 0; i < stepSize; i++) {

                switch (dir) {
                case N:
                    nextRow--;
                    break;
                case S:
                    nextRow++;
                    break;
                case E:
                    nextCol++;
                    break;
                case W:
                    nextCol--;
                    break;
                }

                Block nextBlock = map[nextRow][nextCol];
                newPath.blocks.add(nextBlock);
                newPath.heatLoss += nextBlock.entryCost;
            }


            if (newPath.curDir == dir) {
                newPath.curDirStepCount += stepSize;
            }
            else {
                newPath.curDir          = dir;
                newPath.curDirStepCount = stepSize;
            }

            return newPath;
        }

        Block getCurBlock() {
            return blocks.get(blocks.size() - 1);
        }

        @Override
        public String toString() {
            Block block = getCurBlock();
            return "([" + block.row + "][" + block.col + "] " + curDir
                + " " + curDirStepCount + " : " + blocks.size() + " H " + heatLoss + ")";
        }

    }

    /** List of objects created by parsing the input */
    private Block[][] map;

    int               rows = 0;
    int               cols = 0;

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

        rows = inputs.size();
        cols = inputs.get(0).length();

        map  = new Block[rows][cols];

        for (int row = 0; row < rows; row++) {

            char[] inputChars = inputs.get(row).toCharArray();

            for (int col = 0; col < cols; col++) {

                map[row][col] = new Block(row, col, inputChars[col] - '0');
            }
        }

//        printPath(new Path());

        System.out.println();
    }

    @Override
    public void solvePart2() {

        List<Path> shortestPaths = new ArrayList<>();

        // Add the first two starting directions
        // NOTE: the origin is not a step so the first step is 4 long in
        // each direction

        Path       startingPath  = new Path();
        startingPath.blocks.add(map[0][0]);

        Path east = new Path(startingPath);

        east.blocks.add(map[0][1]);
        east.blocks.add(map[0][2]);
        east.blocks.add(map[0][3]);
        east.blocks.add(map[0][4]);
        east.curDir                                       = Direction.E;
        east.curDirStepCount                              = MIN_STEP_COUNT;
        east.heatLoss                                     = map[0][1].entryCost
            + map[0][2].entryCost + map[0][3].entryCost + map[0][4].entryCost;

        map[0][4].shortestPaths[Direction.E.ordinal()][0] = east;

        Path south = new Path(startingPath);

        south.blocks.add(map[1][0]);
        south.blocks.add(map[2][0]);
        south.blocks.add(map[3][0]);
        south.blocks.add(map[4][0]);
        south.curDir                                      = Direction.S;
        south.curDirStepCount                             = MIN_STEP_COUNT;
        south.heatLoss                                    = map[1][0].entryCost
            + map[2][0].entryCost + map[3][0].entryCost + map[4][0].entryCost;

        map[4][0].shortestPaths[Direction.S.ordinal()][0] = south;

        shortestPaths.add(east);
        shortestPaths.add(south);

        Block destination  = map[rows - 1][cols - 1];
        Path  shortestPath = null;

        while (true) {

            // sort the paths by heat loss
            shortestPaths.sort(new Comparator<Path>() {
                @Override
                public int compare(Path p0, Path p1) {
                    return p0.heatLoss - p1.heatLoss;
                }
            });

            Path p = shortestPaths.remove(0);

            // We are done when a shortest path has been found
            // and the next possible path is longer than the shortest path
            if (shortestPath != null
                && p.heatLoss > shortestPath.heatLoss) {
                break;
            }

            // Take a step in one of the directions
            for (Direction dir : Direction.values()) {

                Path nextPath = p.step(dir);

                if (nextPath == null) {
                    continue;
                }

                Block nextBlock = nextPath.getCurBlock();

                // If the path is the shortest at this next block,
                // then add the path to the shortest path list.
                if (nextBlock.isNewShortestPath(nextPath)) {

                    shortestPaths.add(nextPath);

                    nextBlock.addPath(nextPath);

                    if (nextBlock == destination) {
                        if (shortestPath == null
                            || nextPath.heatLoss < shortestPath.heatLoss) {
                            shortestPath = nextPath;
                        }
                    }
                }

            }
        }

        // Print the shortest path
        printPath(shortestPath);

        System.out.println("Total " + shortestPath.heatLoss);
    }

    @Override
    public void solvePart1() {

        System.out.println("See Day17");
    }

    private void printPath(Path path) {

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {

                Block block = map[row][col];

                if (path.blocks.contains(block)) {
                    System.out.print("*");
                }
                else {
                    System.out.print(block.entryCost);
                }
            }
            System.out.println();
        }
    }
}
