package nl.capaxit.rxexamples.controllingsubscribers;

import io.reactivex.Observable;
import io.reactivex.observables.ConnectableObservable;
import sun.security.provider.certpath.SunCertPathBuilder;

/**
 * Created by jamiecraane on 05/05/2017.
 */
public class ConnectToObservableTest {
    public static void main(String[] args) {
        final ConnectableObservable<Object> observable = Observable.create(subscriber -> {
            System.out.println("Starting application");
            subscriber.onNext("Status");
        }).publish(); // Effectively puts all subscribers on hold until connect() is called. Handy for application initialization.

        observable.subscribe(System.out::println);
        System.out.println("Subscribed 1");

        observable.subscribe(System.out::println);
        System.out.println("Subscribed 2");

        System.out.println("Connect");
        observable.connect(); // connect() make sure all subscribers are actually subscribed.
    }
}
