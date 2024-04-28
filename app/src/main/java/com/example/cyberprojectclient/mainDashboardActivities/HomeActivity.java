package com.example.cyberprojectclient.mainDashboardActivities;

import android.content.Intent;
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
import com.example.cyberprojectclient.utils.NetworkAdapter;
import com.example.cyberprojectclient.utils.SharedPrefUtils;

import org.w3c.dom.Text;

public class HomeActivity extends AppCompatActivity {
    TextView username;
    TextView fullName;
    TextView bio;
    ImageButton profile;
    ImageButton chat;
    ImageButton goodUser;
    ImageButton badUser;

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
                setRandomUser();
                //IT SHOULD MOVE YOU TO PRIVATE CHAT
            }
        }));
        badUser.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRandomUser();
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

        setRandomUser();
    }

    protected void setRandomUser() {
        fullName.setText("loading");
        username.setText("loading");
        bio.setText("loading");
        String[] userData = NetworkAdapter.getRandomUserData(SharedPrefUtils.getInt(HomeActivity.this, getString(R.string.prefUserId)));
        fullName.setText(userData[1] + " " + userData[2]);
        username.setText(userData[0]);
        bio.setText(userData[3]);
    }
}