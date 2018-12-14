package com.cabi.driver.service;

import com.cabi.driver.data.apiData.ApiRequestData;
import com.cabi.driver.data.apiData.CompanyDomainResponse;
import com.cabi.driver.data.apiData.DetailInfo;
import com.cabi.driver.data.apiData.EndStreetPickupResponse;
import com.cabi.driver.data.apiData.GetTripDetailResponse;
import com.cabi.driver.data.apiData.StreetCompleteResponse;
import com.cabi.driver.data.apiData.StreetPickUpResponse;
import com.cabi.driver.data.apiData.TripDetailResponse;
import com.cabi.driver.data.apiData.UpcomingResponse;
import com.cabi.driver.earningchart.Earningresponse;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by developer on 8/31/16.
 */

public interface CoreClient {
    String owner="dGF4aV9hbGw=/";
    //    @GET("{owner}")
//    Call<String> coreDetails(@Path(value = "owner",encoded = true) String owner);
    @GET("{owner}"+"?type=getcoreconfig")
    Call<ResponseBody> coreDetailsN(@Path(value = "owner",encoded = true) String owner,@Query(value = "encode",encoded = true) String auth_key);

    @GET("{owner}")
    Call<ResponseBody> coreDetails(@Path(value = "owner",encoded = true) String owner,@Query("type") String url,@Query(value = "encode",encoded = true) String auth_key);

//    @POST("{owner}")
//    Call<ResponseBody> updateUser(@Path(value = "owner",encoded = true) String owner,@Body RequestBody body, @Query("type") String url,@Query(value = "encode",encoded = true) String auth_key);


    @POST("{owner}")
    Call<ResponseBody> updateUser(@Path(value = "owner",encoded = true) String owner,@Body RequestBody body, @Query("type") String url,@Query(value = "encode",encoded = true) String auth_key,@Query(value = "lang",encoded = true) String lang);


    @POST("{owner}"+"?type=driver_booking_list")
    Call<UpcomingResponse> callData(@Path(value = "owner",encoded = true) String owner,@Body ApiRequestData.UpcomingRequest body, @Query("lang") String lang,@Query(value = "encode",encoded = true) String auth_key);
    @POST("{owner}"+"?type=driver_booking_list")
    Call<UpcomingResponse> callData_(@Path(value = "owner",encoded = true) String owner,@Body ApiRequestData.UpcomingRequest body, @Query("lang") String lang,@Query(value = "encode",encoded = true) String auth_key);
    @POST("{owner}"+"?type=get_trip_detail")
    Call<TripDetailResponse> callData(@Path(value = "owner",encoded = true) String owner,@Body ApiRequestData.getTripDetailRequest body, @Query("lang") String lang,@Query(value = "encode",encoded = true) String auth_key);
    @GET
    Call<ResponseBody> getWhole(@Header("Cache-Control") String cacheControl, @Url String url);
    /**
     * method to start Street trip
     * @param body StreetPickRequest class object
     * @param lang Language
     * @return returns api result on call back
     */
    @POST("{owner}"+"?type=driver_start_trip")
    Call<StreetPickUpResponse> startStreetTrip(@Path(value = "owner",encoded = true) String owner,@Body ApiRequestData.StreetPickRequest body, @Query("lang") String lang,@Query(value = "encode",encoded = true) String auth_key);

    @POST("{owner}"+"?type=street_pickup_end_trip")
    Call<EndStreetPickupResponse> endStreetTrip(@Path(value = "owner",encoded = true) String owner,@Body ApiRequestData.EndStreetPickup body, @Query("lang") String lang,@Query(value = "encode",encoded = true) String auth_key);

    @POST("{owner}"+"?type=get_trip_detail")
    Call<GetTripDetailResponse> getTripDetail(@Path(value = "owner",encoded = true) String owner,@Body ApiRequestData.TripDetailRequest body, @Query("lang") String lang,@Query(value = "encode",encoded = true) String auth_key);

    // http://192.168.1.169:1009/mobileapi117/index/=/?type=street_pickup_tripfare_update

    @POST("{owner}"+"?type=street_pickup_tripfare_update")
    Call<StreetCompleteResponse> completeStreetPickUpdate(@Path(value = "owner",encoded = true) String owner,@Body ApiRequestData.StreetPickComplete body, @Query("lang") String lang,@Query(value = "encode",encoded = true) String auth_key);

    @POST("{owner}"+"?type=driver_earnings")
    Call<Earningresponse> callData(@Path(value = "owner",encoded = true) String owner,@Body ApiRequestData.Earnings body,@Query(value = "encode",encoded = true) String auth_key);

    @POST("{owner}"+"?type=check_companydomain")
    Call<CompanyDomainResponse> callData(@Path(value = "owner",encoded = true) String owner,@Body ApiRequestData.BaseUrl body);
    @POST("/taxidispatch/report_push_notification")
    Call<ResponseBody> detail_infoCall(@Body DetailInfo body, @Query(value = "lang", encoded = true) String langCode);

    @POST("driver_location_history_update/")
    Call<ResponseBody> nodeUpdate(@Body RequestBody body);

    @POST("auth/")
    Call<ResponseBody> nodeAuth(@Body RequestBody body);

    @POST
    Call<ResponseBody> urlCheck(@Url String url, @Body RequestBody body);
}
