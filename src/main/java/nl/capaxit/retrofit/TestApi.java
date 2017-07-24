package nl.capaxit.retrofit;

import retrofit2.http.GET;
import rx.Observable;

public interface TestApi {
    @GET("/api")
    Observable<Message> getMessage();
}
