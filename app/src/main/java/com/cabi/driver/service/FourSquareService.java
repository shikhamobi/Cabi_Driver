package com.cabi.driver.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by developer on 16/3/17.
 */

public interface FourSquareService {

    @GET("venues/suggestcompletion")
    Call<Object> requestExplore(@Query("v") String v, @Query("ll") String ll, @Query("query") String query, @Query("oauth_token") String oauth_token);

    @GET("maps/api/geocode/json")
    Call<Object> requestBounds(@Query("address") String address);

}
