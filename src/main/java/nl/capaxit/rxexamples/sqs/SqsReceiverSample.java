package nl.capaxit.rxexamples.sqs;

import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SqsReceiverSample {
    private static final Random RANDOM = new Random();

    public static void main(String[] args) throws InterruptedException {
        new SqsReceiverSample().run();
    }

    private void run() throws InterruptedException {
        receive()
                .concatMap(this::handleMessages) // The concatMap makes sure the polling of the next messages starts if all the inner observables complete.
                .doOnError(Throwable::printStackTrace)
                .doOnCompleted(() -> System.out.println("COMPLETED -----------------------------------"))
                .repeat() // subscribe again to this observable when this observable completes
                .retry() // subscribe again to this onservable in case of errors
                .subscribe();

        Thread.sleep(10000);
    }

    /**
     * Simluate an SQS long-polling request which returns 0 to max messages at once.
     * @return
     */
    private Observable<List<String>> receive() {
        return Observable.fromCallable(() -> {
            System.out.println("-----------------------------------------");
            final ArrayList<String> result = new ArrayList<>(RANDOM.nextInt(8) + 1);
            for (int i = 0; i < RANDOM.nextInt(8) + 1; i++) {
                result.add(String.valueOf(i));
            }
            System.out.println("MESSAGES TO HANDLE " + result.size());
            return result;
        });
    }

    /**
     * Split the messages and handle each message using a flatMap call
     * @param messages
     * @return
     */
    private Observable<String> handleMessages(final List<String> messages) {
        return Observable.from(messages)
                .flatMap(this::handleMessage)
                .flatMap(this::deleteMessage)
                .onErrorResumeNext(Observable.empty());
    }

    /**
     * This method creates an observable which handles each message in its separate thread.
     * @param message
     * @return
     */
    private Observable<String> handleMessage(final String message) {
        return Observable.fromCallable(() -> {
            Thread.sleep(RANDOM.nextInt(1500));
            System.out.println("HANDLE: " + message);
            return message;
        }).subscribeOn(Schedulers.io());
    }

    /**
     * After processing the message make sure to delete it from the queue.
     * @param message
     * @return
     */
    private Observable<String> deleteMessage(final String message) {
        return Observable.fromCallable(() -> {
            System.out.println("DELETE: " + message);
            return message;
        });
    }
}
