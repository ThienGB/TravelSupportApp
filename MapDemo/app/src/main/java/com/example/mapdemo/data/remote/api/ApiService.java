package com.example.mapdemo.data.remote.api;

import com.example.mapdemo.data.model.api.AccommodationResponse;
import com.example.mapdemo.data.model.api.PlaceResponse;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("place/nearbysearch/json")
    Observable<PlaceResponse> getPlaces(
            @Query("location") String location,
            @Query("radius") int radius,
            @Query("type") String type,
            @Query("key") String apiKey
    );
    @GET("/accommodation")
    Observable<List<AccommodationResponse>> getAccommodations(@Query("city_id") String cityId);
    @GET("/city")
    Observable<Response<ResponseBody>> getCities(@Query("country") int countryId);
}
