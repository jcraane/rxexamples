package nl.capaxit.rxexamples.paging;

import io.reactivex.Observable;
import io.reactivex.functions.Predicate;

import java.util.Arrays;

public class PagingTest {
    public static void main(String[] args) {
        System.out.println("takeUntil");
        getResultsTillConditionUsingTakeUntil();
        System.out.println("Take");
        getResultsTillConditionUsingTake();
    }

    private static void getResultsTillConditionUsingTakeUntil() {
        Observable.range(0, 4)
                .doOnNext(page -> System.out.println("page = " + page))
                .concatMap(PagingTest::getResult)
                .filter(r -> !"Cancelled".equals(r))
                .takeUntil((Predicate<String>) "Valid"::equals)
                .subscribe(System.out::println);
    }

    private static void getResultsTillConditionUsingTake() {
        Observable.range(0, 4)
                .doOnNext(page -> System.out.println("page = " + page))
                .concatMap(PagingTest::getResult)
                .filter(r -> !"Cancelled".equals(r))
                .take(1)
                .subscribe(System.out::println);
    }

    private static Observable<String> getResult(final int page) {
        if (page == 0 || page == 1) {
            return Observable.fromIterable(Arrays.asList("Cancelled", "Cancelled", "Cancelled"));
        }

        return Observable.fromIterable(Arrays.asList("Valid", "Valid", "Valid"));
    }
}
