package nl.capaxit.rxexamples.operators;

import io.reactivex.Observable;

import java.util.Arrays;
import java.util.List;

/**
 * Created by jamiecraane on 08/05/2017.
 */
public class RangeReduceAndAccumulateTest {
    public static void main(String[] args) {
//        The scan operator produces all intermediate results
        System.out.println("Scan");
        final List<Integer> numbers = Arrays.asList(1, 5, 4, 7, 30, 15, 3);
        Observable.fromIterable(numbers)
                .scan((total, chunk) -> total + chunk)
                .subscribe(System.out::println);

        //        while the reduce function only outputs the total result
        System.out.println("Reduce");
        Observable.fromIterable(numbers)
                .reduce((a, b) -> a + b)
                .subscribe(System.out::println);

//        Collect all elements in a mutable container (in this case StringBuilder)
        System.out.println("Collect");
        Observable.fromIterable(numbers)
                .collect(StringBuilder::new, (sb, n) -> sb.append(n))
                .map(StringBuilder::toString)
                .subscribe(System.out::println);
    }
}
