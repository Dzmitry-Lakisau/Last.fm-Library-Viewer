package by.d1makrat.library_fm;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.net.URLEncoder;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLException;

public class Data {

    private final String SECRET = "c51dd917013880fd9e6f7c20e98ab3e1";
    private final String[] image_sizes = {"small", "medium", "large", "extralarge"};
    private final String[] tags = {"name", "artist", "album", "date", "image"};
    private ArrayList<HashMap<String, String>> Tracks = new ArrayList<HashMap<String, String>>();
    private String inputXML;

    public Data(TreeMap<String, String> treeMap) throws MalformedURLException, SocketTimeoutException, UnknownHostException, SSLException, IOException {
        URL url = GenerateURL(treeMap);
        inputXML = RequestAndResponse(true, url, "POST");
    }

    public ArrayList<HashMap<String, String>> search(String resolution) throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(new StringReader(inputXML.substring(inputXML.indexOf("<artistmatches>"), inputXML.indexOf("</results>"))));
        while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
            if (xpp.getEventType() == XmlPullParser.START_TAG && xpp.getName().equals(tags[1])) {
                HashMap<String, String> map = new HashMap<String, String>();
                while (!(xpp.getEventType() == XmlPullParser.END_TAG && xpp.getName().equals(tags[1]))) {
                    if (xpp.getEventType() == XmlPullParser.START_TAG && xpp.getName().equals(tags[0])) {
                        map.put(xpp.getName(), xpp.nextText());
                    }
                    if (xpp.getEventType() == XmlPullParser.START_TAG && xpp.getName().equals(tags[4])) {
                        String temp = xpp.getAttributeValue(0);
                        if (temp.equals(resolution)) {
                            map.put("image", xpp.nextText());
                        }
                    }
                    xpp.next();
                }
                Tracks.add(map);
            }
            xpp.next();
        }
        return Tracks;
    }

    public String parseSingleText(String tag) throws XmlPullParserException, IOException {
        String s = null;
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(new StringReader(inputXML));
        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG && tag.equals(xpp.getName())) {
                s = xpp.nextText();
            }
            eventType = xpp.next();
        }
        return s;
    }

    public String parseAttribute(String tag, String attr) throws XmlPullParserException, IOException {
        String res = null;
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(new StringReader(inputXML));
        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG && tag.equals(xpp.getName())) {

                for (int i = 0; i < xpp.getAttributeCount(); i++) {
                    String temp = xpp.getAttributeName(i);
                    if (temp.equals(attr)) {
                        return res = xpp.getAttributeValue(i);
                    }
                }
            }
            eventType = xpp.next();
        }
        return res;
    }


    private URL GenerateURL(TreeMap<String, String> params) throws MalformedURLException {
        //URL MUST be encoded. hash is not.
        String sig = GenerateSignature(params);
        String s = "https://ws.audioscrobbler.com/2.0/?";
        StringBuilder stringBuilder = new StringBuilder(s);
        for (Map.Entry<String, String> e : params.entrySet()) {
//            s += e.getKey() + "=" + URLEncoder.encode(e.getValue()) + "&";
            stringBuilder.append(e.getKey());
            stringBuilder.append("=");
            stringBuilder.append(URLEncoder.encode(e.getValue()));
            stringBuilder.append("&");
        }
        s = stringBuilder.substring(0, s.length() - 1);
        s += "&api_sig=" + sig;
        return new URL(s);
    }

    private String GenerateSignature(Map<String, String> params) {
        String s = "";
        for (Map.Entry<String, String> e : params.entrySet()) {
            s += e.getKey() + e.getValue();
        }
        s += SECRET;
        return new String(Hex.encodeHex(DigestUtils.md5(s)));
    }

    private String RequestAndResponse(boolean https, URL url, String method) throws SocketTimeoutException, UnknownHostException, SSLException, IOException {
        BufferedReader in;
        InputStreamReader isr;
        HttpURLConnection conn = null;
        StringBuilder sb = null;
        URLConnection c = url.openConnection();
        if (https) conn = (HttpsURLConnection) c;
        else conn = (HttpURLConnection) c;
        if (method.equals("POST")) {
            conn.setRequestMethod("POST");
        } else {
            conn.setRequestMethod("GET");
        }
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setConnectTimeout(2000);
        conn.setReadTimeout(2000);
        conn.connect();

        if (conn.getResponseCode() > 399) {
            isr = new InputStreamReader(conn.getErrorStream());
        } else if (conn.getResponseCode() == 200) {
            isr = new InputStreamReader(conn.getInputStream());
        } else return null;

        in = new BufferedReader(isr);
        sb = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            sb.append(line);
        }
        conn.disconnect();
        return sb.toString();
    }

    public ArrayList<HashMap<String, String>> getTopTracks(String resolution) throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(new StringReader(inputXML));
        while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
            if (xpp.getEventType() == XmlPullParser.START_TAG && xpp.getName().equals("track")) {
                HashMap<String, String> map = new HashMap<String, String>();
                if (xpp.getEventType() == XmlPullParser.START_TAG && xpp.getName().equals("track")) {
                    String temp = xpp.getAttributeValue(0);
                    map.put("rank", temp);
                }
                while (!(xpp.getEventType() == XmlPullParser.END_TAG && xpp.getName().equals("track"))) {
                    if (xpp.getEventType() == XmlPullParser.START_TAG && xpp.getName().equals(tags[0])) {
                        map.put(xpp.getName(), xpp.nextText());
                    }

                    if (xpp.getEventType() == XmlPullParser.START_TAG && xpp.getName().equals("playcount")) {
                        map.put("playcount", xpp.nextText() + " scrobbles");
                    }

                    if (xpp.getEventType() == XmlPullParser.START_TAG && xpp.getName().equals(tags[1])) {
                        while (!(xpp.getEventType() == XmlPullParser.END_TAG && xpp.getName().equals(tags[1]))) {
                            if (xpp.getEventType() == XmlPullParser.START_TAG && xpp.getName().equals(tags[0]))
                                map.put("artist", xpp.nextText());

                            xpp.next();
                        }
                    }

                    if (xpp.getEventType() == XmlPullParser.START_TAG && xpp.getName().equals(tags[4])) {
                        String temp = xpp.getAttributeValue(0);
                        if (temp.equals(resolution))
                            map.put("image", xpp.nextText());
                    }
                    xpp.next();
                }
                Tracks.add(map);
            }
            xpp.next();
        }
        return Tracks;
    }

    public ArrayList<HashMap<String, String>> getTopArtists(String resolution) throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(new StringReader(inputXML));
        while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
            if (xpp.getEventType() == XmlPullParser.START_TAG && xpp.getName().equals(tags[1])) {
                HashMap<String, String> map = new HashMap<String, String>();
                if (xpp.getEventType() == XmlPullParser.START_TAG && xpp.getName().equals(tags[1])) {
                    String temp = xpp.getAttributeValue(0);
                    map.put("rank", temp);
                }
                while (!(xpp.getEventType() == XmlPullParser.END_TAG && xpp.getName().equals(tags[1]))) {
                    if (xpp.getEventType() == XmlPullParser.START_TAG && xpp.getName().equals(tags[0])) {
                        map.put(tags[1], xpp.nextText());
                    }

                    if (xpp.getEventType() == XmlPullParser.START_TAG && xpp.getName().equals("playcount")) {
                        map.put("playcount", xpp.nextText() + " scrobbles");
                    }

                    if (xpp.getEventType() == XmlPullParser.START_TAG && xpp.getName().equals(tags[4])) {
                        String temp = xpp.getAttributeValue(0);
                        if (temp.equals(resolution))
                            map.put("image", xpp.nextText());
                    }
                    xpp.next();
                }
                Tracks.add(map);
            }
            xpp.next();
        }
        return Tracks;
    }

    public ArrayList<HashMap<String, String>> getTopAlbums(String resolution) throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(new StringReader(inputXML));
        while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
            if (xpp.getEventType() == XmlPullParser.START_TAG && xpp.getName().equals(tags[2])) {
                HashMap<String, String> map = new HashMap<String, String>();
                if (xpp.getEventType() == XmlPullParser.START_TAG && xpp.getName().equals(tags[2])) {
                    String temp = xpp.getAttributeValue(0);
                    map.put("rank", temp);
                }

                while (!(xpp.getEventType() == XmlPullParser.END_TAG && xpp.getName().equals(tags[2]))) {

                    if (xpp.getEventType() == XmlPullParser.START_TAG && xpp.getName().equals(tags[0])) {
                        map.put("album", xpp.nextText());
                    }

                    if (xpp.getEventType() == XmlPullParser.START_TAG && xpp.getName().equals("playcount")) {
                        map.put("playcount", xpp.nextText() + " scrobbles");
                    }

                    if (xpp.getEventType() == XmlPullParser.START_TAG && xpp.getName().equals(tags[1])) {
                        while (!(xpp.getEventType() == XmlPullParser.END_TAG && xpp.getName().equals(tags[1]))) {
                            if (xpp.getEventType() == XmlPullParser.START_TAG && xpp.getName().equals(tags[0]))
                                map.put("artist", xpp.nextText());
                            xpp.next();
                        }
                    }

                    if (xpp.getEventType() == XmlPullParser.START_TAG && xpp.getName().equals(tags[4])) {
                        String temp = xpp.getAttributeValue(0);
                        if (temp.equals(resolution))
                            map.put("image", xpp.nextText());
                    }
                    xpp.next();
                }
                Tracks.add(map);
            }
            xpp.next();
        }
        return Tracks;
    }
}