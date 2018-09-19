package by.d1makrat.library_fm;

public final class Constants {

    private Constants(){}

    public static final String EMPTY_STRING = "";

    public static final String API_BASE_URL = "https://ws.audioscrobbler.com/2.0/";

    public static final String ALBUM_KEY = "album";
    public static final String ARTIST_KEY = "artist";
    public static final String TRACK_KEY = "track";
    public static final String URL_KEY = "url";
    public static final String NAME_KEY = "name";
    public static final String DATE_KEY = "date";
    public static final String PLAYCOUNT_KEY = "playcount";
    public static final String USER_KEY = "user";
    public static final String RANK_KEY = "rank";
    public static final String PERIOD_KEY = "period";
    public static final String SCROBBLES_PER_PAGE_KEY = "scrobbles_per_page";
    public static final String RECENT_SCROBBLES_FRAGMENT_TAG = "RecentScrobblesFragment";
    public static final String SCROBBLES_OF_ALBUM_TAG = "ScrobblesOfAlbumFragment";
    public static final String SCROBBLES_OF_ARTIST_TAG = "ScrobblesOfArtistFragment";
    public static final String SCROBBLES_OF_TRACK_TAG = "ScrobblesOfTrackFragment";

    public static final String FILTER_DIALOG_FROM_BUNDLE_KEY = "from";
    public static final String FILTER_DIALOG_TO_BUNDLE_KEY = "to";

    public static final String API_NO_ERROR = "No error";
    public static final int API_MAX_FOR_SCROBBLES_BY_ARTIST = 200;

    public static final Long DATE_LONG_DEFAUT_VALUE = -1L;

    public class JsonConstants {
        public static final String IMAGE_KEY = "image";
        public static final String LISTENERS_KEY = "listeners";
        public static final String TEXT_KEY = "#text";
        public static final String TOTAL_KEY = "total";
        public static final String ATTRIBUTE_KEY = "@attr";
    }

    public class DatabaseConstants {
        public static final String DATABASE_SCROBBLES_TABLE = "Scrobbles";
        public static final String DATABASE_TOP_ALBUMS_TABLE = "TopAlbums";
        public static final String DATABASE_TOP_ARTISTS_TABLE = "TopArtists";
        public static final String DATABASE_TOP_TRACKS_TABLE = "TopTracks";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_TRACK = TRACK_KEY;
        public static final String COLUMN_ARTIST = ARTIST_KEY;
        public static final String COLUMN_ALBUM = ALBUM_KEY;
        public static final String COLUMN_DATE = DATE_KEY;
        public static final String COLUMN_IMAGEURI = "imageUri";
        public static final String COLUMN_RANK = RANK_KEY;
        public static final String COLUMN_PLAYCOUNT = PLAYCOUNT_KEY;
        public static final String COLUMN_PERIOD = PERIOD_KEY;
    }
}
