package com.example.ekaksha;

import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ekaksha.Database.Adapter.ClassroomlistAdapter;
import com.example.ekaksha.Database.Adapter.ExaminationListAdapter;
import com.example.ekaksha.Database.ClassroomContract;

import com.example.ekaksha.Database.JoinClassHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Examination_fragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_examination, container, false);
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        JoinClassHelper joinClassHelper=new JoinClassHelper(getActivity().getBaseContext(),firebaseUser.getUid());

        // Create and/or open a database to read from it
        SQLiteDatabase db = joinClassHelper.getReadableDatabase();

        // Perform this raw SQL query "SELECT * FROM pets"
        // to get a Cursor that contains all rows from the pets table.
        Cursor cursor = db.rawQuery("SELECT * FROM " + ClassroomContract.ExaminationList.JOINED_TABLE_NAME+firebaseUser.getUid(), null);
        ExaminationListAdapter examinationListAdapter=new ExaminationListAdapter(getContext(),R.layout.examination_listitem,cursor,0);
        ListView listView=(ListView)view.findViewById(R.id.examination_listview);
        listView.setAdapter(examinationListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getActivity(),ExaminationActivity.class);
                startActivity(intent);

            }
        });
        return view;
    }
}