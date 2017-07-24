package nl.capaxit.rxexamples.scratchpad;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

public class UnsubscribeInterval {
    public static void main(String[] args) throws InterruptedException {
        final Disposable subscribe = Observable.intervalRange(0, 1, 2, 1, TimeUnit.SECONDS)
                .subscribe(System.out::println);

        Thread.sleep(3000);
        System.out.println("isDisposed = " + subscribe.isDisposed());
    }
}
