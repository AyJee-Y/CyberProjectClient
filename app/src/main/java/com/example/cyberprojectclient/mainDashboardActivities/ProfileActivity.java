package com.example.cyberprojectclient.mainDashboardActivities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cyberprojectclient.R;
import com.example.cyberprojectclient.network.CurrentUserData;
import com.example.cyberprojectclient.network.NetworkAdapter;

import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity {

    ImageButton home;
    ImageButton chat;

    TextView fullName;
    TextView username;
    TextView bio;

    private int thisUserId;
    private Intent thisIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        home = (ImageButton) findViewById(R.id.home);
        chat = (ImageButton) findViewById(R.id.chat);
        thisIntent = getIntent();

        home.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this , HomeActivity.class);
                startActivity(i);
                finish();
            }
        }));
        chat.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this , ChatActivity.class);
                startActivity(i);
                finish();
            }
        }));

        fullName = (TextView) findViewById(R.id.fullName);
        username = (TextView) findViewById(R.id.usernameDisplayer);
        bio = (TextView) findViewById(R.id.bio);

        initializeActivity();
    }

    protected void initializeActivity() {
        thisUserId = thisIntent.getIntExtra("userId", 1);
        String[] currentUserData;
        if (thisUserId == CurrentUserData.getUserId()) {
            currentUserData = CurrentUserData.getFullData();
        }
        else {
            currentUserData = NetworkAdapter.getUserData(thisUserId);
        }

        fullName.setText(currentUserData[1] + " " + currentUserData[2]);
        username.setText(currentUserData[0]);
        bio.setText(currentUserData[3]);
    }
}