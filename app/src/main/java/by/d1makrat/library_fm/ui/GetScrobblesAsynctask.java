package by.d1makrat.library_fm.ui;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import by.d1makrat.library_fm.APIException;
import by.d1makrat.library_fm.NetworkRequester;
import by.d1makrat.library_fm.json.ScrobblesParser;
import by.d1makrat.library_fm.model.Scrobble;

public class GetScrobblesAsynctask extends AsyncTask<URL, Integer, List<Scrobble>>{

    private Exception mException = null;
    private GetScrobblesAsynctaskCallback myAsyncTaskCallback;

    public GetScrobblesAsynctask(GetScrobblesAsynctaskCallback pAsynctaskCallback) {
        myAsyncTaskCallback = pAsynctaskCallback;
    }

    @Override
    protected List<Scrobble> doInBackground(URL... urls) {


        List<Scrobble> scrobbles = new ArrayList<Scrobble>();
        JSONObject obj;
        
        try {

            NetworkRequester networkRequester = new NetworkRequester();
            String response = networkRequester.request(urls[0], "GET");

            ScrobblesParser scrobblesParser = new ScrobblesParser(response);

            String errorOrNot = scrobblesParser.checkForApiErrors();
            if (!errorOrNot.equals("No error"))
                mException = new APIException(errorOrNot);
            else
                scrobbles = scrobblesParser.parseTracks();
        }

        catch (JSONException e){
                //FirebaseCrash.report(e);
                e.printStackTrace();
                mException = e;
            }
//        catch (UnknownHostException e) {
////                //FirebaseCrash.report(e);
//            e.printStackTrace();
//            mException = 8;
//        }
//        catch (SocketTimeoutException e){
////                //FirebaseCrash.report(e);
//            e.printStackTrace();
//            mException = 7;
//        }
//        catch (MalformedURLException e){
//            //FirebaseCrash.report(e);
//            e.printStackTrace();
//            mException = 6;
//        }
//        catch (SSLException e) {
//            //FirebaseCrash.report(e);
//            e.printStackTrace();
//            mException = 5;
//        }
//        catch (FileNotFoundException e){
//            //FirebaseCrash.report(e);
//            e.printStackTrace();
//            mException = 4;
//        }
//        catch (RuntimeException e){
//            //FirebaseCrash.report(e);
//            e.printStackTrace();
//            mException = 3;
//        }
        catch (IOException e){
            //FirebaseCrash.report(e);
            e.printStackTrace();
            mException = e;
        }
//        catch (APIException e){
//            //FirebaseCrash.report(e);
////            message = e.getMessage();
//            mException = 1;
//        }
        finally {
//            publishProgress(limit);
        }
        return scrobbles;
    }

    @Override
    protected void onPostExecute(List<Scrobble> scrobbles) {
        if (mException != null)
            myAsyncTaskCallback.onException(mException);
        else
            myAsyncTaskCallback.onLoadingScrobblesSuccessful(scrobbles);
    }
}
