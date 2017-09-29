package nl.capaxit.rxexamples.imagescaling;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.io.Files;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Contains helper methods for working with file names.
 *
 * Created by jamiecraane on 28/09/15.
 */
public final class FileHelper {
    private static final Pattern PATTERN = Pattern.compile("[^A-Za-z0-9_\\-]");
    private static final int MAX_LENGTH = 127;

    private FileHelper() {
    }

    /**
     * Generates a safe filename from the given name.
     *
     * @param name The name to generate a safe filename for.
     * @return A safe filename.
     * @throws IllegalArgumentException if the specified name is null or empty.
     */
    public static String safeFileName(final String name) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "name is null or empty");

        final StringBuffer sb = new StringBuffer();

        // Apply the regex.
        final Matcher m = PATTERN.matcher(Files.getNameWithoutExtension(name));

        while (m.find()) {
            // Convert matched character to percent-encoded.
            final String replacement = "%" + Integer.toHexString(m.group().charAt(0)).toUpperCase();
            m.appendReplacement(sb, replacement);
        }
        m.appendTail(sb);

        final String encoded = sb.toString();

        // Truncate the string.
        final int end = Math.min(encoded.length(), MAX_LENGTH);
        return encoded.substring(0, end);
    }

    /**
     * Extracts a filename from the given URL.
     * @param url The URL to extract the filename from.
     * @return Filename from the URL (Part after the last /). If url is null, an empty String is returned.
     */
    public static String fileNameFromUrl(final URL url, final String qualifier) {
        if (url == null) {
            return "";
        }

        try {
            final String fileName = fileNameFromString(URLDecoder.decode(url.getPath(), "UTF-8"), qualifier).replace(" ", "-").replace("&amp;", "_").replace("'", "_").replace("&", "_");
            return replaceUntil(fileName, ".", "_", fileName.lastIndexOf("."));
        } catch (final UnsupportedEncodingException e) {
            // Ignore
            return fileNameFromString(url.getPath(), qualifier);
        }
    }

    private static String replaceUntil(final String sequence, final String oldChar, final String newChar, final int untilExclusive) {
        if (untilExclusive != -1) {
            return new StringBuilder(sequence.substring(0, untilExclusive - 1).replace(oldChar, newChar)).append(sequence.substring(untilExclusive - 1, sequence.length())).toString();
        }

        return sequence;
    }

    public static String fileNameFromString(final String path, final String qualifier) {
        if (Strings.isNullOrEmpty(path)) {
            return "";
        }

        if (path.endsWith("/")) {
            return "";
        }

        final int index = path.lastIndexOf("/");
        if (index != -1 && (index + 1) < path.length()) {
            return appendQualifierToFileNameIfPresent(path.substring(index + 1, path.length()), qualifier);
        }

        return appendQualifierToFileNameIfPresent(path, qualifier);
    }

    private static String appendQualifierToFileNameIfPresent(final String path, final String qualifier) {
        if (Strings.isNullOrEmpty(qualifier)) {
            return path;
        }

        final int dotIndex = path.indexOf(".");
        if (dotIndex == -1) {
            return path;
        }

        final String prefix = path.substring(0, dotIndex);
        final String extension = path.substring(dotIndex, path.length());
        return new StringBuilder(prefix).append("-").append(qualifier).append(extension).toString();
    }

    public static String getFileName(final String filename, final String qualifier) {
        return FileHelper.safeFileName(FileHelper.fileNameFromString(filename, qualifier));
    }

    public static File getImageFile(final String imageName, final Double multiplier, final String outputDir) {
        final String safeFilename = getFileName(imageName, "normal");
        return new File(outputDir +"/img-" + multiplier + "-" + safeFilename + "." + Files.getFileExtension(imageName));
    }
}
