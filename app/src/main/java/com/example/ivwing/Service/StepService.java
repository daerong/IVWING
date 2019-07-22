package com.example.ivwing.Service;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.ivwing.Activity.MainActivity;
import com.example.ivwing.Data.StepResult;
import com.example.ivwing.InnerDB.StepVO;
import com.example.ivwing.Network.NetworkService;
import com.example.ivwing.R;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StepService extends Service implements SensorEventListener {
    SensorManager m_sensor_manager;
    Sensor m_step_detect;
    Sensor m_step_counter;

    private static final String TAG = StepService.class.getSimpleName();
    private Realm mStepRealm;

    int mStepDetector;
    int mStepCounter;

    int userId;

    NotificationCompat.Builder builder;
    NotificationManager notificationManager;
    ActivityManager activity_manager;

    Retrofit retrofit;
    NetworkService networkService;

    TimerTask updatePlz;
    Timer timer;

    public StepService() {

    }

    @Override public void onCreate() {
        super.onCreate();

        // 시스템서비스로부터 SensorManager 객체를 얻는다.
        m_sensor_manager = (SensorManager)getSystemService(SENSOR_SERVICE);

        // SensorManager 를 이용해서 가속센서와 자기장 센서 객체를 얻는다.
        m_step_detect = m_sensor_manager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if(m_step_detect == null) {
            Toast.makeText(this, "No Step Detect Sensor", Toast.LENGTH_SHORT).show();
        }
        m_step_counter = m_sensor_manager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(m_step_counter == null) {
            Toast.makeText(this, "No Step Detect Sensor", Toast.LENGTH_SHORT).show();
        }

        retrofit = new Retrofit.Builder()
                .baseUrl(NetworkService.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        networkService = retrofit.create(NetworkService.class);

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("stepRealm.realm")
                .build();
        // Use the config
        mStepRealm = Realm.getInstance(config);

        RealmResults<StepVO> stepList = getStepList(userId);
        mStepDetector = stepList.get(0).getStep_vol();

        sendNotification();
        activity_manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);

        updatePlz = new TimerTask() {
            //TimerTask 추상클래스를 선언하자마자 run()을 강제로 정의하도록 한다.
            @Override
            public void run() {
                /////////////////// 추가한 코드 ////////////////////
                if(isNetworkConnected()){
                    Log.i(TAG, ">>>>>  userId : " + userId);

                    HashMap<String, Object> input = new HashMap<>();
                    input.put("step_vol", mStepDetector);
                    input.put("user_id", userId);

                    Call<StepResult> comment = networkService.postUpdateStep(input);
                    comment.enqueue(new Callback<StepResult>() {
                        @Override
                        public void onResponse(Call<StepResult> call, Response<StepResult> response) {
                            //                    Log.v("Test", response.body().toString());
                            if(response.isSuccessful()){
                                if(response.body().getStatus().equals("Success")){
                                    Log.i(TAG,"onResponse : Success");
                                }else{
                                    Log.i(TAG,"onResponse : Failure");
                                }
                            } else{
                                Log.i(TAG,"onResponse : 통신 실패");
                            }
                        }

                        @Override
                        public void onFailure(Call<StepResult> call, Throwable t) {
                            Log.e(TAG,"네트워크가 원할하지 않습니다.");
                        }
                    });
                }else{
                    Log.i(TAG, ">>>>>  network : off");
                }
            }
        };

        /////////// / Timer 생성 //////////////
        timer = new Timer();
        timer.schedule(updatePlz, 0, 20 * 1000);        // 20초
        //////////////////////////////////////
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        userId = intent.getIntExtra("user_id", 0);
        if (userId != 0) {
            Log.i(TAG,"userId onStartCommand  : " + userId);
        }else{
            Log.e(TAG,"Error : Undefined user.");
        }

        // 센서 값을 이 컨텍스트에서 받아볼 수 있도록 리스너를 등록한다.
        m_sensor_manager.registerListener(this, m_step_detect, SensorManager.SENSOR_DELAY_UI);
        m_sensor_manager.registerListener(this, m_step_counter, SensorManager.SENSOR_DELAY_UI);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR){
            if(event.values[0] == 1.0f) {
                mStepDetector += event.values[0];
                updateStepData(userId, mStepDetector);
                if(isWorking()) ((MainActivity)MainActivity.targetContext).stepDataUpdate(userId);
                updateNotification();
            }
        }else if(event.sensor.getType() == Sensor.TYPE_STEP_COUNTER){
            mStepCounter = (int)event.values[0];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override public void onDestroy() {
        super.onDestroy();
        stopTimer();
        cancelNotification();
        m_sensor_manager.unregisterListener(this);
        mStepRealm.removeAllChangeListeners();
        mStepRealm.close();
    }

    private RealmResults<StepVO> getStepList(int user_id){
        if(mStepRealm.where(StepVO.class).equalTo("step_user", user_id).findAll().size() == 0){
            initStepData(user_id);
            return mStepRealm.where(StepVO.class).equalTo("step_user", user_id).findAll();
        }
        return mStepRealm.where(StepVO.class).equalTo("step_user", user_id).findAll();
    }

    private void initStepData(int user_id){
        mStepRealm.beginTransaction();
        StepVO step = mStepRealm.createObject(StepVO.class);
        step.setStep_user(user_id);
        step.setStep_vol(0);
        mStepRealm.commitTransaction();
    }

    private void deleteStepData(int user_id){
        mStepRealm.beginTransaction();
        RealmResults<StepVO> stepList = mStepRealm.where(StepVO.class).equalTo("step_user", user_id).findAll();
        stepList.deleteAllFromRealm();
        mStepRealm.commitTransaction();
    }

    public void updateStepData(int user_id, int new_vol){
        mStepRealm.beginTransaction(); //트랜잭션 시작
        RealmResults<StepVO> stepList = mStepRealm.where(StepVO.class).equalTo("step_user", user_id).findAll();
        stepList.get(0).setStep_vol(new_vol);
        Log.i(TAG, "stepVO  " + stepList.get(0).getStep_vol());
        mStepRealm.commitTransaction(); //트랜잭션 종료 반영
    }

    public void sendNotification() {

        builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.logo_splash);
        builder.setContentTitle("만보기 사용중");
        builder.setContentText("현재 걸음수 : " + mStepDetector);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Will display the notification in the notification bar
        notificationManager.notify(1, builder.build());
    }

    public void cancelNotification() {

        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);
        nMgr.cancel(1);
    }

    public void updateNotification(){
        builder.setContentText("현재 걸음수 : " + mStepDetector);
        notificationManager.notify(1, builder.build());
    }

    public boolean isWorking(){
        List<ActivityManager.RunningTaskInfo> task_info = activity_manager.getRunningTasks(9999);

        for(int i=0; i<task_info.size(); i++) {
//            Log.i(TAG, ">>>>>  [" + i + "] activity:"+ task_info.get(i).topActivity.getPackageName() + " >> " + task_info.get(i).topActivity.getClassName());
            if(task_info.get(i).topActivity.getClassName().equals("com.example.ivwing.Activity.MainActivity")){
                return true;
            }
        }
        return false;
    }

    public boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mobileNetwork = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiNetwork = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return mobileNetwork.isConnected() || wifiNetwork.isConnected();
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
