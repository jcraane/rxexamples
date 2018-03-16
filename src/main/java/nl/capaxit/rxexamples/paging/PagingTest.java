package nl.capaxit.rxexamples.paging;

import io.reactivex.Observable;
import io.reactivex.functions.Predicate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PagingTest {
    public static void main(String[] args) {
        System.out.println("takeUntil");
        getResultsTillConditionUsingTakeUntil();
        System.out.println("Take");
        getResultsTillConditionUsingTake();
        System.out.println("Paging");
        getNextPagesUntilNoMorePagesExist()
                .subscribe(System.out::println);
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

    private static Observable<Result> getNextPagesUntilNoMorePagesExist() {
        return Observable.range(0, Integer.MAX_VALUE)
                .concatMap(page -> getResultResponse(page))
                .takeWhile(result -> !result.result.isEmpty());
    }

    private static Observable<Result> getResultResponse(final int page) {
        if (page == 0 || page == 1) {
            return Observable.just(new Result(Arrays.asList("Valid", "Valid", "Valid"), true));
        }

        if (page == 2) {
            return Observable.just(new Result(Arrays.asList("Valid", "Valid", "Valid"), false));
        }

        return Observable.just(new Result(Collections.emptyList(), false));
    }

    private static class Result {
        List<String> result;
        boolean hasMoreResults;

        public Result(final List<String> result, final boolean hasMoreResults) {
            this.result = result;
            this.hasMoreResults = hasMoreResults;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Result{");
            sb.append("result=").append(result);
            sb.append(", hasMoreResults=").append(hasMoreResults);
            sb.append('}');
            return sb.toString();
        }
    }
}
