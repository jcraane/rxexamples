package nl.capaxit.rxexamples.operators;

import io.reactivex.Observable;

import java.util.Arrays;

/**
 * Created by jamiecraane on 11/05/2017.
 */
public class MergeIterable {
    public static void main(String[] args) {
        Observable.merge(
                Observable.fromIterable(Arrays.asList(1, 2, 3)),
                Observable.fromIterable(Arrays.asList(4, 5, 6)),
                Observable.fromIterable(Arrays.asList(7, 8, 9))
        ).subscribe(System.out::println);
    }
}
