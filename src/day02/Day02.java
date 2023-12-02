package day02;

import java.util.ArrayList;
import java.util.List;

import template.AdventOfCodeTemplate;

public class Day02 extends AdventOfCodeTemplate {

    private class Game {

        int           id;
        List<CubeSet> rounds = new ArrayList<>();

        public boolean isPossible(CubeSet bag) {

            // Determine if each round is a subset of the passed in bag
            for (CubeSet round : rounds) {
                if (!round.isSubset(bag)) {
                    return false;
                }
            }
            return true;
        }
    }

    private class CubeSet {

        int red   = 0;
        int blue  = 0;
        int green = 0;

        boolean isSubset(CubeSet cubeSet) {

            // This set is a subset of the passed in set if all of the colours are
            // less than or equal to the passed in set.
            if (red > cubeSet.red
                || blue > cubeSet.blue
                || green > cubeSet.green) {

                return false;
            }

            return true;
        }

        int getPower() {
            return red * green * blue;
        }

    }

    public static void main(String[] args) {
        new Day02().solve();
    }

    /** List of objects created by parsing the input */
    private List<Game> games = new ArrayList<>();

    @Override
    public void readInput() {

        // Read the input until a blank line is encountered
        while (true) {

            String input = scanner.nextLine();

            if (input.isBlank()) {
                break;
            }

            // Each line is a new game
            Game game = new Game();
            games.add(game);

            // The game id is before the colon and the rounds are after the colon.
            String[] gameStrings = input.split(":");

            // The game id is the second value in the list of strings before the colon
            game.id = Integer.valueOf(gameStrings[0].trim().split(" ")[1]);

            // Each round is separated by a semicolon
            String[] roundStrings = gameStrings[1].split(";");

            for (String roundString : roundStrings) {

                // Each round is a cube set
                CubeSet cubeSet = new CubeSet();
                game.rounds.add(cubeSet);

                // The individual cube colours and counts are separated by a comma.
                String[] cubeStrings = roundString.trim().split(",");

                for (String cubeString : cubeStrings) {

                    String[] cubeDefinitionStrings = cubeString.trim().split(" ");

                    int      count                 = Integer.valueOf(cubeDefinitionStrings[0].trim());
                    String   color                 = cubeDefinitionStrings[1].trim();

                    switch (color) {
                    case "red":
                        cubeSet.red = count;
                        break;
                    case "blue":
                        cubeSet.blue = count;
                        break;
                    case "green":
                        cubeSet.green = count;
                        break;
                    default:
                        System.out.println("Unknown color :" + color);
                    }
                }
            }
        }
    }

    @Override
    public void solvePart1() {

        CubeSet bag = new CubeSet();
        bag.red   = 12;
        bag.blue  = 13;
        bag.green = 14;

        int total = 0;

        for (Game game : games) {

            if (game.isPossible(bag)) {
                total += game.id;
            }
        }

        System.out.println("Total " + total);
    }

    @Override
    public void solvePart2() {

        long total = 0;

        for (Game game : games) {

            CubeSet minBag = new CubeSet();

            for (CubeSet round : game.rounds) {

                minBag.red   = Math.max(minBag.red, round.red);
                minBag.blue  = Math.max(minBag.blue, round.blue);
                minBag.green = Math.max(minBag.green, round.green);
            }

            total += minBag.getPower();
        }

        System.out.println("Total power " + total);
    }

}
