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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP-NPC on 22/05/2018.
 */

public class UserAdapter extends ArrayAdapter<SingupData> {

    Activity context;
    ArrayList<SingupData> datas;

    public UserAdapter(@NonNull Activity context, @NonNull ArrayList<SingupData> objects) {
        super(context, R.layout.user_item, objects);

        this.context = context;
        datas = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.user_item,null,true);

        TextView nameTextView = (TextView) view.findViewById(R.id.name);

        SingupData data = datas.get(position);

        String name = position+1+". "+data.getName();

        nameTextView.setText(name);

        return view;
    }
}
