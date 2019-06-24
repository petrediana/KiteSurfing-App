package com.kitesurfing.diana.internshipab4app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kitesurfing.diana.internshipab4app.templates.Login.LoginBody;
import com.kitesurfing.diana.internshipab4app.templates.Login.LoginToken;
import com.kitesurfing.diana.internshipab4app.utils.Constants;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.login_layout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                closeKeyboard(v);
                return false;
            }
        });

        emailEditText = (EditText) findViewById(R.id.email_edit_text);
        loginButton = (Button) findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailText = emailEditText.getText().toString();
                closeKeyboard(v);

                if (emailText.contains("@") && emailText.contains(".")) {
                    someTest(emailText);
//                    Intent intent = new Intent(v.getContext(), SpotsListActivity.class);
//                    intent.putExtra(Constants.INTENT_EMAIL, emailText);
//                    v.getContext().startActivity(intent);
//                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "E-mail not valid!", Toast.LENGTH_SHORT).show();
                    Log.i(Constants.TAG, "onClick: E-mail is: " + emailText);
                }
            }
        });
    }

    private void closeKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void someTest(final String emailText) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://internship-2019.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        LoginService loginService = retrofit.create(LoginService.class);

        LoginBody loginBody = new LoginBody();
        loginBody.setEmail(emailText);
        Log.i(Constants.TAG, "someTest: loginbody: " + loginBody.toString() + ", email: " + loginBody.getEmail());

        Call<LoginToken> loginTokenCall = loginService.getLoginToken(loginBody);
        loginTokenCall.enqueue(new Callback<LoginToken>() {
            @Override
            public void onResponse(Call<LoginToken> call, Response<LoginToken> response) {
                Log.i(Constants.TAG, "onResponse: code: " + response.code());
                if (response.isSuccessful()) {
                    try {
                        String token = response.body().getResult().getToken();
                        Log.i(Constants.TAG, "token is: " + token);

                        launchSpotsListActivity(emailText, token);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.w(Constants.TAG, "loginTokenCall response code: " + response.code());
                    Toast.makeText(LoginActivity.this, "Could not log in. Response code: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginToken> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(LoginActivity.this, "No Internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void launchSpotsListActivity(String emailText, String token) {
        Intent intent = new Intent(LoginActivity.this, SpotsListActivity.class);
        intent.putExtra(Constants.INTENT_EMAIL, emailText);
        intent.putExtra(Constants.TOKEN, token);
        startActivity(intent);
        finish();
    }

}
