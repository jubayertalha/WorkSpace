package com.talha.workspace;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;

    TextView userNameTextView,failedTextView,allTextView,successTextView;
    ListView workListView;
    ImageView emptyView,info,refresh;

    int where = 0;

    SingupData singupData;
    ArrayList<WorkData> workDataArrayList,failedDatas,successDatas,pendingDatas;
    WorkAdapter adapter;

    SharedPreferences sharedPreferences;
    String userId;
    int admin = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        Intent intent = getIntent();

        reference = FirebaseDatabase.getInstance().getReference("users");

        userNameTextView = (TextView) findViewById(R.id.user_name);
        failedTextView = (TextView) findViewById(R.id.faildtextView);
        allTextView = (TextView) findViewById(R.id.alltextView);
        successTextView = (TextView) findViewById(R.id.successtextView);
        emptyView = (ImageView) findViewById(R.id.emptyViewInMain);
        info = (ImageView) findViewById(R.id.logo_without_name_in_main);
        refresh = (ImageView) findViewById(R.id.refresh);

        workListView = (ListView) findViewById(R.id.workListView);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        if(sharedPreferences.getInt("admin",0)==1){
            admin = 1;
            userId = intent.getStringExtra("userId");
        }else {
            userId = user.getUid();
        }

        final CountDownTimer timer = new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                Toast.makeText(MainActivity.this,"Make sure your Internet is on.",Toast.LENGTH_SHORT).show();
                this.start();
            }
        }.start();

        final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Getting Data");
        dialog.setCancelable(false);
        dialog.show();

        singupData = new SingupData();
        workDataArrayList = new ArrayList<>();
        successDatas = new ArrayList<>();
        pendingDatas = new ArrayList<>();
        failedDatas = new ArrayList<>();

        Toast.makeText(MainActivity.this,"0",Toast.LENGTH_SHORT).show();


        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(MainActivity.this,"1",Toast.LENGTH_SHORT).show();

                workDataArrayList.clear();

                singupData = dataSnapshot.child(userId).getValue(SingupData.class);
                Toast.makeText(MainActivity.this,"2",Toast.LENGTH_SHORT).show();


                userNameTextView.setText(singupData.getName());
                workDataArrayList = singupData.getWorks();

                successDatas.clear();
                failedDatas.clear();
                pendingDatas.clear();

                for(int i = 0; i < workDataArrayList.size(); i++){
                    WorkData workData = workDataArrayList.get(i);
                    if(workData.getStatus() == 1){
                        successDatas.add(workData);
                    }else if(workData.getStatus() == 0){
                        pendingDatas.add(workData);
                    }else {
                        failedDatas.add(workData);
                    }
                }

                for(int i = 0, j = successDatas.size()-1; i < successDatas.size()/2; i++,j--){
                    WorkData wd = successDatas.get(i);
                    successDatas.set(i,successDatas.get(j));
                    successDatas.set(j,wd);
                }

                for(int i = 0, j = pendingDatas.size()-1; i < pendingDatas.size()/2; i++,j--){
                    WorkData wd = pendingDatas.get(i);
                    pendingDatas.set(i,pendingDatas.get(j));
                    pendingDatas.set(j,wd);
                }

                for(int i = 0, j = failedDatas.size()-1; i < failedDatas.size()/2; i++,j--){
                    WorkData wd = failedDatas.get(i);
                    failedDatas.set(i,failedDatas.get(j));
                    failedDatas.set(j,wd);
                }

                allTextView.setTextColor(Color.parseColor("#594D9A"));
                failedTextView.setTextColor(Color.parseColor("#9B9191"));
                successTextView.setTextColor(Color.parseColor("#9B9191"));

                adapter = new WorkAdapter(MainActivity.this,pendingDatas,workDataArrayList,0,admin,userId,singupData);
                workListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                dialog.dismiss();
                timer.cancel();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        allTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allTextView.setTextColor(Color.parseColor("#594D9A"));
                failedTextView.setTextColor(Color.parseColor("#9B9191"));
                successTextView.setTextColor(Color.parseColor("#9B9191"));

                where = 0;

                adapter = new WorkAdapter(MainActivity.this,pendingDatas,workDataArrayList,0,admin,userId,singupData);
                workListView.setAdapter(adapter);
            }
        });

        failedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allTextView.setTextColor(Color.parseColor("#9B9191"));
                failedTextView.setTextColor(Color.parseColor("#594D9A"));
                successTextView.setTextColor(Color.parseColor("#9B9191"));

                where = -1;

                adapter = new WorkAdapter(MainActivity.this,failedDatas,workDataArrayList,-1,admin,userId,singupData);
                workListView.setAdapter(adapter);
            }
        });

        successTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allTextView.setTextColor(Color.parseColor("#9B9191"));
                failedTextView.setTextColor(Color.parseColor("#9B9191"));
                successTextView.setTextColor(Color.parseColor("#594D9A"));

                where = 1;

                adapter = new WorkAdapter(MainActivity.this,successDatas,workDataArrayList,1,admin,userId,singupData);
                workListView.setAdapter(adapter);
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Info");
                builder.setMessage("Success: "+successDatas.size()+"\nFailed: "+failedDatas.size()+"\nPending: "+pendingDatas.size()+"\nAll: "+workDataArrayList.size());
                AlertDialog dialog1 = builder.create();
                dialog1.show();
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MainActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                finish();
            }
        });

        workListView.setEmptyView(emptyView);

    }

}
