package com.example.cyberprojectclient;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cyberprojectclient.network.networkAdapter;

public class LoginActivity extends AppCompatActivity {

    Button registerButton;
    Button loginButton;
    TextView output;
    EditText username;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        networkAdapter.initializeUsers();
        initializeActivity();
    }

    protected void resetActivity() {
        username.setText("");
        password.setText("");
        output.setText("Please Log In:");
        output.setTextColor(Color.BLACK);
        output.setTextSize(32);
    }

    protected void initializeActivity() {
        registerButton = (Button) findViewById(R.id.register);
        loginButton = (Button) findViewById(R.id.login);
        output = (TextView) findViewById(R.id.output);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        registerButton.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetActivity();
                Intent i = new Intent(LoginActivity.this , RegisterActivity.class);
                startActivity(i);
            }
        }));

        loginButton.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredUsername = username.getText().toString();
                String enteredPassword = password.getText().toString();

                if (networkAdapter.attemptSignIn(enteredUsername, enteredPassword)) {
                    output.setText("Signed in :)");
                    output.setTextColor(Color.GREEN);
                    output.setTextSize(32);
                }
                else {
                    output.setText("Incorrect username or password");
                    output.setTextColor(Color.RED);
                    output.setTextSize(21);
                }
            }
        }));
    }
}