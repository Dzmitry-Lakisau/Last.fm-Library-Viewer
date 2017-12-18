package by.d1makrat.library_fm.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import by.d1makrat.library_fm.NetworkStatusChecker;
import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.adapter.pages.TopAlbumsAdapter;
import by.d1makrat.library_fm.adapter.pages.TopArtistsAdapter;
import by.d1makrat.library_fm.adapter.pages.TopTracksAdapter;

public class TabFragment extends Fragment {

    private String username = null;
    private String sessionKey= null;
    private FragmentPagerAdapter adapter;
    ViewPager pager;
    private String cachepath = null;
    private String tabfragment_type = null;
    private boolean isCreated = true;
    private String resolution;
    private Menu menu;

    private final String[] date_presets= {"", "?date_preset=LAST_7_DAYS", "?date_preset=LAST_30_DAYS", "?date_preset=LAST_90_DAYS", "?date_preset=LAST_180_DAYS", "?date_preset=LAST_365_DAYS"};

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View v = inflater.inflate(R.layout.viewpager, container, false);
        pager = (ViewPager) v.findViewById(R.id.viewpager);
        pager.setOffscreenPageLimit(5);

        switch (tabfragment_type){
            case "albums":
                adapter = new TopAlbumsAdapter(getChildFragmentManager());
                break;
            case "artists":
                adapter = new TopArtistsAdapter(getChildFragmentManager());
                break;
            case "tracks":
                adapter = new TopTracksAdapter(getChildFragmentManager());
                break;
        }
        pager.setAdapter(adapter);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_top, menu);
        this.menu = menu;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        sessionKey = getArguments().getString("sessionKey");
        username = getArguments().getString("username");
        cachepath = getArguments().getString("cachepath");
        tabfragment_type = getArguments().getString("tabfragment_type");
        resolution = getArguments().getString("resolution");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.open_in_browser:
                int i = pager.getCurrentItem();
                Uri address = Uri.parse("https://www.last.fm/user/" + username + "/library/" + tabfragment_type + date_presets[i]);
                Intent intent = new Intent(Intent.ACTION_VIEW, address);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (NetworkStatusChecker.isNetworkAvailable()) {
            return false;
        } else {
            Toast toast;
            toast = Toast.makeText(getContext(), R.string.network_is_not_connected, Toast.LENGTH_SHORT);
            toast.show();
            return true;
        }
    }

    public void onAsynctaskExecuted(){
        menu.getItem(0).setEnabled(true);
    }
}

