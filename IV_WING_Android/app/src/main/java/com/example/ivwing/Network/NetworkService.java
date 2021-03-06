package com.example.ivwing.Network;

import com.example.ivwing.Data.LoginResult;
import com.example.ivwing.Data.PlanResult;
import com.example.ivwing.Data.RecordResult;
import com.example.ivwing.Data.SigninResult;
import com.example.ivwing.Data.StepResult;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface NetworkService {
    String API_URL = "http://13.209.241.53/";

    //통신 담당 메소드 구현.
    @FormUrlEncoded
    @POST("m/user/signup")
    Call<SigninResult> postSignup(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("m/user/login")
    Call<LoginResult> postLogin(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("m/plan/search")
    Call<PlanResult> postSearchPlan(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("m/record/search")
    Call<RecordResult> postSearchRecord(@FieldMap HashMap<String, Integer> param);

    @FormUrlEncoded
    @POST("m/step/search")
    Call<StepResult> postSearchStep(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("m/step/update")
    Call<StepResult> postUpdateStep(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("m/step/today")
    Call<StepResult> postTodayStep(@FieldMap HashMap<String, Object> param);

//    @POST("user/signup")
//    Call<ResponseBody> postSignup(@Body JsonObject jsonObject);
    //    Call<ResponseBody> postSignup(@Query("user_name") String name, @Query("user_email") String email, @Query("user_pwd") String pwd, @Query("user_phone") String phone, @Query("user_age") int age, @Query("user_gender") char gender);
}
