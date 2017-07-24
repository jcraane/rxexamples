package nl.capaxit.rxexamples.scratchpad;

import io.reactivex.Observable;

import java.util.Arrays;
import java.util.List;

public class ListTest {
    public static void main(String[] args) {
        final List<String> list = Arrays.asList("Hallo", "Sukkels", "Mooi", "Weert");
        Observable.just(list)
                .flatMapIterable(i -> i)
                .filter(i -> i.startsWith("M"))
                .toList()
                .subscribe(System.out::println);

    }
}
