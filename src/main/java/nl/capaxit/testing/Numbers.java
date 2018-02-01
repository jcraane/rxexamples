package nl.capaxit.testing;

import java.sql.SQLSyntaxErrorException;
import java.util.Arrays;
import java.util.List;

public class Numbers {
    public static void main(String[] args) {
        final List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        final Integer first = numbers.get(0);
        final long start = System.currentTimeMillis();
        new Numbers().calculate(numbers, 0, first, "", 1);
        final long end = System.currentTimeMillis();
        System.out.println("" + (end - start));
    }

    private void calculate(List<Integer> numbers, long sum, long next, String solution, final int index) {
        if (numbers.size() == index) {
            long finalOutcome = sum + next;
            String finalSolution = solution + " + " + next;
            if (finalOutcome == 100 && !finalSolution.startsWith(" -")) {
                System.out.println("sum = " + finalOutcome + " -> solution = " + finalSolution);
            }
            finalOutcome = sum - next;
            finalSolution = solution + " - " + next;
            if (finalOutcome == 100 && !finalSolution.startsWith(" -")) {
                System.out.println("sum = " + finalOutcome + " -> solution = " + finalSolution);
            }
            finalOutcome = sum * next;
            finalSolution = solution + " * " + next;
            if (finalOutcome == 100 && !finalSolution.startsWith(" -")) {
                System.out.println("sum = " + finalOutcome + " -> solution = " + finalSolution);
            }

            return;
        }

        final Integer current = numbers.get(index);
        calculate(numbers, sum + next, current, solution + " + " + next, index + 1);
        calculate(numbers, sum - next, current, solution + " - " + next, index + 1);
        calculate(numbers, sum * next, current, solution + " * " + next, index + 1);
        final long combined = Long.parseLong("" + next + current);
        calculate(numbers, sum, combined, solution, index + 1);
    }
}
