package com.example.cyberprojectclient.mainDashboardActivities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cyberprojectclient.R;
import com.example.cyberprojectclient.utils.NetworkAdapter;
import com.example.cyberprojectclient.utils.SharedPrefUtils;

public class ProfileActivity extends AppCompatActivity {

    ImageButton home;
    ImageButton chat;

    Button signOut;

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
        signOut = (Button) findViewById(R.id.signOut);
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
        bio = (TextView) findViewById(R.id.passwordText);

        initializeActivity();

        signOut.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setMessage(R.string.signOutMessage).setTitle(R.string.signOutTitle);
                builder.setPositiveButton(R.string.signOutAccept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        resetSharedPrefs();
                        finish();
                    }
                });
                builder.setNegativeButton(R.string.signOutCancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //DOESN'T MATTER SINCE ITS CANCELED
                    }
                });

                AlertDialog dialog = builder.show();
            }
        }));
    }

    protected void initializeActivity() {
        thisUserId = thisIntent.getIntExtra("userId", 1);
        String[] userData;
        if (thisUserId == SharedPrefUtils.getInt(ProfileActivity.this, getString(R.string.prefUserId))) {
            userData = getThisUserData();
        }
        else {
            userData = NetworkAdapter.getUserData(thisUserId);
        }

        fullName.setText(userData[1] + " " + userData[2]);
        username.setText(userData[0]);
        bio.setText(userData[3]);
    }

    /**
     * This function will get the user data of the user currently using this application
     * from the shared preferences of the applications.
     * They will be in the form of a string array with the indexes:
     * 0 - username
     * 1 - first name
     * 2 - last name
     * 3 - bio
     * @return current users data - for a profile
     */
    protected String[] getThisUserData() {
        String[] currentUserData = new String[4];
        currentUserData[0] = SharedPrefUtils.getString(ProfileActivity.this, getString(R.string.prefUsername));
        currentUserData[1] = SharedPrefUtils.getString(ProfileActivity.this, getString(R.string.prefFirstName));
        currentUserData[2] = SharedPrefUtils.getString(ProfileActivity.this, getString(R.string.prefLastName));
        currentUserData[3] = SharedPrefUtils.getString(ProfileActivity.this, getString(R.string.prefBio));

        return currentUserData;
    }

    /**
     * This function is used to reset the shared prefs,
     * it will be used when the user signs out in order
     * to "force" him to sign in.
     */
    protected void resetSharedPrefs() {
        Context thisContext = ProfileActivity.this;
        SharedPrefUtils.saveBoolean(thisContext, getString(R.string.prefLoggedStatus), false);
        SharedPrefUtils.saveInt(thisContext, getString(R.string.prefUserId), 0);
        SharedPrefUtils.saveString(thisContext, getString(R.string.prefUsername), null);
        SharedPrefUtils.saveString(thisContext, getString(R.string.prefBio), null);
        SharedPrefUtils.saveString(thisContext, getString(R.string.prefFirstName), null);
        SharedPrefUtils.saveString(thisContext, getString(R.string.prefLastName), null);
    }
}