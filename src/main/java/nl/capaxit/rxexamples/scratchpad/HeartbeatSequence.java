package nl.capaxit.rxexamples.scratchpad;

import io.reactivex.Observable;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by jamiecraane on 09/05/2017.
 */
public class HeartbeatSequence {
    public static void main(String[] args) throws InterruptedException {
        final Random random = new Random();
        final Observable<Integer> randomNumberEmitter = Observable.interval(0, 500, TimeUnit.MILLISECONDS)
                .map(i -> random.nextInt(1000));

        final Observable<String> heartbeat = Observable.interval(0, 3, TimeUnit.SECONDS)
                .map(i -> "Heartbeat " + i);

        Observable.combineLatest(
                randomNumberEmitter,
                heartbeat,
                (r, h) -> new Result(r, h)
        )
                .subscribe(System.out::println);

//        randomNumberEmitter.subscribe(System.out::println);

        Thread.sleep(30000);
    }

    private static class Result {
        int i;
        String s;

        public Result(final int i, final String s) {
            this.i = i;
            this.s = s;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Result{");
            sb.append("i=").append(i);
            sb.append(", s='").append(s).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }
}
