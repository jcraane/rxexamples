package nl.capaxit.rxexamples.controllingsubscribers;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by jamiecraane on 15/05/2017.
 */
public class BehaviorSubjectInitialValue {
    public static void main(String[] args) {
        final BehaviorSubject<Object> subject = BehaviorSubject.create();
        final Observable<Object> initialValueSubject = subject.startWith("Hello");

        initialValueSubject.subscribe(System.out::println);
        subject.onNext("World");
        initialValueSubject.subscribe(System.out::println);
        subject.onNext("Ending");
        initialValueSubject.subscribe(System.out::println);
    }
}
