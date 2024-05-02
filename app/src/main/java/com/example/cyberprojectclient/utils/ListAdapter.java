package com.example.cyberprojectclient.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cyberprojectclient.R;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<User> {


    public ListAdapter(Context context, ArrayList<User> userArrayList) {
        super(context, R.layout.chat_list_item, userArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        User user = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.chat_list_item, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.itemProfile);
        TextView fullName = convertView.findViewById(R.id.personName);
        TextView username = convertView.findViewById(R.id.username);

        imageView.setImageResource(R.drawable.profile_icon);
        assert user != null;
        fullName.setText(user.getFirstName().concat(" ").concat(user.getLastName()));
        username.setText(user.getUsername());


        return convertView;
    }
}
