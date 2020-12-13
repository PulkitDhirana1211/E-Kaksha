package com.example.ekaksha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.ekaksha.Data.Classroom;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);

            startActivity(intent);
            finish();
        } else if (!user.isEmailVerified()) {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                setContentView(R.layout.activity_main);
                                Toast.makeText(getBaseContext(),"Email sent please verify then login",Toast.LENGTH_LONG).show();
                                firebaseAuth.signOut();
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
        } else {
            setContentView(R.layout.activity_main);
            //TextView textView = (TextView) findViewById(R.id.text_main);
           // textView.setText("Welcome to ekaksha");
            FrameLayout frameLayout=(FrameLayout)findViewById(R.id.flFragment);
            setCurrentFragment(new Classroom_fragment());
            BottomNavigationView bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottom_navigation) ;
            String[] options = {"Create", "Join"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select Option");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(which==0)
                    {
                        Toast.makeText(getBaseContext(),"Create clicked",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(MainActivity.this,CreateClassroom.class);
                        startActivity(intent);


                    }
                    else{
                        Toast.makeText(getBaseContext(),"Join clicked",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(MainActivity.this, JoinClassroom.class);
                        startActivity(intent);

                    }

                    // the user clicked on colors[which]
                }
            });

            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                  int ID=item.getItemId();
                 if(ID==R.id.page_classroom)
                     setCurrentFragment(new Classroom_fragment());
                     else if (ID==R.id.page_assignment)
                     setCurrentFragment(new Assignment_fragment());
                 else if(ID==R.id.page_exams)
                     setCurrentFragment(new Examination_fragment());
                 else if(ID==R.id.page_settings)
                     setCurrentFragment(new Settings_fragment());
                 else {
                     builder.show();
                 }

                    return true;
                }
            });


        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_sign_out) {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(MainActivity.this,
                    "You have been signed out.",
                    Toast.LENGTH_LONG)
                    .show();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            // Close activity
            finish();
        }
        return true;

    }
   public void setCurrentFragment(Fragment fragment) {
       getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, fragment).commit();
   }

}