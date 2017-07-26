package nl.capaxit.rxexamples;

import rx.Observable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

public class SwitchingObservables {
    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        final EnumSet<Type> types = EnumSet.of(Type.CITY, Type.POI, Type.BUSINESS);
        final ArrayList<Observable<List<Location>>> locationObservables = new ArrayList<>(types.size());
        types.forEach(type -> locationObservables.add(createLocationsObservable(RANDOM.nextInt(3), type)));

        Observable.merge(locationObservables)
                .filter(locations -> !locations.isEmpty())
                .collect(ArrayList::new, ArrayList::addAll)
                .subscribe(System.out::println);
    }

    private static Observable<List<Location>> createLocationsObservable(final int numberOfLocations, final Type type) {
        final List<Location> locations = new ArrayList<>(numberOfLocations);
        for (int i = 0; i < numberOfLocations; i++) {
            locations.add(new Location(String.valueOf(i), type));
        }
        return Observable.just(locations);
    }

    private static class Location {
        final String name;
        final Type type;

        private Location(final String name, final Type type) {
            this.name = name;
            this.type = type;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Location{");
            sb.append("name='").append(name).append('\'');
            sb.append(", type=").append(type);
            sb.append('}');
            return sb.toString();
        }
    }

    private enum Type {
        CITY, POI, BUSINESS
    }
}
