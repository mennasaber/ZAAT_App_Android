package com.example.zaat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.zaat.classes.Message;
import com.example.zaat.R;

import java.util.List;

public class messageMemoriesAdapter extends ArrayAdapter<Message> {
    public messageMemoriesAdapter(Context context, int resource, List<Message> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.message_memories, parent, false);
        }
        Message currentMessage = getItem(position);
        TextView textView_date = listItemView.findViewById(R.id.text_view_date_memories);
        textView_date.setText(currentMessage.getmDate());

        TextView textView_message = listItemView.findViewById(R.id.text_view_message_memories);
        textView_message.setText(currentMessage.getMessage());
        return listItemView;
    }
}
