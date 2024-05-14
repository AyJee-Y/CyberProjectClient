package com.example.cyberprojectclient.mainDashboardActivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cyberprojectclient.R;
import com.example.cyberprojectclient.network.Client;
import com.example.cyberprojectclient.utils.DataVerification;
import com.example.cyberprojectclient.utils.NetworkAdapter;
import com.example.cyberprojectclient.utils.SharedPrefUtils;

import org.json.JSONObject;

public class ProfileEditor extends AppCompatActivity {

    Button save;
    EditText firstName;
    EditText lastName;
    EditText biography;
    Client client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_editor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        client = Client.getInstance();
        client.setUpChatParamters(false, 0, null);
        initializeViews();
    }

    protected void initializeViews() {
        firstName = (EditText) findViewById(R.id.FirstNameEditor);
        lastName = (EditText) findViewById(R.id.LastNameEditor);
        biography = (EditText) findViewById(R.id.bioEditor);
        save = (Button) findViewById(R.id.save);

        initializeListeners();
    }

    protected void initializeListeners() {
        save.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newFirstName = firstName.getText().toString();
                String newLastName = lastName.getText().toString();
                String newBio = biography.getText().toString();

                if (verifyNewData(newFirstName, newLastName, newBio)) {
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                client.changeProfileData(SharedPrefUtils.getInt(ProfileEditor.this, getString(R.string.prefUserId)),
                                        newFirstName, newLastName, newBio);

                                SharedPrefUtils.saveString(ProfileEditor.this, getString(R.string.prefFirstName), newFirstName);
                                SharedPrefUtils.saveString(ProfileEditor.this, getString(R.string.prefLastName), newLastName);
                                SharedPrefUtils.saveString(ProfileEditor.this, getString(R.string.prefBio), newBio);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });

                    Intent i = new Intent(ProfileEditor.this, ProfileActivity.class);
                    i.putExtra("userId", SharedPrefUtils.getInt(ProfileEditor.this, getString(R.string.prefUserId)));
                    startActivity(i);
                    finish();
                } else {

                }

            }
        }));
    }

    protected  boolean verifyNewData(String newFirstName, String newLastName, String newBio) {
        return (DataVerification.verifyName(newFirstName) && DataVerification.verifyName(newLastName) && DataVerification.verifyBio(newBio));
    }
}