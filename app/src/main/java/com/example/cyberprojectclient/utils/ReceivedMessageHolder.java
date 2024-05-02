package com.example.cyberprojectclient.utils;

import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cyberprojectclient.R;

public class ReceivedMessageHolder extends RecyclerView.ViewHolder {
    TextView messageText, nameText;
    ImageView profileImage;
    ReceivedMessageHolder(View itemView) {
        super(itemView);
        messageText = (TextView) itemView.findViewById(R.id.text_gchat_message_other);
        nameText = (TextView) itemView.findViewById(R.id.text_gchat_user_other);
        profileImage = (ImageView) itemView.findViewById(R.id.image_gchat_profile_other);
    }

    void bind(Message message) {
        messageText.setText(message.getMessage());
        nameText.setText(message.getUser().getUsername());

        profileImage.setImageResource(R.drawable.profile_icon);
    }
}
