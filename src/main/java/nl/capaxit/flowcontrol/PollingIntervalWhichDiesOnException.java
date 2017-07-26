package nl.capaxit.flowcontrol;

import rx.Observable;

import java.util.concurrent.TimeUnit;

public class PollingIntervalWhichDiesOnException {
    public static void main(String[] args) throws InterruptedException {
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
