package com.example.cyberprojectclient;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cyberprojectclient.mainDashboardActivities.HomeActivity;
import com.example.cyberprojectclient.network.Client;
import com.example.cyberprojectclient.utils.DataVerification;
import com.example.cyberprojectclient.utils.SharedPrefUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    Button returnButton;
    Button registerButton;
    TextView output;
    EditText username;
    EditText password;
    EditText passwordVerify;
    EditText firstName;
    EditText lastName;
    Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        client = Client.getInstance();
        client.setUpChatParamters(false, 0, null);
        initializeActivity();
    }

    protected void initializeActivity() {
        returnButton = (Button) findViewById(R.id.returnButton);
        registerButton = (Button) findViewById(R.id.register);
        output = (TextView) findViewById(R.id.output);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.bio);
        passwordVerify = (EditText) findViewById(R.id.passwordVerify);
        firstName = (EditText) findViewById(R.id.firstname);
        lastName = (EditText) findViewById(R.id.lastname);

        returnButton.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }));

        registerButton.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int verificationResult = verifyRegisterData();
                if (verificationResult == 1) {
                    output.setText("Missing data");
                    output.setTextColor(Color.RED);
                    return;
                }
                if (verificationResult == 2) {
                    output.setText("Weak password");
                    output.setTextColor(Color.RED);
                    return;
                }
                else if (verificationResult == 3) {
                    output.setText("Passwords not matching");
                    output.setTextColor(Color.RED);
                    return;
                }
                else if (verificationResult == 4) {
                    output.setText("Invalid names");
                    output.setTextColor(Color.RED);
                    return;
                }
                else if (verificationResult == 5) {
                    output.setText("Invalid username");
                    output.setTextColor(Color.RED);
                    return;
                }

                String enteredUsername = username.getText().toString();
                String enteredPassword = password.getText().toString();
                String enteredFirstName = firstName.getText().toString();
                String enteredLastName = lastName.getText().toString();

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject answer = client.createNewAccount(enteredUsername,enteredPassword,enteredFirstName,enteredFirstName,"");
                        try {
                            if (answer.get("id").equals("102")) {
                                output.setText("Registered, please sign in");
                                output.setTextColor(Color.GREEN);
                                setUpSharedPreferences(Integer.valueOf(String.valueOf(answer.get("user"))));

                                Intent i = new Intent(RegisterActivity.this, HomeActivity.class);
                                finish();
                                startActivity(i);
                            }
                            else {
                                output.setText("Existing username");
                                output.setTextColor(Color.RED);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        }));
    }

    protected int verifyRegisterData() {
        String enteredUsername = username.getText().toString();
        String enteredPassword = password.getText().toString();
        String enteredPasswordVerification = passwordVerify.getText().toString();
        String enteredFirstName = firstName.getText().toString();
        String enteredLastName = lastName.getText().toString();

        //TODO:
        //Later on add more verifications, such as checking password strength and
        //length of username, first name and/or last name

        if (enteredUsername.equals("") || enteredPassword.equals("") || enteredFirstName.equals("") || enteredLastName.equals(""))
            return 1; //Symbolizes - missing data
        else if (!DataVerification.verifyPassword(enteredPassword))
            return 2; //Symbolizes - Weak password
        else if (!enteredPassword.equals(enteredPasswordVerification))
            return 3; //Symbolizes - passwords aren't matching
        else if (!DataVerification.verifyName(enteredFirstName) || !DataVerification.verifyName(enteredLastName))
            return 4; //Symbolizes - invalid names
        else if (!DataVerification.verifyUsername(enteredUsername))
            return 5; //Symbolizes - invalid username
        else
            return 6;
    }

    protected void setUpSharedPreferences(int userId) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject profileData = client.getProfileData(userId);

                    SharedPrefUtils.saveBoolean(RegisterActivity.this, getString(R.string.prefLoggedStatus), true);
                    SharedPrefUtils.saveInt(RegisterActivity.this, getString(R.string.prefUserId), userId);
                    SharedPrefUtils.saveString(RegisterActivity.this, getString(R.string.prefFirstName), profileData.getString("firstName"));
                    SharedPrefUtils.saveString(RegisterActivity.this, getString(R.string.prefLastName), profileData.getString("lastName"));
                    SharedPrefUtils.saveString(RegisterActivity.this, getString(R.string.prefUsername), profileData.getString("username"));
                    SharedPrefUtils.saveString(RegisterActivity.this, getString(R.string.prefBio), profileData.getString("bio"));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}