package com.talha.workspace;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText,passEditText;
    Button loginButton,goToSingup;
    FirebaseAuth auth;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        emailEditText = (EditText) findViewById(R.id.email_edit_text_in_login);
        passEditText = (EditText) findViewById(R.id.pass_edit_text_in_login);
        loginButton = (Button) findViewById(R.id.login_button);
        goToSingup = (Button) findViewById(R.id.go_to_signup);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);

        loginButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case ACTION_DOWN:
                        loginButton.setBackgroundResource(R.drawable.login_button1);
                        return true;
                    case ACTION_UP:
                        loginButton.setBackgroundResource(R.drawable.login_button);
                        final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
                        dialog.setCancelable(false);
                        dialog.setMessage("Please Wait!");
                        dialog.show();
                        String email = emailEditText.getText().toString().trim();
                        String pass = passEditText.getText().toString().trim();
                        if(!email.isEmpty()&&!pass.isEmpty()){
                            auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        dialog.dismiss();
                                        if(sharedPreferences.getInt("admin",0)==0){
                                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                        }else {
                                            startActivity(new Intent(LoginActivity.this,AdminActivity.class));
                                        }
                                        finish();
                                    }else {
                                        dialog.dismiss();
                                        Toast.makeText(LoginActivity.this,"Something Wrong! Try Again!",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else {
                            dialog.dismiss();
                            Toast.makeText(LoginActivity.this,"Given Data is not Correct!",Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    default:
                        return false;
                }
            }
        });

        goToSingup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case ACTION_DOWN:
                        goToSingup.setBackgroundResource(R.drawable.go_to_signup1);
                        return true;
                    case ACTION_UP:
                        goToSingup.setBackgroundResource(R.drawable.go_to_signup);
                        startActivity(new Intent(LoginActivity.this,SignupActivity.class));
                        finish();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }
}
