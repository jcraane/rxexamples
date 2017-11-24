package nl.capaxit.filehandling;

import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

import java.io.File;
import java.io.FileInputStream;

public final class RxFileSplitter {
    private final int numBytes;

    public RxFileSplitter(final int numBytes) {
        this.numBytes = numBytes;
    }

    public Observable<Chunk> split(final File original) {
        return split(original, Schedulers.io());
    }

    public Observable<Chunk> split(final File original, final Scheduler scheduler) {
        return rx.Observable.<Chunk>create(subscriber -> {
            try (final FileInputStream fis = new FileInputStream(original)) {
                int remainingBytes = (int) original.length(), part = 0;
                while (remainingBytes > 0) {
                    final int numberOfBytesToRead = Math.min(numBytes, remainingBytes);
                    final byte[] chunkOutput = new byte[numberOfBytesToRead];
                    fis.read(chunkOutput, 0, numberOfBytesToRead);
                    subscriber.onNext(new Chunk(chunkOutput, part));
                    part++;
                    remainingBytes -= numberOfBytesToRead;
                    if (subscriber.isUnsubscribed()) {
                        break;
                    }
                }
                subscriber.onCompleted();
            } catch (final Throwable throwable) {
                subscriber.onError(throwable);
            }
        }).subscribeOn(scheduler);
    }

    public static final class Chunk {
        private final byte[] bytes;
        private final int part;

        Chunk(final byte[] bytes, final int part) {
            this.bytes = bytes;
            this.part = part;
        }

        public byte[] getBytes() {
            return bytes;
        }

        public int getPart() {
            return part;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Chunk{");
            sb.append("bytes=").append(bytes.length);
            sb.append(", part=").append(part);
            sb.append('}');
            return sb.toString();
        }
    }
}
