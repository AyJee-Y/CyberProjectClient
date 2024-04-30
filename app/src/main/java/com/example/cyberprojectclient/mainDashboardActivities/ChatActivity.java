package com.example.cyberprojectclient.mainDashboardActivities;

import android.content.Intent;
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
import com.example.cyberprojectclient.utils.ListAdapter;
import com.example.cyberprojectclient.utils.NetworkAdapter;
import com.example.cyberprojectclient.utils.SharedPrefUtils;
import com.example.cyberprojectclient.utils.User;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;
    ImageButton home;
    ImageButton profile;
    int[] userIds;
    String[] firstNames;
    String[] lastNames;
    String[] usernames;

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
        int numOfChats = NetworkAdapter.getNumOfChats();

        userIds = new int[numOfChats];
        firstNames = new String[numOfChats];
        lastNames = new String[numOfChats];
        usernames = new String[numOfChats];

        userIds = NetworkAdapter.getUserIdChats(numOfChats);
        int index = 0;
        for (int userId : userIds) {
            String[] currentData = NetworkAdapter.getUserData(userId);

            usernames[index] = currentData[0];
            firstNames[index] = currentData[1];
            lastNames[index] = currentData[2];

            index++;
        }
    }
}