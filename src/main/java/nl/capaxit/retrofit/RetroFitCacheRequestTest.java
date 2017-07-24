package nl.capaxit.retrofit;

import com.google.gson.GsonBuilder;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Scheduler;
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
        final TestApi api = getApi(mockWebServer.url("/").toString(), Schedulers.immediate());

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

    public static TestApi getApi(final String baseUrl, final Scheduler scheduler) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(new OkHttpClient.Builder()
                        .addInterceptor(new Interceptor() {
                            @Override
                            public Response intercept(final Chain chain) throws IOException {
                                System.out.println(chain.request().method() + " " + chain.request().url().toString());
                                return chain.proceed(chain.request());
                            }
                        }).build())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(scheduler))
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build().create(TestApi.class);
    }
}
