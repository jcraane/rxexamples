package nl.capaxit.rxexamples;

import io.reactivex.Observable;

public class ErrorHandlingTest {
    public static void main(String[] args){
        Observable.defer(() -> Observable.just(createElement(1)))
                .doOnError(throwable -> System.out.println("DSJHDJKSAHKJDSha"))
                .onErrorResumeNext(Observable.empty())
                .subscribe(System.out::println);
    }


    private static String createElement(final int i) {
        if (i == 1) {
            throw new RuntimeException("ERROR");
        }
        return String.valueOf(i);
    }
}
