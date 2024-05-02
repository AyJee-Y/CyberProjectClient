package com.example.cyberprojectclient.mainDashboardActivities;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.cyberprojectclient.utils.NetworkAdapter;
import com.example.cyberprojectclient.utils.SharedPrefUtils;

public class ProfileEditor extends AppCompatActivity {

    Button save;
    EditText firstName;
    EditText lastName;
    EditText biography;
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

                NetworkAdapter.changeUserData(SharedPrefUtils.getInt(ProfileEditor.this, getString(R.string.prefUserId)), newFirstName, newLastName, newBio);
                NetworkAdapter.setThisUserData(SharedPrefUtils.getInt(ProfileEditor.this, getString(R.string.prefUserId)),ProfileEditor.this);

                Intent i = new Intent(ProfileEditor.this, ProfileActivity.class);
                i.putExtra("userId", SharedPrefUtils.getInt(ProfileEditor.this, getString(R.string.prefUserId)));
                startActivity(i);
                finish();
            }
        }));
    }
}