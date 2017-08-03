package nl.capaxit.flowcontrol;

import rx.Observable;

import java.util.Arrays;

public class CombineElementWithPrevious {
    public static void main(String[] args) {
        final Observable<Integer> numbers = Observable.from(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 7));
        numbers
                .zipWith(numbers
                        .skip(1), (first, second) -> Arrays.asList(first, second))
                .subscribe(System.out::println);
    }
}
