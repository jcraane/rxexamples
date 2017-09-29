package nl.capaxit.rxexamples.imagescaling;

import org.apache.sanselan.ImageReadException;
import org.imgscalr.Scalr;
import rx.schedulers.Schedulers;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ImageScaling {
    public static void main(String[] args) throws IOException, ImageReadException, InterruptedException {
//        new ImageScaling().scale("logo_abnamro.png");
        new ImageScaling().scaleWithRx();
    }

    private void scaleWithRx() throws InterruptedException {
        final String folder = "/images";
//        final String folder = "/logos";
        final File imageDir = new File("src/main/resources" + folder);

        final List<String> images = Arrays.stream(imageDir.list())
                .map(image -> folder + "/" + image)
                .collect(Collectors.toList());

        final RxImageScaler scaler = new RxImageScaler();

//get single image from disk or scaling
        /*scaler
                .getImage("logo_abnamro.png", 200, "xxhdpi", "out")
                .subscribe(imageResult -> System.out.println("stream image"));*/

        System.out.println("Start");
        final long start = System.currentTimeMillis();
        scaler.scaleAllImagesForAllIdentifiers(
                images, 300, "out"
        )
                .doOnNext(result -> {
                    final long end = System.currentTimeMillis();
                    System.out.println("Current running time: " + (end - start) + " milliseconds");
                })
                .doOnCompleted(() -> {
                    final long end = System.currentTimeMillis();
                    System.out.println("Finished in:  " + (end - start) + " milliseconds");
                })
                .subscribe();

        Thread.sleep(300000);
    }
}
