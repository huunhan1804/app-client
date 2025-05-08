package com.example.dietarysupplementshop.interfaces;

import com.example.dietarysupplementshop.model.ResponseModel;
import com.example.dietarysupplementshop.requests.AuthenticateRequest;
import com.example.dietarysupplementshop.requests.AuthenticateSocialRequest;
import com.example.dietarysupplementshop.requests.ForgotPasswordRequest;
import com.example.dietarysupplementshop.requests.RefreshTokenRequest;
import com.example.dietarysupplementshop.requests.RegistrationRequest;
import com.example.dietarysupplementshop.requests.SendOTPRequest;
import com.example.dietarysupplementshop.responses.AuthenticateResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthenticateAPI {
    @POST("/api/auth/authenticate")
    Call<ResponseModel<AuthenticateResponse>> authenticate(@Body AuthenticateRequest loginRequest);
    @POST("/api/auth/refresh-token")
    Call<ResponseModel<AuthenticateResponse>> refreshAccessToken(@Body RefreshTokenRequest refreshTokenRequest);

    @POST("/api/auth/registration")
    Call<ResponseModel<AuthenticateResponse>> registration(@Body RegistrationRequest registrationRequest);

    @POST("/api/auth/authenticate-google")
    Call<ResponseModel<AuthenticateResponse>> authenticateGoogle(@Body AuthenticateSocialRequest authenticateSocialRequest);
    @POST("/api/auth/forgot-password")
    Call<ResponseModel<String>> forgotPassword(@Body ForgotPasswordRequest forgotPasswordRequest);
}
