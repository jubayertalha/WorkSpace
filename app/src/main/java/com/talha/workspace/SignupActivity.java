package com.talha.workspace;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;

public class SignupActivity extends AppCompatActivity {

    EditText nameEditText,mobileEditText,emailEditText,passEditText;
    Button singupButton,goToLoginButton;
    String name,mobile,email,pass;
    ArrayList<WorkData> works;
    SingupData singupData;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference reference;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        nameEditText = (EditText) findViewById(R.id.name_edit_text_in_singup);
        mobileEditText = (EditText) findViewById(R.id.mobile_edit_text_in_singup);
        emailEditText = (EditText) findViewById(R.id.email_edit_text_in_singup);
        passEditText = (EditText) findViewById(R.id.pass_edit_text_in_signup);
        singupButton = (Button) findViewById(R.id.signup_button);
        goToLoginButton = (Button) findViewById(R.id.go_to_login);
        logo = (ImageView) findViewById(R.id.logo_with_name_in_signup);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SignupActivity.this);
        editor = sharedPreferences.edit();

        singupData = new SingupData();

        auth = FirebaseAuth.getInstance();
        //user = auth.getCurrentUser();
        //database = FirebaseDatabase.getInstance().getReference().getDatabase();
        //reference = database.getReference(user.getUid());

        singupButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case ACTION_DOWN:
                        singupButton.setBackgroundResource(R.drawable.signup_button1);
                        return true;
                    case ACTION_UP:
                        singupButton.setBackgroundResource(R.drawable.signup_button);

                        final ProgressDialog dialog = new ProgressDialog(SignupActivity.this);
                        dialog.setMessage("Please wait!");
                        dialog.setCancelable(false);
                        dialog.show();

                        name = nameEditText.getText().toString().trim();
                        mobile = mobileEditText.getText().toString().trim();
                        email = emailEditText.getText().toString().trim();
                        pass = passEditText.getText().toString().trim();
                        works = new ArrayList<>();

                        WorkData d1 = new WorkData("New Job","You have to complete all works what is given to you","2 days",2,0,0);
                        WorkData d2 = new WorkData("New Job 1","You have to complete all works what is given to you","2 days",5,0,1);
                        WorkData d3 = new WorkData("New Job 2","You have to complete all works what is given to you","2 days",4,1,2);

                        works.add(d1);
                        works.add(d2);
                        works.add(d3);

                        singupData = new SingupData(name,mobile,email,pass,works,"null");

                        if(!singupData.getName().isEmpty()&&!singupData.getMobile().isEmpty()||!singupData.getEmail().isEmpty()&&!singupData.getPass().isEmpty()){
                            auth.createUserWithEmailAndPassword(singupData.getEmail(),singupData.getPass()).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {


                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        if(sharedPreferences.getInt("admin",0)==0){
                                            auth = FirebaseAuth.getInstance();
                                            user = auth.getCurrentUser();
                                            database = FirebaseDatabase.getInstance();
                                            reference = database.getReference("users");
                                            singupData = new SingupData(name,mobile,email,pass,works,user.getUid());
                                            reference.child(user.getUid()).setValue(singupData);
                                            dialog.dismiss();
                                            startActivity(new Intent(SignupActivity.this,MainActivity.class));
                                        }else {
                                            dialog.dismiss();
                                            startActivity(new Intent(SignupActivity.this,AdminActivity.class));
                                        }
                                        finish();
                                    }else {
                                        dialog.dismiss();
                                        Toast.makeText(SignupActivity.this,"Given Data is not Correct!",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else {
                            dialog.dismiss();
                            Toast.makeText(SignupActivity.this,"Given Data is not correct!",Toast.LENGTH_SHORT).show();
                        }

                        return true;
                    default:
                        return false;
                }
            }
        });

        goToLoginButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case ACTION_DOWN:
                        goToLoginButton.setBackgroundResource(R.drawable.go_to_login1);
                        return true;
                    case ACTION_UP:
                        goToLoginButton.setBackgroundResource(R.drawable.go_to_login);
                        startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                        finish();
                        return true;
                    default:
                        return false;
                }
            }
        });

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builde = new AlertDialog.Builder(SignupActivity.this);
                builde.setTitle("Admin Password");
                final EditText input = new EditText(SignupActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                builde.setView(input);
                builde.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builde.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String pass = input.getText().toString().trim();
                        if (pass.equals("aSdFgH")){
                            Toast.makeText(SignupActivity.this,"Done!",Toast.LENGTH_SHORT).show();
                            editor.putInt("admin",1);
                            editor.commit();
                        }else {
                            Toast.makeText(SignupActivity.this,"Wrong Password!",Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builde.create();
                alertDialog.setCancelable(false);
                alertDialog.show();
                TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
                textView.setTextSize(20);
            }
        });

    }

}
