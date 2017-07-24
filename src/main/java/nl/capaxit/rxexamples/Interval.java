package nl.capaxit.rxexamples;

import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

/**
 * Created by jamiecraane on 28/04/2017.
 */
public class Interval {
    public static void main(String[] args) throws InterruptedException {
        final Observable<Long> intervalObservable = Observable.interval(1, TimeUnit.SECONDS);

        // interval is a 'cold' observable. Every subscriber gets the same sequence of values.
        intervalObservable.subscribe(System.out::println);
        intervalObservable.subscribe(System.out::println);
        intervalObservable.subscribe(System.out::println);

        Observable.interval(1, TimeUnit.SECONDS)
                .flatMap((interval) -> getData())
                .subscribe(System.out::println);

        Thread.sleep(5000);
    }

    private static Observable<String> getData() {
        return Observable.just("Hello World");
    }
}
