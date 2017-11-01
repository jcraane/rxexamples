package nl.capaxit.rxexamples;

import io.reactivex.Completable;
import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class CompletableTest {
    public static void main(String[] args) throws InterruptedException {
//        Handy for doing background work without requiring a result.
        Completable.fromAction(() -> System.out.println("Hello")).subscribe();

        System.out.println("START");
/*
        Completable
                .fromAction(() -> Observable.timer(1, TimeUnit.SECONDS)
                        .flatMap(i -> System.out.println("DELAYED")))
                .subscribe();
*/

        Completable
                .fromAction(() -> System.out.println("DELAYED"))
                .delay(1, TimeUnit.SECONDS)
                .subscribe(() -> System.out.println("DELAYED 2"));

        Thread.sleep(2000);
    }
}
