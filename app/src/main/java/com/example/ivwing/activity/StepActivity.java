package com.example.ivwing.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.ivwing.R;
import com.example.ivwing.adapter.StepAdapter;
import com.example.ivwing.data.StepData;

public class StepActivity extends AppCompatActivity {
    ImageView backButton;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        backButton = findViewById(R.id.back_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StepActivity.this.finish(); // 로딩페이지 Activity stack에서 제거
            }
        });

        //스피너
        final Spinner spinner = (Spinner)findViewById(R.id.spinner_field);

        //item 배열
        String[] str = getResources().getStringArray(R.array.spinnerArray);

        //어댑터 생성
        final ArrayAdapter<String> spinAdapter= new ArrayAdapter<String>(this, R.layout.spinner_item_step, str);
        spinAdapter.setDropDownViewResource(R.layout.spinner_dropdown_step);

        //스피너와 어댑터 연결
        spinner.setAdapter(spinAdapter);

        //spinner 이벤트 리스너
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinner.getSelectedItemPosition() > 0){
                    //선택된 항목
                    Log.v("알림",spinner.getSelectedItem().toString()+ "is selected");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        recyclerView = findViewById(R.id.step_recyclerView);

        // 리사이클러뷰의 notify()처럼 데이터가 변했을 때 성능을 높일 때 사용한다.
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this,  LinearLayoutManager.HORIZONTAL, false);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        String[] monthSet =  {"06", "06", "06", "06", "06", "07", "07", "07", "07", "07", "07", "07", "07", "07", "07"};
        String[] daySet =  {"26", "27", "28", "29", "30", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10"};
        int[] stepSet =  {12000, 11000, 10000, 9000, 8000, 7000, 6000, 5000, 4000, 3000, 2000, 1000, 0, 1000, 2000};

        StepData[] dataArr = new StepData[15];

        for(int i = 0; i < stepSet.length; i++){
            StepData sample = new StepData(monthSet[i], daySet[i], stepSet[i]);
            dataArr[i] = sample;
        }

        // 어댑터 할당, 어댑터는 기본 어댑터를 확장한 커스텀 어댑터를 사용할 것이다.
        adapter = new StepAdapter(dataArr);
//        adapter = new ScheduleAdapter(textSet1, textSet2, textSet3, textSet4, textSet5);
        recyclerView.setAdapter(adapter);

    }
}
