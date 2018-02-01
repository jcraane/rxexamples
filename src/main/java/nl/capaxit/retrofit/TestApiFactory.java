package nl.capaxit.retrofit;

import com.google.gson.GsonBuilder;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Scheduler;

import java.io.IOException;

public class TestApiFactory {
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

    public static PlayerApi getPlayerApi(final String baseUrl, final Scheduler scheduler) {
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
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build().create(PlayerApi.class);
    }
}
