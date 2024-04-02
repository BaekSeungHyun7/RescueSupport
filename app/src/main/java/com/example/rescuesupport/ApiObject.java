package com.example.rescuesupport;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiObject {
    private static Retrofit retrofit = null;
    private static WeatherInterface retrofitService = null;

    private ApiObject() { }

    public static WeatherInterface getRetrofitService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        if (retrofitService == null) {
            retrofitService = retrofit.create(WeatherInterface.class);
        }

        return retrofitService;
    }
}

