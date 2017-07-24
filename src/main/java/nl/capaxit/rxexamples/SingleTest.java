package nl.capaxit.rxexamples;

import io.reactivex.Single;

/**
 * Created by jamiecraane on 26/04/2017.
 */
public class SingleTest {
    public static void main(String[] args) {
        getDate().subscribe(System.out::println);
    }

    private static Single<String> getDate() {
        return Single.create(o -> {
            o.onSuccess("Hello");
        });
    }
}
