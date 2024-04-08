package com.example.cyberprojectclient.mainDashboardActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cyberprojectclient.R;

public class ProfileActivity extends AppCompatActivity {

    ImageButton home;
    ImageButton chat;

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

        home.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i = new Intent(ProfileActivity.this , HomeActivity.class);
                startActivity(i);
                finish();*/
            }
        }));
        chat.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i = new Intent(ProfileActivity.this , ChatActivity.class);
                startActivity(i);
                finish();*/
            }
        }));
    }
}