package com.example.ekaksha.Database.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.example.ekaksha.Database.ClassroomContract;
import com.example.ekaksha.R;

public class AssignmentListAdapter extends ResourceCursorAdapter {
    public AssignmentListAdapter(Context context, int layout, Cursor cursor, int flags) {
        super(context, layout, cursor, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name = (TextView) view.findViewById(R.id.assignment_list_name);
        name.setText(cursor.getString(cursor.getColumnIndex("name")));
        TextView description = (TextView) view.findViewById(R.id.assignment_list_description);
        description.setText(cursor.getString(cursor.getColumnIndex("description")));
        TextView teachername = (TextView) view.findViewById(R.id.assignment_classroom_name);
        description.setText(cursor.getString(cursor.getColumnIndex(ClassroomContract.AssignmentList.COLUMN_CLASSROOM_NAME)));
        TextView deadline = (TextView) view.findViewById(R.id.assignmnet_deadline);
        deadline.setText(cursor.getString(cursor.getColumnIndex("deadline")));

    }

}