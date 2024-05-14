package com.example.cyberprojectclient.mainDashboardActivities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cyberprojectclient.R;
import com.example.cyberprojectclient.network.Client;
import com.example.cyberprojectclient.utils.SharedPrefUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class HomeActivity extends AppCompatActivity {
    TextView username;
    TextView fullName;
    TextView bio;
    ImageButton profile;
    ImageButton chat;
    ImageButton goodUser;
    ImageButton badUser;

    String randomUserId;
    String randomFirstName;
    String randomLastName;
    String randomUsername;

    Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        client = Client.getInstance();
        client.setUpChatParamters(false, 0, null);

        profile = (ImageButton) findViewById(R.id.profile);
        chat = (ImageButton) findViewById(R.id.chat);

        goodUser = (ImageButton) findViewById(R.id.goodRandomUser);
        badUser = (ImageButton) findViewById(R.id.badRandomUser);

        fullName = (TextView) findViewById(R.id.fullName);
        username = (TextView) findViewById(R.id.usernameText);
        bio = (TextView) findViewById(R.id.bio);

        goodUser.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, DirectChatActivity.class);
                i.putExtra("userId", Integer.getInteger(randomUserId));
                i.putExtra("firstName", randomFirstName);
                i.putExtra("lastName", randomLastName);
                i.putExtra("username", randomUsername);
                startActivity(i);
                try {
                    setRandomUser();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }));
        badUser.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    setRandomUser();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }));
        profile.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, ProfileActivity.class);
                i.putExtra("userId", SharedPrefUtils.getInt(HomeActivity.this, getString(R.string.prefUserId)));
                startActivity(i);
                finish();
            }
        }));
        chat.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this , ChatActivity.class);
                startActivity(i);
                finish();
            }
        }));

        try {
            setRandomUser();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    protected void setRandomUser() throws JSONException {
        fullName.setText("");
        username.setText("");
        bio.setText("");
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject randomUser = (Client.getInstance()).getRandomUser(SharedPrefUtils.getInt(HomeActivity.this, getString(R.string.prefUserId)));
                    randomUserId = String.valueOf(randomUser.get("thisUserId"));

                    randomFirstName = String.valueOf(randomUser.get("firstName"));
                    randomLastName = String.valueOf(randomUser.get("lastName"));
                    randomUsername = String.valueOf(randomUser.get("username"));
                    String randomBio = String.valueOf(randomUser.get("bio"));
                    fullName.setText(randomFirstName.concat(" ").concat(randomLastName));
                    username.setText(randomUsername);
                    bio.setText(randomBio);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}