package com.example.ekaksha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Editable username;
   private ProgressDialog progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button button = (Button) findViewById(R.id.reister_button);
        TextView answer = (TextView) findViewById(R.id.register_answer);
        TextView signIn=(TextView)findViewById(R.id.signIn);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText textView = (EditText) findViewById(R.id.register_email);
                EditText textView1 = (EditText) findViewById(R.id.register_passward);
                EditText textView2=(EditText)findViewById(R.id.register_confirm) ;
                EditText name=(EditText)findViewById(R.id.register_name);
                username = textView.getText();
                 Editable passward = textView1.getText();
                 Editable confirm = textView2.getText();

                if (!isValid(username.toString())) {
                    answer.setText("Enter a valid E-mail Id");
                } else if (passward.length() < 8) {
                    answer.setText("Passward must have atleast 8 characters");
                } else if (!passward.toString().equals(confirm.toString())) {
                    Log.v("a",passward.toString());
                    Log.v("b",confirm.toString());
                    answer.setText("Passward Not matches");
                }
                else if(name.getText().length()<2 || name.getText().toString().matches("\\d"))
                {
                    answer.setText("Enter a correct Name");
                }
                else {
                    firebaseAuth = FirebaseAuth.getInstance();
                    progressBar = new ProgressDialog(view.getContext());
                    progressBar.setMessage("Signing Up");
                    progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressBar.show();

                    firebaseAuth.createUserWithEmailAndPassword(username.toString(), passward.toString())
                            .addOnCompleteListener(
                                    new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (!task.isSuccessful()) {
                                                try {
                                                    throw task.getException();
                                                }
                                                // if user enters wrong email.
                                                catch (FirebaseAuthWeakPasswordException weakPassword) {

                                                    progressBar.dismiss();
                                                    answer.setText("Weak Passward");
                                                    // TODO: take your actions!
                                                }
                                                // if user enters wrong password.
                                                catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                                                    progressBar.dismiss();
                                                    answer.setText("Invalid E-mail ");

                                                    // TODO: Take your action
                                                } catch (FirebaseAuthUserCollisionException existEmail) {

                                                } catch (Exception e) {
                                                    progressBar.dismiss();
                                                    answer.setText("E-mail already Exist");
                                                }
                                            } else {

                                                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                        .setDisplayName(name.getText().toString())
                                                        .build();
                                                user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        progressBar.dismiss();
                                                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });

                                            }
                                        }
                                    }
                            );
                }
            }
        });


    }

    static boolean isValid(String email) {
        String regex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
        return email.matches(regex);
    }




}