package com.example.ivwing.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApplicationController {
    private static ApplicationController ourInstance = new ApplicationController();
    public static ApplicationController getInstance() {
        return ourInstance;
    }
    private ApplicationController() {
    }

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create()) // 파싱등록
            .build();

    private ApplicationController service = retrofit.create(ApplicationController.class);

    public ApplicationController getService() {
        return service;
    }
}
