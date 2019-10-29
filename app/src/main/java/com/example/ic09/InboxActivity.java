package com.example.ic09;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class InboxActivity extends AppCompatActivity {

    public TextView tv_fullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        setTitle("Inbox");

        SharedPreferences pref = getPreferences( MODE_PRIVATE);
        String fullName = pref.getString("fullName", null);

        tv_fullName = findViewById(R.id.tv_inboxFullName);
        tv_fullName.setText(fullName);



    }
}
