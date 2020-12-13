package com.example.ekaksha;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NewExamination extends AppCompatActivity {
private long h=10800000;
private TextView timer;
private Button b1;
    private Button b2;
    private CountDownTimer gh;
    private boolean runn;
    private long left=h;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questions);
        timer = findViewById(R.id.maintimer);
        b1=findViewById(R.id.b11);
         b1.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 start();
             }
         });
         update();
    }
public void start(){
//if(runn)
  //  stoptimer();
//else
    starttimer();
}
public void starttimer(){
gh=new CountDownTimer(h,1000) {
    @Override
    public void onTick(long l) {
      h=l;
      update();
    }

    @Override
    public void onFinish() {

    }
}.start();
b1.setText("Pause");
runn=true;
}
/*public void stoptimer(){
        gh.cancel();
        b1.setText("Start");
        runn=false;
}
 */
public void update(){
        int minutes=(int) h/60000;
        int sec=(int) h%60000/1000;
String abc;
abc=""+minutes;
abc+=":";
if(sec<10)
    abc+="0";
abc+=sec;
timer.setText(abc);
}
}