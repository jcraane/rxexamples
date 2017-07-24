package nl.capaxit.rxexamples.operators;

import io.reactivex.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by jamiecraane on 05/05/2017.
 */
public class FlatMapTest {
    public static void main(String[] args) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            users.add(new User(i));
        }

        Observable.fromIterable(users)
                .flatMap(user -> user.loadProfile(), 10)
                .subscribe(profile -> System.out.println(profile.id));
    }

    private static class User {
        private final int i;

        public User(final int i) {
            this.i = i;
        }

        public Observable<Profile> loadProfile() {
            return Observable.fromCallable(() -> new Profile(i));
        }
    }

    private static class Profile {
        int id;

        public Profile(final int i) {
            this.id = i;
        }
    }
}
