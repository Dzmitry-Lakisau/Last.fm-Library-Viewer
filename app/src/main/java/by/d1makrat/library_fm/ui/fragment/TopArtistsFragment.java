package by.d1makrat.library_fm.ui.fragment;

import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import org.xmlpull.v1.XmlPullParserException;
import javax.net.ssl.SSLException;

import by.d1makrat.library_fm.APIException;
import by.d1makrat.library_fm.BuildConfig;
import by.d1makrat.library_fm.Data;
import by.d1makrat.library_fm.NetworkStatusChecker;
import by.d1makrat.library_fm.R;

public class TopArtistsFragment extends ListFragment implements OnScrollListener {
    private final String API_KEY = BuildConfig.API_KEY;
    private String path_to_blank = null;
    private String resolution;
    private String url = null;
    private String filter_string = null;
    private String list_head_text = null;
    private String empty_list_text = null;
    private String username = null;
    private String sessionKey = null;
    private String from = null;
    private String to = null;
    private String cachepath = null;
    private String period = null;

    private boolean isLoading = false;
    private boolean isCreated = true;
    private boolean allIsLoaded = false;
    private boolean wasEmpty = false;

    private GetTopArtistsTask task;
    private SimpleAdapter adapter;
    private ListView lView;
    private View empty_list;
    private View list_head;
    private Menu menu;
    private ArrayList<HashMap<String, String>> items = new ArrayList<>();

    private int page;
    private int limit;//max=200

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionKey = getArguments().getString("sessionKey");
        username = getArguments().getString("username");
        cachepath = getArguments().getString("cachepath");
        period = getArguments().getString("period");
        url = "https://www.last.fm/user/" + username + "/library/artists";
        page = 1;
        path_to_blank = getActivity().getFilesDir().getPath();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.list, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.top_artists));

        adapter = new SimpleAdapter(getActivity(), items, R.layout.ranked_list_item, new String[]{"artist", "image", "playcount", "rank"}, new int[]{R.id.upper_field, R.id.albumart, R.id.bottom_field, R.id.rank});
        lView = (ListView) rootView.findViewById(android.R.id.list);
        empty_list = rootView.findViewById(R.id.empty_list);

        View list_spinner = getActivity().getLayoutInflater().inflate(R.layout.list_spinner, (ViewGroup) null);
        lView.addFooterView(list_spinner);
        lView.setAdapter(adapter);
        lView.removeFooterView(list_spinner);

        lView.setOnScrollListener(this);
        registerForContextMenu(lView);

        if (!isCreated) {
            if (wasEmpty) {
                rootView.findViewById(R.id.empty_list).setVisibility(View.VISIBLE);
                ((TextView) rootView.findViewById(R.id.empty_list_text)).setText(empty_list_text);
            }
        }
        else {
            if (!NetworkStatusChecker.isNetworkAvailable()) {
                empty_list.setVisibility(View.VISIBLE);
                ((TextView) empty_list.findViewById(R.id.empty_list_text)).setText(R.string.network_is_not_connected);
                empty_list_text = getText(R.string.network_is_not_connected).toString();
                wasEmpty = true;
            }
        }

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        limit = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(getContext()).getString("limit", null));
        resolution = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("resolution", null);
        if (isCreated)
            LoadItems(page);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
    }

    @Override
    public void onScrollStateChanged(AbsListView l, int ScrollState) {
    }

    @Override
    public void onScroll(AbsListView l, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if ((firstVisibleItem + visibleItemCount) == totalItemCount && (totalItemCount > 0) && !isLoading) {
            if (NetworkStatusChecker.isNetworkAvailable()) {
                page++;
                LoadItems(page);
            }
            else {
                Toast toast;
                toast = Toast.makeText(getContext(), R.string.network_is_not_connected, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_refresh:
                if (NetworkStatusChecker.isNetworkAvailable()) {
                    if (!IsTaskRunning()) {
                        items.clear();
                        adapter.notifyDataSetChanged();
                        page = 1;
                        LoadItems(page);
                    }
                }
                else {
                    Toast toast;
                    toast = Toast.makeText(getContext(), R.string.network_is_not_connected, Toast.LENGTH_SHORT);
                    toast.show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean IsTaskRunning(){
        if (task != null && task.getStatus() != AsyncTask.Status.FINISHED){
            return true;
        }
        return false;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v ,menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.context_menu, menu);
        menu.getItem(1).setVisible(false);
        menu.getItem(2).setVisible(false);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            switch (item.getItemId()) {
                case R.id.scrobbles_of_artist:
                    String artist = ((TextView) info.targetView.findViewById(R.id.upper_field)).getText().toString();
                    Bundle bundle = new Bundle();
                    FragmentTransaction fragmentTransaction;
                    bundle.putString("sessionKey", sessionKey);
                    bundle.putString("username", username);
                    bundle.putString("cachepath", cachepath);
                    bundle.putString("artist", artist);
                    bundle.putString("resolution", resolution);
                    Fragment fragment = new ScrobblesOfArtistFragment();
                    fragment.setArguments(bundle);
                    fragmentTransaction = getParentFragment().getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_main, fragment, "ScrobblesOfArtistFragment");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    return true;
                case R.id.scrobbles_of_track:
                    artist = ((TextView) info.targetView.findViewById(R.id.upper_field)).getText().toString();
                    bundle = new Bundle();
                    bundle.putString("sessionKey", sessionKey);
                    bundle.putString("username", username);
                    bundle.putString("cachepath", cachepath);
                    bundle.putString("artist", artist);
                    bundle.putString("resolution", resolution);
                    fragment = new ScrobblesOfTrackFragment();
                    String track = ((TextView) info.targetView.findViewById(R.id.track)).getText().toString();
                    bundle.putString("track", track);
                    fragment.setArguments(bundle);
                    fragmentTransaction = getParentFragment().getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_main, fragment, "ScrobblesOfTrackFragment");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    return true;
                case R.id.scrobbles_of_album:
                    artist = ((TextView) info.targetView.findViewById(R.id.artist)).getText().toString();
                    bundle = new Bundle();
                    bundle.putString("sessionKey", sessionKey);
                    bundle.putString("username", username);
                    bundle.putString("cachepath", cachepath);
                    bundle.putString("artist", artist);
                    bundle.putString("resolution", resolution);
                    fragment = new ScrobblesOfAlbumFragment();
                    String album = ((TextView) info.targetView.findViewById(R.id.album)).getText().toString();
                    bundle.putString("album", album);
                    fragment.setArguments(bundle);
                    fragmentTransaction = getParentFragment().getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_main, fragment, "ScrobblesOfAlbumFragment");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    return true;
                default:
                    return super.onContextItemSelected(item);
            }
    }


    private void KillTaskIfRunning(AsyncTask task) {
        if (task != null && task.getStatus() != AsyncTask.Status.FINISHED){
            page--;
            task.cancel(true);
        }
    }

    public void LoadItems(Integer page) {
        if (NetworkStatusChecker.isNetworkAvailable()) {
            if (!allIsLoaded) {
                isLoading = true;
                TreeMap<String, String> treeMap = new TreeMap<>();
                treeMap.put("method", "user.getTopArtists");
                treeMap.put("api_key", API_KEY);
                treeMap.put("sk", sessionKey);
                treeMap.put("user", username);
                treeMap.put("limit", String.valueOf(limit));
                treeMap.put("page", String.valueOf(page));
                treeMap.put("period", period);
                task = new GetTopArtistsTask();
                task.execute(treeMap);
            }
        }
        else {
            empty_list.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        isCreated = false;
    }

    @Override
    public void onStop(){
        super.onStop();
        isCreated = false;
        KillTaskIfRunning(task);
    }

    public class GetTopArtistsTask extends AsyncTask<TreeMap<String, String>, Integer, ArrayList<HashMap<String, String>>> {

        View list_spinner = getActivity().getLayoutInflater().inflate(R.layout.list_spinner, (ViewGroup) null);
        private int exception = 0;
        private String message = null;
        private ProgressBar progressBar = (ProgressBar) list_spinner.findViewById(R.id.progressbar);

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(TreeMap<String, String>... params) {

            ArrayList<HashMap<String, String>> Tracks = new ArrayList<>();
            FileOutputStream out = null;
            Bitmap image;
            String filename = null;

            try {
                Data rawxml = new Data(params[0]);
                if (rawxml.parseAttribute("lfm", "status").equals("failed"))
                    throw new APIException(rawxml.parseSingleText("error"));
                Tracks = rawxml.getTopArtists(resolution);

                File folder = new File(cachepath + File.separator + resolution + File.separator + "Artists");
                if (!folder.exists()) {
                    folder.mkdirs();
                }
                for (int i = 0; i < Tracks.size(); i++) {
                    if (isCancelled()) return null;
                    HashMap<String, String> item = Tracks.get(i);
                    String temp = item.get("image");
                    if (!temp.equals("")) {
                        filename = folder.getPath() + File.separator + item.get("artist").replaceAll("[\\\\/:*?\"<>|]", "_") + ".png";
                        if (!(new File(filename).exists())) {
                            try {
                                URL newurl = new URL(temp);
                                image = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
                                out = new FileOutputStream(filename);
                                image.compress(Bitmap.CompressFormat.PNG, 100, out);
                                out.flush();
                                out.close();
                            }
                            catch (Exception e){
                                //FirebaseCrash.report(e);
                                e.printStackTrace();
                                File file = new File(filename);
                                boolean deleted = file.delete();
                            }
                            finally {
                                try {
                                    if (out != null) {
                                        out.close();
                                    }
                                } catch (IOException e) {
                                    //FirebaseCrash.report(e);
                                    e.printStackTrace();
                                    exception = 3;
                                }
                            }
                        }
                    } else {
                        filename = path_to_blank + File.separator + "ic_person.png";
                    }
                    item.put("image", filename);
                    Tracks.set(i, item);
                    publishProgress(i + 1);
                }
            }
            catch (XmlPullParserException e){
                //FirebaseCrash.report(e);
                e.printStackTrace();
                exception = 9;
            }
            catch (UnknownHostException e) {
                e.printStackTrace();
                exception = 8;
            }
            catch (SocketTimeoutException e){
                e.printStackTrace();
                exception = 7;
            }
            catch (MalformedURLException e){
                //FirebaseCrash.report(e);
                e.printStackTrace();
                exception = 6;
            }
            catch (SSLException e) {
                //FirebaseCrash.report(e);
                e.printStackTrace();
                exception = 5;
            }
            catch (FileNotFoundException e){
                //FirebaseCrash.report(e);
                e.printStackTrace();
                exception = 4;
            }
            catch (RuntimeException e){
                //FirebaseCrash.report(e);
                e.printStackTrace();
                exception = 3;
            }
            catch (IOException e){
                //FirebaseCrash.report(e);
                e.printStackTrace();
                exception = 2;
            }
            catch (APIException e){
                //FirebaseCrash.report(e);
                message = e.getMessage();
                exception = 1;
            }
            finally {
                publishProgress(limit);
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    //FirebaseCrash.report(e);
                    e.printStackTrace();
                    exception = 3;
                }
            }
            return Tracks;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
            lView.removeFooterView(list_spinner);
            isLoading = false;
            wasEmpty = false;
            if (exception == 0 && result.size() > 0){
                for (int i = 0; i < result.size(); i++) {
                    items.add(result.get(i));
                }
                adapter.notifyDataSetChanged();
            }
            if (exception == 0 && result.size() == 0 && lView.getCount() > 0) {
                allIsLoaded = true;
            }
            if (exception == 0 && result.size() == 0 && lView.getCount() == 0) {
                empty_list.setVisibility(View.VISIBLE);
                empty_list_text = "Nothing found";
                ((TextView) empty_list.findViewById(R.id.empty_list_text)).setText(empty_list_text);
                wasEmpty = true;
            }
            if (exception == 1 && lView.getCount() == 0){
                page--;
                empty_list.setVisibility(View.VISIBLE);
                empty_list_text = "Error occurred";
                ((TextView) empty_list.findViewById(R.id.empty_list_text)).setText(empty_list_text);
                wasEmpty = true;
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
            if (exception == 1 && lView.getCount() > 0) {
                page--;
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
            if (exception > 1 && lView.getCount() == 0){
                page--;
                empty_list.setVisibility(View.VISIBLE);
                empty_list_text = "Error occurred";
                ((TextView) empty_list.findViewById(R.id.empty_list_text)).setText(empty_list_text);
                wasEmpty = true;
                String[] exception_message = getResources().getStringArray(R.array.Exception_messages);
                Toast.makeText(getContext(), exception_message[exception - 1], Toast.LENGTH_SHORT).show();
            }
            if (exception > 1 && lView.getCount() > 0) {
                page--;
                String[] exception_message = getResources().getStringArray(R.array.Exception_messages);
                Toast.makeText(getContext(), exception_message[exception - 1], Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            empty_list.setVisibility(View.GONE);
            progressBar.setProgress(0);
            progressBar.setMax(limit);
            lView.addFooterView(list_spinner);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress((values[0]));
        }

        @Override
        protected void onCancelled(ArrayList<HashMap<String, String>> result) {
            isLoading = false;
            wasEmpty = false;
        }
    }
}
