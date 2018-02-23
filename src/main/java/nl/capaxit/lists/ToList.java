package nl.capaxit.lists;

import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.Arrays;

public class ToList {
    public static void main(String[] args) throws InterruptedException {
        Observable.defer(() -> Observable.just("A").observeOn(Schedulers.io()))
                .concatWith(Observable.from(Arrays.asList("B", "C", "D")))
                .toList()
                .doOnNext(System.out::println)
                .subscribe(System.out::println);

        Thread.sleep(1000);
    }
}
