package nl.capaxit.rxexamples.scratchpad;

import rx.Observable;

import java.util.concurrent.TimeUnit;

public class EventCountingTimeWindow {
    public static void main(String[] args) throws InterruptedException {
        Observable.range(0, 10)
                .concatMap(i -> Observable.just(i).delay(300, TimeUnit.MILLISECONDS))
                .buffer(3000, TimeUnit.MILLISECONDS, 2)
                .filter(clicks -> clicks.size() >= 2)
                .subscribe(System.out::println);

        Thread.sleep(5000);
    }
}
