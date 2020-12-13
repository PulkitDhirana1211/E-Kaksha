package com.example.ekaksha.Database.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.example.ekaksha.ClassroomActivity;
import com.example.ekaksha.MainActivity;
import com.example.ekaksha.R;

public class ClassroomlistAdapter extends ResourceCursorAdapter {
    public ClassroomlistAdapter(Context context, int layout, Cursor cursor, int flags) {
        super(context, layout, cursor, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name = (TextView) view.findViewById(R.id.classroom_list_name);
        name.setText(cursor.getString(cursor.getColumnIndex("name")));
        TextView description = (TextView) view.findViewById(R.id.classroom_list_description);
        description.setText(cursor.getString(cursor.getColumnIndex("description")));
        TextView teachername = (TextView) view.findViewById(R.id.list_teacher_name);
        description.setText(cursor.getString(cursor.getColumnIndex("teacher")));

    }

}

