package com.example.ivwing.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.ivwing.R;
import com.example.ivwing.adapter.IntravenousAdapter;
import com.example.ivwing.adapter.ScheduleAdapter;
import com.example.ivwing.data.IntravenousData;
import com.example.ivwing.data.ScheduleData;

public class InformationActivity extends AppCompatActivity {
    ImageView backButton;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        backButton = findViewById(R.id.back_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InformationActivity.this.finish(); // 로딩페이지 Activity stack에서 제거
            }
        });
        recyclerView = findViewById(R.id.intravenous_recyclerView);

        // 리사이클러뷰의 notify()처럼 데이터가 변했을 때 성능을 높일 때 사용한다.
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        String[] nameSet =  {
                "0.9% N/S (0.9% 생리식염수)", "5% DS (5% 포도당생리식염액)",
                "0.45% N/S (0.45% 생리식염수)", "콤비플렉스주",
                "5% DW (5% 포도당주사액)", "10% DW (10% 포도당주사액)",
                "50% DW (50% 포도당주사액)", "HD (하트만덱스액)",
                "HS (하트만용액)", "염화칼륨 40 주사액"
        };
        int[] leftSet =  {100, 200, 300, 400, 500, 600, 700, 800, 900, 1000};
        int[] maxSet =  {1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000};
        int[] timeSet =  {1200, 1200, 1200, 1200, 1200, 1200, 1200, 1200, 1200, 1200};
        int[] gttSet =  {24, 24, 24, 24, 24, 24, 24, 24, 24, 24};


        IntravenousData[] dataArr = new IntravenousData[10];

        for(int i = 0; i < nameSet.length; i++){
            IntravenousData sample = new IntravenousData(nameSet[i], leftSet[i], maxSet[i], timeSet[i], gttSet[i]);
            dataArr[i] = sample;
        }

        // 어댑터 할당, 어댑터는 기본 어댑터를 확장한 커스텀 어댑터를 사용할 것이다.
        adapter = new IntravenousAdapter(dataArr);
//        adapter = new ScheduleAdapter(textSet1, textSet2, textSet3, textSet4, textSet5);
        recyclerView.setAdapter(adapter);
    }
}
