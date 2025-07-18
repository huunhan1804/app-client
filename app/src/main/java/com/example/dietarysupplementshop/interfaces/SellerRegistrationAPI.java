package com.example.dietarysupplementshop.interfaces;

import com.example.dietarysupplementshop.model.ResponseModel;
import com.example.dietarysupplementshop.responses.SellerRegistrationResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface SellerRegistrationAPI {
    @Multipart
    @POST("api/seller/register")
    Call<ResponseModel<SellerRegistrationResponse>> registerSeller(
            @Part("registrationData") RequestBody shopDataJson,
            @Part MultipartBody.Part idCardFront,
            @Part MultipartBody.Part idCardBack,
            @Part List<MultipartBody.Part> businessLicense,
            @Part List<MultipartBody.Part> professionalCertificates,
            @Part List<MultipartBody.Part> diplomaCertificates
    );
    @GET("api/seller/status")
    Call<ResponseModel<SellerRegistrationResponse>> getSellerRegistrationStatus(@Query("accountId") long accountId);
}