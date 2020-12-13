package com.example.ekaksha;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ekaksha.Data.Classroom;
import com.example.ekaksha.Database.Adapter.ClassroomlistAdapter;
import com.example.ekaksha.Database.ClassroomContract;
import com.example.ekaksha.Database.JoinClassHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;


public class Classroom_fragment extends Fragment {
    JoinClassHelper joinClassHelper;
    SQLiteDatabase db;
    Cursor cursor;
    ClassroomlistAdapter classroomlistAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view=inflater.inflate(R.layout.fragment_classroom, container, false);
        ListView listView=(ListView)view.findViewById(R.id.clasroom_listview);
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        joinClassHelper=new JoinClassHelper(getContext(),firebaseUser.getUid());

        // Create and/or open a database to read from it
         db = joinClassHelper.getWritableDatabase();
         cursor = db.rawQuery("SELECT * FROM " + ClassroomContract.joined.JOINED_TABLE_NAME + firebaseUser.getUid(), null);
        classroomlistAdapter=new ClassroomlistAdapter(getContext(),R.layout.classroom_listitem,cursor,0);
        listView.setAdapter(classroomlistAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                if(cursor != null) {
                    Intent intent = new Intent(getActivity(), ClassroomActivity.class) ;
                    intent.putExtra("name",cursor.getString(cursor.getColumnIndex("name")));
                    intent.putExtra("id",cursor.getString(cursor.getColumnIndex("classroomId")));
                    startActivity(intent);
                }
            }
        });

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(firebaseUser.getUid()+"/classroom");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                     Classroom temprory=snapshot.getValue(Classroom.class);

                            //SQLiteDatabase db_read = joinClassHelper.getReadableDatabase();
                            String[] projection = {ClassroomContract.joined.COLUMN_CLASSROOM_ID};
                            String selection = ClassroomContract.joined.COLUMN_CLASSROOM_ID + "=?";
                            Log.v("classroom", temprory.getId());
                            String[] selectionArgs = {temprory.getId()};
                            Cursor cursor_query = db.query(ClassroomContract.joined.JOINED_TABLE_NAME + firebaseUser.getUid(), projection, selection, selectionArgs, null, null, null);

                            ContentValues values = new ContentValues();
                            values.put(ClassroomContract.joined.COLUMN_CLASSROOM_ID,temprory.getId());
                            values.put(ClassroomContract.joined.COLUMN_NAME, temprory.getName());
                            values.put(ClassroomContract.joined.COLUMN_DESCRIPTION, temprory.getDescription());
                            values.put(ClassroomContract.joined.COLUMN_TEACHER, temprory.getTeacherName());
                            if (cursor_query.getCount() == 0) {
                                db.insert(ClassroomContract.joined.JOINED_TABLE_NAME + firebaseUser.getUid(), null, values);
                                cursor_query.close();
                                Log.v("data", "inserted");
                                Intent intent = getActivity().getIntent();
                                // getActivity().finish();
                               // startActivity(intent);
                                // Cursor temp_cursor   = db.rawQuery("SELECT * FROM " + ClassroomContract.joined.JOINED_TABLE_NAME + firebaseUser.getUid(), null);
                                // cursor.requery();
                                // cursor.close();
                                // cursor=temp_cursor;
                                cursor.requery();
                                classroomlistAdapter.swapCursor(cursor);
                                listView.setAdapter(classroomlistAdapter);
                                //  classroomlistAdapter.notifyDataSetChanged();
                                // temp_cursor.close();
                                //Toast.makeText(getBaseContext(), "Classroom Joined sucessfully", Toast.LENGTH_LONG).show();
                            } else {
                                Log.v("id ", "id found");
                            }



                         Log.v("new Child shiva", temprory.getId());

                    }



            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.v("shivam",snapshot.toString());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Log.v("shivam",snapshot.toString());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // Perform this raw SQL query "SELECT * FROM pets"
        // to get a Cursor that contains all rows from the pets table.


         return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        cursor.requery();
        classroomlistAdapter.notifyDataSetChanged();
    }
}