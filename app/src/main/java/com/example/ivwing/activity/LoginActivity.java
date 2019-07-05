package com.example.ivwing.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.ivwing.R;

public class LoginActivity extends AppCompatActivity {

    Button login_action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_action = (Button)findViewById(R.id.login_btn);
        login_action.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplication(), Splash2Activity.class)); //로딩이 끝난 후, ChoiceFunction 이동
                LoginActivity.this.finish(); // 로딩페이지 Activity stack에서 제거
            }
        });
    }
}
