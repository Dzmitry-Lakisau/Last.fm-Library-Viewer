package by.d1makrat.library_fm.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.image_loader.Malevich;
import by.d1makrat.library_fm.model.User;

public class StartFragment extends Fragment {

    private Menu mMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.start_layout, container, false);

        AdView adView = rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(null);

        User user = AppContext.getInstance().getUser();
        Malevich.INSTANCE.load(user.getAvatarUri()).into((ImageView) rootView.findViewById(R.id.user_avatar_start_screen));

        TextView textView = rootView.findViewById(R.id.hello_start_screen);
        textView.setText(String.format("You are logged in as %s.\n%s scrobbles since %s.\nExplore your music!", user.getUsername(), user.getPlaycount(), user.getRegistered()));
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                AboutDialogFragment dialogFragment = new AboutDialogFragment();
                dialogFragment.setTargetFragment(this, 0);
                dialogFragment.show(getFragmentManager(), "AboutDialogFragment");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
