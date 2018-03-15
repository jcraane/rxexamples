package nl.capaxit.rxexamples;


import io.reactivex.Observable;

import java.util.Arrays;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        final List<Integer> numbers = Arrays.asList(1, 0, 0, 1, 1, 0, 1, 0, 0, 1, 1, 1, 0, 0);
        Observable.fromIterable(numbers)
                .count()
                .subscribe(System.out::println);
    }
}
