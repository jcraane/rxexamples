package nl.capaxit.rxexamples.scratchpad;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

public class DelayOneObservable {
    public static void main(String[] args) throws InterruptedException {
        final Observable<String> downloadObs = Observable.just("Download");

        final Observable<String> delayedDownload = Observable.just(0L)
                .delay(1500, TimeUnit.MILLISECONDS)
                .flatMap(i -> downloadObs);

        final Disposable disposable = delayedDownload.subscribe(System.out::println, throwable -> throwable.printStackTrace());
        Thread.sleep(500);
        disposable.dispose();

        Thread.sleep(10000);

    }
}
