package com.example.ekaksha;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private Editable username;
    ProgressDialog progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button button = (Button) findViewById(R.id.cirLoginButton);
        TextView answer=(TextView)findViewById(R.id.login_answer);
        TextView signUp=(TextView)findViewById(R.id.signUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText textView = (EditText) findViewById(R.id.editTextEmail);
                EditText textView1 = (EditText) findViewById(R.id.editTextPassword);

                username = textView.getText();
                final Editable passward = textView1.getText();

                if (!isValid(username.toString())) {
                    answer.setText("Enter a valid Email");
                } else if (passward.length() < 8) {
                    answer.setText("Passward Must have atleast 8 characters");
                } else {
                    firebaseAuth = FirebaseAuth.getInstance();
                    progressBar = new ProgressDialog(view.getContext());
                    progressBar.setMessage("Signing In");
                    progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressBar.show();
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(username.toString(), passward.toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                progressBar.dismiss();
                                try {
                                    throw task.getException();
                                }
                                // if user enters wrong email.
                                catch (FirebaseAuthInvalidCredentialsException e) {

                                    answer.setText("Bad credentials");
                                    //TextView textView=(TextView)findViewById(R.id.forgot);
                                    // textView.setVisibility(View.VISIBLE);
                                } catch (Exception e) {
                                    answer.setText("Invalid Details");
                                    e.printStackTrace();
                                }


                            } else {
                                progressBar.dismiss();
                                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });

                }

            }
        });
        findViewById(R.id.forgot_passward).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username!=null && isValid(username.toString())) {
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.sendPasswordResetEmail(username.toString());
                    Toast.makeText(getBaseContext(),"Passward reset Email Sent",Toast.LENGTH_LONG).show();
                }
                else
                    answer.setText("Enter correct email ");
            }
        });

    }



    static boolean isValid(String email) {
        String regex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
        return email.matches(regex);
    }




}