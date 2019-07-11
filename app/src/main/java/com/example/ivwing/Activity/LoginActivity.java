package com.example.ivwing.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ivwing.Data.LoginResult;
import com.example.ivwing.Network.NetworkService;
import com.example.ivwing.R;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    Button login_action;
    TextView signup_action;
    EditText login_email;
    EditText login_pwd;

    Retrofit retrofit;
    NetworkService networkService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        retrofit = new Retrofit.Builder()
                .baseUrl(NetworkService.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        networkService = retrofit.create(NetworkService.class);

        login_email = findViewById(R.id.login_email);
        login_pwd = findViewById(R.id.login_pwd);

        login_action = (Button)findViewById(R.id.login_btn);
        login_action.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

            HashMap<String, Object> input = new HashMap<>();
            input.put("user_email", login_email.getText().toString());
            input.put("user_pwd", login_pwd.getText().toString());

            Call<LoginResult> comment = networkService.postLogin(input);
            comment.enqueue(new Callback<LoginResult>() {
                @Override
                public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                    Log.v("Test", response.body().toString());
                    if(response.isSuccessful()){
                        if(response.body().getStatus().equals("Success")){
                            startActivity(new Intent(getApplication(), Splash2Activity.class)); //로딩이 끝난 후, ChoiceFunction 이동
                            LoginActivity.this.finish(); // 로딩페이지 Activity stack에서 제거
                            Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(LoginActivity.this, "일치하는 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    } else{
                        Toast.makeText(LoginActivity.this, "통신 실패", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResult> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "네트워크가 원할하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            });
            }
        });


        signup_action = (TextView) findViewById(R.id.signup_action);
        signup_action.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplication(), SignupActivity.class)); //로딩이 끝난 후, ChoiceFunction 이동
//                LoginActivity.this.finish(); // 로딩페이지 Activity stack에서 제거
            }
        });
    }
}
