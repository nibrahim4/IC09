package com.example.ic09;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DisplayMailActivity extends AppCompatActivity {

    public Bundle extrasFromInbox;
    public Email selectedEmail;
    public TextView tv_subject;
    public TextView tv_date;
    public TextView tv_message;
    public Button btn_close;
    public TextView tv_sender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_mail);
        setTitle("Display Mail");

        extrasFromInbox = getIntent().getExtras().getBundle("toDisplayMail");
        selectedEmail = (Email) extrasFromInbox.getSerializable("selectedEmail");

        tv_date = findViewById(R.id.tv_createdAt);
        tv_subject = findViewById(R.id.tv_subject_display);
        tv_message = findViewById(R.id.tv_message_display);
        btn_close = findViewById(R.id.btn_close);
        tv_sender = findViewById(R.id.tv_sender);

        tv_subject.setText("Subject: " + selectedEmail.subject);
        tv_date.setText("CreatedAt: " + selectedEmail.date);
        tv_message.setText("Message: " + selectedEmail.message);
        tv_sender.setText("Sender: " + selectedEmail.senderFName + " " + selectedEmail.senderLName);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}
