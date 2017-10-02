package nl.capaxit.rxexamples.imagescaling;

import java.awt.image.BufferedImage;

public class ImageResult {
    private final BufferedImage image;
    private final String format;

    public ImageResult(final BufferedImage image, final String format) {
        this.image = image;
        this.format = format;
    }

    public BufferedImage getImage() {
        return image;
    }

    public String getFormat() {
        return format;
    }
}
