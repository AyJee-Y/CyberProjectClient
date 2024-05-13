package com.example.cyberprojectclient.mainDashboardActivities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;

import com.example.cyberprojectclient.R;
import com.example.cyberprojectclient.databinding.ActivityChatBinding;
import com.example.cyberprojectclient.network.Client;
import com.example.cyberprojectclient.utils.ListAdapter;
import com.example.cyberprojectclient.utils.NetworkAdapter;
import com.example.cyberprojectclient.utils.SharedPrefUtils;
import com.example.cyberprojectclient.utils.User;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;
    ImageButton home;
    ImageButton profile;
    int[] userIds;
    String[] firstNames;
    String[] lastNames;
    String[] usernames;
    Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        home = (ImageButton) findViewById(R.id.home);
        profile = (ImageButton) findViewById(R.id.profile);

        client = Client.getInstance();

        home.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChatActivity.this , HomeActivity.class);
                startActivity(i);
                finish();
            }
        }));
        profile.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChatActivity.this , ProfileActivity.class);
                i.putExtra("userId", SharedPrefUtils.getInt(ChatActivity.this, getString(R.string.prefUserId)));
                startActivity(i);
                finish();
            }
        }));

        setUpChatUsers();

        ArrayList<User> userArrayList = new ArrayList<>();
        for (int i = 0; i < userIds.length; i++) {
            User user = new User(firstNames[i], lastNames[i], usernames[i], userIds[i]);
            userArrayList.add(user);
        }

        ListAdapter listAdapter = new ListAdapter(ChatActivity.this, userArrayList);

        binding.chatList.setAdapter(listAdapter);
        binding.chatList.setClickable(true);
        binding.chatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ChatActivity.this, DirectChatActivity.class);
                i.putExtra("userId", userIds[position]);
                i.putExtra("firstName", firstNames[position]);
                i.putExtra("lastName", lastNames[position]);
                i.putExtra("username", usernames[position]);
                startActivity(i);
            }
        });
    }

    protected void setUpChatUsers() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject randomUser = client.getUserChats(SharedPrefUtils.getInt(ChatActivity.this, getString(R.string.prefUserId)));

                    userIds = new int[Integer.valueOf(String.valueOf(randomUser.get("numOfUsers")))];
                    for (int i = 0; i < userIds.length; i++) {
                        String currentKey = "userId".concat(String.valueOf(i + 1));
                        userIds[i] = Integer.valueOf(String.valueOf(randomUser.get(currentKey)));
                    }

                    firstNames = new String[userIds.length];
                    lastNames = new String[userIds.length];
                    usernames = new String[userIds.length];

                    setUpChatProfiles();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    protected void setUpChatProfiles() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final ArrayList<User> userArrayList = new ArrayList<>();

                for (int i = 0; i < userIds.length; i++) {
                    try {
                        JSONObject userData = client.getProfileData(userIds[i]);

                        firstNames[i] = String.valueOf(userData.get("firstName"));
                        lastNames[i] = String.valueOf(userData.get("lastName"));
                        usernames[i] = String.valueOf(userData.get("username"));

                        User user = new User(firstNames[i], lastNames[i], usernames[i], userIds[i]);
                        userArrayList.add(user);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

                ListAdapter listAdapter = new ListAdapter(ChatActivity.this, userArrayList);

                binding.chatList.setAdapter(listAdapter);
                binding.chatList.setClickable(true);
                binding.chatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent i = new Intent(ChatActivity.this, DirectChatActivity.class);
                        i.putExtra("userId", userIds[position]);
                        i.putExtra("firstName", firstNames[position]);
                        i.putExtra("lastName", lastNames[position]);
                        i.putExtra("username", usernames[position]);
                        startActivity(i);
                    }
                });
            }
        });
    }
}