package nl.capaxit.rxexamples.operators;

import io.reactivex.Observable;

import java.util.Random;

/**
 * Created by jamiecraane on 08/05/2017.
 */
public class DroppingDuplicatesTest {
    public static void main(String[] args) throws InterruptedException {
        final Observable<Object> randomInts = Observable.create(subscriber -> {
            final Random random = new Random();
            while (!subscriber.isDisposed()) {
                subscriber.onNext(random.nextInt(5));
            }
        });

        System.out.println("Take 10");
        randomInts
                .take(10)
                .subscribe(System.out::println);

        System.out.println("Take 5 distinct");
        System.out.println();
        randomInts
                .distinct()
                .take(5)
                .subscribe(System.out::println);

        System.out.println("Take 10 distinct until changed");
        System.out.println();
        randomInts
                .distinctUntilChanged()
                .take(10)
                .subscribe(System.out::println);
    }
}
