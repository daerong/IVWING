package com.example.ivwing.Network;

import com.example.ivwing.RequestData.User;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NetworkService {
    //통신 담당 메소드 구현.
    @POST("user/signup")
    Call<User> postSignup(@Query("user_name") String name, @Query("user_email") String email, @Query("user_pwd") String pwd, @Query("user_phone") String phone, @Query("user_age") int age, @Query("user_gender") char gender);
}
