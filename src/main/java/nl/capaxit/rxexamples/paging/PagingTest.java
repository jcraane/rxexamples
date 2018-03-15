package nl.capaxit.rxexamples.paging;

import io.reactivex.Observable;

import java.util.Arrays;

public class PagingTest {
    public static void main(String[] args) {
        Observable.range(0, 4)
                .doOnNext(page -> System.out.println("page = " + page))
                .concatMap(page -> getResult(page))
                .filter(r -> !"Cancelled".equals(r))
                .takeUntil(s -> {
                    return "Valid".equals(s);
                })
                .subscribe(System.out::println);
    }

    private static Observable<String> getResult(final int page) {
        if (page == 0 || page == 1) {
            return Observable.fromIterable(Arrays.asList("Cancelled", "Cancelled", "Cancelled"));
        }

        return Observable.fromIterable(Arrays.asList("Valid", "Valid", "Valid"));
    }
}
