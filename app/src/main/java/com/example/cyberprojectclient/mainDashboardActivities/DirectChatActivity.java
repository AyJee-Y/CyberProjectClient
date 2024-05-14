package com.example.cyberprojectclient.mainDashboardActivities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cyberprojectclient.LoginActivity;
import com.example.cyberprojectclient.R;
import com.example.cyberprojectclient.network.Client;
import com.example.cyberprojectclient.utils.ListAdapter;
import com.example.cyberprojectclient.utils.Message;
import com.example.cyberprojectclient.utils.MessageListAdapter;
import com.example.cyberprojectclient.utils.NetworkAdapter;
import com.example.cyberprojectclient.utils.SharedPrefUtils;
import com.example.cyberprojectclient.utils.User;

import org.json.JSONException;
import org.json.JSONObject;
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
    int otherUserId;
    String otherFirstName;
    String otherLastname;
    String otherUsername;
    TextView fullNameText;
    TextView usernameText;
    ImageButton returnButton;

    EditText messageSender;
    Button sendButton;
    Button loadButton;
    User thisUser;
    User otherUser;

    Client client;

    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;
    private ArrayList<Message> messageList;

    ArrayList<Integer> listOfMessageIds;
    int currentChadId;
    int currentMessagesLoaded = 0;
    int numOfMessages;

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

        thisUserFirstName = SharedPrefUtils.getString(this, this.getString(R.string.prefFirstName));
        thisUserLastName = SharedPrefUtils.getString(this, this.getString(R.string.prefLastName));
        thisUserUsername = SharedPrefUtils.getString(this, this.getString(R.string.prefUsername));
        thisUserUserId = SharedPrefUtils.getInt(this, this.getString(R.string.prefUserId));

        otherUserId = thisIntent.getIntExtra("userId", 1);
        otherFirstName = thisIntent.getStringExtra("firstName");
        otherLastname = thisIntent.getStringExtra("lastName");
        otherUsername = thisIntent.getStringExtra("username");

        fullNameText = (TextView)findViewById(R.id.fullName);
        usernameText = (TextView)findViewById(R.id.username);
        returnButton = (ImageButton)findViewById(R.id.returnButton);

        fullNameText.setText(otherFirstName.concat(" ").concat(otherLastname));
        usernameText.setText(otherUsername);

        thisUser = new User(thisUserFirstName, thisUserLastName, thisUserUsername, thisUserUserId);
        otherUser = new User(otherFirstName, otherLastname, otherUsername, otherUserId);

        setUpBaseChat();
        setUpButtons();
        getMessagesFromServer();
    }

    protected void setUpBaseChat() {
        messageList = new ArrayList<>();

        mMessageRecycler = (RecyclerView) findViewById(R.id.recycler_gchat);
        mMessageAdapter = new MessageListAdapter(this, messageList);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMessageRecycler.setAdapter(mMessageAdapter);

        messageSender = (EditText) findViewById(R.id.edit_gchat_message);
    }

    protected void setUpButtons() {
        sendButton = (Button)findViewById(R.id.button_gchat_send);
        returnButton = (ImageButton)findViewById(R.id.returnButton);
        sendButton.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message(messageSender.getText().toString(), thisUser);
                messageSender.setText("");

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject confirmation = client.sendMessage(currentChadId, thisUserUserId, message.getMessage());
                            int thisMessageId = Integer.valueOf(String.valueOf(confirmation.get("messageId")));

                            listOfMessageIds.add(0, thisMessageId);

                            numOfMessages++;
                            currentMessagesLoaded++;
                            messageList.add(message);
                            runOnUiThread(new Runnable(){
                                public void run() {
                                    mMessageAdapter.notifyItemInserted(messageList.size());
                                }
                            });
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        }));
        returnButton.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }));
    }

    protected void getMessagesFromServer() {
        client = Client.getInstance();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject thisChatMessages = client.getChatBetween(thisUserUserId, otherUserId);
                    numOfMessages = Integer.valueOf(String.valueOf(thisChatMessages.get("numOfMessages")));
                    listOfMessageIds = new ArrayList<>();
                    currentChadId = Integer.valueOf(String.valueOf(thisChatMessages.get("chatId")));

                    client.setUpChatParamters(true, currentChadId, DirectChatActivity.this);

                    String baseKey = "messageId";
                    for (int i = 0; i < numOfMessages; i++) {
                        String currentKey = baseKey.concat(String.valueOf(i + 1));
                        listOfMessageIds.add(Integer.valueOf(String.valueOf(thisChatMessages.get(currentKey))));
                    }

                    currentMessagesLoaded = 0;
                    loadMessages();
                    setUpLoadButton();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    protected void setUpLoadButton() {
        loadButton = (Button)findViewById(R.id.loadMessagesButton);
        loadButton.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMessages();
            }
        }));
    }

    protected void loadMessages() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 10 && currentMessagesLoaded < numOfMessages; i++) {
                        JSONObject currentMessage = client.getMessage(currentChadId, listOfMessageIds.get(currentMessagesLoaded));
                        currentMessagesLoaded++;

                        int thisUserId = Integer.valueOf(String.valueOf(currentMessage.get("senderId")));
                        User currentUser;
                        if (thisUserId == thisUserUserId)
                            currentUser = thisUser;
                        else
                            currentUser = otherUser;
                        Message thisMessage = new Message(String.valueOf(currentMessage.get("content")), currentUser);

                        messageList.add(0, thisMessage);
                        runOnUiThread(new Runnable(){
                            public void run() {
                                mMessageAdapter.notifyItemRangeChanged(1, messageList.size());
                            }
                        });
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void notifyMessage() {
        //THIS IS CALLED WHEN YOU RECEIVED A MESSAGE
    }
}