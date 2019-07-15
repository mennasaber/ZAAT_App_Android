package com.example.zaat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {

    public MessageAdapter(Context context, int resource, List<Message> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.message_layout, parent, false);
        }
        Message currentMessage = getItem(position);
        TextView textView_date = listItemView.findViewById(R.id.text_view_date);
        textView_date.setText(currentMessage.getmDate());

        TextView textView_message = listItemView.findViewById(R.id.text_view_message);
        textView_message.setText(currentMessage.getMessage());

        return listItemView;
    }
}
