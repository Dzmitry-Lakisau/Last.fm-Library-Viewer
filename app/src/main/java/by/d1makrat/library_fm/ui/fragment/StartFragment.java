package by.d1makrat.library_fm.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
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
import by.d1makrat.library_fm.ui.fragment.dialog.AboutDialogFragment;

public class StartFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_start, container, false);

        AdView adView = rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        setUpActionBar((AppCompatActivity) getActivity());

        User user = AppContext.getInstance().getUser();
        Malevich.INSTANCE.load(user.getAvatarUrl()).onError(getResources().getDrawable(R.drawable.img_app_logo_large)).
                into((ImageView) rootView.findViewById(R.id.user_avatar_start_screen));

        TextView textView = rootView.findViewById(R.id.hello_textView);
        textView.setText(getString(R.string.hello_message, user.getUsername(), user.getPlaycount(), user.getRegistered()));
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_about_app, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                if (getFragmentManager() != null) {
                    AboutDialogFragment dialogFragment = new AboutDialogFragment();
                    dialogFragment.setTargetFragment(this, 0);
                    dialogFragment.show(getFragmentManager(), "AboutDialogFragment");
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void setUpActionBar(AppCompatActivity pActivity) {
        ActionBar actionBar = pActivity.getSupportActionBar();
        if (actionBar != null){
            actionBar.setTitle(R.string.app_name);
            actionBar.setSubtitle(null);
        }
    }
}
