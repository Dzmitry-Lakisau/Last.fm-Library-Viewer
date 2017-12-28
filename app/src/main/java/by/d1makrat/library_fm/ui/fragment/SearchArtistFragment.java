package by.d1makrat.library_fm.ui.fragment;

import android.app.Activity;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import by.d1makrat.library_fm.HttpsClient;
import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.SearchArtistsAsynctaskCallback;
import by.d1makrat.library_fm.adapter.list.SearchArtistsAdapter;
import by.d1makrat.library_fm.asynctask.SearchArtistsAsynctask;
import by.d1makrat.library_fm.model.Artist;

public class SearchArtistFragment extends ListFragment implements OnScrollListener, SearchArtistsAsynctaskCallback {

    private String url = null;
    private String empty_list_text = null;

    private boolean isLoading = false;
    private boolean isCreated = true;
    private boolean allIsLoaded = false;
    private boolean wasEmpty = false;

    private AsyncTask task;
    private BaseAdapter mListAdapter;
    private View rootView;
    private ListView mListView;
    private View empty_list;
    private Menu menu;
    private List<Artist> mArtists = new ArrayList<>();
    private EditText search_field;

    private int mPage;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        url = "https://www.last.fm/search/artists?q=";
        mPage = 1;
        setHasOptionsMenu(true);
        mListAdapter = createAdapter();
    }

//    @Override
    protected BaseAdapter createAdapter(){

        Drawable drawable;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = getResources().getDrawable(R.drawable.ic_person);
        } else
            drawable = getResources().getDrawable(R.drawable.ic_person, null);

        return new SearchArtistsAdapter(getActivity().getLayoutInflater(), drawable, mArtists);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.search, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.search));
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(null);

        if (!isCreated) {
            if (wasEmpty) {
                //было загружено и пусто
                rootView.findViewById(R.id.empty_list).setVisibility(View.VISIBLE);
                ((TextView) rootView.findViewById(R.id.empty_list_text)).setText(empty_list_text);
            } else {
                //было загружено и данные не пустые
//                rootView.findViewById(R.id.list_head).setVisibility(View.VISIBLE);
//                ((TextView) rootView.findViewById(R.id.list_head)).setText(list_head_text);
            }
        } else {
            if (!HttpsClient.isNetworkAvailable()) {
                //создаётся и сеть отсуствует
                rootView.findViewById(R.id.empty_list).setVisibility(View.VISIBLE);
                ((TextView) rootView.findViewById(R.id.empty_list).findViewById(R.id.empty_list_text)).setText(R.string.network_is_not_connected);
                empty_list_text = getText(R.string.network_is_not_connected).toString();
                wasEmpty = true;
            }
        }

        mListView = (ListView) rootView.findViewById(android.R.id.list);
        empty_list = rootView.findViewById(R.id.empty_list);

        View list_spinner = getActivity().getLayoutInflater().inflate(R.layout.list_spinner, (ViewGroup) null);
        mListView.addFooterView(list_spinner);
        mListView.setAdapter(mListAdapter);
        mListView.removeFooterView(list_spinner);

//        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(this);
        registerForContextMenu(mListView);

        search_field = (EditText) rootView.findViewById(R.id.search_field);

        rootView.findViewById(R.id.search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

                mArtists.clear();
                mListAdapter.notifyDataSetChanged();
                mListView.getFooterViewsCount();
                KillTaskIfRunning(task);
                String query = search_field.getText().toString();
                if (query.length() > 0) {
//                    Log.d("DEBUG",query);
                    LoadItems(mPage, query);
                }
            }
        });

        search_field.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                rootView.findViewById(R.id.search_button).setEnabled(true);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        return rootView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String artist = ((TextView) v.findViewById(R.id.track)).getText().toString();
        Bundle bundle = new Bundle();
        FragmentTransaction fragmentTransaction;
        bundle.putString("artist", artist);
        Fragment fragment = new ScrobblesOfArtistFragment();
        fragment.setArguments(bundle);
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_main, fragment, "ScrobblesOfArtistFragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onScrollStateChanged(AbsListView l, int ScrollState) {
    }

    @Override
    public void onScroll(AbsListView l, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if ((firstVisibleItem + visibleItemCount) == totalItemCount && (totalItemCount > 0) && !isLoading) {
            if (HttpsClient.isNetworkAvailable()) {
                mPage++;
                LoadItems(mPage, search_field.getText().toString());
            } else {
                Toast toast;
                toast = Toast.makeText(getContext(), R.string.network_is_not_connected, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_search, menu);

        this.menu = menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.open_in_browser:
                Uri address = Uri.parse(url + search_field.getText().toString());
                Intent intent = new Intent(Intent.ACTION_VIEW, address);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
//        super.onCreateContextMenu(menu, v ,menuInfo);
//        getActivity().getMenuInflater().inflate(R.menu.context_menu, menu);
//    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (HttpsClient.isNetworkAvailable()) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            switch (item.getItemId()) {
                case R.id.scrobbles_of_artist:
                    String artist = ((TextView) info.targetView.findViewById(R.id.track)).getText().toString();
                    Bundle bundle = new Bundle();
                    FragmentTransaction fragmentTransaction;
                    bundle.putString("artist", artist);
                    Fragment fragment = new ScrobblesOfArtistFragment();
                    fragment.setArguments(bundle);
                    fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_main, fragment, "ScrobblesOfArtistFragment");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    return true;
                default:
                    return super.onContextItemSelected(item);
            }
        } else {
            Toast toast;
            toast = Toast.makeText(getContext(), R.string.network_is_not_connected, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
    }

    private void KillTaskIfRunning(AsyncTask task) {
        if (task != null && task.getStatus() != AsyncTask.Status.FINISHED) {
            mPage = 1;
            task.cancel(true);
        }
    }

    @Override
    public void onException(Exception pException) {
        Toast.makeText(getContext(), pException.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoadingSearchArtistsResultsSuccessful(List<Artist> result) {
        isLoading = false;
        wasEmpty = false;

        if (result.size() > 0) {
            mArtists.addAll(result);
            mListAdapter.notifyDataSetChanged();
        }
    }

    public void LoadItems(Integer page, String artist) {
        if (HttpsClient.isNetworkAvailable()) {
            if (!allIsLoaded) {
                isLoading = true;

                task = new SearchArtistsAsynctask(this);
                task.execute(new String[]{artist, String.valueOf(page)});
            }
        } else {
            empty_list.setVisibility(View.VISIBLE);
            ((TextView) empty_list.findViewById(R.id.empty_list_text)).setText(R.string.network_is_not_connected);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isCreated = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        isCreated = false;//TODO isCreated должно быть true и false только В onPostExecute
        KillTaskIfRunning(task);
    }
}