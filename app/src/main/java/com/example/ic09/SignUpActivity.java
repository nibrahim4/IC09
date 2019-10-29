package com.example.ic09;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUpActivity extends AppCompatActivity {

    public EditText et_firstName;
    public EditText et_lastName;
    public EditText et_email;
    public EditText et_choosePassword;
    public EditText et_repeatPassword;
    public Button btn_signUp;
    public Button btn_cancel;
    public String acceptedPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("Sign Up");

        et_firstName = findViewById(R.id.et_firstName);
        et_lastName = findViewById(R.id.et_lastName);
        et_email = findViewById(R.id.et_emailSignUp);
        et_choosePassword = findViewById(R.id.et_password1);
        et_repeatPassword = findViewById(R.id.et_password2);
        btn_signUp = findViewById(R.id.btn_SignUp2);
        btn_cancel = findViewById(R.id.btn_cancel);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final OkHttpClient client = new OkHttpClient();

                if (isConnected()) {
                    String choosePassword = et_choosePassword.getText().toString();
                    String repeatPassword = et_repeatPassword.getText().toString();

                    if (choosePassword.trim().equals(repeatPassword.trim())) {
                        acceptedPassword = choosePassword;
                    }

                    Log.d("test", "choosePassword: " + choosePassword);
                    Log.d("test", "repeatPassword: " + repeatPassword);

                    RequestBody formBody = new FormBody.Builder()
                            .add("email", et_email.getText().toString())
                            .add("password", acceptedPassword)
                            .add("fname", et_firstName.getText().toString())
                            .add("lname", et_lastName.getText().toString())
                            .build();
                    Request request = new Request.Builder()
                            .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/signup")
                            .post(formBody)
                            .build();

                    try (Response response = client.newCall(request).execute()) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Error is thrown!", Toast.LENGTH_SHORT).show();
                            throw new IOException("Unexpected code " + response);
                        }else{
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            String token = jsonObject.getString("token");

                            SharedPreferences prefs =  SignUpActivity.this.getSharedPreferences("PREFERENCE_NAME",
                                    Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("token", token);
                            editor.putString("fullName", et_firstName.getText().toString() + " " + et_lastName.getText().toString());
                            editor.apply();

                            Toast.makeText(SignUpActivity.this, "User is successfully signed up!", Toast.LENGTH_SHORT).show();
                            Intent intentToInbox = new Intent(SignUpActivity.this, InboxActivity.class);
                            startActivity(intentToInbox);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(SignUpActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }

}
