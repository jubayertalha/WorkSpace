package com.talha.workspace;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {

    ListView userListView;
    ImageView emptyView;
    FloatingActionButton addFab;

    DatabaseReference reference;

    SingupData singupData;

    ArrayList<SingupData> usersArray;

    UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        userListView = (ListView) findViewById(R.id.userListView);
        emptyView = (ImageView) findViewById(R.id.emptyViewInAdmin);
        addFab = (FloatingActionButton) findViewById(R.id.addfab);

        reference = FirebaseDatabase.getInstance().getReference("users");

        singupData = new SingupData();
        usersArray = new ArrayList<>();

        final CountDownTimer timer = new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                Toast.makeText(AdminActivity.this,"Make sure your Internet is on.",Toast.LENGTH_SHORT).show();
                this.start();
            }
        }.start();

        final ProgressDialog dialog = new ProgressDialog(AdminActivity.this);
        dialog.setMessage("Getting Data");
        dialog.setCancelable(false);
        dialog.show();

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usersArray.clear();
                for(DataSnapshot myspnapshot:dataSnapshot.getChildren()){
                    singupData = myspnapshot.getValue(SingupData.class);
                    usersArray.add(singupData);
                }
                adapter = new UserAdapter(AdminActivity.this,usersArray);
                userListView.setAdapter(adapter);
                dialog.dismiss();
                timer.cancel();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        userListView.setEmptyView(emptyView);

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AdminActivity.this,MainActivity.class);
                intent.putExtra("userId", usersArray.get(position).getuId());
                startActivity(intent);
            }
        });

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this,PostActivity.class));
            }
        });
    }
}
