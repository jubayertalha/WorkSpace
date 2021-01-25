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
import android.widget.CheckBox;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP-NPC on 25/05/2018.
 */

public class SendAdapter extends ArrayAdapter<SingupData> {
    SingupData singupData = new SingupData();
    ArrayList<SingupData> users = new ArrayList<>();
    Activity context;
    int allCheck;
    public SendAdapter(@NonNull Activity context, @NonNull ArrayList<SingupData> objects,int allCheck) {
        super(context, R.layout.send_item, objects);
        this.context = context;
        users = objects;
        this.allCheck = allCheck;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.send_item,null,true);

        TextView nameTextView = (TextView) view.findViewById(R.id.name_send);
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.userCheckBox);

        singupData = users.get(position);

        nameTextView.setText(position+1+". "+singupData.getName());

        if (allCheck==0){
            checkBox.setChecked(false);
        }else if (allCheck==1){
            checkBox.setChecked(true);
        }

        return view;
    }
}
