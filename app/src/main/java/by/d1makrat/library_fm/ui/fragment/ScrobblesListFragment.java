package by.d1makrat.library_fm.ui.fragment;

import android.graphics.drawable.Drawable;
import android.os.Build;
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
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import by.d1makrat.library_fm.NetworkStatusChecker;
import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.adapter.ScrobblesListAdapter;
import by.d1makrat.library_fm.image_loader.Malevich;
import by.d1makrat.library_fm.UrlConstructor;
import by.d1makrat.library_fm.model.Scrobble;
import by.d1makrat.library_fm.ui.GetScrobblesAsynctask;
import by.d1makrat.library_fm.ui.GetScrobblesAsynctaskCallback;

import static by.d1makrat.library_fm.Constants.*;

public class ScrobblesListFragment extends ListFragment implements OnScrollListener, FilterDialogFragment.DialogListener, GetScrobblesAsynctaskCallback {

//    private ResolutionOfImage resolutionOfImage;
    private Uri urlForBrowser = null;
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

    private GetScrobblesAsynctask mGetScrobblesAsynctask;
    private ScrobblesListAdapter mScrobblesListAdapter;
    private ListView mListView;
    private View empty_list;
    private View list_head;
//    private Menu menu;

    private List<Scrobble> mScrobbles = new ArrayList<>();
    private int page;
//    private int limit;//max=1000
    private View mFooterView;
//    private ProgressBar progressBar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Malevich.INSTANCE.setConfig(new Malevich.Config(getActivity().getApplicationContext().getCacheDir()));//TODO move into Application class?

        UrlConstructor urlConstructor = new UrlConstructor(getActivity().getApplicationContext());
        urlForBrowser = urlConstructor.constructRecentScrobblesUrlForBrowser(from, to); //"https://www.last.fm/user/" + username + "/library";
        page = 1;

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.list_with_head, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.scrobbles));
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(null);

        if (!isCreated) {
            if (wasEmpty) {
                //было загружено и пусто
                rootView.findViewById(R.id.empty_list).setVisibility(View.VISIBLE);
                ((TextView) rootView.findViewById(R.id.empty_list_text)).setText(empty_list_text);
            } else {
                //было загружено и данные не пустые
                rootView.findViewById(R.id.list_head).setVisibility(View.VISIBLE);
                ((TextView) rootView.findViewById(R.id.list_head)).setText(list_head_text);
            }
        } else {
            if (!NetworkStatusChecker.isNetworkAvailable(getActivity().getApplicationContext())) {
                //создаётся и сеть отсуствует

                //loadItemsFromDatabase
            }
        }

        Drawable drawable;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = getResources().getDrawable(R.drawable.default_albumart);
        } else
            drawable = getResources().getDrawable(R.drawable.default_albumart, null);

        mScrobblesListAdapter = new ScrobblesListAdapter(getActivity().getLayoutInflater(), drawable, mScrobbles);// SimpleAdapter(getActivity(), mScrobbles, R.layout.list_item, new String[]{"name", "artist", "album", "date", "image"}, new int[]{R.id.track, R.id.artist, R.id.album, R.id.timestamp, R.id.albumart});
        mListView = (ListView) rootView.findViewById(android.R.id.list);
        empty_list = rootView.findViewById(R.id.empty_list);
        list_head = rootView.findViewById(R.id.list_head);

        mFooterView = getActivity().getLayoutInflater().inflate(R.layout.list_spinner, (ViewGroup) null);
        mListView.addFooterView(mFooterView);
        mListView.setAdapter(mScrobblesListAdapter);
        mListView.removeFooterView(mFooterView);

//        progressBar = mFooterView.findViewById(R.id.progressbar);

//        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(this);
        registerForContextMenu(mListView);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

//        AppSettings appSettings = new AppSettings(getActivity().getApplicationContext());
//        limit = Integer.parseInt(appSettings.getLimit());
//        resolutionOfImage = appSettings.getResolutionOfImage();

        if (isCreated)
            loadItemsFromWeb(page, null, null);
    }

    @Override
    public void onScrollStateChanged(AbsListView l, int ScrollState) {
    }

    @Override
    public void onScroll(AbsListView l, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if ((firstVisibleItem + visibleItemCount) == totalItemCount && (totalItemCount > 0) && !isLoading) {
            if (NetworkStatusChecker.isNetworkAvailable(getActivity().getApplicationContext())) {
                page++;
                loadItemsFromWeb(page, from, to);
            } else {
                //loadItemsFromDatabase
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_scrobbles, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                if (NetworkStatusChecker.isNetworkAvailable(getActivity().getApplicationContext())) {
                    if (!isLoading) {
                        allIsLoaded = false;
                        getView().findViewById(R.id.list_head).setVisibility(View.GONE);
                        KillTaskIfRunning(mGetScrobblesAsynctask);
                        mScrobbles.clear();
                        mScrobblesListAdapter.notifyDataSetChanged();
                        page = 1;
                        loadItemsFromWeb(page, from, to);
                    }
                } else {
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
                Intent intent = new Intent(Intent.ACTION_VIEW, urlForBrowser);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu pContextMenu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(pContextMenu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.context_menu, pContextMenu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem pMenuItem) {

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        View listItemPressed = ((AdapterContextMenuInfo) pMenuItem.getMenuInfo()).targetView;
        Fragment fragment;

        switch (pMenuItem.getItemId()) {
            case R.id.scrobbles_of_artist:
                fragment = createFragment(SCROBBLES_OF_ARTIST_TAG, listItemPressed);
                fragmentTransaction.replace(R.id.content_main, fragment, SCROBBLES_OF_ARTIST_TAG);
                break;
            case R.id.scrobbles_of_track:
                fragment = createFragment(SCROBBLES_OF_TRACK_TAG, listItemPressed);
                fragmentTransaction.replace(R.id.content_main, fragment, SCROBBLES_OF_TRACK_TAG);
                break;
            case R.id.scrobbles_of_album:
                fragment = createFragment(SCROBBLES_OF_ALBUM_TAG, listItemPressed);
                fragmentTransaction.replace(R.id.content_main, fragment, SCROBBLES_OF_ALBUM_TAG);
                break;
            default:
                return super.onContextItemSelected(pMenuItem);
        }
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        return true;
    }

    private Fragment createFragment(String pTypeOfFragment, View pListItemPressed){

        Fragment fragment = new Fragment();
        Bundle bundle = new Bundle();

        String artist = ((TextView) pListItemPressed.findViewById(R.id.artist)).getText().toString();

        switch (pTypeOfFragment) {
            case SCROBBLES_OF_ARTIST_TAG:
                fragment = new ScrobblesOfArtistFragment();
                break;
            case SCROBBLES_OF_TRACK_TAG:
                String track = ((TextView) pListItemPressed.findViewById(R.id.track)).getText().toString();
                fragment = new ScrobblesOfTrackFragment();
                bundle.putString("track", track);
                break;
            case SCROBBLES_OF_ALBUM_TAG:
                String album = ((TextView) pListItemPressed.findViewById(R.id.album)).getText().toString();
                fragment = new ScrobblesOfAlbumFragment();
                bundle.putString("album", album);
                break;
        }
        bundle.putString("artist", artist);
        fragment.setArguments(bundle);
        return fragment;
    }


    private void KillTaskIfRunning(AsyncTask task) {
        if (task != null && task.getStatus() != AsyncTask.Status.FINISHED) {
            page--;
            task.cancel(true);
        }
    }

    public void loadItemsFromWeb(Integer page, String from, String to) {
        if (!allIsLoaded) {
            isLoading = true;

            URL url = null;
            try {
                UrlConstructor urlConstructor = new UrlConstructor(getActivity().getApplicationContext());
                url = urlConstructor.constructRecentScrobblesApiRequestUrl(page, from, to);
            } catch (MalformedURLException e) {
                onException(e);
            }

            mListView.addFooterView(mFooterView);

            mGetScrobblesAsynctask = new GetScrobblesAsynctask(this);
            mGetScrobblesAsynctask.execute(url);
        }
    }

    @Override
    public void onFinishEditDialog(String from, String to) {
        getView().findViewById(R.id.list_head).setVisibility(View.GONE);
        allIsLoaded = false;
        this.from = from;
        this.to = to;
        mScrobbles.clear();
        mScrobblesListAdapter.notifyDataSetChanged();
        page = 1;
        filter_string = null;

        UrlConstructor urlConstructor = new UrlConstructor(getActivity().getApplicationContext());
        urlForBrowser = urlConstructor.constructRecentScrobblesUrlForBrowser(from, to);

        KillTaskIfRunning(mGetScrobblesAsynctask);
        loadItemsFromWeb(page, from, to);
    }

    @Override
    public void onPause() {
        super.onPause();
        isCreated = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        isCreated = false;//TODO isCreated должно быть екгу и афдыу только В onPostExecute
        KillTaskIfRunning(mGetScrobblesAsynctask);
    }

    @Override
    public void onLoadingScrobblesSuccessful(List<Scrobble> scrobbles) {
        mListView.removeFooterView(mFooterView);
        isLoading = false;
        wasEmpty = false;
//        if (exception == 0 && scrobbles.size() > 0){
        if (scrobbles.size() > 0) {
            list_head.setVisibility(View.VISIBLE);
            // result.get(i));
            mScrobbles.addAll(scrobbles);
            mScrobblesListAdapter.notifyDataSetChanged();
            list_head_text = "Scrobbles: " + mListView.getCount() + ((filter_string == null) ? "" : " within " + filter_string);
            ((TextView) list_head.findViewById(R.id.list_head)).setText(list_head_text);
        }
    }

    @Override
    public void onException(Exception pException) {
        mListView.removeFooterView(mFooterView);
        Toast.makeText(getContext(), pException.getMessage(), Toast.LENGTH_LONG).show();//TODO get context activity or fragment here?
    }
}

//        @Override
//        protected void onPostExecute(List<Scrobble> result) {
//            mListView.removeFooterView(mFooterView);
//            isLoading = false;
//            wasEmpty = false;
//            if (exception == 0 && result.size() > 0){
//                list_head.setVisibility(View.VISIBLE);
//                for (int i = 0; i < result.size(); i++) {
//                    mScrobbles.add(result.get(i));// result.get(i));
//                }
//                mScrobblesListAdapter.notifyDataSetChanged();
//                list_head_text = "Scrobbles: " + mListView.getCount() + ((filter_string == null) ? "" : " within " + filter_string);
//                ((TextView) list_head.findViewById(R.id.list_head)).setText(list_head_text);
//            }
//            if (exception == 0 && result.size() == 0 && mListView.getCount() > 0) {
//                allIsLoaded = true;
//                wasEmpty = false;
//            }
//            if (exception == 0 && result.size() == 0 && mListView.getCount() == 0) {
//                empty_list.setVisibility(View.VISIBLE);
//                empty_list_text = "No scrobbles" + ((filter_string == null) ? "" : "\nwithin " + filter_string);
//                ((TextView) empty_list.findViewById(R.id.empty_list_text)).setText(empty_list_text);
//            }
//            if (exception == 1 && mListView.getCount() == 0){
//                page--;
//                empty_list.setVisibility(View.VISIBLE);
//                empty_list_text = "Error occurred";
//                ((TextView) empty_list.findViewById(R.id.empty_list_text)).setText(empty_list_text);
//                wasEmpty = true;
//                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
//            }
//            if (exception == 1 && mListView.getCount() > 0) {
//                page--;
//                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
//            }
//            if (exception > 1 && mListView.getCount() == 0){
//                page--;
//                empty_list.setVisibility(View.VISIBLE);
//                empty_list_text = "Error occurred";
//                ((TextView) empty_list.findViewById(R.id.empty_list_text)).setText(empty_list_text);
//                wasEmpty = true;
//                String[] exception_message = getResources().getStringArray(R.array.Exception_messages);
//                Toast.makeText(getContext(), exception_message[exception - 1], Toast.LENGTH_SHORT).show();
//            }
//            if (exception > 1 && mListView.getCount() > 0) {
//                page--;
//                String[] exception_message = getResources().getStringArray(R.array.Exception_messages);
//                Toast.makeText(getContext(), exception_message[exception - 1], Toast.LENGTH_SHORT).show();
//            }
//        }

//        @Override
//        protected void onCancelled() {
//            isLoading = false;
//        }