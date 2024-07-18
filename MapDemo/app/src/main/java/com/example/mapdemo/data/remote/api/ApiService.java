package com.example.mapdemo.data.remote.api;

import com.example.mapdemo.data.model.api.AccommodationResponse;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("/accommodation")
    Observable<List<AccommodationResponse>> getAccommodations(@Query("city_id") String cityId);
    @GET("/city")
    Observable<Response<ResponseBody>> getCities(@Query("country") int countryId);
}
