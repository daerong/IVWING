package com.example.ivwing.Activity;

import android.content.Intent;
import android.net.Network;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ivwing.Data.SigninResult;
import com.example.ivwing.Network.NetworkService;
import com.example.ivwing.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignupActivity extends AppCompatActivity {

    EditText user_name;
    EditText user_email;
    EditText user_pwd;
    EditText user_phone;
    EditText user_age;
    Button user_female;
    Button user_male;
    Button signup_button;

    boolean maleBtnPressed = false;
    boolean femaleBtnPressed = false;

    Retrofit retrofit;
    NetworkService networkService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        retrofit = new Retrofit.Builder()
                .baseUrl(NetworkService.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        networkService = retrofit.create(NetworkService.class);

        user_name = findViewById(R.id.name);
        user_email = findViewById(R.id.email);
        user_pwd = findViewById(R.id.pwd);
        user_phone = findViewById(R.id.phone);
        user_age = findViewById(R.id.age);


        user_female = findViewById(R.id.gender_female);
        user_female.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                femaleBtnPressed = true;
                maleBtnPressed = false;
                user_female.setBackgroundResource(R.drawable.signup_btn_on); // 배경이미지 변경.
                user_male.setBackgroundResource(R.drawable.signup_btn_off); // 배경이미지 변경.
            }
        });

        user_male = findViewById(R.id.gender_male);
        user_male.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                femaleBtnPressed = false;
                maleBtnPressed = true;
                user_female.setBackgroundResource(R.drawable.signup_btn_off); // 배경이미지 변경.
                user_male.setBackgroundResource(R.drawable.signup_btn_on); // 배경이미지 변경.
            }
        });

        signup_button  = findViewById(R.id.signup_btn);
        signup_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 이름 입력 확인
                if( user_name.getText().toString().length() == 0 ) {
                    Toast.makeText(SignupActivity.this, "이름을 입력하세요!", Toast.LENGTH_SHORT).show();
                    user_name.requestFocus();
                    return;
                }

                // 이메일 입력 확인
                if( user_email.getText().toString().length() == 0 ) {
                    Toast.makeText(SignupActivity.this, "이메일을 입력하세요!", Toast.LENGTH_SHORT).show();
                    user_email.requestFocus();
                    return;
                }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(user_email.getText().toString()).matches()){
                    Toast.makeText(SignupActivity.this,"이메일 형식이 아닙니다", Toast.LENGTH_SHORT).show();
                    user_email.setText(null);
                    user_email.requestFocus();
                    return;
                }

                // 비밀번호 입력 확인
                if( user_pwd.getText().toString().length() == 0 ) {
                    Toast.makeText(SignupActivity.this, "비밀번호를 입력하세요!", Toast.LENGTH_SHORT).show();
                    user_pwd.requestFocus();
                    return;
                }
                else if(!Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()a-zA-Z]).{8,20}$", user_pwd.getText().toString())) {
                    Toast.makeText(SignupActivity.this,"비밀번호 형식을 지켜주세요.",Toast.LENGTH_SHORT).show();
                    user_pwd.setText(null);
                    user_pwd.requestFocus();
                    return;
                }

                // 폰번호 입력 확인
                if( user_phone.getText().toString().length() == 0 ) {
                    Toast.makeText(SignupActivity.this, "연락처를 입력하세요!", Toast.LENGTH_SHORT).show();
                    user_phone.requestFocus();
                    return;
                }
                else if(!Pattern.matches("^01(?:0|1|[6-9])[0-9]{8}", user_phone.getText().toString())){
                    Toast.makeText(SignupActivity.this,"올바른 핸드폰 번호가 아닙니다.",Toast.LENGTH_SHORT).show();
                    user_phone.setText(null);
                    user_phone.requestFocus();
                    return;
                }

                // 나이 입력 확인
                if( user_age.getText().toString().length() == 0 ) {
                    Toast.makeText(SignupActivity.this, "나이를 입력하세요!", Toast.LENGTH_SHORT).show();
                    user_age.requestFocus();
                    return;
                }
                else if( Integer.parseInt(user_age.getText().toString()) < 0 || 150 < Integer.parseInt(user_age.getText().toString()) ){
                    Toast.makeText(SignupActivity.this,"나이를 확인해주세요.",Toast.LENGTH_SHORT).show();
                    user_age.setText(null);
                    user_age.requestFocus();
                }

                // 성별 선택 확인
                char genderType = '\n';

                if( maleBtnPressed == femaleBtnPressed ) {
                    Toast.makeText(SignupActivity.this, "성별을 선택하세요!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    if(maleBtnPressed){
                        genderType = 'M';
                    }else if(femaleBtnPressed){
                        genderType = 'F';
                    }
                }

                HashMap<String, Object> input = new HashMap<>();
                input.put("user_name", user_name.getText().toString());
                input.put("user_email", user_email.getText().toString());
                input.put("user_pwd", user_pwd.getText().toString());
                input.put("user_phone", user_phone.getText().toString());
                input.put("user_age", Integer.parseInt(user_age.getText().toString()));
                input.put("user_gender", genderType);

                Call<SigninResult> comment = networkService.postSignup(input);

                comment.enqueue(new Callback<SigninResult>() {
                    @Override
                    public void onResponse(Call<SigninResult> call, Response<SigninResult> response) {
                        Log.v("Test", response.body().toString());
                        if(response.isSuccessful()){
                            if(response.body().getStatus().equals("Success")){
                                startActivity(new Intent(getApplication(), LoginActivity.class));
                                SignupActivity.this.finish(); // 페이지 Activity stack에서 제거
                                Toast.makeText(SignupActivity.this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                            }else{
                                if(response.body().getMsg().equals("This email already exist.")){
                                    Toast.makeText(SignupActivity.this, "이미 가입된 이메일입니다.", Toast.LENGTH_SHORT).show();
                                    user_email.requestFocus();
                                }
                                else{
                                    Toast.makeText(SignupActivity.this, "알 수 없는 오류", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else{
                            Toast.makeText(SignupActivity.this, "통신 실패", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SigninResult> call, Throwable t) {
                        Toast.makeText(SignupActivity.this, "네트워크가 원할하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
