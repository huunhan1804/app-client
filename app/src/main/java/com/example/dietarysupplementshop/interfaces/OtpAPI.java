package com.example.dietarysupplementshop.interfaces;

import com.example.dietarysupplementshop.model.ResponseModel;
import com.example.dietarysupplementshop.requests.SendOTPRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OtpAPI {
    @POST("/api/auth/send-otp-registration")
    Call<ResponseModel<String>> sendOTPRegistration(@Body SendOTPRequest sendOTPRequest);
    @POST("/api/auth/send-otp-forgot-password")
    Call<ResponseModel<String>> sendOTPForgotPassword(@Body SendOTPRequest sendOTPRequest);
}
