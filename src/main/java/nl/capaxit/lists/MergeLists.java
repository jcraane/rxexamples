package nl.capaxit.lists;

import io.reactivex.Observable;

import java.util.Arrays;
import java.util.List;

public class MergeLists {
    public static void main(String[] args) {
        final List<Passenger> preFilledPassengers = Arrays.asList(
                new Passenger(true, "a"),
                new Passenger(true, "b"),
                new Passenger(false, "c"));

        final List<Passenger> basketPassengers = Arrays.asList(
                new Passenger(true, ""),
                new Passenger(true, ""),
                new Passenger(true, ""),
                new Passenger(false, ""),
                new Passenger(false, ""));

        final Observable<Passenger> preFilledObs = Observable.fromIterable(preFilledPassengers);
        final Observable<Passenger> basketObs = Observable.fromIterable(basketPassengers);

        Observable.concat(
                Observable.concat(
                        preFilledObs
                                .filter(Passenger::isAdult)
                                .take(basketObs.filter(Passenger::isAdult).count().blockingGet()),
                        basketObs
                                .filter(Passenger::isAdult)
                                .skip(preFilledObs.filter(Passenger::isAdult).count().blockingGet())
                ),
                Observable.concat(
                        preFilledObs
                                .filter(Passenger::isChild)
                                .take(basketObs.filter(Passenger::isChild).count().blockingGet()),
                        basketObs
                                .filter(Passenger::isChild)
                                .skip(preFilledObs.filter(Passenger::isChild).count().blockingGet())
                )
        )
        .subscribe(System.out::println);
    }

    private static class Passenger {
        boolean adult;
        String name;

        public Passenger(final boolean adult, final String name) {
            this.adult = adult;
            this.name = name;
        }

        public boolean isAdult() {
            return adult;
        }

        public boolean isChild() {
            return !isAdult();
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Passenger{");
            sb.append("adult=").append(adult);
            sb.append(", name='").append(name).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

}
