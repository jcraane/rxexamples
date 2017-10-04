package nl.capaxit.rxexamples.imagescaling;

import com.google.common.io.Files;
import org.imgscalr.Scalr;
import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.List;

public class RxImageScaler {
    public Observable<ImageResult> scaleAllImagesForAllIdentifiers(
            final List<String> images,
            final int desiredHeight,
            final String outputDir) {
        return Observable.from(ScalingSpecification.getIdentifiers())
                .concatMap(identifier ->
                        Observable.from(images)
                                .flatMap(image -> getImage(image, desiredHeight, identifier, outputDir)
                                        .doOnError(throwable -> System.out.println("Error scaling: " + image))
                                        .onErrorResumeNext(Observable.empty()))
                );
    }

    public Observable<ImageResult> scaleAllImagesForAllIdentifiers(
            final List<String> images,
            final int desiredHeight,
            final String outputDir,
            final Scheduler scheduler) {
        return Observable.from(ScalingSpecification.getIdentifiers())
                .flatMap(identifier ->
                        Observable.from(images)
                                .flatMap(image -> getImage(image, desiredHeight, identifier, outputDir, scheduler))
                );
    }

    public Observable<ImageResult> getImage(final String name,
                                            final int desiredHeight,
                                            final String identifier,
                                            final String outputDir) {
        return Observable.concat(
                getImageFromDisk(name, identifier, outputDir).doOnNext(r -> System.out.println(String.format("From disk -> %s for %s", name, identifier))),
                scaleImage(name, desiredHeight, identifier, outputDir).doOnNext(r -> System.out.println(String.format("Scale -> %s for %s", name, identifier)))
        )
                .take(1);
    }

    public Observable<ImageResult> getImage(final String name,
                                            final int desiredHeight,
                                            final String identifier,
                                            final String outputDir,
                                            final Scheduler scheduler) {
        return Observable.concat(
                getImageFromDisk(name, identifier, outputDir, scheduler).doOnNext(r -> System.out.println(String.format("From disk -> %s for %s", name, identifier))),
                scaleImage(name, desiredHeight, identifier, outputDir, scheduler).doOnNext(r -> System.out.println(String.format("Scale -> %s for %s", name, identifier)))
        )
                .take(1);
    }

    public Observable<ImageResult> getImageFromDisk(final String name,
                                                    final String identifier,
                                                    final String outputDir) {
        return getImageFromDisk(name, identifier, outputDir, Schedulers.io());
    }

    public Observable<ImageResult> getImageFromDisk(final String name,
                                                    final String identifier,
                                                    final String outputDir,
                                                    final Scheduler scheduler) {
        return Observable.defer(() -> {
            final Double mutiplier = ScalingSpecification.getMultiplier(identifier);
            final File imageFile = FileHelper.getImageFile(name, mutiplier, outputDir);
            if (imageFile.exists()) {
                try {
                    return Observable.just(new ImageResult(ImageIO.read(imageFile), Files.getFileExtension(name)));
                } catch (Exception e) {
                    return Observable.error(e);
                }
            }

            return Observable.empty();
        })
                .subscribeOn(scheduler);
    }

    public Observable<ImageResult> scaleImage(
            final String name,
            final int desiredHeight,
            final String identifier,
            final String outputDir) {
        return scaleImage(name, desiredHeight, identifier, outputDir, Schedulers.computation());
    }

    public Observable<ImageResult> scaleImage(
            final String name,
            final int desiredHeight,
            final String identifier,
            final String outputDir,
            final Scheduler scheduler) {
        return Observable.fromCallable(() -> {
            try (final InputStream inputStream = getClass().getResourceAsStream(name)) {
                final JpegReader jpegReader = new JpegReader();
                final BufferedImage image = jpegReader.readImage(inputStream, System.getProperty("java.io.tmpdir"));
                final Double mutiplier = ScalingSpecification.getMultiplier(identifier);
                final double height = desiredHeight * mutiplier;
                final BufferedImage resizedImage = Scalr.resize(image, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_HEIGHT, (int) height);
                final File imageFile = FileHelper.getImageFile(name, mutiplier, outputDir);
                final String format = Files.getFileExtension(name);
                ImageIO.write(resizedImage, format, imageFile);
                return new ImageResult(image, format);
            }

        })
                .subscribeOn(scheduler);
    }
}
