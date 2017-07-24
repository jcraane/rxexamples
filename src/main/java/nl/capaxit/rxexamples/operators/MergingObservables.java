package nl.capaxit.rxexamples.operators;

import io.reactivex.Observable;
import io.reactivex.internal.operators.observable.ObservableError;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by jamiecraane on 05/05/2017.
 */
public class MergingObservables {
    private static final Random RANDOM = new Random();

    public static void main(String[] args) throws InterruptedException {
        final CarPhoto carPhoto = new CarPhoto();
        fastAlgo(carPhoto)
                .subscribe(System.out::println);
        preciseAlgo(carPhoto)
                .subscribe(System.out::println);
        experimentalAlgo(carPhoto)
                .subscribe(System.out::println);

        System.out.println("Combine algorithms");
        Observable.merge(
                preciseAlgo(carPhoto),
                fastAlgo(carPhoto),
                experimentalAlgo(carPhoto))
                .subscribe(System.out::println);
    }

    private static Observable<LicensePlate> fastAlgo(final CarPhoto carPhoto) throws InterruptedException {
        return Observable.fromCallable(() -> new LicensePlate("fast", 50));
    }

    private static Observable<LicensePlate> preciseAlgo(final CarPhoto carPhoto) throws InterruptedException {
        return Observable.fromCallable(() -> new LicensePlate("precise", 800));
    }

    private static Observable<LicensePlate> experimentalAlgo(final CarPhoto carPhoto) throws InterruptedException {
        return Observable.fromCallable(() -> new LicensePlate("experimental", 250));
    }

    private static class LicensePlate {
        String id;

        public LicensePlate(final String algo, final long delay) {
            System.out.println("Create " + algo);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                // Ignore
            }
            id = String.valueOf(RANDOM.nextInt(1000));
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("LicensePlate{");
            sb.append("id='").append(id).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    private static class CarPhoto {
    }
}
