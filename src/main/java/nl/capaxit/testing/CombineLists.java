package nl.capaxit.testing;

import io.reactivex.Observable;

import java.util.Arrays;
import java.util.List;

public class CombineLists {
    public static void main(String[] args) {
        final Observable<Integer> nObs = Observable.fromIterable(Arrays.asList(1, 2, 3, 4));
        final Observable<String> sObs = Observable.fromIterable(Arrays.asList("a", "b", "c"));

//        Observable.merge(nObs.map(n -> String.valueOf(n)), sObs)
//                .subscribe(System.out::println);


        nObs.map(n -> String.valueOf(n))
                .zipWith(sObs, (f, s) -> Arrays.asList(f, s))
                .flatMapIterable(e -> e)
                .subscribe(System.out::println);
        /*nObs.map(n -> String.valueOf(n))
                .mergeWith(sObs)
                .subscribe(System.out::println);*/
    }
}
