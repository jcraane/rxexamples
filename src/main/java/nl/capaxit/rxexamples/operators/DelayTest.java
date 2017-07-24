package nl.capaxit.rxexamples.operators;

import io.reactivex.Observable;
import javafx.collections.ObservableArrayBase;

import java.util.concurrent.TimeUnit;

/**
 * Created by jamiecraane on 05/05/2017.
 */
public class DelayTest {
    public static void main(String[] args) throws InterruptedException {
        Observable.just(1, 2, 3)
                .delay(500, TimeUnit.MILLISECONDS)
                .subscribe(System.out::println);

        Thread.sleep(1000);

        Observable.just("Hello", "a", "app")
                .delay(word -> Observable.timer(word.length(), TimeUnit.SECONDS))
                .subscribe(System.out::println);

        Thread.sleep(6000);

        Observable.just("Hello", "a", "app")
                .flatMap(word -> Observable.timer(word.length(), TimeUnit.SECONDS)
                        .map(x -> word))
                .subscribe(System.out::println);

        Thread.sleep(6000);
    }
}
