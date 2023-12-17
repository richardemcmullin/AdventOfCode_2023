package day06;

import java.util.ArrayList;
import java.util.List;

import template.AdventOfCodeTemplate;

public class Day06 extends AdventOfCodeTemplate {

    public static void main(String[] args) {
        new Day06().solve();
    }

    List<Integer> times     = new ArrayList<>();
    List<Integer> distances = new ArrayList<>();

    @Override
    public void readInput() {

        // First line is time
        if (!scanner.next().equals("Time:")) {
            System.out.println("Time: expected as the start of the first line.");
            return;
        }

        while (true) {

            try {
                String next = scanner.next();
                times.add(Integer.valueOf(next));
            }
            catch (Exception e) {
                break; // Distance tag
            }
        }

        for (int i = 0; i < times.size(); i++) {

            try {
                String next = scanner.next();
                distances.add(Integer.valueOf(next));
            }
            catch (Exception e) {
                break;
            }
        }

        System.out.println("Times " + times);
        System.out.println("Dists " + distances);
    }

    @Override
    public void solvePart1() {

        List<Integer> waysToWin = new ArrayList<>();

        for (int race = 0; race < times.size(); race++) {

            int raceRecordDist = distances.get(race);
            int raceTime       = times.get(race);

            int raceWaysToWin  = 0;

            for (int accelerationTime = 1; accelerationTime < raceTime - 1; accelerationTime++) {

                int speed = accelerationTime;
                int dist  = speed * (raceTime - accelerationTime);

                if (dist > raceRecordDist) {
                    raceWaysToWin++;
                }
            }
            waysToWin.add(raceWaysToWin);
        }

        System.out.println(waysToWin);

        int total = 1;

        for (int raceWaysToWin : waysToWin) {
            total *= raceWaysToWin;
        }

        System.out.println("Total " + total);
    }

    @Override
    public void solvePart2() {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < times.size(); i++) {
            sb.append(times.get(i));
        }

        long raceTime = Long.parseLong(sb.toString());

        sb.setLength(0);

        for (int i = 0; i < times.size(); i++) {
            sb.append(distances.get(i));
        }

        long raceRecordDist = Long.parseLong(sb.toString());

        long waysToWin      = 0;

        for (long accelerationTime = 1; accelerationTime < raceTime - 1; accelerationTime++) {

            long speed = accelerationTime;
            long dist  = speed * (raceTime - accelerationTime);

            if (dist > raceRecordDist) {
                waysToWin++;
            }
        }

        System.out.println("Total " + waysToWin);
    }

}
