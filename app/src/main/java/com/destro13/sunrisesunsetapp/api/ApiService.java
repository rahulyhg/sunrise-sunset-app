package com.destro13.sunrisesunsetapp.api;

import com.destro13.sunrisesunsetapp.mvp.model.SunriseSunsetReport;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface ApiService {
    @GET("json")
    Observable<SunriseSunsetReport> getSunriseSunset(@Query("lat") double lat, @Query("lng") double lng,
                                                     @Query("formatted") int formatted, @Query("date") String date);
}
