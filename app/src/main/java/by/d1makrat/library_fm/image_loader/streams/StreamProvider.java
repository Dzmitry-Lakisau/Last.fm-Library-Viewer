package by.d1makrat.library_fm.image_loader.streams;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

public interface StreamProvider<T> {
    InputStream get(T path) throws IOException;
}
