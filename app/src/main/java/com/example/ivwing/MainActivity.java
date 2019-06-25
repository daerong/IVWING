package com.example.ivwing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    //슬라이드 열기/닫기 플래그
    boolean isPageOpen = false;
    //슬라이드 열기 애니메이션
    Animation translateLeftAnim;
    //슬라이드 닫기 애니메이션

    Animation translateRightAnim;
    //슬라이드 레이아웃
    LinearLayout slidingPage;
    RelativeLayout toggle_icon_on;
    RelativeLayout toggle_icon_off;

    LinearLayout calendar_action;
    LinearLayout iv_action;
    LinearLayout step_action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //UI
        slidingPage = (LinearLayout)findViewById(R.id.slidingPage);
        toggle_icon_on = (RelativeLayout)findViewById(R.id.toggle_icon_on);
        toggle_icon_off = (RelativeLayout)findViewById(R.id.toggle_icon_off);

        calendar_action = (LinearLayout)findViewById(R.id.calendar_action);
        iv_action = (LinearLayout)findViewById(R.id.iv_action);
        step_action = (LinearLayout)findViewById(R.id.step_action);

        //애니메이션
        translateLeftAnim = AnimationUtils.loadAnimation(this, R.anim.translate_left);
        translateRightAnim = AnimationUtils.loadAnimation(this, R.anim.translate_right);

        //애니메이션 리스너 설정
        SlidingPageAnimationListener animationListener = new SlidingPageAnimationListener();
        translateLeftAnim.setAnimationListener(animationListener);
        translateRightAnim.setAnimationListener(animationListener);

        //클릭 리스너 설정
        calendar_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplication(), CalendarActivity.class));
//                MainActivity.this.finish(); // 로딩페이지 Activity stack에서 제거
            }
        });
        iv_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplication(), InformationActivity.class));
//                MainActivity.this.finish(); // 로딩페이지 Activity stack에서 제거
            }
        });
        step_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplication(), StepActivity.class));
//                MainActivity.this.finish(); // 로딩페이지 Activity stack에서 제거
            }
        });

    }

    //버튼
    public void onButton1Clicked(View v){
        //닫기
        if(isPageOpen){
            //애니메이션 시작
            slidingPage.startAnimation(translateRightAnim);
        }
        //열기
        else{
            toggle_icon_off.setVisibility(View.VISIBLE);
            slidingPage.setVisibility(View.VISIBLE);
            slidingPage.startAnimation(translateLeftAnim);
        }
    }

    @Override
    public void onBackPressed() {
        //초반 플래시 화면에서 넘어갈때 뒤로가기 버튼 못누르게 함
    }

    //애니메이션 리스너
    private class SlidingPageAnimationListener implements Animation.AnimationListener {
        @Override
        public void onAnimationEnd(Animation animation) {
            //슬라이드 열기->닫기
            if(isPageOpen){
                toggle_icon_off.setVisibility(View.INVISIBLE);
                slidingPage.setVisibility(View.INVISIBLE);
                isPageOpen = false;
            }
            //슬라이드 닫기->열기
            else{

                isPageOpen = true;
            }
        }
        @Override
        public void onAnimationRepeat(Animation animation) {

        }
        @Override
        public void onAnimationStart(Animation animation) {

        }
    }
}
