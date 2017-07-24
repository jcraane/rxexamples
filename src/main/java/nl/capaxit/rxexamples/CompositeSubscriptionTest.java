package nl.capaxit.rxexamples;

import rx.Observable;
import rx.subscriptions.CompositeSubscription;

public class CompositeSubscriptionTest {
    public static void main(String[] args) {
        final Observable<String> hello = Observable.just("Hello");
        final Observable<String> world = Observable.just("World");

        final CompositeSubscription compositeSubscription = new CompositeSubscription();
        compositeSubscription.add(hello.subscribe(System.out::println));
        compositeSubscription.add(world.subscribe(System.out::println));

        // better to call clear than ubsubscribe since the compositesubscription can be re-used now. Calling unsubscribe you cannot re-use the compositesubscription again.
        compositeSubscription.clear();
    }
}
