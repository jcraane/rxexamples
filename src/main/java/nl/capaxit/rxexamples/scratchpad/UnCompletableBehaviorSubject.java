package nl.capaxit.rxexamples.scratchpad;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class UnCompletableBehaviorSubject {
    public static void main(String[] args) {
        final BehaviorSubject<Object> subject = BehaviorSubject
                .create();

        final Observable<Object> wrappedError = subject
                .doOnError(throwable -> System.out.println("An error occurred"));

        subject.onNext("Init");
        wrappedError.subscribe(System.out::println, throwable -> System.out.println("An error occurred"));
        subject.onNext("Hello");
        subject.onNext("World");
        subject.onError(new RuntimeException("Error"));

        subject.onNext("After error");
    }
}
