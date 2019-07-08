package com.example.ivwing.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ivwing.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


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
                }

                // 비밀번호 입력 확인
                if( user_pwd.getText().toString().length() == 0 ) {
                    Toast.makeText(SignupActivity.this, "비밀번호를 입력하세요!", Toast.LENGTH_SHORT).show();
                    user_pwd.requestFocus();
                    return;
                }

                // 폰번호 입력 확인
                if( user_phone.getText().toString().length() == 0 ) {
                    Toast.makeText(SignupActivity.this, "연락처를 입력하세요!", Toast.LENGTH_SHORT).show();
                    user_phone.requestFocus();
                    return;
                }

                // 나이 입력 확인
                if( user_age.getText().toString().length() == 0 ) {
                    Toast.makeText(SignupActivity.this, "나이를 입력하세요!", Toast.LENGTH_SHORT).show();
                    user_age.requestFocus();
                    return;
                }

                // 성별 선택 확인
                if( user_age.getText().toString().length() == 0 ) {
                    Toast.makeText(SignupActivity.this, "성별을 선택하세요!", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });
    }
}
