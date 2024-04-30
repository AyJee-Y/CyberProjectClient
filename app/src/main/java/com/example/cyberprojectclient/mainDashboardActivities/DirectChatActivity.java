package com.example.cyberprojectclient.mainDashboardActivities;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cyberprojectclient.LoginActivity;
import com.example.cyberprojectclient.R;
import com.example.cyberprojectclient.utils.NetworkAdapter;

import org.w3c.dom.Text;

public class DirectChatActivity extends AppCompatActivity {

    int thisUserId;
    String firstName;
    String lastname;
    String username;
    TextView fullNameText;
    TextView usernameText;
    ImageButton returnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_direct_chat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent thisIntent = getIntent();

        thisUserId = thisIntent.getIntExtra("userId", 1);
        firstName = thisIntent.getStringExtra("firstName");
        lastname = thisIntent.getStringExtra("lastName");
        username = thisIntent.getStringExtra("username");

        fullNameText = (TextView)findViewById(R.id.fullName);
        usernameText = (TextView)findViewById(R.id.username);
        returnButton = (ImageButton)findViewById(R.id.returnButton);

        fullNameText.setText(firstName + " " + lastname);
        usernameText.setText(username + " " + thisUserId);
        returnButton.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }));
    }
}