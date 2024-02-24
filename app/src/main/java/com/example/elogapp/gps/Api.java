package com.example.elogapp.gps;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    //String BASE_URL = "http://geo.migps.in/Geo/Address?lat=28.5080214&lng=77.0909803";//
    String BASE_URL = "http://geo.migps.in/Geo/";

    @GET("Address")
    Call<String> getAddress(@Query("lat") String mLat,
                            @Query("lng") String mLon);

}
