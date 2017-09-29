package nl.capaxit.rxexamples.imagescaling;

import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.byteSources.ByteSource;
import org.apache.sanselan.common.byteSources.ByteSourceFile;
import org.apache.sanselan.formats.jpeg.JpegImageParser;
import org.apache.sanselan.formats.jpeg.segments.UnknownSegment;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

/**
 * {@link} http://stackoverflow.com/questions/8118712/java-cmyk-to-rgb-with-profile-output-is-too-dark/12132556#12132556
 *
 * This class is thread safe.
 *
 * Created by jamiecraane on 16/12/15.
 */
public final class JpegReader {
    private static final int COLOR_TYPE_CMYK = 2;
    private static final int COLOR_TYPE_YCCK = 3;

    public BufferedImage readImage(final InputStream originalStream, final String tempFolder) throws IOException, ImageReadException {
        final int colorType;
        final boolean hasAdobeMarker;
        final File tempFile = createTempFileName(originalStream, tempFolder);

        try {
            final ImageInputStream stream = ImageIO.createImageInputStream(tempFile);
            final Iterator<ImageReader> iter = ImageIO.getImageReaders(stream);
            if (iter.hasNext()) {
                final ImageReader reader = iter.next();
                reader.setInput(stream);

                BufferedImage image;
                final ICC_Profile profile;
                try {
                    image = reader.read(0);
                } catch (final IIOException e) {
                    final AdobeInfo adobeInfo = checkAdobeMarker(tempFile);
                    colorType = adobeInfo.colorType;
                    hasAdobeMarker = adobeInfo.hasAdobeMarker;
                    profile = Sanselan.getICCProfile(tempFile);
                    final WritableRaster raster = (WritableRaster) reader.readRaster(0, null);
                    if (colorType == COLOR_TYPE_YCCK) {
                        convertYcckToCmyk(raster);
                    }
                    if (hasAdobeMarker) {
                        convertInvertedColors(raster);
                    }
                    image = convertCmykToRgb(raster, profile);
                }

                return image;
            }

            return null;
        } finally {
            //noinspection ResultOfMethodCallIgnored
            tempFile.delete();
        }
    }

    private static File createTempFileName(final InputStream inputStream, final String tempFolder) throws IOException {
        final String tempFileName = tempFolder + '/' + UUID.randomUUID();
        final File targetFile = new File(tempFileName);

        try (OutputStream os = new FileOutputStream(tempFileName)) {
            final byte[] b = new byte[2048];
            int length;

            while ((length = inputStream.read(b)) != -1) {
                os.write(b, 0, length);
            }

            inputStream.close();
        }

        return targetFile;
    }

    private AdobeInfo checkAdobeMarker(final File file) throws IOException, ImageReadException {
        final JpegImageParser parser = new JpegImageParser();

        final ByteSource byteSource = new ByteSourceFile(file);
        @SuppressWarnings("rawtypes") final ArrayList segments = parser.readSegments(byteSource, new int[]{0xffee}, true);
        if (segments != null && segments.size() >= 1) {
            final UnknownSegment app14Segment = (UnknownSegment) segments.get(0);
            final byte[] data = app14Segment.bytes;
            if (data.length >= 12 && data[0] == 'A' && data[1] == 'd' && data[2] == 'o' && data[3] == 'b' && data[4] == 'e') {
                final int transform = app14Segment.bytes[11] & 0xff;
                if (transform == 2) {
//                    colorType = COLOR_TYPE_YCCK;
                    return new AdobeInfo(true, COLOR_TYPE_YCCK);
                } else {
                    return new AdobeInfo(true, COLOR_TYPE_CMYK);
                }
            }
        }

        return new AdobeInfo(false, COLOR_TYPE_CMYK);
    }

    private static void convertYcckToCmyk(final WritableRaster raster) {
        final int height = raster.getHeight();
        final int width = raster.getWidth();
        final int stride = width * 4;
        final int[] pixelRow = new int[stride];
        for (int h = 0; h < height; h++) {
            raster.getPixels(0, h, width, 1, pixelRow);

            for (int x = 0; x < stride; x += 4) {
                int y = pixelRow[x];
                final int cb = pixelRow[x + 1];
                final int cr = pixelRow[x + 2];

                int c = (int) (y + 1.402 * cr - 178.956);
                int m = (int) (y - 0.34414 * cb - 0.71414 * cr + 135.95984);
                y = (int) (y + 1.772 * cb - 226.316);

                if (c < 0) {
                    c = 0;
                } else if (c > 255) {
                    c = 255;
                }
                if (m < 0) {
                    m = 0;
                } else if (m > 255) {
                    m = 255;
                }
                if (y < 0) {
                    y = 0;
                } else if (y > 255) {
                    y = 255;
                }

                pixelRow[x] = 255 - c;
                pixelRow[x + 1] = 255 - m;
                pixelRow[x + 2] = 255 - y;
            }

            raster.setPixels(0, h, width, 1, pixelRow);
        }
    }

    private static void convertInvertedColors(final WritableRaster raster) {
        final int height = raster.getHeight();
        final int width = raster.getWidth();
        final int stride = width * 4;
        final int[] pixelRow = new int[stride];
        for (int h = 0; h < height; h++) {
            raster.getPixels(0, h, width, 1, pixelRow);
            for (int x = 0; x < stride; x++) {
                pixelRow[x] = 255 - pixelRow[x];
            }
            raster.setPixels(0, h, width, 1, pixelRow);
        }
    }

    private static BufferedImage convertCmykToRgb(final Raster cmykRaster, ICC_Profile cmykProfile) throws IOException {
        if (cmykProfile == null) {
            cmykProfile = ICC_Profile.getInstance(JpegReader.class.getResourceAsStream("/ISOcoated_v2_300_eci.icc"));
        }
        final ICC_ColorSpace cmykCS = new ICC_ColorSpace(cmykProfile);
        final BufferedImage rgbImage = new BufferedImage(cmykRaster.getWidth(), cmykRaster.getHeight(), BufferedImage.TYPE_INT_RGB);
        final WritableRaster rgbRaster = rgbImage.getRaster();
        final ColorSpace rgbCS = rgbImage.getColorModel().getColorSpace();
        final ColorConvertOp cmykToRgb = new ColorConvertOp(cmykCS, rgbCS, null);
        cmykToRgb.filter(cmykRaster, rgbRaster);
        return rgbImage;
    }

    private static class AdobeInfo {
        private final boolean hasAdobeMarker;
        private final int colorType;

        private AdobeInfo(final boolean hasAdobeMarker, final int colorType) {
            this.hasAdobeMarker = hasAdobeMarker;
            this.colorType = colorType;
        }
    }
}
