package com.example.ekaksha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.ekaksha.Data.Classroom;
import com.example.ekaksha.Data.Student;
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

import java.util.ArrayList;

public class CreateClassroom extends AppCompatActivity {
  FirebaseUser firebaseUser;
    DatabaseReference mDatabase;
    FirebaseDatabase database;
    JoinClassHelper joinClassHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_classroom);

        Classroom classroom= new Classroom();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
         database=FirebaseDatabase.getInstance();
        mDatabase = database.getReference("classroom");
        joinClassHelper=new JoinClassHelper(getBaseContext(),firebaseUser.getUid());
         db = joinClassHelper.getWritableDatabase();
        Button create=(Button)findViewById(R.id.create_button);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText name=(EditText)findViewById(R.id.create_classroom_name);
                EditText description=(EditText)findViewById(R.id.create_classroom_description);
                EditText student=(EditText)findViewById(R.id.student_one);
                classroom.setName(name.getText().toString());
                classroom.setDescription(description.getText().toString());
                int ID=0;
                ID=100000+ (int)(Math.random()*899999);
               classroom.setId(Integer.toString(ID)+FirebaseAuth.getInstance().getCurrentUser().getEmail());
               classroom.setStudentsList(student.getText().toString());
               classroom.setTeacherName(firebaseUser.getDisplayName());
                mDatabase.push().setValue(classroom);
                database.getReference(firebaseUser.getUid()+"/classroom").push().setValue(classroom);
                ContentValues values=new ContentValues();
                values.put(ClassroomContract.joined.COLUMN_CLASSROOM_ID,classroom.getId());
                values.put(ClassroomContract.joined.COLUMN_NAME,classroom.getName());
                values.put(ClassroomContract.joined.COLUMN_DESCRIPTION,classroom.getDescription());
                values.put(ClassroomContract.joined.COLUMN_TEACHER,classroom.getTeacherName());
                db.insert(ClassroomContract.joined.JOINED_TABLE_NAME+firebaseUser.getUid(),null,values);
                Toast.makeText(getBaseContext(),"Classroom Created ",Toast.LENGTH_LONG).show();

            }
        });

    }

}