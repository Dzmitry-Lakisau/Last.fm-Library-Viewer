package by.d1makrat.library_fm.ui.fragment.tabTop;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.adapter.pages.TabTopAdapter;

public abstract class TabTopItemsFragment<A extends TabTopAdapter> extends Fragment {

    private A adapter;
    private ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       adapter = createAdapter();

        View view = inflater.inflate(R.layout.viewpager, container, false);
        viewPager = view.findViewById(R.id.viewpager);
//        viewPager.setOffscreenPageLimit(0);
        viewPager.setAdapter(adapter);

        return view;
    }

    protected abstract A createAdapter();

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_options, menu);
        menu.removeItem(R.id.action_filter);
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
