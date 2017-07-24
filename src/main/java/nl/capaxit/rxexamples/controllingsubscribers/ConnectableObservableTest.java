package nl.capaxit.rxexamples.controllingsubscribers;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * Created by jamiecraane on 05/05/2017.
 */
public class ConnectableObservableTest {
    public static void main(String[] args) {
        final Observable<Integer> observable = Observable.create(subscribe -> {
            System.out.println("Connect");
            subscribe.onNext(1);

            subscribe.setDisposable(new Disposable() {
                @Override
                public void dispose() {
                    System.out.println("Cleanup");
                }

                @Override
                public boolean isDisposed() {
                    return false;
                }
            });
        });

        System.out.println("Before subscribers");
        Disposable sub1 = observable.subscribe();
        System.out.println("Subscribed 1");
        Disposable sub2 = observable.subscribe();
        System.out.println("Subscribed 2");
        sub1.dispose();
        System.out.println("Disposed 1");
        sub2.dispose();
        System.out.println("Disposed 2");

        System.out.println();
        System.out.println("------ Lazy ------");
        // The share operator creates a lazy observable and only creates it once (so it is shared between subscribers instead of creating a backend conection each time).
        final Observable<Integer> lazy = observable.share(); // same as publish().refCount().
        System.out.println("Before subscribers");
        sub1 = lazy.subscribe();
        System.out.println("Subscribed 1");
        sub2 = lazy.subscribe();
        System.out.println("Subscribed 2");
        sub1.dispose();
        System.out.println("Disposed 1");
        sub2.dispose();
        System.out.println("Disposed 2");
    }
}
