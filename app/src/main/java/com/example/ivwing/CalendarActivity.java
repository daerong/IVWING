package com.example.ivwing;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.skyhope.weekday.WeekDaySelector;
import com.skyhope.weekday.callback.WeekItemClickListener;
import com.skyhope.weekday.data.Holiday;
import com.skyhope.weekday.model.WeekModel;

import java.util.HashSet;
import java.util.Set;

public class CalendarActivity extends AppCompatActivity implements WeekItemClickListener {
    private WeekDaySelector weekDaySelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        weekDaySelector = findViewById(R.id.weekDaySelector);

        Set<Integer> holiday = new HashSet<>();
        holiday.add(Holiday.SUNDAY);
        weekDaySelector.setHoliday(holiday);

        weekDaySelector.setWeekItemClickListener(this);
    }

    @Override
    public void onGetItem(WeekModel model) {
        Toast.makeText(this, model.getMonth(), Toast.LENGTH_SHORT).show();
    }
}
