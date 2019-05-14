package sk.uniza.fri.kromka.marek.fricords.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import sk.uniza.fri.kromka.marek.fricords.R;
import sk.uniza.fri.kromka.marek.fricords.data.HostPreferences;

public class LoginActivity extends Activity {

    private EditText userView;
    private EditText passView;
    private ProgressDialog mProgressDialog;
    private UserLoginTask mAuthTask = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userView = findViewById(R.id.userInput);
        passView = findViewById(R.id.passInput);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            View my_view = findViewById(R.id.logo_fri);
            my_view.setVisibility(View.GONE);
        }

        CardView logBtn = findViewById(R.id.loginBtn);
        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryLogin();
            }
        });

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("wait");
    }

    private void tryLogin() {

        String url = "http://localhost:8080/mobile-app-ws/users/login";

        String user = userView.getText().toString();
        String password = passView.getText().toString();

        //check username and password
        if (TextUtils.isEmpty(user))
        {
            Toast.makeText(getApplicationContext(),"Vyplň pole Email",Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(getApplicationContext(),"Vyplň pole Heslo",Toast.LENGTH_LONG).show();
            return;
        }

        HostPreferences.saveSharedSetting(this, "userEmail", user);

        showProgress(true);
        mAuthTask = new UserLoginTask(user, password);
        mAuthTask.execute(url);

    }

    private void showProgress(final boolean show)
    {
        if (show)
        {
            mProgressDialog.show();
        }
        else
        {
            mProgressDialog.dismiss();
        }
    }


    public class UserLoginTask extends AsyncTask<String, Void, Integer>
    {
        private final String LOG_TAG = UserLoginTask.class.getName();

        private final int SUCCESS = 200;
        private final int FORBIDDEN = 403;
        private final int INTERNAL_ERROR = 500;
        private final int IO_EXCEPTION = 3;

        private final String userName;
        private final String password;

        public UserLoginTask(String userName, String password) {
            this.userName = userName;
            this.password = password;
        }

        @Override
        protected Integer doInBackground(String... params)
        {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonStr;
            int respondCode = 500;

            try
            {
                URL url = new URL("http://10.0.2.2:8080/mobile-app-ws/users/login");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setConnectTimeout(10000);
                urlConnection.setReadTimeout(10000);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("POST");

                JSONObject cred = new JSONObject();
                cred.put("email", userName);
                cred.put("password", password);

                DataOutputStream localDataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
                localDataOutputStream.writeBytes(cred.toString());
                localDataOutputStream.flush();
                localDataOutputStream.close();

                respondCode = urlConnection.getResponseCode();

                if(respondCode == SUCCESS){
                    String token = urlConnection.getHeaderField("Authorization");
                    String userId = urlConnection.getHeaderField("UserID");
                    String teacher = urlConnection.getHeaderField("Admin");
                    HostPreferences.saveSharedSetting(LoginActivity.this, "token", token);
                    HostPreferences.saveSharedSetting(LoginActivity.this, "userId", userId);
                    HostPreferences.saveSharedSetting(LoginActivity.this,"teacher",teacher);
                }

            }
            catch (ConnectTimeoutException e) {
                Toast.makeText(LoginActivity.this, "Connection timed out.", Toast.LENGTH_LONG).show();
            }

            catch (Exception e){
                Log.v("ErrorAPP",e.toString());
            }
            return respondCode;

        }

        @Override
        protected void onPostExecute(final Integer result)
        {
            if (result == SUCCESS)
            {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            else if (result == FORBIDDEN)
            {
                Toast.makeText(getApplicationContext(),"Neplatný email alebo heslo",Toast.LENGTH_LONG).show();
            }
            else
            {

            }

            mAuthTask = null;
            showProgress(false);
        }

        @Override
        protected void onCancelled()
        {
            mAuthTask = null;
            showProgress(false);
        }
    }
}