package com.example.ic09;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    public ImageView iv_logout;
    public String token;
    public List<Email> messages = new ArrayList<Email>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        setTitle("Inbox");

        SharedPreferences pref = InboxActivity.this.getSharedPreferences( "com.example.ic09", MODE_PRIVATE);
        String fullName = pref.getString("fullName", null);
        token = pref.getString("token", null);

        tv_fullName = findViewById(R.id.tv_inboxFullName);
        tv_fullName.setText(fullName);

        iv_logout = findViewById(R.id.iv_logOut);
        iv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToLogin = new Intent(InboxActivity.this, MainActivity.class);
                startActivity(intentToLogin);
            }
        });

        iv_newEmail = findViewById(R.id.iv_newEmail);
        iv_newEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToNewEmail = new Intent(InboxActivity.this, CreateNewEmailActivity.class);
                startActivity(intentToNewEmail);
            }
        });


        try {
            run();

            EmailAdapter emailAdapter = new EmailAdapter(InboxActivity.this, R.layout.email_item, messages);

            lv_emails = findViewById(R.id.lv_emails);
            // give adapter to ListView UI element to render
            lv_emails.setAdapter(emailAdapter);

            lv_emails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Intent intentToCreateNewEmail = new Intent(InboxActivity.this, CreateNewEmailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("selectedEmail", messages.get(i));
                    intentToCreateNewEmail.putExtra("toCreateNewEmail", intentToCreateNewEmail);
                    startActivity(intentToCreateNewEmail);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void run() throws Exception {
        final OkHttpClient client = new OkHttpClient();


        Request request = new Request.Builder()
                .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/inbox")
                .header("Authorization", "BEARER " + token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()){
                        throw new IOException("Unexpected code " + response);
                    }else{
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        JSONArray messageArray = jsonObject.getJSONArray("messages");

                        for (int i =0; i <messageArray.length(); i++){

                            JSONObject messageObj = messageArray.getJSONObject(i);

                            Email email = new Email();
                            email.subject = messageObj.getString("subject");
                            //email.date = messageObj.getString("date");

                            messages.add(email);
                        }

                        Headers responseHeaders = response.headers();
                        for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                            Log.d("test", "header: " + responseHeaders.name(i) + ": " + responseHeaders.value(i));
                        }
                       // Log.d("test", "onResponse: " + responseBody.string());

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
