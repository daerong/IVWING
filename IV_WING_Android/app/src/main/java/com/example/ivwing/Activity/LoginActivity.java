package com.example.ivwing.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ivwing.Data.LoginResult;
import com.example.ivwing.InnerDB.UserVO;
import com.example.ivwing.Network.NetworkService;
import com.example.ivwing.R;

import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private Realm mRealm;

    Button login_action;
    TextView signup_action;
    EditText login_email;
    EditText login_pwd;

    Retrofit retrofit;
    NetworkService networkService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Realm.init(this);
        mRealm = Realm.getDefaultInstance();

        RealmResults<UserVO> userList = getUserList();
        Log.i(TAG, ">>>>>   userList.size :  " + userList.size());

        retrofit = new Retrofit.Builder()
                .baseUrl(NetworkService.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        networkService = retrofit.create(NetworkService.class);

        login_email = findViewById(R.id.login_email);
        login_pwd = findViewById(R.id.login_pwd);

        if(userList.size() != 0){
            login_email.setText(userList.get(0).getUser_email());
        }

        login_action = (Button)findViewById(R.id.login_btn);
        login_action.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

            // 이메일 입력 확인
            if( login_email.getText().toString().length() == 0 ) {
                Toast.makeText(LoginActivity.this, "이메일을 입력하세요!", Toast.LENGTH_SHORT).show();
                login_email.requestFocus();
                return;
            }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(login_email.getText().toString()).matches()){
                Toast.makeText(LoginActivity.this,"이메일 형식이 아닙니다", Toast.LENGTH_SHORT).show();
                login_email.setText(null);
                login_email.requestFocus();
                return;
            }

            // 비밀번호 입력 확인
            if( login_pwd.getText().toString().length() == 0 ) {
                Toast.makeText(LoginActivity.this, "비밀번호를 입력하세요!", Toast.LENGTH_SHORT).show();
                login_pwd.requestFocus();
                return;
            }

            HashMap<String, Object> input = new HashMap<>();
            input.put("user_email", login_email.getText().toString());
            input.put("user_pwd", login_pwd.getText().toString());

            Call<LoginResult> comment = networkService.postLogin(input);
            comment.enqueue(new Callback<LoginResult>() {
                @Override
                public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
//                    Log.v("Test", response.body().toString());
                    if(response.isSuccessful()){
                        if(response.body().getStatus().equals("Success")){

                            deleteUserData();

                            LoginResult loginResult = response.body();
                            LoginResult.LoginData loginData = loginResult.getData();

                            insertUserData(loginData.getUser_id(), loginData.getUser_name(), loginData.getUser_email(), loginData.getUser_phone(), loginData.getUser_age(), loginData.getUser_gender(), loginData.getUser_stat(),loginData.getUser_room(), loginData.getUser_linker());

                            startActivity(new Intent(getApplication(), Splash2Activity.class)); //로딩이 끝난 후, ChoiceFunction 이동
                            LoginActivity.this.finish(); // 로딩페이지 Activity stack에서 제거
                            Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(LoginActivity.this, "일치하는 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    } else{
                        Toast.makeText(LoginActivity.this, "통신 실패", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResult> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "네트워크가 원할하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            });
            }
        });


        signup_action = (TextView) findViewById(R.id.signup_action);
        signup_action.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplication(), SignupActivity.class)); //로딩이 끝난 후, ChoiceFunction 이동
//                LoginActivity.this.finish(); // 로딩페이지 Activity stack에서 제거
            }
        });
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

    private void insertUserData(int user_id, String user_name, String user_email, String user_phone, int user_age, char user_gender, char user_stat, int user_room, int user_linker){
        mRealm.beginTransaction();
        UserVO user = mRealm.createObject(UserVO.class);
        user.setUser_id(user_id);
        user.setUser_name(user_name);
        user.setUser_email(user_email);
        user.setUser_phone(user_phone);
        user.setUser_age(user_age);
        user.setUser_gender(String.valueOf(user_gender));
        user.setUser_stat(String.valueOf(user_stat));
        user.setUser_room(user_room);
        user.setUser_linker(user_linker);
        mRealm.commitTransaction();
    }

    private void deleteUserData(){

        mRealm.beginTransaction();

        RealmResults<UserVO> userList = mRealm.where(UserVO.class).findAll();
        userList.deleteAllFromRealm();
        mRealm.commitTransaction();
    }
}
