package com.talha.workspace;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

/**
 * Created by HP-NPC on 22/05/2018.
 */

public class WorkAdapter extends ArrayAdapter<WorkData> {

    Activity context;
    ArrayList<WorkData> workDatas = new ArrayList<>();
    ArrayList<WorkData> workDataArrayList = new ArrayList<>();

    int status,admin;
    String userId;
    SingupData singupData;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

    public WorkAdapter(@NonNull Activity context, @NonNull ArrayList<WorkData> objects,ArrayList<WorkData> arrayListAll,int status,int admin,String userId,SingupData singupData) {
        super(context, R.layout.work_item, objects);

        this.context = context;
        this.workDataArrayList = arrayListAll;
        this.status = status;
        this.admin = admin;
        this.userId = userId;
        this.singupData = singupData;
        this.workDatas = objects;

    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.work_item,null,true);

        ImageView starImageView = (ImageView) view.findViewById(R.id.star);
        TextView titleTextView = (TextView) view.findViewById(R.id.title);
        TextView detailsTextView = (TextView) view.findViewById(R.id.details);
        TextView timeTextView = (TextView) view.findViewById(R.id.time);
        LinearLayout adminButton = (LinearLayout) view.findViewById(R.id.admin_button);
        Button successButton = (Button) view.findViewById(R.id.successButton);
        Button failedButton = (Button) view.findViewById(R.id.failedButton);


        final WorkData workData = workDatas.get(position);

        String title = workData.getTitle();
        String details = workData.getDescription();
        String time = "Time : "+ workData.getTime();

        int star = workData.getStar();
        int category = workData.getStatus();

        titleTextView.setText(title);
        detailsTextView.setText(details);
        timeTextView.setText(time);

        if(category == 0){
            view.setBackgroundResource(R.drawable.work_item);
            if (admin==0){
                adminButton.setVisibility(View.GONE);
            }
        }else if(category == 1){
            view.setBackgroundResource(R.drawable.work_item_success);
            adminButton.setVisibility(View.GONE);
        }else {
            view.setBackgroundResource(R.drawable.work_item_failed);
            adminButton.setVisibility(View.GONE);
        }

        if(star == 1){
            starImageView.setImageResource(R.drawable.star_1);
        }else if(star == 2){
            starImageView.setImageResource(R.drawable.star_2);
        }else if(star == 3){
            starImageView.setImageResource(R.drawable.star_3);
        }else if(star == 4){
            starImageView.setImageResource(R.drawable.star_4);
        }else if(star == 5){
            starImageView.setImageResource(R.drawable.star_5);
        }

        successButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workData.setStatus(1);
                int id = workData.getId();
                for (int i = 0;i<workDataArrayList.size();i++){
                    WorkData wd = workDataArrayList.get(i);
                    if(id==wd.getId()){
                        workDataArrayList.set(i,workData);
                    }
                }
                singupData.setWorks(workDataArrayList);
                reference.child(userId).setValue(singupData);
                WorkAdapter.this.notifyDataSetChanged();
            }
        });

        failedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workData.setStatus(-1);
                int id = workData.getId();
                for (int i = 0;i<workDataArrayList.size();i++){
                    WorkData wd = workDataArrayList.get(i);
                    if(id==wd.getId()){
                        workDataArrayList.set(i,workData);
                    }
                }
                singupData.setWorks(workDataArrayList);
                reference.child(userId).setValue(singupData);
                WorkAdapter.this.notifyDataSetChanged();
            }
        });

        return view;
    }
}
