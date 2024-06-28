package com.example.albumap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeocodingService {
    @GET("maps/api/geocode/json")
    Call<GeocodingResponse> getGeocodingResult(
            @Query("latlng") String latlng,
            @Query("key") String apiKey
    );
}