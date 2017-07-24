package nl.capaxit.retrofit;

import com.google.gson.GsonBuilder;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Scheduler;
import rx.Subscription;
import rx.schedulers.Schedulers;

import java.io.IOException;

public class RetrofitTest {
    public static void main(String[] args) throws IOException {
        final MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.enqueue(new MockResponse().setBody("{\"message\": \"Hallo\"}").setResponseCode(200));
        mockWebServer.start();
        final TestApi api = getApi(mockWebServer.url("/").toString(), Schedulers.immediate());
        final Subscription subscription = api.getMessage()
                .subscribe(System.out::println,
                        Throwable::printStackTrace);

//        when result is delivered, subscription is unsubscribed. In Android, best to unsubscribe in lifecycle methods since
        // the request does not always complete (for example on orientation change, back button etc.)
        System.out.println(subscription.isUnsubscribed());

        mockWebServer.shutdown();
    }

    public static TestApi getApi(final String baseUrl, final Scheduler scheduler) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(scheduler))
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build().create(TestApi.class);
    }

}
