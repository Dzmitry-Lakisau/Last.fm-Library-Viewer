package by.d1makrat.library_fm;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

import javax.net.ssl.SSLException;

public class ScrobblesListFragment extends ListFragment implements OnScrollListener, FilterDialogFragment.DialogListener {
    private final String API_KEY = BuildConfig.API_KEY;
//    private String path_to_blank = null;
    private ResolutionOfImage resolutionOfImage;
    private String url = null;
    private String filter_string = null;
    private String list_head_text = null;
    private String empty_list_text = null;
    private String username = null;
    private String sessionKey = null;
    private String from = null;
    private String to = null;
    private String cachepath = null;

    private boolean isLoading = false;
    private boolean isCreated = true;
    private boolean allIsLoaded = false;
    private boolean wasEmpty = false;

    private GetScrobblesListTask task;
    private SimpleAdapter adapter;
    private ListView lView;
    private View empty_list;
    private View list_head;
    private Menu menu;
    private ArrayList<HashMap<String, String>> items = new ArrayList<>();

    private int page;
    private int limit;//max=1000




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        sessionKey = getArguments().getString("sessionKey");
//        username = getArguments().getString("username");
        cachepath = getArguments().getString("cachepath");
        url = "https://www.last.fm/user/" + username + "/library";
        page = 1;
//        path_to_blank = getActivity().getFilesDir().getPath();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.list_with_head, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.scrobbles));
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(null);

        if (!isCreated) {
                if (wasEmpty){
                    //было загружено и пусто
                    rootView.findViewById(R.id.empty_list).setVisibility(View.VISIBLE);
                    ((TextView) rootView.findViewById(R.id.empty_list_text)).setText(empty_list_text);
                }
                else {
                    //было загружено и данные не пустые
                    rootView.findViewById(R.id.list_head).setVisibility(View.VISIBLE);
                    ((TextView) rootView.findViewById(R.id.list_head)).setText(list_head_text);
                }
        }
        else {
            if(!NetworkStatusChecker.isNetworkAvailable(getActivity().getApplicationContext())){
                //создаётся и сеть отсуствует
                rootView.findViewById(R.id.list_head).setVisibility(View.GONE);
                rootView.findViewById(R.id.empty_list).setVisibility(View.VISIBLE);
                ((TextView) rootView.findViewById(R.id.empty_list).findViewById(R.id.empty_list_text)).setText(R.string.network_is_not_connected);
                empty_list_text = getText(R.string.network_is_not_connected).toString();
                wasEmpty = true;
            }
        }

        adapter = new SimpleAdapter(getActivity(), items, R.layout.list_item, new String[]{"name", "artist", "album", "date", "image"}, new int[]{R.id.track, R.id.artist, R.id.album, R.id.timestamp, R.id.albumart});
        lView = (ListView) rootView.findViewById(android.R.id.list);
        empty_list = rootView.findViewById(R.id.empty_list);
        list_head = rootView.findViewById(R.id.list_head);

        View list_spinner = getActivity().getLayoutInflater().inflate(R.layout.list_spinner, (ViewGroup) null);
        lView.addFooterView(list_spinner);
        lView.setAdapter(adapter);
        lView.removeFooterView(list_spinner);

//        lView.setOnItemClickListener(this);
        lView.setOnScrollListener(this);
        registerForContextMenu(lView);


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        AppSettings appSettings = new AppSettings(getActivity().getApplicationContext());
        limit = Integer.parseInt(appSettings.getLimit());
        resolutionOfImage = appSettings.getResolutionOfImage();

        if (isCreated)
            LoadItems(page, null, null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {//TODO переделать в по клику, а не контекстное меню?
//        super.onListItemClick(l, v, position, id);
//        getActivity().openContextMenu(v);
//
//        /** Instantiating PopupMenu class */
//        Context wrapper = new ContextThemeWrapper(getContext(), R.style.PopupMenu);
//        PopupMenu popup = new PopupMenu(wrapper, v);
//        popup.setGravity(Gravity.FILL);
//
//        /** Adding menu items to the popumenu */
//        popup.getMenuInflater().inflate(R.menu.context_menu, popup.getMenu());
//
//        /** Defining menu item click listener for the popup menu */
//        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                Toast.makeText(getActivity(), "You selected the action : " + item.getTitle(), Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });
//
//        /** Showing the popup menu */
//        popup.show();
    }

    @Override
    public void onScrollStateChanged(AbsListView l, int ScrollState) {}

    @Override
    public void onScroll(AbsListView l, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if ((firstVisibleItem + visibleItemCount) == totalItemCount && (totalItemCount > 0) && !isLoading) {
            if (NetworkStatusChecker.isNetworkAvailable(getActivity().getApplicationContext())) {
                page++;
                LoadItems(page, from, to);
            }
            else {
                Toast toast;
                toast = Toast.makeText(getContext(), R.string.network_is_not_connected, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_scrobbles, menu);



        this.menu = menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_refresh:
                if (NetworkStatusChecker.isNetworkAvailable(getActivity().getApplicationContext())) {
                    if (!isLoading) {
                        allIsLoaded = false;
                        getView().findViewById(R.id.list_head).setVisibility(View.GONE);
                        KillTaskIfRunning(task);
                        items.clear();
                        adapter.notifyDataSetChanged();
                        page = 1;
                        LoadItems(page, from, to);
                    }
                }
                else {
                    Toast toast;
                    toast = Toast.makeText(getContext(), R.string.network_is_not_connected, Toast.LENGTH_SHORT);
                    toast.show();
                }
                return true;
            case R.id.action_filter:
                if (!isLoading) {
                    FilterDialogFragment dialogFragment = new FilterDialogFragment();
                    Bundle args = new Bundle();
                    args.putString("from", from);
                    args.putString("to", to);
                    dialogFragment.setArguments(args);
                    dialogFragment.setTargetFragment(this, 0);
                    dialogFragment.show(getFragmentManager(), "DialogFragment");
                }
                return true;
            case R.id.open_in_browser:
                Uri address = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, address);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v ,menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        if (NetworkStatusChecker.isNetworkAvailable(getActivity().getApplicationContext())) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            switch (item.getItemId()) {
                case R.id.scrobbles_of_artist:
                    String artist = ((TextView) info.targetView.findViewById(R.id.artist)).getText().toString();
                    Bundle bundle = new Bundle();
                    FragmentTransaction fragmentTransaction;
                    bundle.putString("sessionKey", sessionKey);
                    bundle.putString("username", username);
                    bundle.putString("cachepath", cachepath);
                    bundle.putString("artist", artist);
                    bundle.putString("resolutionOfImage", resolutionOfImage.name());
                    Fragment fragment = new ScrobblesOfArtistFragment();
                    fragment.setArguments(bundle);
                    fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_main, fragment, "ScrobblesOfArtistFragment");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    return true;
                case R.id.scrobbles_of_track:
                    artist = ((TextView) info.targetView.findViewById(R.id.artist)).getText().toString();
                    bundle = new Bundle();
                    bundle.putString("sessionKey", sessionKey);
                    bundle.putString("username", username);
                    bundle.putString("cachepath", cachepath);
                    bundle.putString("artist", artist);
                    bundle.putString("resolutionOfImage", resolutionOfImage.name());
                    fragment = new ScrobblesOfTrackFragment();
                    String track = ((TextView) info.targetView.findViewById(R.id.track)).getText().toString();
                    bundle.putString("track", track);
                    fragment.setArguments(bundle);
                    fragmentTransaction = getFragmentManager().beginTransaction();
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
                    bundle.putString("resolutionOfImage", resolutionOfImage.name());
                    fragment = new ScrobblesOfAlbumFragment();
                    String album = ((TextView) info.targetView.findViewById(R.id.album)).getText().toString();
                    bundle.putString("album", album);
                    fragment.setArguments(bundle);
                    fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_main, fragment, "ScrobblesOfAlbumFragment");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    return true;
                default:
                    return super.onContextItemSelected(item);
            }
        }
        else {
            Toast toast;
            toast = Toast.makeText(getContext(), R.string.network_is_not_connected, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
    }

    private void KillTaskIfRunning(AsyncTask task) {
        if (task != null && task.getStatus() != AsyncTask.Status.FINISHED){
            page--;
            task.cancel(true);
        }
    }

    public void LoadItems(Integer page, String from, String to) {
        if (NetworkStatusChecker.isNetworkAvailable(getActivity().getApplicationContext())) {
            if (!allIsLoaded) {
                isLoading = true;
                TreeMap<String, String> treeMap = new TreeMap<>();
                treeMap.put("method", "user.getRecentTracks");
//                treeMap.put("api_key", BuildConfig.API_KEY);
//                treeMap.put("sk", sessionKey);
//                treeMap.put("user", username);
//                treeMap.put("limit", String.valueOf(limit));
                treeMap.put("page", String.valueOf(page));
                if (from != null) treeMap.put("from", from);
                if (to != null) treeMap.put("to", to);
                task = new GetScrobblesListTask();
                task.execute();
            }
        }
        else {
            empty_list.setVisibility(View.VISIBLE);
            ((TextView) empty_list.findViewById(R.id.empty_list_text)).setText(R.string.network_is_not_connected);
        }
    }

    @Override
    public void onFinishEditDialog(String from, String to){
        getView().findViewById(R.id.list_head).setVisibility(View.GONE);
        allIsLoaded = false;
        this.from = from;
        this.to = to;
        items.clear();
        adapter.notifyDataSetChanged();
        page = 1;
        filter_string = null;
        url = "https://www.last.fm/user/" + username + "/library";

        KillTaskIfRunning(task);
        LoadItems(page, from, to);

//        TimeZone tz = TimeZone.getDefault();
//        Log.d("DEBUG", tz.toString());

        if (from != null && to != null) {
            Date date_from = new Date(Long.valueOf(from) * 1000);
            String string_from = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH).format(date_from);
            Date date_to = new Date(Long.valueOf(to) * 1000);// - TimeZone.getDefault().getRawOffset());
            String string_to = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH).format(date_to);
            filter_string = string_from + " - " + string_to;
            url += "?from=" + new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(date_from) + "&to=" + new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(date_to);
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
        isCreated = false;//TODO isCreated должно быть екгу и афдыу только В onPostExecute
        KillTaskIfRunning(task);
    }

    public class GetScrobblesListTask extends AsyncTask<Void, Integer, List<HashMap<String, String>>> {

        private View list_spinner = getActivity().getLayoutInflater().inflate(R.layout.list_spinner, (ViewGroup) null);
        private int exception = 0;
        private String message = null;
        private ProgressBar progressBar = (ProgressBar) list_spinner.findViewById(R.id.progressbar);

        @Override
        protected List<HashMap<String, String>> doInBackground(Void... params) {

            List<HashMap<String, String>> Tracks = new ArrayList<>();
            List<Scrobble> scrobbles = new ArrayList<>();

            try {

                UrlConstructor urlConctructor = new UrlConstructor(getActivity().getApplicationContext());
                URL url = urlConctructor.constructRecentScrobblesRequest(page, from, to);

                NetworkRequester networkRequester = new NetworkRequester();
                String response = networkRequester.request(url, "GET");

                ScrobblesParser scrobblesParser = new ScrobblesParser(response);

                if (!scrobblesParser.checkForApiErrors().equals("No error"))
                    throw new APIException(scrobblesParser.checkForApiErrors());
                else
//                    Tracks = scrobblesParser.parseTracks();
                    scrobbles = scrobblesParser.parseTracks();

                for (int i = 0; i < scrobbles.size(); i++) {
                    if (isCancelled()) return null;

                    Scrobble scrobble = scrobbles.get(i);
                    ImageDownloaderFromNetwork imageDownloaderFromNetwork = new ImageDownloaderFromNetwork(getActivity().getApplicationContext());
                    imageDownloaderFromNetwork.download(scrobble);

                    scrobbles.set(i, scrobble);
                    publishProgress(i+1);
                }

                for (int i = 0; i < scrobbles.size(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("artist", scrobbles.get(i).getArtist().getArtistName());
                    map.put("name", scrobbles.get(i).getTrackTitle());
                    map.put("album", scrobbles.get(i).getAlbum().getAlbumTitle());
                    map.put("date", scrobbles.get(i).getDate().getFormattedDate());
                    map.put("image", scrobbles.get(i).getImageUriBySize(resolutionOfImage));
                    Tracks.add(map);
                }
            }


//            catch (XmlPullParserException e){
//                //FirebaseCrash.report(e);
//                e.printStackTrace();
//                exception = 9;
//            }
            catch (UnknownHostException e) {
//                //FirebaseCrash.report(e);
                e.printStackTrace();
                exception = 8;
            }
            catch (SocketTimeoutException e){
//                //FirebaseCrash.report(e);
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
            }
            return Tracks;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {
            lView.removeFooterView(list_spinner);
            isLoading = false;
            wasEmpty = false;
            if (exception == 0 && result.size() > 0){
                list_head.setVisibility(View.VISIBLE);
                for (int i = 0; i < result.size(); i++) {
                    items.add(result.get(i));
                }
                adapter.notifyDataSetChanged();
                list_head_text = "Scrobbles: " + lView.getCount() + ((filter_string == null) ? "" : " within " + filter_string);
                ((TextView) list_head.findViewById(R.id.list_head)).setText(list_head_text);
            }
            if (exception == 0 && result.size() == 0 && lView.getCount() > 0) {
                allIsLoaded = true;
                wasEmpty = false;
            }
            if (exception == 0 && result.size() == 0 && lView.getCount() == 0) {
                empty_list.setVisibility(View.VISIBLE);
                empty_list_text = "No scrobbles" + ((filter_string == null) ? "" : "\nwithin " + filter_string);
                ((TextView) empty_list.findViewById(R.id.empty_list_text)).setText(empty_list_text);
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
        protected void onPreExecute(){
            empty_list.setVisibility(View.GONE);
            progressBar.setProgress(0);
            progressBar.setMax(limit);
            lView.addFooterView(list_spinner);
        }

        @Override
        protected void onProgressUpdate(Integer... values){
            super.onProgressUpdate(values);
            progressBar.setProgress((values[0]));
        }

        @Override
        protected void onCancelled() {
            isLoading = false;
        }
    }
}