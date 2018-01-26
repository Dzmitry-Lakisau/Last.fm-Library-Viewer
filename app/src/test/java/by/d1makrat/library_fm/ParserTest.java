package by.d1makrat.library_fm;

import android.annotation.TargetApi;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.List;

import by.d1makrat.library_fm.json.JsonParser;
import by.d1makrat.library_fm.json.TopsParser;
import by.d1makrat.library_fm.mocks.IHttpClient;
import by.d1makrat.library_fm.mocks.Mocks;
import by.d1makrat.library_fm.model.Artist;
import by.d1makrat.library_fm.model.TopAlbum;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(
        constants = BuildConfig.class,
        sdk = 27
)
@TargetApi(23)
public class ParserTest {

    private static final String EXPECTED_ARTIST_NAME = "Metallica";
    private static final String EXPECTED_ARTIST_NAME_2 = "SHINee";
    private static final String EXPECTED_LISTENERS_COUNT = "144559";
    private static final String EXPECTED_URL = "https://www.last.fm/music/SHINee";
    private static final String EXPECTED_IMAGE_URI = "https://lastfm-img2.akamaized.net/i/u/300x300/ed753560072eda4aced924492ba72c71.png";

    private IHttpClient mHttpClient;
    private InputStream mockedInputStream = null;
    private InputStream response = null;
    private Reader reader = null;

    @Before
    public void mockHttpClient() {
        mHttpClient = mock(IHttpClient.class);
    }

    @Test
    public void parseUserTopAlbums() throws Exception {
        mockedInputStream = Mocks.stream("topalbums.json");
        when(mHttpClient.request((URL) ArgumentMatchers.any(), ArgumentMatchers.anyString())).thenReturn(mockedInputStream);
        response = mHttpClient.request(new URL("http://api"), "GET");

        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        reader = new InputStreamReader(response, "UTF-8");
        for (; ; ) {
            int rsz = reader.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }

        final TopsParser topsParser = new TopsParser(out.toString());
        List<TopAlbum> topAlbumsList = topsParser.parseUserTopAlbums();
        assertEquals(topAlbumsList.size(), 10);
        assertEquals(topAlbumsList.get(0).getArtistName(), EXPECTED_ARTIST_NAME);
    }

    @Test
    public void parseArtists() throws Exception {
        mockedInputStream = Mocks.stream("search_results.json");
        when(mHttpClient.request((URL) ArgumentMatchers.any(), ArgumentMatchers.anyString())).thenReturn(mockedInputStream);
        response = mHttpClient.request(new URL("http://api"), "GET");

        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        reader = new InputStreamReader(response, "UTF-8");
        for (; ; ) {
            int rsz = reader.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }

        final JsonParser jsonParser = new JsonParser();
        List<Artist> artists = jsonParser.parseSearchArtistResults(out.toString());
        assertEquals(artists.size(), 10);
        assertEquals(artists.get(0).getName(), EXPECTED_ARTIST_NAME_2);
        assertEquals(artists.get(0).getListenersCount(), EXPECTED_LISTENERS_COUNT);
        assertEquals(artists.get(0).getImageUri(), EXPECTED_IMAGE_URI);
        assertEquals(artists.get(0).getUrl(), EXPECTED_URL);
    }


    @After
    public void closeStreams() throws IOException{
        if (mockedInputStream != null) mockedInputStream.close();
        if (response != null) response.close();
        if (reader != null) reader.close();
    }
}