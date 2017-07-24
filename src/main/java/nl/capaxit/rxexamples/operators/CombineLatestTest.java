package nl.capaxit.rxexamples.operators;

import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

/**
 * Created by jamiecraane on 08/05/2017.
 */
public class CombineLatestTest {
    public static void main(String[] args) throws InterruptedException {
        final Observable<String> slow = Observable.interval(17, TimeUnit.MILLISECONDS).map(x -> "S" + x);
        final Observable<String> fast = Observable.interval(10, TimeUnit.MILLISECONDS).map(x -> "F" + x);

        // combines the latest events from both streams. Events are missed in this case because streams are not synchronized.
        Observable.combineLatest(
                slow,
                fast,
                (s, f) -> f + ":" + s
        ).forEach(System.out::println);

        Thread.sleep(5000);

//        Make slow the primary stream but events are only emitted when fast also emits at least one event!
        slow
                .withLatestFrom(fast, (s, f) -> s + ":" + f)
                .forEach(System.out::println);

        Thread.sleep(5000);

        // Whenever observable emits events first, the other is discarded and only events from the stream that emitted first are passed through.
        slow
                .ambWith(fast)
                .forEach(System.out::println);

        Thread.sleep(5000);
    }
}
