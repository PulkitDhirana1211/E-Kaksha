package com.example.ekaksha;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ekaksha.Database.Adapter.AssignmentListAdapter;
import com.example.ekaksha.Database.ClassroomContract;
import com.example.ekaksha.Database.JoinClassHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Assignment_fragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assignment, container, false);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        JoinClassHelper joinClassHelper = new JoinClassHelper(getContext(), firebaseUser.getUid());

        // Create and/or open a database to read from it
        SQLiteDatabase db = joinClassHelper.getReadableDatabase();

        // Perform this raw SQL query "SELECT * FROM pets"
        // to get a Cursor that contains all rows from the pets table.
        Cursor cursor = db.rawQuery("SELECT * FROM " + ClassroomContract.AssignmentList.JOINED_TABLE_NAME + firebaseUser.getUid(), null);
        AssignmentListAdapter assignmentListAdapter = new AssignmentListAdapter(getContext(), R.layout.assignment_listitem, cursor, 0);
        ListView listView = (ListView) view.findViewById(R.id.assignmnet_listview);
        listView.setAdapter(assignmentListAdapter);
        return view;
    }

}