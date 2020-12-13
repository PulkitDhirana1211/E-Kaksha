package com.example.ekaksha.Database.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.ekaksha.R;

public class ClassroomChatAdapter extends CursorAdapter {
    public ClassroomChatAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        Log.v("wjkdbjked","adpter_cursor");
        return LayoutInflater.from(context).inflate(R.layout.received_message, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name = (TextView) view.findViewById(R.id.text_message_name);
        name.setText(cursor.getString(cursor.getColumnIndex("name")));
        TextView message = (TextView) view.findViewById(R.id.text_message_body);
        message.setText(cursor.getString(cursor.getColumnIndex("message")));
        TextView time = (TextView) view.findViewById(R.id.text_message_time);
        time.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                cursor.getLong(cursor.getColumnIndex("time"))));
        Log.v("wjkdbjked","adpter");


    }
}
