package nl.capaxit.rxexamples.parallelprocessing;

import io.reactivex.internal.operators.observable.ObservableError;
import rx.Observable;
import rx.Scheduler;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

import java.util.Arrays;
import java.util.List;

public class ParellelZip {
    public static void main(String[] args) throws InterruptedException {
        final long start = System.currentTimeMillis();
        final Scheduler scheduler = Schedulers.io();
        Observable.zip(
                getValue("value1", scheduler),
                getValue("value1", scheduler),
                getValue("value1", scheduler),
                (a, b, c) -> Arrays.asList(a, b, c)
        )
                .subscribe(result -> {
                    final long end = System.currentTimeMillis();
                    System.out.println("result = " + result);
                    System.out.println("end = " + (end - start));
                });

        Thread.sleep(7000);
    }

    private static Observable<String> getValue(final String value, final Scheduler scheduler) {
        return Observable.defer((Func0<Observable<String>>) () -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return Observable.just(value);
        }).subscribeOn(scheduler);
    }
}
