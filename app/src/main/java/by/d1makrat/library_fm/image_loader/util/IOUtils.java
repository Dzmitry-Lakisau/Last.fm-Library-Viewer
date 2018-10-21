package by.d1makrat.library_fm.image_loader.util;

import com.crashlytics.android.Crashlytics;

import java.io.Closeable;
import java.io.IOException;

public final class IOUtils {

    private IOUtils() {
    }

    public static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
                Crashlytics.logException(e);
            }
        }
    }

}
