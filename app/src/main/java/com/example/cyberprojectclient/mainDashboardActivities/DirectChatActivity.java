package com.example.cyberprojectclient.mainDashboardActivities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cyberprojectclient.LoginActivity;
import com.example.cyberprojectclient.R;
import com.example.cyberprojectclient.utils.Message;
import com.example.cyberprojectclient.utils.MessageListAdapter;
import com.example.cyberprojectclient.utils.NetworkAdapter;
import com.example.cyberprojectclient.utils.SharedPrefUtils;
import com.example.cyberprojectclient.utils.User;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class DirectChatActivity extends AppCompatActivity {

    String thisUserFirstName;
    String thisUserLastName;
    String thisUserUsername;
    int thisUserUserId;
    int userId;
    String firstName;
    String lastname;
    String username;
    TextView fullNameText;
    TextView usernameText;
    ImageButton returnButton;

    EditText messageSender;
    Button sendButton;

    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;

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

        userId = thisIntent.getIntExtra("userId", 1);
        firstName = thisIntent.getStringExtra("firstName");
        lastname = thisIntent.getStringExtra("lastName");
        username = thisIntent.getStringExtra("username");

        fullNameText = (TextView)findViewById(R.id.fullName);
        usernameText = (TextView)findViewById(R.id.username);
        returnButton = (ImageButton)findViewById(R.id.returnButton);

        fullNameText.setText(firstName.concat(" ").concat(lastname));
        usernameText.setText(username.concat(" ").concat(String.valueOf(userId)));
        returnButton.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }));

        ArrayList<Message> messageList = new ArrayList<>();

        mMessageRecycler = (RecyclerView) findViewById(R.id.recycler_gchat);
        mMessageAdapter = new MessageListAdapter(this, messageList);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMessageRecycler.setAdapter(mMessageAdapter);

        messageSender = (EditText) findViewById(R.id.edit_gchat_message);
        sendButton = (Button) findViewById(R.id.button_gchat_send);

        thisUserFirstName = SharedPrefUtils.getString(this, this.getString(R.string.prefFirstName));
        thisUserLastName = SharedPrefUtils.getString(this, this.getString(R.string.prefLastName));
        thisUserUsername = SharedPrefUtils.getString(this, this.getString(R.string.prefUsername));
        thisUserUserId = SharedPrefUtils.getInt(this, this.getString(R.string.prefUserId));

        User thisUser = new User(thisUserFirstName, thisUserLastName, thisUserUsername, thisUserUserId);
        sendButton.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message(messageSender.getText().toString(), thisUser);
                messageSender.setText("");
                messageList.add(message);
                mMessageAdapter.notifyItemInserted(messageList.size());
            }
        }));


    }
}