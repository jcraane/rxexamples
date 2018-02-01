package nl.capaxit.retrofit;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface PlayerApi {
    @GET("/api/player")
    Observable<Player> getPlayer();
}
