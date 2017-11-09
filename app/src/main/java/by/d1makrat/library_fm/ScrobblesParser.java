package by.d1makrat.library_fm;

import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScrobblesParser {
    
    private String mStringToParse; 
    
    public ScrobblesParser(String stringToParse){
              mStringToParse = stringToParse;
    }

    public String checkForApiErrors(){

        try {
            JSONObject obj = new JSONObject(mStringToParse);
            if (obj.has("error")) {
                return obj.getString("message");
            } else return "No error";
        }
        catch (JSONException exc){
            return exc.getMessage();
        }
    }

//    public String parseSingleText(String tag) throws XmlPullParserException, IOException{
//        String s = null;
//        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//        factory.setNamespaceAware(true);
//        XmlPullParser xpp = factory.newPullParser();
//        xpp.setInput(new StringReader(mStringToParse));
//        int eventType = xpp.getEventType();
//        while (eventType != XmlPullParser.END_DOCUMENT) {
//            if (eventType == XmlPullParser.START_TAG && tag.equals(xpp.getName())) {
//                s = xpp.nextText();
//            }
//            eventType = xpp.next();
//        }
//        return s;
//    }

    public List<Scrobble> parseTracks() {

        List<Scrobble> scrobbles = new ArrayList<Scrobble>();

        try {
//            scrobbles = new ArrayList<Scrobble>();

            JSONObject obj = new JSONObject(mStringToParse);
            JSONObject recenttracks = obj.getJSONObject("recenttracks");
            JSONArray tracksArray = recenttracks.getJSONArray("track");
            for (int i=0; i<tracksArray.length(); i++){
                Scrobble scrobble = new Scrobble();
                JSONObject jsonObject = tracksArray.optJSONObject(i).optJSONObject("artist");
                Artist artist = new Artist();
                artist.setArtistName(jsonObject.optString("#text"));
                artist.setMbid(jsonObject.optString("mbid"));
                scrobble.setArtist(artist);
                String track = tracksArray.optJSONObject(i).getString("name");
                scrobble.setTrackTitle(track);
                jsonObject = tracksArray.optJSONObject(i).optJSONObject("album");
                Album album = new Album();
                album.setAlbumTitle(jsonObject.optString("#text"));
                album.setMbid(jsonObject.optString("mbid"));
                scrobble.setAlbum(album);
                JSONArray jsonArray = tracksArray.optJSONObject(i).optJSONArray("image");
                List<Image> images = new ArrayList<Image>();
                for (int j = 0; j < jsonArray.length(); j++) {
                    jsonObject = jsonArray.getJSONObject(j);
                    Image image = new Image();
                    image.setImageURI(jsonObject.optString("#text"));
                    image.setSize(jsonObject.optString("size"));
                    images.add(image);
                }
                scrobble.setImage(images);
                jsonObject = tracksArray.optJSONObject(i).optJSONObject("date");
                Date date = new Date();
                date.setFormattedDate(jsonObject.optString("#text"));
                date.setUnixDate(jsonObject.optLong("uts"));
                scrobble.setDate(date);
                scrobbles.add(scrobble);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

//        List<HashMap<String, String>> Tracks = new ArrayList<HashMap<String, String>>();
//        for (Scrobble scrobble : scrobbles) {
//            HashMap<String, String> map = new HashMap<String, String>();//"name", "artist", "album", "date", "image"
//            map.put("name", scrobble.getTrackTitle());
//            map.put("artist", scrobble.getArtist().getArtistName());
//            map.put("album", scrobble.getAlbum().getAlbumTitle());
//            map.put("date", scrobble.getDate().getFormattedDate());
//            map.put("image", scrobble.getImages().get(3).getImageURI());
//
//            Tracks.add(map);
//        }

        return scrobbles;
    }
}
