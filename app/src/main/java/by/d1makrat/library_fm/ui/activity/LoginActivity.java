package by.d1makrat.library_fm.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.GetSessionKeyAsynctaskCallback;
import by.d1makrat.library_fm.GetUserInfoAsynctaskCallback;
import by.d1makrat.library_fm.HttpsClient;
import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.asynctask.GetSessionKeyAsynctask;
import by.d1makrat.library_fm.asynctask.GetUserInfoAsynctask;
import by.d1makrat.library_fm.model.User;

//TODO Firebase Test Lab
public class LoginActivity extends AppCompatActivity implements GetSessionKeyAsynctaskCallback, GetUserInfoAsynctaskCallback {

    private GetSessionKeyAsynctask getSessionKeyAsynctask;
    private TextView username_field = null;
    private TextView password_field = null;
    public String mUsername;
    private ProgressBar spinner = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sign_in_page);

        spinner = findViewById(R.id.progressBar2);
        username_field = findViewById(R.id.login);
        password_field = findViewById(R.id.password);

        if (AppContext.getInstance().getSessionKey() != null && AppContext.getInstance().getUser() != null){
            startActivity(new Intent(this, MainActivity.class));
        }
        else {
            findViewById(R.id.enter_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    if (HttpsClient.isNetworkAvailable()) {
                        mUsername = username_field.getText().toString();
                        String password = password_field.getText().toString();

                        getSessionKeyAsynctask = new GetSessionKeyAsynctask(LoginActivity.this);
                        getSessionKeyAsynctask.execute(mUsername, password);

                        spinner.setVisibility(View.VISIBLE);
                        username_field.setVisibility(View.INVISIBLE);
                        password_field.setVisibility(View.INVISIBLE);
                    }
                    else
                        Toast.makeText(getApplicationContext(), R.string.network_is_not_connected, Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    public void onSessionKeyGranted(String sessionKey) {
        AppContext.getInstance().setSessionKey(sessionKey);
        AppContext.getInstance().saveSettings();

        GetUserInfoAsynctask getUserInfoAsynctask = new GetUserInfoAsynctask(this);
        getUserInfoAsynctask.execute();

//        NetworkStateReceiver networkStateReceiver =

    }

    @Override
    public void onUserInfoReceived(User result) {
        AppContext.getInstance().setUser(result);
        AppContext.getInstance().saveSettings();

        spinner.setVisibility(View.INVISIBLE);

        startActivity(new Intent(this, MainActivity.class));

        finish();
    }

    @Override
    public void onException(Exception exception) {
        username_field.setVisibility(View.VISIBLE);
        password_field.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.INVISIBLE);
        findViewById(R.id.enter_button).setVisibility(View.VISIBLE);

        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
    }
}

