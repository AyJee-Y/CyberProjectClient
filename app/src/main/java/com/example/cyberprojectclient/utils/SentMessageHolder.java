package com.example.cyberprojectclient.utils;


import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cyberprojectclient.R;

public class SentMessageHolder extends RecyclerView.ViewHolder {
    TextView messageText;

    SentMessageHolder(View itemView) {
        super(itemView);

        messageText = (TextView) itemView.findViewById(R.id.text_gchat_message_me);
    }

    void bind(Message message) {
        messageText.setText(message.getMessage());
    }
}
