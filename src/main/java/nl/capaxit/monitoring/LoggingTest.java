package nl.capaxit.monitoring;

import io.reactivex.Observable;

public class LoggingTest {
    public static void main(String[] args) {
        Observable.just("Hello World")
                .doOnNext(s -> System.out.println("s = " + s))
                .doOnNext(s -> System.out.println("Next logging"))
                .subscribe();
    }
}
