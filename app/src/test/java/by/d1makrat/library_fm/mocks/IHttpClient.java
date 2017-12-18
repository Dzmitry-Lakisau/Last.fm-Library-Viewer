package by.d1makrat.library_fm.mocks;

import java.io.InputStream;
import java.net.URL;

public interface IHttpClient {
    InputStream request(URL url, String method);
}
