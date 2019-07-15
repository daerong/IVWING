package com.example.ivwing.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ivwing.Adapter.PlanAdapter;
import com.example.ivwing.Adapter.StepAdapter;
import com.example.ivwing.Data.LoginResult;
import com.example.ivwing.Data.PlanResult;
import com.example.ivwing.Network.NetworkService;
import com.example.ivwing.R;
import com.example.ivwing.Data.ScheduleData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CalendarActivity extends AppCompatActivity {
    ImageView backButton;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    Retrofit retrofit;
    NetworkService networkService;

    int arr_vol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* starts before 1 month from now */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        recyclerView = findViewById(R.id.plan_recyclerView);
        // 리사이클러뷰의 notify()처럼 데이터가 변했을 때 성능을 높일 때 사용한다.
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);

        retrofit = new Retrofit.Builder()
                .baseUrl(NetworkService.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        networkService = retrofit.create(NetworkService.class);

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
                int year = date.get(Calendar.YEAR);
                int mon = date.get(Calendar.MONTH);
                int day = date.get(Calendar.DAY_OF_MONTH);
                int hour = date.get(Calendar.HOUR_OF_DAY);
                int min = date.get(Calendar.MINUTE);
                int sec = date.get(Calendar.SECOND);

//                Toast.makeText(CalendarActivity.this, year+"/"+mon+"/"+day+" "+hour+":"+min+":"+sec, Toast.LENGTH_SHORT).show();

                HashMap<String, Object> input = new HashMap<>();
                input.put("year", year);
                input.put("mon", mon);
                input.put("day", day);
                input.put("hour", hour);
                input.put("min", min);
                input.put("sec", sec);

                Call<PlanResult> comment = networkService.postSearchPlan(input);
                comment.enqueue(new Callback<PlanResult>() {
                    @Override
                    public void onResponse(Call<PlanResult> call, Response<PlanResult> response) {
//                        Log.v("Test", response.body().toString());
                        if(response.isSuccessful()){
                            if(response.body().getStatus().equals("Success")){
                                if(response.body().getMsg().equals("Empty data")){
                                    recyclerView.setAdapter(null);

                                    recyclerView.setLayoutManager(layoutManager);
                                    Toast.makeText(CalendarActivity.this, "표시할 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                                }else{
//                                    Toast.makeText(CalendarActivity.this, "Success", Toast.LENGTH_SHORT).show();

                                    Log.v("Count", String.valueOf(response.body().getData().size()));

                                    int arr_vol = response.body().getData().size();

                                    PlanResult planResult = response.body();
                                    PlanResult.PlanData[] planData = new PlanResult.PlanData[arr_vol];

                                    planResult.copy(planData, arr_vol);


                                    ScheduleData[] dataArr = new ScheduleData[arr_vol];

                                    for(int i = 0; i < arr_vol; i++){
                                        Log.v("arrData", "" + planData[i].getPlan_id() + ", " + planData[i].getPlan_date() + ", " + planData[i].getPlan_type() + ", " + planData[i].getDept_name() + ", " + planData[i].getDoctor_name()+" "+planData[i].getDoctor_type());
                                        ScheduleData sample = new ScheduleData(planData[i].getPlan_date().substring(11, 13), planData[i].getPlan_date().substring(14, 16), planData[i].getPlan_type(), planData[i].getDept_name(), planData[i].getDoctor_name()+" "+planData[i].getDoctor_type());
                                        dataArr[i] = sample;
                                    }

                                    // 어댑터 할당, 어댑터는 기본 어댑터를 확장한 커스텀 어댑터를 사용할 것이다.
                                    adapter = new PlanAdapter(dataArr);
                                    recyclerView.setAdapter(adapter);

                                    recyclerView.setLayoutManager(layoutManager);
                                }
                            }else{
                                Toast.makeText(CalendarActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                            }
                        } else{
                            Toast.makeText(CalendarActivity.this, "통신 실패", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PlanResult> call, Throwable t) {
                        Toast.makeText(CalendarActivity.this, "네트워크가 원할하지 않습니다.", Toast.LENGTH_SHORT).show();
                        Log.e("retrofit", t.getMessage());
                    }
                });
            }
        });


    }
}
