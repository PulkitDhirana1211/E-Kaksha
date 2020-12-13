package com.example.ekaksha.Database.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.example.ekaksha.R;

public class ExaminationListAdapter extends ResourceCursorAdapter {
    public ExaminationListAdapter(Context context, int layout, Cursor cursor, int flags) {
        super(context, layout, cursor, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name = (TextView) view.findViewById(R.id.examination_name);
        name.setText(cursor.getString(cursor.getColumnIndex("name")));
        TextView description = (TextView) view.findViewById(R.id.examination_list_description);
        description.setText(cursor.getString(cursor.getColumnIndex("description")));
        TextView starttime = (TextView) view.findViewById(R.id.timing);
        starttime.setText(cursor.getString(cursor.getColumnIndex("start")));
        TextView maximumMarks = (TextView) view.findViewById(R.id.maximum_marks);
        starttime.setText(cursor.getString(cursor.getColumnIndex("maximumMarks")));
        TextView classroom = (TextView) view.findViewById(R.id.examination_classroom_name);
        starttime.setText(cursor.getString(cursor.getColumnIndex("classroomName")));

    }

}