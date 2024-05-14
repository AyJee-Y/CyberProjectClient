package com.example.cyberprojectclient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cyberprojectclient.mainDashboardActivities.HomeActivity;
import com.example.cyberprojectclient.network.Client;
import com.example.cyberprojectclient.utils.SharedPrefUtils;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    Button registerButton;
    Button loginButton;
    TextView output;
    EditText username;
    EditText password;
    Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                client = Client.getInstance();
                client.setUpChatParamters(false, 0, null);
            }
        });
        initializeActivity();

        if (SharedPrefUtils.getBoolean(LoginActivity.this, getString(R.string.prefLoggedStatus))) {
            int userId = SharedPrefUtils.getInt(LoginActivity.this, getString(R.string.prefUserId));
            announceUser(userId);
            resetActivity();
            setUpSharedPreferences(userId);
            Intent i = new Intent(LoginActivity.this , HomeActivity.class);
            startActivity(i);
        }
    }

    protected void announceUser(int userId) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                client.announceUser(userId);
            }
        });
    }

    protected void resetActivity() {
        username.setText("");
        password.setText("");
        output.setText("Please Log In:");
        output.setTextColor(Color.BLACK);
        output.setTextSize(32);
    }

    protected void initializeActivity() {
        registerButton = (Button) findViewById(R.id.register);
        loginButton = (Button) findViewById(R.id.login);
        output = (TextView) findViewById(R.id.output);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.passwordLogin);
        registerButton.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetActivity();
                Intent i = new Intent(LoginActivity.this , RegisterActivity.class);
                startActivity(i);
            }
        }));

        loginButton.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredUsername = username.getText().toString();
                String enteredPassword = password.getText().toString();
                resetActivity();
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        int userId = client.attemptSignIn(enteredUsername, enteredPassword);

                        if (userId != -1) {
                            output.setText("Signed in");
                            output.setTextColor(Color.GREEN);
                            output.setTextSize(32);

                            setUpSharedPreferences(userId);

                            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(i);
                        }
                        else {
                            output.setText("Incorrect username or password");
                            output.setTextColor(Color.RED);
                            output.setTextSize(21);
                        }
                    }
                });
            }
        }));
    }

    protected void setUpSharedPreferences(int userId) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject profileData = client.getProfileData(userId);

                    SharedPrefUtils.saveBoolean(LoginActivity.this, getString(R.string.prefLoggedStatus), true);
                    SharedPrefUtils.saveInt(LoginActivity.this, getString(R.string.prefUserId), userId);
                    SharedPrefUtils.saveString(LoginActivity.this, getString(R.string.prefFirstName), profileData.getString("firstName"));
                    SharedPrefUtils.saveString(LoginActivity.this, getString(R.string.prefLastName), profileData.getString("lastName"));
                    SharedPrefUtils.saveString(LoginActivity.this, getString(R.string.prefUsername), profileData.getString("username"));
                    SharedPrefUtils.saveString(LoginActivity.this, getString(R.string.prefBio), profileData.getString("bio"));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}