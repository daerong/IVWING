package com.example.ivwing.Application;

import com.example.ivwing.Network.NetworkService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApplicationController {
    private static final ApplicationController ourInstance = new ApplicationController();

    public static ApplicationController getInstance() {
        return ourInstance;
    }

    private ApplicationController() {
    }


    private Retrofit retrofit = new Retrofit.Builder() // retrofit builder
            .baseUrl("http://localhost:3000/")
            .addConverterFactory(GsonConverterFactory.create()) // 파싱등록
            .build();

    private NetworkService networkService; // Interface container

    public NetworkService getMemberFactoryIm(){ // Interface 객체
        if(networkService == null){
            networkService = retrofit.create(NetworkService.class);
        }

        return networkService;
    }
}