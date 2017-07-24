package nl.capaxit.connectableobservables;

import rx.Observable;
import rx.observables.ConnectableObservable;

public class SharingTest {
    public static void main(String[] args) throws InterruptedException {
        final Observable<Long> obs = Observable.fromCallable(() -> System.nanoTime());

        System.out.println("not shared");
        // printout different values
        obs.subscribe(t -> System.out.println(t));
        obs.subscribe(t -> System.out.println(t));

        final ConnectableObservable<Long> obsConnect = Observable.fromCallable(() -> System.nanoTime()).publish();

        System.out.println("shared");
        // result is shared and only outputted when connect() is called
        obsConnect.subscribe(t -> System.out.println(t));
        obsConnect.subscribe(t -> System.out.println(t));
        obsConnect.connect();

        final Observable<Long> shareObs = Observable.fromCallable(() -> System.nanoTime()).share(); // share is an alias for publish().refCount().

        System.out.println("share");
        // result is not shared since the first subsribed the refcount goes from 0 to 1 en then unsubscribes
        shareObs.subscribe(t -> System.out.println(t));
        // here the same happens so we see two diffferent results
        shareObs.subscribe(t -> System.out.println(t));

        final Observable<Long> autoConnectObs = Observable.fromCallable(() -> System.nanoTime()).share().replay().autoConnect();

        System.out.println("autoconnect");
        autoConnectObs.subscribe(t -> System.out.println(t));
        autoConnectObs.subscribe(t -> System.out.println(t));
    }
}
