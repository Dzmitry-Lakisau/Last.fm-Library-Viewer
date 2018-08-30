package by.d1makrat.library_fm.ui.activity;

import android.app.Activity;
import android.database.SQLException;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.io.IOException;

import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.database.DatabaseWorker;
import by.d1makrat.library_fm.image_loader.Malevich;
import by.d1makrat.library_fm.ui.CenteredToast;

import static by.d1makrat.library_fm.Constants.API_MAX_FOR_SCROBBLES_BY_ARTIST;
import static by.d1makrat.library_fm.utils.InputUtils.hideKeyboard;

public class PreferenceActivity extends Activity {

    private static final int MAX_ITEMS_PER_REQUEST = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_preferences);

        ((EditText) findViewById(R.id.set_limit_editText)).setHint(String.valueOf(AppContext.getInstance().getLimit()));

        ((EditText) findViewById(R.id.set_limit_editText)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                findViewById(R.id.set_limit_button).setEnabled(s.length() > 0);
            }
        });

        findViewById(R.id.set_limit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int temp = Integer.parseInt(((EditText) findViewById(R.id.set_limit_editText)).getText().toString());
                    if (temp > 0 && temp <= MAX_ITEMS_PER_REQUEST) {
                        if (temp > API_MAX_FOR_SCROBBLES_BY_ARTIST) {
                            CenteredToast.show(getApplicationContext(), R.string.limit_more_than_200, Toast.LENGTH_LONG);
                        }
                        AppContext.getInstance().setLimit(String.valueOf(temp));
                        CenteredToast.show(getApplicationContext(), R.string.limit_has_been_set, Toast.LENGTH_SHORT);
                        hideKeyboard(PreferenceActivity.this);
                        ((EditText) findViewById(R.id.set_limit_editText)).setHint(String.valueOf(AppContext.getInstance().getLimit()));
                    } else {
                        CenteredToast.show(getApplicationContext(), R.string.limit_must_be_between, Toast.LENGTH_SHORT);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                    CenteredToast.show(getApplicationContext(), R.string.limit_nonnumerical_input, Toast.LENGTH_SHORT);
                }
            }
        });

        findViewById(R.id.clear_image_cache_textView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Malevich.INSTANCE.clearCache();

                    CenteredToast.show(getApplicationContext(), R.string.OK, Toast.LENGTH_SHORT);
                } catch (IOException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                    CenteredToast.show(getApplicationContext(), R.string.unable_to_clear_cache, Toast.LENGTH_SHORT);
                }
            }
        });

        findViewById(R.id.drop_database_textView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseWorker databaseWorker = new DatabaseWorker();

                try {
                    databaseWorker.openDatabase();
                    databaseWorker.deleteScrobbles();
                    databaseWorker.deleteTopAlbums(null);
                    databaseWorker.deleteTopArtists(null);
                    databaseWorker.deleteTopTracks(null);

                    CenteredToast.show(getApplicationContext(), R.string.OK, Toast.LENGTH_SHORT);
                }
                catch (SQLException e){
                    e.printStackTrace();
                    Crashlytics.logException(e);
                    CenteredToast.show(getApplicationContext(), R.string.unable_to_drop_database, Toast.LENGTH_SHORT);
                }
                finally {
                    databaseWorker.closeDatabase();
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        AppContext.getInstance().saveSettings();
    }
}
