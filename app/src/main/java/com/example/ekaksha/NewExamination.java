package com.example.ekaksha;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ekaksha.Data.Examination;
import com.example.ekaksha.Data.Question;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

public class NewExamination extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
Button startime;
Button endtime;
Button next;
Button previous;
Button create;
    EditText noquestion;
ArrayList<Question> questions=new ArrayList<Question>();
    int day, month, year, hour, minute;
    int myday, myMonth, myYear, myHour, myMinute;
    int current=0;
    int total=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_examination);
        questions=new ArrayList<Question>();
        startime=(Button)findViewById(R.id.new_examination_startTime);
        next=(Button)findViewById(R.id.new_examination_next);
        previous=(Button)findViewById(R.id.new_examination_previous);
        create=(Button)findViewById(R.id.new_examination_create);
        noquestion=(EditText)findViewById(R.id.new_examination_numberquestion);
         next.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if(noquestion.getText()==null || noquestion.getText().length()==0){
                     Toast.makeText(getBaseContext(),"Please enter No of question ",Toast.LENGTH_LONG).show();
                 return;}
                 total=Integer.parseInt(noquestion.getText().toString());
                 questions.add(getUserQuestion());
                 current=(current+1)%total;
             }
         });
         previous.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 current=(current-1)%total;
                 if(questions.size()<current)
                     SetUserQuestion(questions.get(current));


             }
         });

        startime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(NewExamination.this, NewExamination.this,year, month,day);
                datePickerDialog.show();
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questions.add(getUserQuestion());
                current=(current+1)%total;
                EditText name=(EditText)findViewById(R.id.new_examination_name);
                EditText description=(EditText)findViewById(R.id.new_examination_description);
                EditText maximum=(EditText)findViewById(R.id.new_examination_maximumMarks);
                EditText duration=(EditText)findViewById(R.id.new_examination_duration);
                Examination e=new Examination();
                e.setClassroomID(getIntent().getStringExtra("id"));
                e.setClassroomName(getIntent().getStringExtra("name"));
                e.setName(name.getText().toString());
                e.setMaximumMarks(Integer.parseInt(maximum.getText().toString()));
                e.setTimeStart("Year: " + myYear + "\\" +
                        "Month: " + myMonth + "\\" +
                        "Day: " + myday + "\\" +
                        "Hour: " + myHour + "\\" +
                        "Minute: " + myMinute);
                e.setDuration(Integer.parseInt(duration.getText().toString()));
                e.setUrl(System.currentTimeMillis()+"");
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference( getIntent().getStringExtra("name")+ "/examination");
                databaseReference.push().setValue(e);
                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference( getIntent().getStringExtra("name/")+e.getUrl() );
                databaseReference1.push().setValue(questions);
                Toast.makeText(getBaseContext(),"Exam created",Toast.LENGTH_LONG).show();

            }
        });



    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        myYear = year;
        myday = day;
        myMonth = month;
        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(NewExamination.this, NewExamination.this, hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        myHour = hourOfDay;
        myMinute = minute;
        startime.setText("Year: " + myYear + "\\" +
                "Month: " + myMonth + "\\" +
                "Day: " + myday + "\\" +
                "Hour: " + myHour + "\\" +
                "Minute: " + myMinute);
    }
    public Question getUserQuestion()
    {
        Question q=new Question();
        EditText question=(EditText)findViewById(R.id.new_examination_question);
        EditText option1=(EditText)findViewById(R.id.new_examination_option1);
        EditText option2=(EditText)findViewById(R.id.new_examination_option2);
        EditText option3=(EditText)findViewById(R.id.new_examination_option3);
        EditText option4=(EditText)findViewById(R.id.new_examination_option4);
        q.setQuestion(question.getText().toString());
        ArrayList<String> option=new ArrayList<String>();
        option.add(option1.getText().toString());
        option.add(option2.getText().toString());
        option.add(option3.getText().toString());
        option.add(option4.getText().toString());
        q.setOptions(option);
        question.setText("");
        option1.setText("");
        option2.setText("");
        option3.setText("");
        option4.setText("");

        return q;
    }
    public void  SetUserQuestion(Question q)
    {

        EditText question=(EditText)findViewById(R.id.new_examination_question);
        EditText option1=(EditText)findViewById(R.id.new_examination_option1);
        EditText option2=(EditText)findViewById(R.id.new_examination_option2);
        EditText option3=(EditText)findViewById(R.id.new_examination_option3);
        EditText option4=(EditText)findViewById(R.id.new_examination_option4);
        q.setQuestion(question.getText().toString());
        question.setText(q.getQuestion());
        option1.setText(q.getOptions().get(0));
        option2.setText(q.getOptions().get(1));
        option3.setText(q.getOptions().get(2));
        option4.setText(q.getOptions().get(3));


    }
}