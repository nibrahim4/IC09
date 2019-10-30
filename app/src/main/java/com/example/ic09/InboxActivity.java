package com.example.ic09;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

//import okhttp3.Request;

public class InboxActivity extends AppCompatActivity {

    public TextView tv_fullName;
    public ListView lv_emails;
    public ImageView iv_newEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        setTitle("Inbox");

        SharedPreferences pref = getPreferences( MODE_PRIVATE);
        String fullName = pref.getString("fullName", null);

        tv_fullName = findViewById(R.id.tv_inboxFullName);
        tv_fullName.setText(fullName);

        iv_newEmail = findViewById(R.id.iv_newEmail);
        iv_newEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToNewEmail = new Intent(InboxActivity.this, CreateNewEmailActivity.class);
                startActivity(intentToNewEmail);
            }
        });

        lv_emails = findViewById(R.id.lv_emails);

        EmailAdapter emailAdapter = new EmailAdapter(InboxActivity.this, R.layout.email_item, null);

        // give adapter to ListView UI element to render
        lv_emails.setAdapter(emailAdapter);

        try {
            run();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void run() throws Exception {
        final OkHttpClient client = new OkHttpClient();


        Request request = new Request.Builder()
                .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/inbox")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    Headers responseHeaders = response.headers();
                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }

                    System.out.println(responseBody.string());
                }
            }
        });
    }
}
