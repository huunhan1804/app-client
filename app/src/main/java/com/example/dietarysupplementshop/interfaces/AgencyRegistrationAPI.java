package com.example.dietarysupplementshop.interfaces;

import com.example.dietarysupplementshop.model.ResponseModel;
import com.example.dietarysupplementshop.responses.AgencyInfoDTO;
import com.example.dietarysupplementshop.responses.AgencyRegistrationResponse;
import okhttp3.RequestBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AgencyRegistrationAPI {
    @Headers("Content-Type: application/json")
    @POST("api/account/register-agency")
    Call<ResponseModel<AgencyInfoDTO>> registerAgency(@Body RequestBody requestBodyJson);

    @GET("api/account/status")
    Call<ResponseModel<AgencyRegistrationResponse>> getAgencyRegistrationStatus(@Query("accountId") long accountId);
}