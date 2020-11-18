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
import android.widget.TextView;
import android.widget.Toast;

import com.example.ivwing.Adapter.IntravenousAdapter;
import com.example.ivwing.Data.IntravenousData;
import com.example.ivwing.Data.RecordResult;
import com.example.ivwing.Data.StepResult;
import com.example.ivwing.InnerDB.UserVO;
import com.example.ivwing.Network.NetworkService;
import com.example.ivwing.R;
import com.example.ivwing.Adapter.StepAdapter;
import com.example.ivwing.Data.PamperData;

import java.text.DecimalFormat;
import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StepActivity extends AppCompatActivity implements View.OnTouchListener{
    private static final String TAG = InformationActivity.class.getSimpleName();
    private Realm mRealm;

    ImageView backButton;
    TextView stepVolume;
    TextView stepMinite;
    TextView stepDistance;
    TextView stepCalorie;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ImageView dashLine;
    Spinner spinner;

    Retrofit retrofit;
    NetworkService networkService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        Realm.init(this);
        mRealm = Realm.getDefaultInstance();

        RealmResults<UserVO> userList = getUserList();
        Log.i(TAG, ">>>>>   userList.size :  " + userList.size());

        dashLine = (ImageView)findViewById(R.id.dashLine);
        dashLine.setOnTouchListener(this);

        stepVolume = findViewById(R.id.step_volume);
        stepMinite = findViewById(R.id.step_minite);
        stepDistance = findViewById(R.id.step_distance);
        stepCalorie = findViewById(R.id.step_calorie);

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

        retrofit = new Retrofit.Builder()
                .baseUrl(NetworkService.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        networkService = retrofit.create(NetworkService.class);

        if(userList.size() != 0) {
            HashMap<String, Object> input = new HashMap<>();
            input.put("user_id", userList.get(0).getUser_id());

            Call<StepResult> comment = networkService.postSearchStep(input);
            comment.enqueue(new Callback<StepResult>() {
                @Override
                public void onResponse(Call<StepResult> call, Response<StepResult> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equals("Success")) {
                            if (response.body().getMsg().equals("Empty data")) {
                                recyclerView.setAdapter(null);

                                recyclerView.setLayoutManager(layoutManager);
                                Toast.makeText(StepActivity.this, "표시할 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.v("Count", String.valueOf(response.body().getData().size()));

                                int arr_vol = response.body().getData().size();

                                StepResult stepResult = response.body();
                                StepResult.StepData[] stepData = new StepResult.StepData[arr_vol];

                                stepResult.copy(stepData, arr_vol);

                                PamperData[] dataArr = new PamperData[arr_vol];

                                for (int i = 0; i < arr_vol; i++) {
                                    PamperData sample = new PamperData(stepData[i].getStep_date().substring(5, 7), stepData[i].getStep_date().substring(8, 10), stepData[i].getStep_vol());
                                    dataArr[i] = sample;
                                }

                                // 어댑터 할당, 어댑터는 기본 어댑터를 확장한 커스텀 어댑터를 사용할 것이다.
                                adapter = new StepAdapter(dataArr, StepActivity.this);
                                recyclerView.setAdapter(adapter);

                                recyclerView.setLayoutManager(layoutManager);
                            }
                        } else {
                            Toast.makeText(StepActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(StepActivity.this, "통신 실패", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<StepResult> call, Throwable t) {
                    Toast.makeText(StepActivity.this, "네트워크가 원할하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(StepActivity.this, "Failed : DB error", Toast.LENGTH_SHORT).show();
        }
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

    public void RecyclerClickEvent(int vol){

        double calorie = vol / 30.0;
        double distance = 0.0007 * vol;
        double minite = 0.00858 * vol;

        stepVolume.setText(String.valueOf(vol));
        stepMinite.setText(new DecimalFormat("#.#").format(minite));
        stepDistance.setText(new DecimalFormat("#.##").format(distance));
        stepCalorie.setText(new DecimalFormat("#.#").format(calorie));
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
