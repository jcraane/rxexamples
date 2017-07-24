package nl.capaxit.rxexamples;

import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class Delay {
    public static void main(String[] args) throws InterruptedException {
        Observable.range(0, 10)
                .concatMap(i -> {
                    final int delay = 150 - (i * (150 / 10));
                    System.out.println("delay = " + delay);
                    return Observable.just(i).delay(delay, TimeUnit.MILLISECONDS);
                })
                .subscribe(System.out::println);

        Thread.sleep(5000);
    }
}
