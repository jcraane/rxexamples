package nl.capaxit.testing;

import java.util.ArrayList;

public class SumNumbers {
    public static void main(String[] args) {
        /**
         *
         * Write three functions that compute the sum of the numbers in a given list using a for-loop, a while-loop, and recursion.
         *
         */

        final ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            numbers.add(i);
        }
        System.out.println(new SumNumbers().forLoop(numbers));
        System.out.println(new SumNumbers().whileLoop(numbers));
        System.out.println(new SumNumbers().recursion(numbers, 0, 0));
    }

    private int recursion(final ArrayList<Integer> numbers, final int sum, final int index) {
        if (numbers.size() == index) {
            return sum;
        }

        return recursion(numbers, sum + numbers.get(index), index + 1);
    }

    private int whileLoop(final ArrayList<Integer> numbers) {
        int index = (numbers.size() - 1), sum= 0;
        while (index > 0) {
            sum += numbers.get(index);
            index--;
        }
        return sum;
    }

    private int forLoop(final ArrayList<Integer> numbers) {
        int sum = 0;
        for (int i = 0; i < numbers.size(); i++) {
            sum += numbers.get(i);
        }
        return sum;
    }
}
