package com.talha.workspace;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PostActivity extends AppCompatActivity {

    ImageView starImageView;
    FloatingActionButton starFab,sendFab,starMFab;
    EditText titleEditText,descriptionEditText,timeEditText;
    CheckBox allCheckbox;
    ListView userListView;

    DatabaseReference reference;
    SingupData singupData;
    ArrayList<SingupData> usersArray,selectedArray;
    SendAdapter adapter;
    WorkData workData;
    int star = 1;
    int done = 0;
    //String size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        starImageView = (ImageView) findViewById(R.id.star_send);
        starFab = (FloatingActionButton) findViewById(R.id.staraddFab);
        starMFab = (FloatingActionButton) findViewById(R.id.starminusFab);
        sendFab = (FloatingActionButton) findViewById(R.id.sendfab);
        titleEditText = (EditText) findViewById(R.id.titleeditText);
        descriptionEditText = (EditText) findViewById(R.id.descriptioneditText);
        timeEditText = (EditText) findViewById(R.id.timeeditText);
        allCheckbox = (CheckBox) findViewById(R.id.checkBoxAll);
        userListView = (ListView) findViewById(R.id.sendUserListView);


        reference = FirebaseDatabase.getInstance().getReference("users");

        workData = new WorkData();

        singupData = new SingupData();
        usersArray = new ArrayList<>();
        selectedArray = new ArrayList<>();

        final CountDownTimer timer = new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                Toast.makeText(PostActivity.this,"Make sure your Internet is on.",Toast.LENGTH_SHORT).show();
                this.start();
            }
        }.start();

        final ProgressDialog dialog = new ProgressDialog(PostActivity.this);
        dialog.setMessage("Getting Data");
        dialog.setCancelable(false);
        //dialog.show();

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usersArray.clear();
                for(DataSnapshot myspnapshot:dataSnapshot.getChildren()){
                    singupData = myspnapshot.getValue(SingupData.class);
                    usersArray.add(singupData);
                }
                adapter = new SendAdapter(PostActivity.this,usersArray,2);
                userListView.setAdapter(adapter);
                dialog.dismiss();
                timer.cancel();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        allCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allCheckbox.isChecked()){
                    //allCheckbox.setChecked(true);
                    selectedArray.clear();
                    for (int i = 0; i < usersArray.size(); i++){
                        SingupData s = usersArray.get(i);
                        selectedArray.add(s);
                    }
                    //selectedArray = usersArray;
                    adapter = new SendAdapter(PostActivity.this,usersArray,1);
                    userListView.setAdapter(adapter);
//                    size = "Size: "+selectedArray.size()+"\n";
//                    for (int i = 0; i < selectedArray.size(); i++){
//                        size = size+selectedArray.get(i).name+"\n";
//                    }
//                    Toast.makeText(PostActivity.this,size,Toast.LENGTH_SHORT).show();
                }else {
                    //allCheckbox.setChecked(true);
                    selectedArray.clear();
                    adapter = new SendAdapter(PostActivity.this,usersArray,0);
                    userListView.setAdapter(adapter);
//                    size = "Size: "+selectedArray.size()+"\n";
//                    for (int i = 0; i < selectedArray.size(); i++){
//                        size = size+selectedArray.get(i).name+"\n";
//                    }
//                    Toast.makeText(PostActivity.this,size,Toast.LENGTH_SHORT).show();
                }
            }
        });

        userListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        userListView.setFocusable(false);

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox cb = (CheckBox) view.findViewById(R.id.userCheckBox);
                SingupData sd = usersArray.get(position);
                if (cb.isChecked()){
                    cb.setChecked(false);
                    for(int i = 0; i < selectedArray.size(); i++){
                        if (sd.getuId().equals(selectedArray.get(i).getuId())){
                            selectedArray.remove(i);
                        }
                    }
                }else {
                    cb.setChecked(true);
                    selectedArray.add(usersArray.get(position));
                }
//                size = "Size: "+selectedArray.size()+"\n";
//                for (int i = 0; i < selectedArray.size(); i++){
//                    size = size+selectedArray.get(i).name+"\n";
//                }
//                Toast.makeText(PostActivity.this,size,Toast.LENGTH_SHORT).show();
            }
        });

        starFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(star <= 4){
                    star++;
                    if (star==1){
                        starImageView.setImageResource(R.drawable.star_1);
                    }else if(star==2){
                        starImageView.setImageResource(R.drawable.star_2);
                    }else if(star==3){
                        starImageView.setImageResource(R.drawable.star_3);
                    }else if(star==4){
                        starImageView.setImageResource(R.drawable.star_4);
                    }else if(star==5){
                        starImageView.setImageResource(R.drawable.star_5);
                    }
                }
            }
        });

        starMFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (star>=2){
                    star--;
                    if (star==1){
                        starImageView.setImageResource(R.drawable.star_1);
                    }else if(star==2){
                        starImageView.setImageResource(R.drawable.star_2);
                    }else if(star==3){
                        starImageView.setImageResource(R.drawable.star_3);
                    }else if(star==4){
                        starImageView.setImageResource(R.drawable.star_4);
                    }else if(star==5){
                        starImageView.setImageResource(R.drawable.star_5);
                    }
                }
            }
        });

        sendFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String title = titleEditText.getText().toString().trim();
                final String description = descriptionEditText.getText().toString().trim();
                final String time = timeEditText.getText().toString().trim();
                final int status = 0;

                if(!title.isEmpty()&&!description.isEmpty()&&!time.isEmpty()){

                    if(selectedArray.size()>0){
                        done = 0;

                        final ProgressDialog dialog1 = new ProgressDialog(PostActivity.this);
                        dialog1.setMessage("Sending");
                        dialog1.setCancelable(false);
                        dialog1.show();

                        CountDownTimer cdt = new CountDownTimer(3000,1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {
                                for (int i = 0; i < selectedArray.size(); i++){

                                    SingupData sdf = selectedArray.get(i);
                                    int id = sdf.getWorks().size();
                                    WorkData wd = new WorkData(title,description,time,star,status,id);
                                    sdf.getWorks().add(wd);

                                    reference.child(sdf.getuId()).setValue(sdf).addOnSuccessListener(PostActivity.this, new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            done++;
                                        }
                                    });
                                }

                                dialog1.dismiss();

                                Toast.makeText(PostActivity.this,"Sent Successfully!",Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(PostActivity.this,PostActivity.class));

                                finish();

                            }
                        }.start();

//                    if (done == selectedArray.size()){
//                        Toast.makeText(PostActivity.this,"Sent Successfully!",Toast.LENGTH_SHORT).show();
//                    }else {
//                        Toast.makeText(PostActivity.this,"Sending Failed, try again!",Toast.LENGTH_SHORT).show();
//                    }
                    }else {
                        Toast.makeText(PostActivity.this,"Select members!",Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(PostActivity.this,"Sorry, try again please!!",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(PostActivity.this);
        builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder1.setMessage("Do you want do exit posting? All data will be removed!");
        AlertDialog alertDialog1 = builder1.create();
        alertDialog1.setCancelable(true);
        alertDialog1.show();
        TextView textView1 = (TextView) alertDialog1.findViewById(android.R.id.message);
        textView1.setTextSize(20);
    }
}
