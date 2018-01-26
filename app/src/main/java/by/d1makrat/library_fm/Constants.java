package by.d1makrat.library_fm;

public final class Constants {

    private Constants(){}

    public static final String ALBUM_KEY = "album";
    public static final String ARTIST_KEY = "artist";
    public static final String TRACK_KEY = "track";
    public static final String PERIOD_KEY = "period";
    public static final String SCROBBLES_PER_PAGE_KEY = "scrobbles_per_page";
    public static final String SCROBBLES_OF_ARTIST_TAG = "ScrobblesOfArtistFragment";
    public static final String SCROBBLES_OF_TRACK_TAG = "ScrobblesOfTrackFragment";
    public static final String SCROBBLES_OF_ALBUM_TAG = "ScrobblesOfAlbumFragment";
    public static final String FILTER_DIALOG_FROM_BUNDLE_KEY = "from";
    public static final String FILTER_DIALOG_TO_BUNDLE_KEY = "to";
    public static final String[] DATE_PRESETS_FOR_URL = {"", "?date_preset=LAST_7_DAYS", "?date_preset=LAST_30_DAYS", "?date_preset=LAST_90_DAYS", "?date_preset=LAST_180_DAYS", "?date_preset=LAST_365_DAYS"};
    public static final String[] DATE_PERIODS_FOR_API = {"overall", "7day", "1month", "3month", "6month", "12month"};
    public static final String[] DATE_PERIODS_FOR_TAB_NAMES = {"Overall", "Week", "Month", "3 months", "6 months", "Year"};

    public static final String API_NO_ERROR = "No error";

    public static final String DIALOG_FRAGMENT_KEY = "DialogFragment";
    public static final Long DATE_LONG_DEFAUT_VALUE = -1L;
    public static final Long DATE_LASTFM_LAUNCHED = 1016582400L;

    public class DatabaseConstants {
        public static final String DATABASE_SCROBBLES_TABLE = "Scrobbles";
        public static final String DATABASE_TOP_ALBUMS_TABLE = "TopAlbums";
        public static final String DATABASE_TOP_ARTISTS_TABLE = "TopArtists";
        public static final String DATABASE_TOP_TRACKS_TABLE = "TopTracks";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_TRACK = TRACK_KEY;
        public static final String COLUMN_ARTIST = ARTIST_KEY;
        public static final String COLUMN_ALBUM = ALBUM_KEY;
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_IMAGEURI = "imageUri";
        public static final String COLUMN_RANK = "rank";
        public static final String COLUMN_PLAYCOUNT = "playcount";
        public static final String COLUMN_PERIOD = "period";
    }
}