package by.d1makrat.library_fm.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.adapter.list.SearchArtistsAdapter;
import by.d1makrat.library_fm.asynctask.GetItemsAsyncTask;
import by.d1makrat.library_fm.asynctask.GetItemsCallback;
import by.d1makrat.library_fm.model.Artist;
import by.d1makrat.library_fm.operation.SearchArtistOperation;
import by.d1makrat.library_fm.ui.CenteredToast;
import by.d1makrat.library_fm.utils.InputUtils;

public class SearchArtistFragment extends ItemsFragment<Artist> implements GetItemsCallback<Artist>{

    private static final int MENU_SCROBBLES_OF_ARTIST = 0;
    private static final int MENU_OPEN_IN_BROWSER = 1;
    private String mSearchQuery;
    private Button mSearchButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUrlForBrowser = "https://www.last.fm/search/artists?q=";
    }

    @Override
    protected SearchArtistsAdapter createAdapter(LayoutInflater pLayoutInflater){
        return new SearchArtistsAdapter(pLayoutInflater, ContextCompat.getDrawable(AppContext.getInstance(), R.drawable.ic_person_black_24dp_large));
    }

    @Override
    protected void performOperation() {
        SearchArtistOperation searchArtistOperation = new SearchArtistOperation(mSearchQuery, mPage);
        GetItemsAsyncTask<Artist> getItemsAsyncTask = new GetItemsAsyncTask<>(this);
        getItemsAsyncTask.execute(searchArtistOperation);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        setUpRecyclerView(rootView);
        setUpActionBar((AppCompatActivity) getActivity());

        mSearchButton = rootView.findViewById(R.id.search_button);

        ((EditText) rootView.findViewById(R.id.search_field)).addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                mSearchButton.setEnabled(s.length() > 0);
                mSearchQuery = s.toString();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLoading) {
                    InputUtils.hideKeyboard(getActivity());

                    mListAdapter.removeAll();

                    allIsLoaded = false;

                    killTaskIfRunning(mGetItemsAsynctask);

                    mPage = 1;
                    loadItems();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_options, menu);
        menu.removeItem(R.id.action_refresh);
        menu.removeItem(R.id.action_filter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.open_in_browser:
                Uri address = Uri.parse(mUrlForBrowser + mSearchQuery);
                Intent intent = new Intent(Intent.ACTION_VIEW, address);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add(0, MENU_SCROBBLES_OF_ARTIST, 0, R.string.scrobbles_of_artist);
        menu.add(0, MENU_OPEN_IN_BROWSER, 1, R.string.open_in_browser);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_SCROBBLES_OF_ARTIST:
                replaceFragment(mListAdapter.getSelectedItem().getName(), null, null);
                return true;
            case MENU_OPEN_IN_BROWSER:
                Uri address = Uri.parse(mListAdapter.getSelectedItem().getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, address);
                startActivity(intent);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onLoadingSuccessful(List<Artist> items) {
        isLoading = false;

        mListAdapter.removeAllHeadersAndFooters();

        int size = items.size();
        if (size > 0) {
            mListAdapter.addAll(items);

            checkIfAllIsLoaded(size);
        }
        else if (mListAdapter.isEmpty()){
            mListAdapter.addEmptyHeader(getString(R.string.search_fragment_no_result));
        }
        else {
            checkIfAllIsLoaded(size);
        }
    }

    protected void checkIfAllIsLoaded(int size){
        if (size < AppContext.getInstance().getLimit()){
            allIsLoaded = true;
            CenteredToast.show(getContext(), R.string.all_artists_are_loaded, Toast.LENGTH_SHORT);
        }
    }

    @Override
    protected void setUpActionBar(AppCompatActivity pActivity) {
        ActionBar actionBar = pActivity.getSupportActionBar();
        if (actionBar != null){
            actionBar.setTitle(R.string.search_artist);
        }
    }
}
