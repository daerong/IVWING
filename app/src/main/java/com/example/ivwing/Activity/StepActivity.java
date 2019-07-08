package com.example.ivwing.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.ivwing.R;
import com.example.ivwing.Adapter.StepAdapter;
import com.example.ivwing.Data.StepData;

public class StepActivity extends AppCompatActivity implements View.OnTouchListener{
    ImageView backButton;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ImageView dashLine;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        dashLine = (ImageView)findViewById(R.id.dashLine);
        dashLine.setOnTouchListener(this);

        backButton = findViewById(R.id.back_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StepActivity.this.finish(); // 로딩페이지 Activity stack에서 제거
            }
        });

        //스피너
         spinner = (Spinner)findViewById(R.id.spinner_field);

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
                Log.v("알림",spinner.getSelectedItem().toString()+ "is selected");
                float dash_locate = 93 * (11 - spinner.getSelectedItemPosition()) - 34;
                dashLine.setY(dash_locate);
//                if(spinner.getSelectedItemPosition() > 0){
//                    //선택된 항목
//                    Log.v("알림",spinner.getSelectedItem().toString()+ "is selected");
//                    float dash_locate = 1116 * spinner.getSelectedItemPosition() / 12;
//                    dashLine.setY(dash_locate);
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.v("알림","none selected");
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
        int[] stepSet =  {12000, 11000, 10000, 9000, 8000, 7000, 6000, 5000, 4000, 3000, 2000, 1000, 0, 1000, 12000};

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

    float oldYvalue;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int parentHeight = ((ViewGroup)v.getParent()).getHeight();    // 부모 View 의 Height

        if(event.getAction() == MotionEvent.ACTION_DOWN){
            // 뷰 누름
            oldYvalue = event.getY();
            Log.d("viewTest", "oldYvalue : " + oldYvalue);    // View 내부에서 터치한 지점의 상대 좌표값.
            Log.d("viewTest", "RawY : " + event.getRawY());    // View 를 터치한 지점의 절대 좌표값.
            Log.d("viewTest", "v.getHeight : " + v.getHeight() + " v.getWidth : " + v.getWidth());    // View 의 Width, Height

        }else if(event.getAction() == MotionEvent.ACTION_MOVE){
            // 뷰 이동 중
            v.setY(v.getY() + (event.getY()) - (v.getHeight()/2));

        }else if(event.getAction() == MotionEvent.ACTION_UP){
            // 뷰에서 손을 뗌
            float last_locate = v.getY();
            if(last_locate < 33) {
                v.setY(-34);
                spinner.setSelection(11);
            }
            else if(last_locate < 126){
                v.setY(59);
                spinner.setSelection(10);
            }
            else if(last_locate < 219){
                v.setY(152);
                spinner.setSelection(9);
            }
            else if(last_locate < 312){
                v.setY(245);
                spinner.setSelection(8);
            }
            else if(last_locate < 405){
                v.setY(338);
                spinner.setSelection(7);
            }
            else if(last_locate < 498){
                v.setY(431);
                spinner.setSelection(6);
            }
            else if(last_locate < 591){
                v.setY(524);
                spinner.setSelection(5);
            }
            else if(last_locate < 684){
                v.setY(617);
                spinner.setSelection(4);
            }
            else if(last_locate < 777){
                v.setY(710);
                spinner.setSelection(3);
            }
            else if(last_locate < 870){
                v.setY(803);
                spinner.setSelection(2);
            }
            else if(last_locate < 963){
                v.setY(896);
                spinner.setSelection(1);
            }
            else {
                v.setY(989);
                spinner.setSelection(0);
            }

//
//            if(v.getY() < -14){
//                v.setY(-14);
//            }else if((v.getY() + v.getHeight()) > parentHeight - 40){
//                v.setY(parentHeight - v.getHeight() - 42);
//                Log.v("알림","height : " + String.valueOf(parentHeight - v.getHeight() - 42 + 14));
//            }

        }
        return true;
    }
}
