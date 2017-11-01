package nl.capaxit.rxexamples;

import io.reactivex.Observable;

public class CompletingObservables {
    public static void main(String[] args) {
        final Observable<String> obs = Observable.fromCallable(() -> {
            throw new RuntimeException("dsds");
        });

        obs
                .doOnError(Throwable::printStackTrace)
                .doOnComplete(() -> System.out.println("complete"))
                .subscribe(
                        System.out::println,
                        throwable -> System.out.println("Error"));
    }
}
