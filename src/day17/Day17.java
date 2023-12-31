package day17;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import template.AdventOfCodeTemplate;

public class Day17 extends AdventOfCodeTemplate {

    public static void main(String[] args) {
        new Day17().solve();
    }

    private enum Direction {
        N, S, E, W
    }

    private class Block {

        final int entryCost;
        final int row, col;

        // Shortest paths by entry direction and steps
        Path[][]  shortestPaths = new Path[4][3];

        Block(int row, int col, int entryCost) {
            this.row       = row;
            this.col       = col;
            this.entryCost = entryCost;
            reset();
        }

        void reset() {
            for (int dir = 0; dir < 4; dir++) {
                for (int step = 0; step < 3; step++) {
                    shortestPaths[dir][step] = null;
                }
            }
        }

        public List<Path> addPath(Path path) {

            List<Path> replacedPaths = new ArrayList<>();

            if (!isNewShortestPath(path)) {
                return replacedPaths;
            }

            int dir  = path.curDir.ordinal();
            int step = path.curDirStepCount - 1;

            // Replace all values at higher number of steps with this value
            // if the distance is longer at a higher number of steps
            for (int i = step; i < 2; i++) {

                if (shortestPaths[dir][i] == null) {
                    shortestPaths[dir][i] = path;
                }
                else {
                    if (path.heatLoss < shortestPaths[dir][i].heatLoss) {
                        replacedPaths.add(shortestPaths[dir][i]);
                        shortestPaths[dir][i] = path;
                    }
                }
            }

            return replacedPaths;
        }

        public boolean isNewShortestPath(Path path) {

            int dir  = path.curDir.ordinal();
            int step = path.curDirStepCount - 1;

            if (shortestPaths[dir][step] == null) {
                return true;
            }

            return path.heatLoss < shortestPaths[dir][step].heatLoss;

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

            // If there are already 3 steps in this direction, then the
            // next step is not possible
            if (dir == curDir && curDirStepCount >= 3) {
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
            if (dir == Direction.N && curBlock.row == 0
                || dir == Direction.S && curBlock.row == rows - 1
                || dir == Direction.E && curBlock.col == cols - 1
                || dir == Direction.W && curBlock.col == 0) {
                return null;
            }

            int nextRow = curBlock.row;
            int nextCol = curBlock.col;

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

            // If we have already been here, then do not re-visit
            if (blocks.contains(nextBlock)) {
                return null;
            }

            // Copy this path and add the next block
            Path newPath = new Path(this);

            newPath.blocks.add(nextBlock);
            newPath.heatLoss += nextBlock.entryCost;

            if (newPath.curDir == dir) {
                newPath.curDirStepCount++;
            }
            else {
                newPath.curDir          = dir;
                newPath.curDirStepCount = 1;
            }

            return newPath;
        }

        Block getCurBlock() {
            return blocks.get(blocks.size() - 1);
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

        printPath(new Path());

        System.out.println();
    }

    @Override
    public void solvePart1() {

        List<Path> shortestPaths = new ArrayList<>();

        // Add the first two starting directions
        Path       startingPath  = new Path();
        startingPath.blocks.add(map[0][0]);

        Path east = new Path(startingPath);
        east.blocks.add(map[0][1]);
        east.curDir                                       = Direction.E;
        east.curDirStepCount                              = 2;
        east.heatLoss                                     = map[0][1].entryCost;

        map[0][1].shortestPaths[Direction.E.ordinal()][1] = east;

        Path south = new Path(startingPath);
        south.blocks.add(map[1][0]);
        south.curDir                                      = Direction.S;
        south.curDirStepCount                             = 2;
        south.heatLoss                                    = map[1][0].entryCost;

        map[0][1].shortestPaths[Direction.S.ordinal()][1] = south;

        shortestPaths.add(east);
        shortestPaths.add(south);

        Block   destination      = map[rows - 1][cols - 1];
        Path    shortestPath     = null;

        boolean destinationFound = false;

        while (!destinationFound) {

            // sort the paths by heat loss
            shortestPaths.sort(new Comparator<Path>() {
                @Override
                public int compare(Path p0, Path p1) {
                    return p0.heatLoss - p1.heatLoss;
                }
            });

            Path p = shortestPaths.remove(0);

            // Take a step in one of the directions
            for (Direction dir : Direction.values()) {

                Path nextPath = p.step(dir);

                if (nextPath == null) {
                    continue;
                }

                Block nextBlock = nextPath.getCurBlock();

                // If the path is still the shortest at this block,
                // then add the path to the shortest path list.
                if (nextBlock.isNewShortestPath(nextPath)) {

                    shortestPaths.add(nextPath);

                    List<Path> replacedPaths = nextBlock.addPath(nextPath);

                    // Remove all replaced shortest paths from
                    // the current list of shortest paths
                    for (Path replacedPath : replacedPaths) {
                        if (shortestPaths.contains(replacedPath)) {
                            shortestPaths.remove(replacedPath);
                        }
                    }
                }

                if (nextBlock == destination) {
                    destinationFound = true;
                    shortestPath     = nextPath;
                    break;
                }
            }
        }

        // Print the shortest path
        printPath(shortestPath);

        System.out.println("Total " + shortestPath.heatLoss);
    }

    @Override
    public void solvePart2() {

        System.out.println("see Day17b");
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
