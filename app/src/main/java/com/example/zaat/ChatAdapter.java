package com.example.zaat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class ChatAdapter extends ArrayAdapter<Message_chatting> {


    public ChatAdapter(Context context, int resource, List<Message_chatting> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.chatitem, parent, false);
        }

        Message_chatting currentMessage = getItem(position);
        TextView textview_message = listItemView.findViewById(R.id.text_view_chat);

        textview_message.setText(currentMessage.getMessage());

        ImageView image = listItemView.findViewById(R.id.image_chat);
        if(currentMessage.getuID().equals(Chat.user.uID))
           image.setVisibility(View.GONE);


        return listItemView;
    }
}
