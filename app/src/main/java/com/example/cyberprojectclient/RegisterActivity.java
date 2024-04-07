package com.example.cyberprojectclient;

import android.content.Intent;
import android.graphics.Color;
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

import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity {

    Button returnButton;
    Button registerButton;
    TextView output;
    EditText username;
    EditText password;
    EditText passwordVerify;
    EditText firstName;
    EditText lastName;

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

        initializeActivity();
    }

    protected void initializeActivity() {
        returnButton = (Button) findViewById(R.id.returnButton);
        registerButton = (Button) findViewById(R.id.register);
        output = (TextView) findViewById(R.id.output);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
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
                else if (verificationResult == 2) {
                    output.setText("Passwords not matching");
                    output.setTextColor(Color.RED);
                    return;
                }

                String enteredUsername = username.getText().toString();
                String enteredPassword = password.getText().toString();

                if (networkAdapter.attemptRegister(enteredUsername, enteredPassword)) {
                    output.setText("Registered, please sign in");
                    output.setTextColor(Color.GREEN);
                }
                else {
                    output.setText("Existing username");
                    output.setTextColor(Color.RED);
                }
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
        else if (!enteredPassword.equals(enteredPasswordVerification))
            return 2; //Symbolizes - passwords aren't matching
        else
            return 3;
    }
}