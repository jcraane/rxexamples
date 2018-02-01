package nl.capaxit.retrofit;

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.io.IOException;

public class RetroFitCacheRequestTest {
    public static void main(String[] args) throws IOException {
        final MockWebServer mockWebServer = new MockWebServer();
        // Response for the first three requests
        mockWebServer.enqueue(new MockResponse().setBody("{\"message\": \"Hallo\"}").setResponseCode(200));
        mockWebServer.enqueue(new MockResponse().setBody("{\"message\": \"Hallo\"}").setResponseCode(200));
        mockWebServer.enqueue(new MockResponse().setBody("{\"message\": \"Hallo\"}").setResponseCode(200));

        // Response for the fourth request
        mockWebServer.enqueue(new MockResponse().setBody("{\"message\": \"Hallo\"}").setResponseCode(200));
        mockWebServer.start();
        final TestApi api = TestApiFactory.getApi(mockWebServer.url("/").toString(), Schedulers.immediate());

        // When no sharing of the observable takes place, three requests are executed here.
        api.getMessage().serialize().subscribe(System.out::println);
        api.getMessage().serialize().subscribe(System.out::println);
        api.getMessage().serialize().subscribe(System.out::println);

        // Now, only one network request is made and result of the first one is shared/replayed to the other subscribers.
        // Without the autoconnect to have to cast to AutoConnectableObservable and manually call connect() on it when all subsribers are subsribed (that might be desirable in some cases).
        final Observable<Message> messageObservable = api.getMessage().share().replay().autoConnect();
        messageObservable.serialize().subscribe(System.out::println);
        messageObservable.serialize().subscribe(System.out::println);
        messageObservable.serialize().subscribe(System.out::println);

        mockWebServer.shutdown();
    }

}
