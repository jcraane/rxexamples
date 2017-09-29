package nl.capaxit.rxexamples.imagescaling;

import java.awt.image.BufferedImage;

public class ImageResult {
    private final BufferedImage image;

    public ImageResult(final BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }
}
