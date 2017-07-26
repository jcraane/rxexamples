package nl.capaxit.flowcontrol;

import rx.Observable;

public class BackendService {
    public static Observable<String> getResponse(final boolean throwError) {
        return Observable.fromCallable(() -> {
            if (throwError) {
                throw new RuntimeException("An error occurred");
            }

            return "Hello World";
        });
    }
}
