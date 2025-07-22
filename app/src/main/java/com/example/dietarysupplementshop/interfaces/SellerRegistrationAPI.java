package com.example.dietarysupplementshop.interfaces;

import com.example.dietarysupplementshop.model.ResponseModel;
import com.example.dietarysupplementshop.responses.AgencyInfoDTO;
import com.example.dietarysupplementshop.responses.SellerRegistrationResponse;
import okhttp3.RequestBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SellerRegistrationAPI {
    @Headers("Content-Type: application/json")
    @POST("api/account/register-agency")
    Call<ResponseModel<AgencyInfoDTO>> registerSeller(@Body RequestBody requestBodyJson);

    @GET("api/account/status")
    Call<ResponseModel<SellerRegistrationResponse>> getSellerRegistrationStatus(@Query("accountId") long accountId);
}