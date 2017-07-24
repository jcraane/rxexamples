package nl.capaxit.rxexamples.operators;

import io.reactivex.Observable;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by jamiecraane on 05/05/2017.
 */
public class FilterTest {
    public static void main(String[] args) {
        final Random random = new Random();
        final ArrayList<Integer> numberList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            numberList.add(random.nextInt(20));
        }
        final Observable<Integer> numbers = Observable.fromIterable(numberList);

        // observables are immutable and can be re-used.
        System.out.println("Numbers gteq 10");
        numbers
                .filter(i -> i >= 10)
                .map(i -> "large number " + i)
                .subscribe(System.out::println);

        System.out.println("Numbers lt 10");
        numbers
                .filter(i -> i < 10)
                .map(i -> "small number " + i)
                .subscribe(System.out::println);
    }
}
