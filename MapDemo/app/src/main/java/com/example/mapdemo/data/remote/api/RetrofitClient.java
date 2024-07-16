package com.example.mapdemo.data.remote.api;

import com.example.mapdemo.data.remote.api.ApiService;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;
//    public static final String MAP_BASE_URL = "https://maps.googleapis.com/maps/api/";
    public static final String ACCOM_BASE_URL = "https://667e62a7f2cb59c38dc578da.mockapi.io";
    public static final String CITY_BASE_URL = "https://114b03e0-11eb-4811-ab9e-9f853823c74c.mock.pstmn.io";
    public static ApiService getApiService(String baseUrl) {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
        return retrofit.create(ApiService.class);
    }

}
