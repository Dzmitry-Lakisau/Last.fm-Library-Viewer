package by.d1makrat.library_fm.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.adapter.pages.TopArtistsAdapter;

public class TabTopArtistsFragment extends Fragment {

    private TopArtistsAdapter adapter;
    private ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new TopArtistsAdapter(getChildFragmentManager());

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.top_artists);

        View view = inflater.inflate(R.layout.viewpager, container, false);
        viewPager = view.findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(adapter.getCount() - 1);
        viewPager.setAdapter(adapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_top, menu);
//        this.menu = menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.open_in_browser:
                Uri address = adapter.getUrlForBrowser(viewPager.getCurrentItem());
                startActivity(new Intent(Intent.ACTION_VIEW, address));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}