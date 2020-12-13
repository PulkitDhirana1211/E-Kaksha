package com.example.ekaksha;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IInterface;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ekaksha.Data.Assignment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewAssignment extends AppCompatActivity {
private Button upload;
private Button select;
private Button create;
String downloadURL;
AlertDialog.Builder builder;

    private TextView filename;
FirebaseDatabase database;
FirebaseStorage storage;
ProgressDialog progressDialog;
private Uri pdfUri;
private Button deadline;
    int day, month, year;
    int myday, mymonth, myyear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_assignment);
        select=(Button)findViewById(R.id.new_assignment_upload);
        create=(Button)findViewById(R.id.new_assignment_create);
        filename=(TextView)findViewById(R.id.new_assignment_filename);
        deadline=(Button) findViewById(R.id.new_assignment_deadline);
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        builder = new AlertDialog.Builder(this);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                myyear=i;
                mymonth=i1;
                myday=i2;


            }
        },2020,11,1);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                    selectPDF();

                }
                else
                {
                    ActivityCompat.requestPermissions(NewAssignment.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
                }
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Assignment assignment=new Assignment();
                EditText name=(EditText)findViewById(R.id.new_assignment_name);
                EditText description=(EditText)findViewById(R.id.new_assignment_name);

                assignment.setName(name.getText().toString());
                assignment.setDescription(description.getText().toString());
                assignment.setUrl(downloadURL);


                assignment.setDeadline(myday+"/"+mymonth+"/"+myyear +" 23:59");;
                builder.setMessage("Name:"+assignment.getName()+"\n"+"description:"+assignment.getDescription()+"\n"+
                        "File"+pdfUri.getPath()+"\n"+"deadline:"+assignment.getDeadline())
                        .setCancelable(false)
                        .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                assignment.setId(System.currentTimeMillis());
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference( getIntent().getStringExtra("name")+ "/assignmnet");
                                databaseReference.push().setValue(assignment);
                                Toast.makeText(getApplicationContext(),"you choose yes action for alertbox",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                                Toast.makeText(getApplicationContext(),"you choose no action for alertbox",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Create Assignmnet Preview");
                alert.show();

            }
        });
        deadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog.show();
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       if(requestCode==9 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
           selectPDF();
    }
    public void selectPDF()
    {
        Intent intent=new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,19);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 19 && resultCode == RESULT_OK && data != null) {
             pdfUri=data.getData();
             filename.setText(pdfUri.toString());
             uploadfile(pdfUri);
        }
        else
            Toast.makeText(this,"Please Select file",Toast.LENGTH_LONG).show();

    }
    public void uploadfile(Uri data)
    {   Log.v("shivam","shivamGupta");
        progressDialog=new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        progressDialog.setTitle("Uploading...");
        progressDialog.setProgress(0);
        progressDialog.show();
       Log.v("shivam",new Date(System.currentTimeMillis())+"");
        String name=System.currentTimeMillis()+"";

        StorageReference storageReference=storage.getReference().child("assignments").child(name);
        Log.v("shivam","shivam1");
        storageReference.putFile(data)
        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            Log.v("shivam","shivam2");
            downloadURL= storageReference.getDownloadUrl().toString();
            Toast.makeText(NewAssignment.this,"File Successfully uploaded",Toast.LENGTH_LONG).show();
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
    }
}