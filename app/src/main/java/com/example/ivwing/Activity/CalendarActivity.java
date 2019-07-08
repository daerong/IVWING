package com.example.ivwing.Activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.ivwing.R;
import com.example.ivwing.Adapter.ScheduleAdapter;
import com.example.ivwing.Data.ScheduleData;

import java.util.Calendar;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class CalendarActivity extends AppCompatActivity {
    ImageView backButton;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* starts before 1 month from now */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        backButton = findViewById(R.id.back_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarActivity.this.finish(); // 로딩페이지 Activity stack에서 제거
            }
        });

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        Drawable selected_background = getResources().getDrawable(R.drawable.calendar_selected_background, null);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(7)
                .configure()    // starts configuration.
                    .textColor(Color.rgb(72, 171, 255), Color.rgb(255, 255, 255))    // .textColor(int normalColor, int selectedColor)
                    .selectorColor(Color.rgb(0, 137, 254))               // set selection indicator bar's color (default to colorAccent).
                    .selectedDateBackground(selected_background)      // set selected date cell background.
                .end()
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                //do something
            }
        });

        recyclerView = findViewById(R.id.schedule_recyclerView);

        // 리사이클러뷰의 notify()처럼 데이터가 변했을 때 성능을 높일 때 사용한다.
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



        String[] textSet1 =  {"09", "11", "13", "16", "17", "09", "11", "13", "16", "17"};
        String[] textSet2 =  {"00", "00", "00", "00", "00", "00", "00", "00", "00", "00"};
        String[] textSet3 =  {"진료", "검사", "외래", "진료", "검사", "진료", "검사", "외래", "진료", "검사"};
        String[] textSet4 =  {"본관 2층 회복실", "본관 3층 정형외과", "본관 1층 신경외과"," 본관 1층 신경내과", "본관 2층 정형외과", "본관 2층 회복실", "본관 3층 정형외과", "본관 1층 신경외과"," 본관 1층 신경내과", "본관 2층 정형외과"};
        String[] textSet5 =  {"김지영 간호사", "김지영 원장", "이미영 원장", "이미영 원장", "김지영 원장", "김지영 간호사", "김지영 원장", "이미영 원장", "이미영 원장", "김지영 원장"};

        ScheduleData dataArr[] = new ScheduleData[10];

        for(int i = 0; i < textSet1.length; i++){
            ScheduleData sample = new ScheduleData(textSet1[i], textSet2[i], textSet3[i], textSet4[i], textSet5[i]);
            dataArr[i] = sample;
        }

        // 어댑터 할당, 어댑터는 기본 어댑터를 확장한 커스텀 어댑터를 사용할 것이다.
        adapter = new ScheduleAdapter(dataArr);
//        adapter = new ScheduleAdapter(textSet1, textSet2, textSet3, textSet4, textSet5);
        recyclerView.setAdapter(adapter);

    }

}
