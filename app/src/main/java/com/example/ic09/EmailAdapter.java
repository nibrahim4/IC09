package com.example.ic09;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class EmailAdapter extends ArrayAdapter<Email> {


    public EmailAdapter(@NonNull Context context, int resource, @NonNull List<Email> objects) {

        super(context, resource, objects);

        Log.d("test", "EmailAdapter: " + "HIIII");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Log.d("test", "Email 12333333 ");

        Email email = getItem(position);



        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.email_item,parent, false);
        }
        TextView tv_subject = convertView.findViewById(R.id.tv_subject_display);
        tv_subject.setText(email.subject);

        TextView tv_date = convertView.findViewById(R.id.tv_date);
        tv_date.setText(email.date);


        return convertView;
    }
}

