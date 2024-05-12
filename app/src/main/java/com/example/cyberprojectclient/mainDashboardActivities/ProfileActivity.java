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
import com.example.cyberprojectclient.network.Client;
import com.example.cyberprojectclient.utils.NetworkAdapter;
import com.example.cyberprojectclient.utils.SharedPrefUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {

    ImageButton home;
    ImageButton chat;

    Button edit;
    Button signOut;

    TextView fullName;
    TextView username;
    TextView bio;
    Client client;

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

        client = Client.getInstance();

        home = (ImageButton) findViewById(R.id.home);
        chat = (ImageButton) findViewById(R.id.chat);
        signOut = (Button) findViewById(R.id.signOut);
        edit = (Button) findViewById(R.id.edit);
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
        bio = (TextView) findViewById(R.id.bioText);

        try {
            initializeActivity();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        edit.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this , ProfileEditor.class);
                startActivity(i);
                finish();
            }
        }));
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

    protected void initializeActivity() throws JSONException {
        thisUserId = thisIntent.getIntExtra("userId", 1);
        String[] userData;
        if (thisUserId == SharedPrefUtils.getInt(ProfileActivity.this, getString(R.string.prefUserId))) {
            userData = getThisUserData();
        }
        else {
            JSONObject answer = client.getProfileData(thisUserId);
            userData = new String[4];
            userData[0] = String.valueOf(answer.get("username"));
            userData[1] = String.valueOf(answer.get("firstName"));
            userData[2] = String.valueOf(answer.get("lastName"));
            userData[3] = String.valueOf(answer.get("bio"));
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