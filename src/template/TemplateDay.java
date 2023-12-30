package template;

import java.util.ArrayList;
import java.util.List;

public class TemplateDay extends AdventOfCodeTemplate {

    public static void main(String[] args) {
        new TemplateDay().solve();
    }

    private class DayDataClass {

        DayDataClass(String input) {

            // Parse the input string for one line into some object
        }
    }

    /** List of objects created by parsing the input */
    private List<DayDataClass> dayData = new ArrayList<>();

    @Override
    public void readInput() {

        // Read the input until a blank line is encountered
        while (true) {

            String input = scanner.nextLine();

            if (input.isBlank()) {
                break;
            }

            dayData.add(new DayDataClass(input));
        }
    }

    @Override
    public void solvePart1() {

        int total = 0;

        for (DayDataClass dayDataClass : dayData) {

        }

        System.out.println("Total " + total);
    }

    @Override
    public void solvePart2() {

        System.out.println("Not yet implemented");
    }

}
