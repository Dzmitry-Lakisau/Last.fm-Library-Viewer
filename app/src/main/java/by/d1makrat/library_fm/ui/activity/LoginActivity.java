package by.d1makrat.library_fm.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.asynctask.GetSessionKeyCallback;
import by.d1makrat.library_fm.asynctask.GetUserInfoCallback;
import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.asynctask.GetSessionKeyAsyncTask;
import by.d1makrat.library_fm.asynctask.GetUserInfoAsyncTask;
import by.d1makrat.library_fm.https.HttpsClient;
import by.d1makrat.library_fm.model.User;

import static by.d1makrat.library_fm.utils.InputUtils.hideKeyboard;

public class LoginActivity extends AppCompatActivity implements GetSessionKeyCallback, GetUserInfoCallback {

    private GetSessionKeyAsyncTask getSessionKeyAsyncTask;
    private TextView username_field;
    private TextView password_field;
    private View spinner;
    private View mButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        spinner = findViewById(R.id.progressBar2);
        username_field = findViewById(R.id.login);
        password_field = findViewById(R.id.password);
        mButton = findViewById(R.id.enter_button);

        if (AppContext.getInstance().getSessionKey() != null && AppContext.getInstance().getUser() != null){
            startActivity(new Intent(this, MainActivity.class));
        }
        else {
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideKeyboard(LoginActivity.this);
                    if (HttpsClient.isNetworkAvailable()) {
                        String username = username_field.getText().toString();
                        String password = password_field.getText().toString();

                        getSessionKeyAsyncTask = new GetSessionKeyAsyncTask(LoginActivity.this);
                        getSessionKeyAsyncTask.execute(username, password);

                        spinner.setVisibility(View.VISIBLE);
                        username_field.setVisibility(View.INVISIBLE);
                        password_field.setVisibility(View.INVISIBLE);
                        mButton.setVisibility(View.INVISIBLE);
                    }
                    else
                        Toast.makeText(getApplicationContext(), R.string.network_is_not_connected, Toast.LENGTH_SHORT).show();
                }
            });
            findViewById(R.id.sign_in_page).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideKeyboard(LoginActivity.this);
                }
            });
        }

    }

    @Override
    public void onSessionKeyGranted(String sessionKey) {
        AppContext.getInstance().setSessionKey(sessionKey);
        AppContext.getInstance().saveSettings();

        GetUserInfoAsyncTask getUserInfoAsyncTask = new GetUserInfoAsyncTask(this);
        getUserInfoAsyncTask.execute();
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
        mButton.setVisibility(View.VISIBLE);

        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
    }
}

