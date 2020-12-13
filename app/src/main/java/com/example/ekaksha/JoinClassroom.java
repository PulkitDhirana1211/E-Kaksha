package com.example.ekaksha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ekaksha.Data.Classroom;
import com.example.ekaksha.Database.ClassroomContract;
import com.example.ekaksha.Database.JoinClassHelper;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

public class JoinClassroom extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference data;
    ProgressDialog progressBar;
    FirebaseUser firebaseUser;
    private TextView response;
    JoinClassHelper joinClassHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_classroom);
        EditText classroomId = (EditText) findViewById(R.id.classroomId);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Button join = (Button) findViewById(R.id.joinClassroom);
        response = (TextView) findViewById(R.id.join_answer);
        database = FirebaseDatabase.getInstance();
        data = database.getReference("classroom");
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = classroomId.getText().toString();

                    progressBar = new ProgressDialog(view.getContext());
                    progressBar.setMessage("Joining Classroom");
                    progressBar.setCancelable(false);
                    progressBar.setCanceledOnTouchOutside(false);
                    progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressBar.show();
                    idExist(id);


                

            }
        });
        joinClassHelper = new JoinClassHelper(getBaseContext(), firebaseUser.getUid());
         db = joinClassHelper.getWritableDatabase();
    }

    public void idExist(String id) {

        final boolean[] result = {false, false};
        final int[] index = new int[1];
        data.orderByChild("id").equalTo(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    result[0] = true;
                    index[0] = snapshot.getValue().toString().indexOf(firebaseUser.getEmail());
                    if (index[0] != -1) {

                        for (DataSnapshot temp : snapshot.getChildren()) {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(temp.getValue().toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {


                                //SQLiteDatabase db_read = joinClassHelper.getReadableDatabase();
                                String[] projection = {ClassroomContract.joined.COLUMN_CLASSROOM_ID};
                                String selection = ClassroomContract.joined.COLUMN_CLASSROOM_ID + "=?";
                                Log.v("classroom", jsonObject.getString("id"));
                                String[] selectionArgs = {jsonObject.getString("id")};
                                Cursor cursor = db.query(ClassroomContract.joined.JOINED_TABLE_NAME + firebaseUser.getUid(), projection, selection, selectionArgs, null, null, null);
                                Classroom classroom=new Classroom();
                                classroom.setId(jsonObject.getString("id"));
                                classroom.setTeacherName(jsonObject.getString("name"));
                                classroom.setDescription(jsonObject.getString("description"));
                                classroom.setTeacherName(jsonObject.getString("teacherName"));
                                DatabaseReference userReference=FirebaseDatabase.getInstance().getReference(firebaseUser.getUid()+"/classroom");
                                userReference.push().setValue(classroom);
                                ContentValues values = new ContentValues();
                                values.put(ClassroomContract.joined.COLUMN_CLASSROOM_ID, jsonObject.getString("id"));
                                values.put(ClassroomContract.joined.COLUMN_NAME, jsonObject.getString("name"));
                                values.put(ClassroomContract.joined.COLUMN_DESCRIPTION, jsonObject.getString("description"));
                                values.put(ClassroomContract.joined.COLUMN_TEACHER, jsonObject.getString("teacherName"));
                                if (cursor.getCount()==0) {
                                   db.insert(ClassroomContract.joined.JOINED_TABLE_NAME + firebaseUser.getUid(), null, values);
                                    Toast.makeText(getBaseContext(), "Classroom Joined sucessfully", Toast.LENGTH_LONG).show();
                                } else {
                                    cursor.close();
                                    Cursor cursor1 = db.rawQuery("SELECT * FROM " + ClassroomContract.joined.JOINED_TABLE_NAME + firebaseUser.getUid(), null);

                                    response.setText(Integer.toString(cursor1.getCount()));
                                    Toast.makeText(getBaseContext(), "Classroom  Already Joined ", Toast.LENGTH_LONG).show();
                                     cursor1.close();
                                }

                            } catch (JSONException e) {

                                e.printStackTrace();
                            }


                            // Create and/or open a database to read from it

                        }
                    } else
                        Toast.makeText(getBaseContext(), "Not allowed to join classroom", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getBaseContext(), "Classroom Not found", Toast.LENGTH_LONG).show();
                }

                progressBar.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.dismiss();
                Toast.makeText(getBaseContext(), "Please try Again", Toast.LENGTH_LONG).show();
            }
        });

    }


}