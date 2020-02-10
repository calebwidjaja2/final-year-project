package com.example.selftourismapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class usser_message_adapter extends BaseAdapter {
    List<user_message> messages = new ArrayList<user_message>();
    Context c;

    public usser_message_adapter(Context c) {
        this.c = c;

    }

    public void add(user_message message) {
        this.messages.add(message);
        notifyDataSetChanged();
    }

    public int getCount() {
        return messages.size();

    }

    @Override
    public Object getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        MessageViewHolder holder = new MessageViewHolder();
        LayoutInflater messageLayoutInflater = (LayoutInflater) c.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        user_message message = messages.get(i);

        if (message.isCurrentUser()) {
            convertView = messageLayoutInflater.inflate(R.layout.message, null);
            holder.messageBody = (TextView) convertView.findViewById(R.id.messageBody);
            convertView.setTag(holder);
            holder.messageBody.setText(message.getText());
        } else {
            convertView = messageLayoutInflater.inflate(R.layout.user_message, null);
            holder.profile = (View) convertView.findViewById(R.id.profile_picture);
            holder.name = (TextView) convertView.findViewById(R.id.profile_name);
            holder.messageBody = (TextView) convertView.findViewById(R.id.messageBody);
            convertView.setTag(holder);

            holder.name.setText(message.getData().getName());
            holder.messageBody.setText(message.getText());
            GradientDrawable drawable = (GradientDrawable) holder.profile.getBackground();
            drawable.setColor(Color.parseColor(message.getData().getColor()));
        }
        return convertView;
    }


}

class MessageViewHolder{
    public View profile;
    public TextView name;
    public TextView messageBody;
}
