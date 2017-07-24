package nl.capaxit.rxexamples;

import io.reactivex.Observable;

import java.util.Arrays;
import java.util.List;

/**
 * Created by jamiecraane on 28/04/2017.
 */
public class CreatingObservables {
    public static void main(String[] args) {
        final String and = "And";
        final List<String> strings = Arrays.asList("Hello", "World", and, "Moon");
        Observable.fromIterable(strings)
                .filter(s -> and.equals(s))
                .take(1) // Automically unsubsubcribes
                .doOnNext(s -> System.out.println("Action on Next"))
                .subscribe(System.out::println);
    }
}
