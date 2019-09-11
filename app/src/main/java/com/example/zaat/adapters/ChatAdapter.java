package com.example.zaat.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zaat.classes.Message_chatting;
import com.example.zaat.R;
import com.example.zaat.activities.Chat;

import java.util.List;

public class ChatAdapter extends ArrayAdapter<Message_chatting> {


    public ChatAdapter(Context context, int resource, List<Message_chatting> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView;
        // = convertView;

        //if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.chatitem, parent, false);
        //}

        Message_chatting currentMessage = getItem(position);
        TextView textview_message = listItemView.findViewById(R.id.text_view_chat);

        textview_message.setText(currentMessage.getMessage());

        ImageView image = listItemView.findViewById(R.id.image_chat);

        if (currentMessage.getuID()== Chat.user.getuID()) {
            LinearLayout layout = listItemView.findViewById(R.id.chatitem);
            RelativeLayout relative = listItemView.findViewById(R.id.relativeLayout_chat);
            relative.setBackgroundResource(R.drawable.rounded_chatitem2);
            image.setVisibility(View.GONE);
            textview_message.setGravity(Gravity.RIGHT);
            textview_message.setTextColor(Color.parseColor("#460202"));
            layout.setGravity(Gravity.RIGHT);
        }

        return listItemView;
    }
}
