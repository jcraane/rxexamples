package nl.capaxit.rxexamples;

import io.reactivex.Observable;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class TimeoutTest {
    private static final Random RANDOM = new Random();

    public static void main(String[] args) throws InterruptedException {
        final Observable<String> delayed_1 = createDelayed(2000, "Hello 1");
        final Observable<String> delayed_2 = createDelayed(3000, "Hello 2");
        final Observable<String> delayed_3 = createDelayed(4000, "Hello 3");

        // Here we get a timeout because the timeout is less than the total time of all observables.
        delayed_1
                .flatMap(s -> delayed_2)
                .flatMap(s -> delayed_3)
                .timeout(5000, TimeUnit.MILLISECONDS)
                .subscribe(System.out::println, System.out::println);

        final Observable<String> delayed_timeout_1 = createDelayed(2000, "Hello 1").timeout(3500, TimeUnit.MILLISECONDS);
        final Observable<String> delayed_timeout_2 = createDelayed(3000, "Hello 2").timeout(3500, TimeUnit.MILLISECONDS);
        final Observable<String> delayed_timeout_3 = createDelayed(4000, "Hello 3").timeout(3500, TimeUnit.MILLISECONDS);;

        //        Here, we get a timeout because the delayed_timeout_3 does not respond in time.
        delayed_timeout_1
                .flatMap(s -> delayed_timeout_2)
                .flatMap(s -> delayed_timeout_3)
                .subscribe(System.out::println, System.out::println);

        Thread.sleep(20000);
    }

    private static Observable<String> createDelayed(final int millis, final String value) {
        return Observable.just(value).delay(millis, TimeUnit.MILLISECONDS);
    }
}
