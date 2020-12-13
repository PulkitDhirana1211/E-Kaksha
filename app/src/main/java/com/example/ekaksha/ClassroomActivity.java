package com.example.ekaksha;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ekaksha.Data.Assignment;
import com.example.ekaksha.Data.Examination;
import com.example.ekaksha.Data.Message;
import com.example.ekaksha.Database.Adapter.ClassroomChatAdapter;
import com.example.ekaksha.Database.ClassroomContract;
import com.example.ekaksha.Database.JoinClassHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

public class ClassroomActivity extends AppCompatActivity {
    JoinClassHelper joinClassHelper;
    SQLiteDatabase db;
    AlertDialog.Builder builder;
    Cursor cursor;
    ClassroomChatAdapter classroomChatAdapter;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ProgressDialog progressDialog;
    private Uri pdfUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        setTitle(name);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(name + "/messages");
        TextInputEditText messageBox = (TextInputEditText) findViewById(R.id.editTextNumberDecimal);
        FloatingActionButton send = (FloatingActionButton) findViewById(R.id.floating_action_button);
        joinClassHelper = new JoinClassHelper(getBaseContext(), user.getUid());
        db = joinClassHelper.getWritableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + ClassroomContract.MessageList.TABLE_NAME + user.getUid(), null);
        classroomChatAdapter = new ClassroomChatAdapter(getBaseContext(), cursor);
        Log.v(" message data", "inserted" + cursor.getCount());
        ListView recyclerView = (ListView) findViewById(R.id.reyclerview_message_list);
        recyclerView.setSelection(recyclerView.getCount());
        recyclerView.setAdapter(classroomChatAdapter);
        // Create and/or open a database to read from it

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (messageBox.getText().length() > 0) {
                    Message msg = new Message();
                    msg.setUserName(user.getEmail());
                    msg.setMessage(messageBox.getText().toString());
                    msg.setTime(new Date().getTime());
                    databaseReference.push().setValue(msg);
                    messageBox.setText("");
                }
            }
        });
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message msg = snapshot.getValue(Message.class);
                String[] projection = {ClassroomContract.MessageList.COLUMN_MESSAGE_ID};
                String selection = ClassroomContract.MessageList.COLUMN_MESSAGE_ID + "=?";
                Log.v("classroom", "time" + msg.getTime());
                String[] selectionArgs = {snapshot.getKey()};
                Cursor cursor_query = db.query(ClassroomContract.MessageList.TABLE_NAME + user.getUid(), projection, selection, selectionArgs, null, null, null);
                if (cursor_query.getCount() == 0) {
                    ContentValues values = new ContentValues();
                    values.put(ClassroomContract.MessageList.COLUMN_CLASSROOM_ID, name);
                    values.put(ClassroomContract.MessageList.COLUMN_MESSAGE_ID, snapshot.getKey());
                    values.put(ClassroomContract.MessageList.COLUMN_NAME, msg.getUserName());
                    values.put(ClassroomContract.MessageList.COLUMN_URL, msg.getFileUrl());
                    values.put(ClassroomContract.MessageList.COLUMN_message, msg.getMessage());
                    values.put(ClassroomContract.MessageList.COLUMN_TIME, msg.getTime());


                    db.insert(ClassroomContract.MessageList.TABLE_NAME + user.getUid(), null, values);
                    cursor_query.close();

                    //Intent intent = getActivity().getIntent();
                    // getActivity().finish();
                    // startActivity(intent);
                    // Cursor temp_cursor   = db.rawQuery("SELECT * FROM " + ClassroomContract.joined.JOINED_TABLE_NAME + firebaseUser.getUid(), null);
                    // cursor.requery();
                    // cursor.close();
                    // cursor=temp_cursor;
                    cursor.requery();
                    Log.v(" message data", "inserted" + cursor.getCount());
                    classroomChatAdapter.swapCursor(cursor);
                    recyclerView.setAdapter(classroomChatAdapter);
                    //  classroomlistAdapter.notifyDataSetChanged();
                    // temp_cursor.close();
                    //Toast.makeText(getBaseContext(), "Classroom Joined sucessfully", Toast.LENGTH_LONG).show();
                } else {
                    Log.v("id ", "id found");
                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseDatabase.getInstance().getReference(name + "/assignmnet").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Assignment assignment = snapshot.getValue(Assignment.class);
                String[] projection = {ClassroomContract.AssignmentList.COLUMN_CLASSROOM_ID};
                String selection = ClassroomContract.AssignmentList.COLUMN_ASSIGNMENT_ID + "=?";
                Log.v("classroom", "time" + assignment.getId() + "");
                String[] selectionArgs = {String.valueOf(assignment.getId())};
                Cursor cursor_query = db.query(ClassroomContract.AssignmentList.JOINED_TABLE_NAME + user.getUid(), projection, selection, selectionArgs, null, null, null);
                if (cursor_query.getCount() == 0) {
                    ContentValues values = new ContentValues();
                    values.put(ClassroomContract.AssignmentList.COLUMN_CLASSROOM_ID, assignment.getClassroomID());
                    values.put(ClassroomContract.AssignmentList.COLUMN_ASSIGNMENT_ID, assignment.getId());
                    values.put(ClassroomContract.AssignmentList.COLUMN_NAME, assignment.getName());
                    values.put(ClassroomContract.AssignmentList.COLUMN_URL, assignment.getUrl());
                    values.put(ClassroomContract.AssignmentList.COLUMN_DEADLINE, assignment.getDeadline());
                    values.put(ClassroomContract.AssignmentList.COLUMN_DESCRIPTION, assignment.getDescription());
                    values.put(ClassroomContract.AssignmentList.COLUMN_CLASSROOM_NAME, assignment.getClassroomName());


                    db.insert(ClassroomContract.AssignmentList.JOINED_TABLE_NAME + user.getUid(), null, values);
                    cursor_query.close();


                } else {
                    Log.v("id ", "id found");
                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseDatabase.getInstance().getReference(name + "/examination").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Examination examination = snapshot.getValue(Examination.class);
                String[] projection = {ClassroomContract.ExaminationList.COLUMN_CLASSROOM_ID};
                String selection = ClassroomContract.ExaminationList.COLUMN_URL + "=?";
                Log.v("classroom", "time" + examination.getUrl()+ "");
                String[] selectionArgs = {examination.getUrl()};
                Cursor cursor_query = db.query(ClassroomContract.ExaminationList.JOINED_TABLE_NAME + user.getUid(), projection, selection, selectionArgs, null, null, null);
                if (cursor_query.getCount() == 0) {
                    ContentValues values = new ContentValues();
                    values.put(ClassroomContract.ExaminationList.COLUMN_CLASSROOM_ID,examination.getClassroomID());
                    values.put(ClassroomContract.ExaminationList.COLUMN_MAXIMUM_MARKS,examination.getMaximumMarks());
                    values.put(ClassroomContract.ExaminationList.COLUMN_NAME, examination.getName());
                    values.put(ClassroomContract.ExaminationList.COLUMN_URL,examination.getUrl());
                    values.put(ClassroomContract.ExaminationList.COLUMN_START, examination.getTimeStart());
                    values.put(ClassroomContract.ExaminationList.COLUMN_DESCRIPTION,examination.getDescription());
                    values.put(ClassroomContract.ExaminationList.COLUMN_CLASSROOM_NAME, examination.getClassroomName());
                    values.put(ClassroomContract.ExaminationList.COLUMN_END, String.valueOf(examination.getDuration()));


                    db.insert(ClassroomContract.ExaminationList.JOINED_TABLE_NAME + user.getUid(), null, values);
                    cursor_query.close();


                } else {
                    Log.v("id ", "id found");
                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //TextView textView=(TextView)findViewById(R.id.clasroom_activity_name);
        //textView.setText(name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.classroom_create_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.create_assignment) {
            Intent intent = new Intent(this, NewAssignment.class);
            intent.putExtra("name", getIntent().getStringExtra("name"));
            intent.putExtra("id", getIntent().getStringExtra("id"));

            startActivity(intent);

        } else if (item.getItemId() == R.id.create_examination) {
            Intent intent = new Intent(this, NewExamination.class);
            intent.putExtra("name", getIntent().getStringExtra("name"));
            intent.putExtra("id", getIntent().getStringExtra("id"));
            startActivity(intent);

        } else if (item.getItemId() == R.id.uploadfile) {

            database=FirebaseDatabase.getInstance();
            storage=FirebaseStorage.getInstance();
            builder = new AlertDialog.Builder(this);

            if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                selectPDF();

            } else {
                ActivityCompat.requestPermissions(ClassroomActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
            }

        }

        return true;
    }
    public void selectPDF()
    {
        Intent intent=new Intent();
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,19);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 19 && resultCode == RESULT_OK && data != null) {
            pdfUri=data.getData();

            uploadfile(pdfUri);
        }
        else
            Toast.makeText(this,"Please Select file",Toast.LENGTH_LONG).show();

    }
    public void uploadfile(Uri data)
    {   Log.v("shivam","shivamGupta");
        progressDialog=new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
       progressDialog.setCancelable(false);
        progressDialog.setTitle("Uploading...");
        progressDialog.setProgress(0);
        progressDialog.show();
        Log.v("shivam",new Date(System.currentTimeMillis())+"");
        String name=System.currentTimeMillis()+"";

        StorageReference storageReference=storage.getReference();
        Log.v("shivam","shivam1");
        storageReference.child("assignments").child(name).putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.v("shivam","shivam2");
                       String downloadURL= taskSnapshot.getStorage().getDownloadUrl().toString();
                        Toast.makeText(ClassroomActivity.this,"File Successfully uploaded",Toast.LENGTH_LONG).show();
                        Log.v("shivam",downloadURL);
                        progressDialog.dismiss();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.v("error",e.toString());
                progressDialog.dismiss();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                Log.v("shivam","shivam3");
                int progress = (int)((100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount());
                progressDialog.setProgress(progress);

            }
        });
    }@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==9 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            selectPDF();
    }

}
