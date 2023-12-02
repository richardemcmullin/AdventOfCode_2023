package template;

import java.util.Scanner;

public abstract class AdventOfCodeTemplate {

	public static boolean debug = false;

	public Scanner scanner = new Scanner(System.in);

	/**
	 * Read and parse all of the input using the scanner class
	 */
	public abstract void readInput();

	/**
	 * This is the main solve method.  All classes that
	 * extend AdventOfCodeSolver should call this method.
	 */
	public void solve() {

		System.out.println("Paste puzzle input and press Enter to enter a blank line");

		readInput();

		System.out.println("\nSolve Part 1");

		long start = System.currentTimeMillis();

		solvePart1();

		long end = System.currentTimeMillis();

		System.out.println("solved in " + (end-start) + "ms");


		System.out.println("\n\nSolve Part 2");

		start = System.currentTimeMillis();

		solvePart2();

		end = System.currentTimeMillis();

		System.out.println("solved in " + (end-start) + "ms");
	}

	/**
	 * Solve part one of the puzzle and print
	 * the result using System.out.println()
	 */
	public abstract void solvePart1();

	/**
	 * Solve part two of the puzzle and print
	 * the result using System.out.println()
	 */
	public abstract void solvePart2();

}
