package nl.capaxit.rxexamples;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by jamiecraane on 28/04/2017.
 */
public class CreateVsFromCallable {
    public static void main(String[] args) {
        fromCreate().subscribe(System.out::println);
        fromCallable().subscribe(System.out::println);
    }

    private static Observable<String> fromCreate() {
        return Observable.create(e -> {
            try {
                e.onNext("From Create");
                e.onComplete();
            } catch (Exception e1) {
                e.onError(e1);
            }
        });
    }

    // From callable has build-in completion and wrapping in try-catch for onError propagating.
    private static Observable<String> fromCallable() {
        return Observable.fromCallable(() -> "From callable");
    }
}
