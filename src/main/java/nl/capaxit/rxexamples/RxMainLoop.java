package nl.capaxit.rxexamples;

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import nl.capaxit.retrofit.Player;
import nl.capaxit.retrofit.PlayerApi;
import nl.capaxit.retrofit.TestApiFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RxMainLoop {
    private final MockWebServer mockWebServer = new MockWebServer();

    private PlayerApi playerApi;
    private PublishSubject<BaseEvent> eventSubject = PublishSubject.create();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static void main(String[] args) throws Exception {
        final RxMainLoop rxMainLoop = new RxMainLoop();
        rxMainLoop.startMainLoop();
        rxMainLoop.getEventStream()
                .subscribe(System.out::println);
        Thread.sleep(10000);
        rxMainLoop.mockWebServer.shutdown();
    }

    private Observable<BaseEvent> getEventStream() {
        return eventSubject;
    }

    private void startMainLoop() throws IOException {
        setupMockWebServer();

        final List<Observable<BaseEvent>> actions = new ArrayList<>();

        final Observable<BaseEvent> time = Observable.interval(1, TimeUnit.SECONDS)
                .map(i -> new UpdateTime());

        final Observable<BaseEvent> player = Observable.interval(3, TimeUnit.SECONDS)
                .doOnNext(i -> System.out.println("Update player"))
                .switchMap(i -> playerApi.getPlayer()
                        .onErrorResumeNext(Observable.empty())
                        .doOnError(Throwable::printStackTrace)
                        .map(UpdatePlayer::new));

        actions.add(time);
        actions.add(player);

        compositeDisposable.add(
                Observable.merge(actions).subscribe(e -> eventSubject.onNext(e), t -> {})
        );
    }

    private void setupMockWebServer() throws IOException {
        mockWebServer.enqueue(new MockResponse().setBody("{\"name\": \"Jantje\"}").setResponseCode(200));
        mockWebServer.enqueue(new MockResponse().setBody("{\"name\": \"Kees\"}").setResponseCode(200));
        mockWebServer.enqueue(new MockResponse().setBody("{\"name\": \"Klaas\"}").setResponseCode(200));
        mockWebServer.start();
        playerApi = TestApiFactory.getPlayerApi(mockWebServer.url("/").toString(), rx.schedulers.Schedulers.immediate());
    }

    private static class BaseEvent {
    }

    public static class UpdatePlayer extends BaseEvent {
        Player player;

        public UpdatePlayer(final Player player) {
            this.player = player;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("UpdatePlayer{");
            sb.append("name='").append(player.name).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    public static class UpdateTime extends BaseEvent {
        LocalDateTime localDateTime;

        public UpdateTime() {
            this.localDateTime = LocalDateTime.now();
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("UpdateTime{");
            sb.append("localDateTime=").append(localDateTime);
            sb.append('}');
            return sb.toString();
        }
    }
}
