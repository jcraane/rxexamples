package nl.capaxit.rxexamples.controllingsubscribers;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by jamiecraane on 05/05/2017.
 */
public class PublishSubjectTest {
    public static void main(String[] args) throws InterruptedException {
        final WeatherService weatherService = new WeatherService();

        weatherService
                .toObservable()
                .subscribe(System.out::println);

        Thread.sleep(3000);

//        new subscribers to PublishSubject only see new emitted events. PublishSubject does not cache passed events..
        weatherService
                .toObservable()
                .subscribe(System.out::println);

        Thread.sleep(3000);
    }

    //    Hot observable.
    private static class WeatherService {
        private final PublishSubject<Integer> subject = PublishSubject.create();

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
