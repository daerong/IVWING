package com.example.ivwing.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ivwing.Adapter.PlanAdapter;
import com.example.ivwing.Data.LoginResult;
import com.example.ivwing.Data.PlanResult;
import com.example.ivwing.Data.RecordResult;
import com.example.ivwing.Data.ScheduleData;
import com.example.ivwing.Data.StepResult;
import com.example.ivwing.InnerDB.UserVO;
import com.example.ivwing.Network.NetworkService;
import com.example.ivwing.R;
import com.example.ivwing.Adapter.IntravenousAdapter;
import com.example.ivwing.Data.IntravenousData;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InformationActivity extends AppCompatActivity {
    private static final String TAG = InformationActivity.class.getSimpleName();
    private Realm mRealm;

    ImageView backButton;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    Retrofit retrofit;
    NetworkService networkService;

    TimerTask updatePlz;
    Timer timer;

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

        retrofit = new Retrofit.Builder()
                .baseUrl(NetworkService.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        networkService = retrofit.create(NetworkService.class);

        updatePlz = new TimerTask() {
            //TimerTask 추상클래스를 선언하자마자 run()을 강제로 정의하도록 한다.
            @Override
            public void run() {
                /////////////////// 추가한 코드 ////////////////////
                Realm.init((InformationActivity)InformationActivity.this);
                mRealm = Realm.getDefaultInstance();

                RealmResults<UserVO> userList = getUserList();
                Log.i(TAG, ">>>>>   userList.size :  " + userList.size());

                if(userList.size() != 0){
                    HashMap<String, Integer> input = new HashMap<>();
                    input.put("user_id", userList.get(0).getUser_id());

                    Call<RecordResult> comment = networkService.postSearchRecord(input);
                    comment.enqueue(new Callback<RecordResult>() {
                        @Override
                        public void onResponse(Call<RecordResult> call, Response<RecordResult> response) {
                            if(response.isSuccessful()){
                                if(response.body().getStatus().equals("Success")){
                                    if(response.body().getMsg().equals("Empty data")){
                                        recyclerView.setAdapter(null);

                                        recyclerView.setLayoutManager(layoutManager);
                                        Toast.makeText(InformationActivity.this, "표시할 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                                    }else{
//                                    Toast.makeText(CalendarActivity.this, "Success", Toast.LENGTH_SHORT).show();

                                        Log.v("Count", String.valueOf(response.body().getData().size()));

                                        int arr_vol = response.body().getData().size();

                                        RecordResult recordResult = response.body();
                                        RecordResult.RecordData[] recordData = new RecordResult.RecordData[arr_vol];

                                        recordResult.copy(recordData, arr_vol);


                                        IntravenousData[] dataArr = new IntravenousData[arr_vol];

                                        for(int i = 0; i < arr_vol; i++){
                                            IntravenousData sample = new IntravenousData(recordData[i].getIv_name(), recordData[i].getIv_now(), recordData[i].getIv_max(), recordData[i].getIv_time(), recordData[i].getRecord_gtt());
                                            dataArr[i] = sample;
                                        }

                                        // 어댑터 할당, 어댑터는 기본 어댑터를 확장한 커스텀 어댑터를 사용할 것이다.
                                        adapter = new IntravenousAdapter(dataArr);
                                        recyclerView.setAdapter(adapter);

                                        recyclerView.setLayoutManager(layoutManager);
                                    }
                                }else{
                                    Toast.makeText(InformationActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                                }
                            } else{
                                Toast.makeText(InformationActivity.this, "통신 실패", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<RecordResult> call, Throwable t) {
                            Toast.makeText(InformationActivity.this, "네트워크가 원할하지 않습니다.", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Network : Retrofit Exception -> " + ((t != null && t.getMessage() != null) ? t.getMessage() : "---"));
                            Log.d(TAG, "Network : Avs Exception -> " + ((t != null && t.getMessage() != null) ? t.getMessage() : "---"));
                        }
                    });
                }else{
                    Toast.makeText(InformationActivity.this, "Failed : DB error", Toast.LENGTH_SHORT).show();
                }
            }
        };

        /////////// / Timer 생성 //////////////
        timer = new Timer();
        timer.schedule(updatePlz, 0, 3 * 1000);        // 20초
        //////////////////////////////////////
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
//        mRealm.removeAllChangeListeners();
//        mRealm.close();
    }

    private RealmResults<UserVO> getUserList(){
        return mRealm.where(UserVO.class).findAll();
    }

    public void stopTimer(){
        if(updatePlz != null){
            updatePlz.cancel(); //타이머task를 timer 큐에서 지워버린다
            updatePlz = null;
        }
        if(timer != null){
            timer.cancel(); //스케쥴task과 타이머를 취소한다.
            timer.purge(); //task큐의 모든 task를 제거한다.
            timer = null;
        }
    }
}