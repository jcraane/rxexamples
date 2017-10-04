package nl.capaxit.rxexamples.videoprocessing;

import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VideoProcessing {
    private static final BehaviorSubject<Image> webcamSubject = BehaviorSubject.create();
    private static final int IMAGE_PROCESSING_TIME = 30;
    private static final int SLEEP_VARIATION = 100;
    private static final Random RANDOM = new Random();

    public static void main(String[] args) throws InterruptedException {
        // Simulate video of 30 frames per second.
        new Thread(new Runnable() {
            @Override
            public void run() {
                long index = 0;
                while (true) {
                    webcamSubject.onNext(new Image(index));
                    index++;
                    try {
                        System.out.println("Stream image " + index); // logging om te zien met welke image de webcam bezig is
                        Thread.sleep(32); // 30fps
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        new VideoProcessing().run();
    }

    private void run() throws InterruptedException {
        // Serial processing. Consumer cannot keep up with producer.
        /*webcamSubject
                .doOnNext(image -> System.out.println("received " + image))
                .map(image -> doProcessImage(image))
                .subscribe(processedImage -> System.out.println("final image " + processedImage.image.id));*/

        // Parallel but events are delivered out of order when there are slight variations in processing time.
        /*webcamSubject
                .doOnNext(image -> System.out.println("received " + image))
                .flatMap(image -> procesImage(image))
                .subscribe(processedImage -> System.out.println("final image " + processedImage.image.id));*/

        // Parallel processing.
        webcamSubject
                .doOnNext(image -> System.out.println("received " + image))
                .buffer(8) // Process 8 images at once
                .doOnNext(System.out::println)
                .concatMap(images -> Observable.from(images) // process 8 images from buffer in serial
                        .flatMap(this::procesImage) // process each image im parallel
                        .toSortedList() // collect all process images in a list and sort based on the order the images are received from the video stream
                        .flatMapIterable(i -> i)) // emit the images as separate events downstream
                .subscribe(processedImage -> System.out.println("final image " + processedImage.image.id));

//        Parallel processing but only take the first image which is done (the fastes) out of the batch of 8 images.
        /*webcamSubject
                .doOnNext(image -> System.out.println("received " + image))
                .buffer(8) // Process 8 images at once
                .concatMap(images -> {
                    final List<Observable<ProcessedImage>> toProcess = new ArrayList<>();
                    for (final Image image : images) {
                        toProcess.add(procesImage(image));
                    }
                    return Observable.merge(toProcess)
                            .take(1);
                })
                .doOnNext(System.out::println)
                .subscribe(processedImage -> System.out.println("final image " + processedImage.image.id));*/
    }

    private Observable<ProcessedImage> procesImage(final Image image) {
        return Observable.fromCallable(() -> doProcessImage(image)).subscribeOn(Schedulers.computation());
    }

    private ProcessedImage doProcessImage(final Image image) {
        final ProcessedImage processedImage = new ProcessedImage();
        processedImage.image = image;
        try {
            System.out.println("Process image " + image.id);
            Thread.sleep(IMAGE_PROCESSING_TIME + RANDOM.nextInt(SLEEP_VARIATION));
        } catch (InterruptedException e) {
            // Ignore
        }
        return processedImage;
    }

    public static class Image {
        long id;

        public Image(final long id) {
            this.id = id;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Image{");
            sb.append("id=").append(id);
            sb.append('}');
            return sb.toString();
        }
    }

    public static class ProcessedImage implements Comparable<ProcessedImage> {
        Image image;

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("ProcessedImage{");
            sb.append("image=").append(image);
            sb.append('}');
            return sb.toString();
        }

        @Override
        public int compareTo(final ProcessedImage o) {
            if (image.id > o.image.id) {
                return 1;
            } else if (image.id < o.image.id) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}
