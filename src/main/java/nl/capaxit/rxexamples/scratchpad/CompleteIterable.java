package nl.capaxit.rxexamples.scratchpad;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by jamiecraane on 08/05/2017.
 */
public class CompleteIterable {
    public static void main(String[] args) {
        /*final Disposable disposable = Observable.fromIterable(Arrays.asList("Hello", "World", "Jamie", "Berghem"))
                .doOnComplete(() -> System.out.println("Complete"))
                .subscribe(System.out::println);

        System.out.println("disposable.isDisposed() = " + disposable.isDisposed());*/

        Observable.empty()
                .first(0)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(final Object o) throws Exception {
                        System.out.println("DSKJDKS");
                    }
                });
    }
}
