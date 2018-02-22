package nl.capaxit.rxexamples;

import rx.Observable;

import java.util.concurrent.TimeUnit;

public class Repeat {
    public static void main(String[] args) {
        getTimeMillisObs()
                .delay(250, TimeUnit.MILLISECONDS)
                .repeat(5)
                .toBlocking().subscribe(System.out::println);

        getTimeMillisObsDefer()
                .delay(250, TimeUnit.MILLISECONDS)
                .repeat(5)
                .toBlocking().subscribe(System.out::println);
    }

    private static Observable<Long> getTimeMillisObs() {
        return Observable.just(System.currentTimeMillis());
    }

    private static Observable<Long> getTimeMillisObsDefer() {
        return Observable.defer(() -> Observable.just(System.currentTimeMillis()));
    }
}
