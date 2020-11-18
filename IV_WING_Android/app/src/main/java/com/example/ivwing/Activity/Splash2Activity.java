package com.example.ivwing.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ivwing.InnerDB.UserVO;
import com.example.ivwing.R;

import io.realm.Realm;
import io.realm.RealmResults;

public class Splash2Activity extends AppCompatActivity {
    private static final String TAG = Splash2Activity.class.getSimpleName();
    private Realm mRealm;

    TextView welcome_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);

        Realm.init(this);
        mRealm = Realm.getDefaultInstance();

        RealmResults<UserVO> userList = getUserList();
        Log.i(TAG, ">>>>>   userList.size :  " + userList.size());


        welcome_text = findViewById(R.id.welcome_text);
        if(userList.size() != 0){
            welcome_text.setText(userList.get(0).getUser_name()+"님, 안녕하세요.");
        }else{
            Toast.makeText(Splash2Activity.this, "Failed : DB error", Toast.LENGTH_SHORT).show();
        }

        Handler hd = new Handler();
        hd.postDelayed(new Splash2Activity.splashhandler(), 3000); // 1초 후에 hd handler 실행  3000ms = 3초

    }

    private class splashhandler implements Runnable {
        public void run() {
            startActivity(new Intent(getApplication(), MainActivity.class)); //로딩이 끝난 후, ChoiceFunction 이동
            Splash2Activity.this.finish(); // 로딩페이지 Activity stack에서 제거
        }
    }

    @Override
    public void onBackPressed() {
        //초반 플래시 화면에서 넘어갈때 뒤로가기 버튼 못누르게 함
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