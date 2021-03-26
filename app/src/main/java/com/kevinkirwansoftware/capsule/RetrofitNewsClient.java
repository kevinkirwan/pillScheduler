package com.kevinkirwansoftware.capsule;

import com.kevinkirwansoftware.capsule.general.ApplicationTools;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitNewsClient {
    private static Retrofit ourInstance;

    public static Retrofit getInstance(){
        if(ourInstance == null){
            ourInstance = new Retrofit.Builder()
                    .baseUrl(ApplicationTools.getNewsUrl())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return ourInstance;
    }

    private RetrofitNewsClient(){

    }
}
