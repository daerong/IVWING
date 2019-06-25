package com.example.ivwing;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.skyhope.weekday.WeekDaySelector;
import com.skyhope.weekday.callback.WeekItemClickListener;
import com.skyhope.weekday.data.Holiday;
import com.skyhope.weekday.model.WeekModel;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class CalendarActivity extends AppCompatActivity {
//    private WeekDaySelector weekDaySelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /* starts before 1 month from now */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        Drawable selected_background = getResources().getDrawable(R.drawable.selected_background, null);

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


//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_calendar);
//
//        weekDaySelector = findViewById(R.id.weekDaySelector);
//
//        Set<Integer> holiday = new HashSet<>();
//        holiday.add(Holiday.SUNDAY);
//        weekDaySelector.setHoliday(holiday);
//
//        weekDaySelector.setWeekItemClickListener(this);
    }

//    @Override
//    public void onGetItem(WeekModel model) {
//        Toast.makeText(this, model.getMonth(), Toast.LENGTH_SHORT).show();
//    }
}
