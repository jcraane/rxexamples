package nl.capaxit.filehandling;

import org.apache.commons.io.IOUtils;
import rx.Completable;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FileSplitTest {
    public static void main(String[] args) throws Exception {
        new FileSplitTest().run();
    }

    private void run() throws Exception {
        final InputStream is = getClass().getResourceAsStream("/testfiles/IMG_3781.JPG");
        final File output = new File("out/admin.mp4");
        IOUtils.copy(is, new FileOutputStream(output));
        new RxFileSplitter(1024 * 1024)
                .split(output)
                .subscribeOn(Schedulers.immediate())
                .doOnNext(System.out::println)
                .switchMap(chunk -> Observable.fromCallable(() -> {
                    IOUtils.write(chunk.getBytes(), new FileOutputStream(new File("out/IMG_3781" + chunk.getPart() + ".JPG")));
                    return chunk;
                }))
                .doOnCompleted(() -> System.out.println("Complete"))
                .subscribe(chunk -> {}, throwable -> throwable.printStackTrace());
    }
}
