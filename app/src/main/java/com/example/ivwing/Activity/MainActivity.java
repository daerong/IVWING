package com.example.ivwing.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ivwing.InnerDB.UserVO;
import com.example.ivwing.R;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Realm mRealm;

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

    Button logout_action;

    TextView user_name;
    TextView user_stat;
    TextView user_email;
    TextView user_phone;
    TextView user_linker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(this);
        mRealm = Realm.getDefaultInstance();

        RealmResults<UserVO> userList = getUserList();
        Log.i(TAG, ">>>>>   userList.size :  " + userList.size());

        user_name = findViewById(R.id.user_name);
        user_stat = findViewById(R.id.user_stat);
        user_email = findViewById(R.id.user_email);
        user_phone = findViewById(R.id.user_phone);
        user_linker = findViewById(R.id.user_linker);

        if(userList.size() != 0){
            user_name.setText(userList.get(0).getUser_name() + "(" + userList.get(0).getUser_age() + ") 님");
            switch (userList.get(0).getUser_stat()){
                case "A" : {
                    user_stat.setText("상태 : 관리자");
                    break;
                }
                case "O" : {
                    user_stat.setText("상태 : 입원중이 아님");
                    break;
                }
                case "I" : {
                    user_stat.setText("상태 : " + userList.get(0).getUser_room() + "호에 입원중");
                    break;
                }
                default : {
                    user_stat.setText("상태 : 입원중이 아님");
                }
            }
            user_email.setText("이메일 : " + userList.get(0).getUser_email());
            user_phone.setText("연락처 : " + userList.get(0).getUser_phone());

            if(userList.get(0).getUser_linker() == 0){
                user_linker.setText("링거대 : 사용중이 아님");
            }else{
                user_linker.setText("링거대 : " + userList.get(0).getUser_linker() + "번 사용중");
            }
        }else{
            Toast.makeText(MainActivity.this, "Failed : DB error", Toast.LENGTH_SHORT).show();
        }

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
                if(!isPageOpen){
                    startActivity(new Intent(getApplication(), CalendarActivity.class));
//                  MainActivity.this.finish(); // 로딩페이지 Activity stack에서 제거
                }
            }
        });
        iv_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isPageOpen){
                    startActivity(new Intent(getApplication(), InformationActivity.class));
//                  MainActivity.this.finish(); // 로딩페이지 Activity stack에서 제거
                }
            }
        });
        step_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isPageOpen){
                    startActivity(new Intent(getApplication(), StepActivity.class));
//                  MainActivity.this.finish(); // 로딩페이지 Activity stack에서 제거
                }
            }
        });

        logout_action = findViewById(R.id.logout_btn);
        logout_action.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplication(), LoginActivity.class)); //로딩이 끝난 후, ChoiceFunction 이동
                MainActivity.this.finish(); // 로딩페이지 Activity stack에서 제거
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.removeAllChangeListeners();
        mRealm.close();
    }

    private RealmResults<UserVO> getUserList(){
        return mRealm.where(UserVO.class).findAll();
    }
}
