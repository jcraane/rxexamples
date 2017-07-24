package nl.capaxit.rxexamples.controllingsubscribers;

import io.reactivex.Observable;
import io.reactivex.subjects.ReplaySubject;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by jamiecraane on 05/05/2017.
 */
public class ReplaySubjectTest {
    public static void main(String[] args) throws InterruptedException {
        final WeatherService weatherService = new WeatherService();

        weatherService
                .toObservable()
                .subscribe(System.out::println);

        Thread.sleep(3000);

//        new subscribers to ReplaySubject see the last x events emitted when the ReplaySubject is created with createWithSize. Timerange is also possible.
        // Beware with unbounded ReplaySubject or you might run out of memory.
        weatherService
                .toObservable()
                .subscribe(System.out::println);

        Thread.sleep(3000);
    }

//    Hot observable.
    private static class WeatherService {
        private final ReplaySubject<Integer> subject = ReplaySubject.createWithSize(3);

        private static final Random RANDOM = new Random();

        public WeatherService() {
//            simulate continuous stream of weather data.
            Observable.interval(0, 1, TimeUnit.SECONDS).
                    subscribe((interval) -> {
                        final int temperature = RANDOM.nextInt(30);
                        System.out.println("backend temp = " + temperature);
                        subject.onNext(temperature);
                    });
        }

        public Observable<Integer> toObservable() {
            return subject;
        }
    }
}
