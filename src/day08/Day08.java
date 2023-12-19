package day08;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import template.AdventOfCodeTemplate;

public class Day08 extends AdventOfCodeTemplate {

    public static void main(String[] args) {
        new Day08().solve();
    }

    private class Node {
        final String name;
        Node         left;
        Node         right;

        Node(String name) {
            this.name = name;
        }
    }

    /** List of objects created by parsing the input */
    private Map<String, Node> nodeMap = new HashMap<>();
    char[]                    instructions;
    Node                      start   = null;
    Node                      end     = null;

    @Override
    public void readInput() {

        // First line is left/right instructions
        instructions = scanner.nextLine().toCharArray();

        // next line is blank
        scanner.nextLine();

        // Read all nodes
        // Read the input until a blank line is encountered
        while (true) {

            String input = scanner.nextLine();

            if (input.isBlank()) {
                break;
            }

            // All nodes follow the same pattern
            // AAA = (BBB, CCC)
            String nodeName      = input.substring(0, 3);
            String leftNodeName  = input.substring(7, 10);
            String rightNodeName = input.substring(12, 15);

            Node   node          = null;
            if (nodeMap.containsKey(nodeName)) {
                node = nodeMap.get(nodeName);
            }
            else {
                node = new Node(nodeName);
                nodeMap.put(nodeName, node);
            }

            // Set the left and right
            if (nodeMap.containsKey(leftNodeName)) {
                node.left = nodeMap.get(leftNodeName);
            }
            else {
                node.left = new Node(leftNodeName);
                nodeMap.put(leftNodeName, node.left);
            }

            if (nodeMap.containsKey(rightNodeName)) {
                node.right = nodeMap.get(rightNodeName);
            }
            else {
                node.right = new Node(rightNodeName);
                nodeMap.put(rightNodeName, node.right);
            }

            if (node.name.equals("AAA")) {
                start = node;
            }
            if (node.name.equals("ZZZ")) {
                end = node;
            }
        }

        // Check that the start and end are in the map
        if (start == null) {
            System.out.println("Starting node not found.");
        }
        if (end == null) {
            System.out.println("Ending node not found.");
        }
    }

    @Override
    public void solvePart1() {

        int  step    = 0;

        Node curNode = start;

        while (curNode != end) {

            char instruction = instructions[step % instructions.length];

            if (instruction == 'L') {
                curNode = curNode.left;
            }
            else {
                curNode = curNode.right;
            }

            step++;
        }

        System.out.println("Total " + step);
    }

    @Override
    public void solvePart2() {

        // Make a list of starting nodes
        List<Node> startNodes = new ArrayList<>();

        for (Node node : nodeMap.values()) {
            if (node.name.endsWith("A")) {
                startNodes.add(node);
            }
        }

        List<Long> periods = new ArrayList<>();

        // For each starting position, print the first 3 z values encountered.
        for (Node node : startNodes) {

            Node curNode = node;

            System.out.println("Starting node " + node.name);

            int  zCount    = 0;
            long lastZStep = 0;
            long step      = 0;

            long period    = 0;

            while (zCount < 3) {

                char instruction = instructions[(int) (step % instructions.length)];

                if (instruction == 'L') {
                    curNode = curNode.left;
                }
                else {
                    curNode = curNode.right;
                }

                step++;

                if (curNode.name.endsWith("Z")) {
                    period = step - lastZStep;
                    System.out.println(curNode.name + " " + step + " " + period);
                    zCount++;
                    lastZStep = step;
                }
            }

            periods.add(period);

            System.out.println();
        }

        // Visually inspect for periodic movement.
        // Find the lowest common multiple of all of the periods (assuming they are periodic)

        long totalPeriod = 1;
        for (long period : periods) {
            totalPeriod = lcm(totalPeriod, period);
        }

        System.out.println("Total Steps " + totalPeriod);
    }

    private long lcm(long value1, long value2) {

        // Calculate the least common multiple
        long lcm = value1;

        while (lcm % value2 != 0) {
            lcm += value1;
        }

        return lcm;
    }
}
