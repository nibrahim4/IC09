package com.example.ic09;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class CreateNewEmailActivity extends AppCompatActivity {
    public Spinner sp_users;
    public String token;
    public List<User> users = new ArrayList<User>();
    public EditText et_subject;
    public EditText et_message;
    public Button btn_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_email);
        setTitle("Create New Email");

        sp_users = findViewById(R.id.sp_users);

        try {
            SharedPreferences pref = CreateNewEmailActivity.this.getSharedPreferences("com.example.ic09", MODE_PRIVATE);
            String fullName = pref.getString("fullName", null);
            token = pref.getString("token", null);

            run();

            ArrayAdapter<User> adapter = new ArrayAdapter<User>(CreateNewEmailActivity.this,android.R.layout.simple_spinner_item,users);

            sp_users.setAdapter(adapter);
            adapter.notifyDataSetChanged();


            sp_users.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
              @Override
              public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                  Log.d("test", "selected User: " + users.get(i).toString());
              }

              @Override
              public void onNothingSelected(AdapterView<?> adapterView) {

              }
            });




            btn_send = findViewById(R.id.btn_send);
            btn_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final OkHttpClient client = new OkHttpClient();

                    //if (isConnected()) {
                        RequestBody formBody = new FormBody.Builder()
                                .add("receiver_id", "1")
                                .add("subject", "hi")
                                .add("message", "hello")
                                .build();
                        Request request = new Request.Builder()
                                .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/inbox/add")
                                .header("Authorization", "BEARER " + token)
                                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                                .post(formBody)
                                .build();

                        try (Response response = client.newCall(request).execute()) {
                            if (!response.isSuccessful()) {
                                Toast.makeText(CreateNewEmailActivity.this, "Error is thrown!", Toast.LENGTH_SHORT).show();
                                throw new IOException("Unexpected code " + response);
                            }else{
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                String token = jsonObject.getString("token");

                                Toast.makeText(CreateNewEmailActivity.this, "User is successfully signed up!", Toast.LENGTH_SHORT).show();
                                Intent intentToInbox = new Intent(CreateNewEmailActivity.this, InboxActivity.class);
                                startActivity(intentToInbox);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    //} else {
                      //  Toast.makeText(CreateNewEmailActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                   // }
                }
            });

            } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void run() throws Exception {
        final OkHttpClient client = new OkHttpClient();


        Log.d("test", "token: " + token);

        Request request = new Request.Builder()
                .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/users")
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
                        JSONArray userArray = jsonObject.getJSONArray("users");

                        for (int i =0; i <6; i++){

                            User user = new User();

                            JSONObject userObj = userArray.getJSONObject(i);

                            //JSONObject userObj =  userJSON.getJSONObject();

                            user.id = userObj.getString("id");
                            user.firstName = userObj.getString("fname");
                            user.lastName = userObj.getString("lname");

                            users.add(user);

                            Log.d("test", "user: " + user.toString());

                        }

                        Headers responseHeaders = response.headers();
                        for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                            Log.d("test", "header: " + responseHeaders.name(i) + ": " + responseHeaders.value(i));
                        }
                        Log.d("test", "onResponse: " + responseBody.toString());

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
