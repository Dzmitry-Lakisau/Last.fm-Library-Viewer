package by.d1makrat.library_fm.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        }
        else {
            Toast toast;
            toast = Toast.makeText(getContext(), R.string.network_is_not_connected, Toast.LENGTH_SHORT);
            toast.show();
            return true;
        }
    }


    public class TopAlbumsAdapter extends FragmentPagerAdapter {
//        Context context = null;
        private final String periods[] = new String[]{"overall", "7day", "1month", "3month", "6month",  "12month"};
        private final String tab_names[] = new String[]{"Overall", "Week", "Month", "3 months", "6 months", "Year"};
//        private String sessionKey = null;
//        private String username = null;
//        private String cachepath = null;

        public TopAlbumsAdapter(FragmentManager mgr) {
            super(mgr);
        }

        @Override
        public int getCount() {
            return(periods.length);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putString("sessionKey", sessionKey);
            bundle.putString("username", username);
            bundle.putString("period", periods[position]);
            bundle.putString("cachepath", cachepath);
            bundle.putString("resolution", resolution);
            Fragment fragment = new TopAlbumsFragment();
            fragment.setArguments(bundle);
//        viewpager.setRetainInstance(true);
//            Log.d("DEBUG", "viewpager " + position + " is showed");
            return fragment;
        }

        @Override
        public String getPageTitle(int position) {
            return tab_names[position];
        }
    }

    public class TopArtistsAdapter extends FragmentPagerAdapter {
        //        Context context = null;
        private final String periods[] = new String[]{"overall", "7day", "1month", "3month", "6month",  "12month"};
        private final String tab_names[] = new String[]{"Overall", "Week", "Month", "3 months", "6 months", "Year"};
//        private String sessionKey = null;
//        private String username = null;
//        private String cachepath = null;

        public TopArtistsAdapter(FragmentManager mgr) {
            super(mgr);
//            this.context = context;
//            sessionKey = params[0];
//            username = params[1];
//            cachepath = params[2];
        }

        @Override
        public int getCount() {
            return(periods.length);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putString("sessionKey", sessionKey);
            bundle.putString("username", username);
            bundle.putString("period", periods[position]);
            bundle.putString("cachepath", cachepath);
            bundle.putString("resolution", resolution);
            Fragment fragment = new TopArtistsFragment();
            fragment.setArguments(bundle);
//        viewpager.setRetainInstance(true);
//            Log.d("DEBUG", "viewpager " + position + " is showed");
            return fragment;
        }

        @Override
        public String getPageTitle(int position) {
            return tab_names[position];
        }
    }

    public class TopTracksAdapter extends FragmentPagerAdapter {
        //        Context context = null;
        private final String periods[] = new String[]{"overall", "7day", "1month", "3month", "6month",  "12month"};
        private final String tab_names[] = new String[]{"Overall", "Week", "Month", "3 months", "6 months", "Year"};
//        private String sessionKey = null;
//        private String username = null;
//        private String cachepath = null;

        public TopTracksAdapter(FragmentManager mgr) {
            super(mgr);
//            this.context = context;
//            sessionKey = params[0];
//            username = params[1];
//            cachepath = params[2];
        }

        @Override
        public int getCount() {
            return(periods.length);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putString("sessionKey", sessionKey);
            bundle.putString("username", username);
            bundle.putString("period", periods[position]);
            bundle.putString("cachepath", cachepath);
            bundle.putString("resolution", resolution);
            Fragment fragment = new TopTracksFragment();
            fragment.setArguments(bundle);
//        viewpager.setRetainInstance(true);
//            Log.d("DEBUG", "viewpager " + position + " is showed");
            return fragment;
        }

        @Override
        public String getPageTitle(int position) {
            return tab_names[position];
        }
    }

    public void onAsynctaskExecuted(){
        menu.getItem(0).setEnabled(true);
    }
}

