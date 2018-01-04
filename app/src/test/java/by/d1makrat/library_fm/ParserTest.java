package by.d1makrat.library_fm;

import android.annotation.TargetApi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.List;

import by.d1makrat.library_fm.json.JsonParser;
import by.d1makrat.library_fm.mocks.IHttpClient;
import by.d1makrat.library_fm.mocks.Mocks;
import by.d1makrat.library_fm.model.Artist;
import by.d1makrat.library_fm.model.RankedItem;

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

    @Before
    public void mockHttpClient() {
        mHttpClient = mock(IHttpClient.class);
    }

    @Test
    public void parseUserTopAlbums() throws Exception {
        InputStream mockedInputStream = Mocks.stream("topalbums.json");
        when(mHttpClient.request((URL) Matchers.anyObject(), Matchers.anyString())).thenReturn(mockedInputStream);
        InputStream response = mHttpClient.request(new URL("http://api"), "GET");

        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(response, "UTF-8");
        for (; ; ) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        //TODO input stream is not closed
        final JsonParser jsonParser = new JsonParser();
        List<RankedItem> topAlbumsList = jsonParser.parseUserTopAlbums(out.toString());
        assertEquals(topAlbumsList.size(), 10);
        assertEquals(topAlbumsList.get(0).getSecondaryField(), EXPECTED_ARTIST_NAME);
    }

    @Test
    public void parseArtists() throws Exception {
        InputStream mockedInputStream = Mocks.stream("search_results.json");
        when(mHttpClient.request((URL) Matchers.anyObject(), Matchers.anyString())).thenReturn(mockedInputStream);
        InputStream response = mHttpClient.request(new URL("http://api"), "GET");

        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(response, "UTF-8");
        for (; ; ) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        //TODO input stream is not closed

        final JsonParser jsonParser = new JsonParser();
        List<Artist> artists = jsonParser.parseSearchArtistResults(out.toString());
        assertEquals(artists.size(), 10);
        assertEquals(artists.get(0).getName(), EXPECTED_ARTIST_NAME_2);
        assertEquals(artists.get(0).getListenersCount(), EXPECTED_LISTENERS_COUNT);
        assertEquals(artists.get(0).getImageUri(), EXPECTED_IMAGE_URI);
        assertEquals(artists.get(0).getUrl(), EXPECTED_URL);
    }    
}