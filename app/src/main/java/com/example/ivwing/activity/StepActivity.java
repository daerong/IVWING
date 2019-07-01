package com.example.ivwing.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.ivwing.R;

public class StepActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        //스피너
        Spinner spinner = (Spinner)findViewById(R.id.spinner_field);

        //어댑터 생성
        //이 예제 같은 경우 string,xml에 리스트를 추가해 놓고 그 리스트를 불러온다.
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.spinnerArray, android.R.layout.simple_spinner_item);

        //스피너와 어댑터 연결
        spinner.setAdapter(adapter);
    }
}
