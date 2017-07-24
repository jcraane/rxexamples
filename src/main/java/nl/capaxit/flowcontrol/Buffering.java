package nl.capaxit.flowcontrol;

import com.google.gson.Gson;
import io.reactivex.subjects.BehaviorSubject;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Buffering {
    private static final Random RANDOM = new Random();

    public static void main(String[] args) throws InterruptedException {

        final BehaviorSubject<Integer> subject = BehaviorSubject.create();

        final Gson gson = new Gson();
        subject
                .buffer(1, TimeUnit.SECONDS)
                .map(list -> gson.toJson(list))
                .subscribe(System.out::println);


        for (int i = 0; i < 10000; i++) {
            Thread.sleep(RANDOM.nextInt(6) + 6);
            subject.onNext(i);
        }

/*
        Observable.range(0, 10000)
                .concatMap(r -> Observable.just(r).delay(RANDOM.nextInt(6) + 6, TimeUnit.MILLISECONDS))
                .buffer(1, TimeUnit.SECONDS)
                .subscribe(System.out::println);
*/
    }
}
