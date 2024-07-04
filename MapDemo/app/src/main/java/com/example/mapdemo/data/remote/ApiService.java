package com.example.mapdemo.data.remote;

import com.example.mapdemo.data.model.api.AccomServiceRespon;
import com.example.mapdemo.data.model.api.AccommodationResponse;
import com.example.mapdemo.data.model.api.CityResponse;
import com.example.mapdemo.data.model.api.PlaceResponse;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
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
    @GET("/api/accom/{endpoint}")
    Observable<List<AccomServiceRespon>> getAccomServices(@Path("endpoint") String endpoint);

    @GET("/api/accom/{endpoint}")
    Observable<List<AccommodationResponse>> getAccommodations(@Path("endpoint") String endpoint);
    @GET("/api/accom/{endpoint}")
    Observable<List<CityResponse>> getCities(@Path("endpoint") String endpoint);
}
