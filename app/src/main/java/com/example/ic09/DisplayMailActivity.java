package com.example.ic09;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class DisplayMailActivity extends AppCompatActivity {

    public Bundle extrasFromInbox;
    public Email selectedEmail;
    public TextView tv_subject;
    public TextView tv_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_mail);

        extrasFromInbox = getIntent().getExtras().getBundle("toDisplayMail");
        selectedEmail = (Email) extrasFromInbox.getSerializable("selectedEmail");

        tv_date = findViewById(R.id.tv_createdAt);
        tv_subject = findViewById(R.id.tv_subject_display);

        tv_subject.setText("Subject: " + selectedEmail.subject);
        tv_date.setText("CreatedAt: " + selectedEmail.date);
    }
}
