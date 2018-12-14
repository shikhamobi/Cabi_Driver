package com.cabi.driver.interfaces;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.POST;
import retrofit.http.Query;

public interface Retrofit
{
	@POST("/json")
	void Whereis(@Query("latlng") String request, @Query("sensor") String device_id, @Query("language") String lang, @Query("key") String key, Callback<Response> cb);
}
