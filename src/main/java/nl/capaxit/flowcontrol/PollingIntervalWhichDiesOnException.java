package nl.capaxit.flowcontrol;

import rx.Observable;

import java.util.concurrent.TimeUnit;

public class PollingIntervalWhichDiesOnException {
    public static void main(String[] args) throws InterruptedException {
        // if the stream in the switchMap produces an error, the interval is killed since the error event is forwared
        // to the outer steam. An error (or completed) event terminates the stream.
        Observable.interval(500, TimeUnit.MILLISECONDS)
                .switchMap(i -> BackendService.getResponse(i == 3)
                        .doOnError(throwable -> System.out.println("Error from service")))
                .onErrorResumeNext(throwable -> Observable.empty())
                .subscribe(
                        System.out::println,
                        throwable -> System.out.println("DEAD"),
                        () -> System.out.println("DEAD"));

        Thread.sleep(2500);
    }
}
