package com.example.dietarysupplementshop.services;

import com.example.dietarysupplementshop.interfaces.AuthenticateAPI;
import com.example.dietarysupplementshop.interfaces.OtpAPI;
import com.example.dietarysupplementshop.interfaces.RetrofitClient;
import com.example.dietarysupplementshop.model.ResponseModel;
import com.example.dietarysupplementshop.requests.SendOTPRequest;
import com.example.dietarysupplementshop.responses.AuthenticateResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpService {

    private OtpAPI otpAPI;

    public OtpService() {
        this.otpAPI = RetrofitClient.getRetrofitInstance().create(OtpAPI.class);
    }

    public void sendOtpRegistration(String loginId, final OtpCallback otpCallback) {
        SendOTPRequest sendOTPRequest = new SendOTPRequest(loginId);
        Call<ResponseModel<String>> call = otpAPI.sendOTPRegistration(sendOTPRequest);

        call.enqueue(new Callback<ResponseModel<String>>() {
            @Override
            public void onResponse(Call<ResponseModel<String>> call, Response<ResponseModel<String>> response) {
                ResponseModel<String> responseModel = response.body();
                if(response.isSuccessful()) {
                    if(responseModel != null && responseModel.getStatus() == 200) {
                        otpCallback.onSuccess(responseModel.getMessage());
                    }
                } else {
                    otpCallback.onError(String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<String>> call, Throwable t) {
                otpCallback.onError(t.getMessage());
            }
        });
    }

    public void sendOtpForgotPassword(String loginId, final OtpCallback otpCallback) {
        SendOTPRequest sendOTPRequest = new SendOTPRequest(loginId);
        Call<ResponseModel<String>> call = otpAPI.sendOTPForgotPassword(sendOTPRequest);

        call.enqueue(new Callback<ResponseModel<String>>() {
            @Override
            public void onResponse(Call<ResponseModel<String>> call, Response<ResponseModel<String>> response) {
                ResponseModel<String> responseModel = response.body();
                if(response.isSuccessful()) {
                    if(responseModel != null && responseModel.getStatus() == 200) {
                        otpCallback.onSuccess(responseModel.getMessage());
                    }
                } else {
                    otpCallback.onError(String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<String>> call, Throwable t) {
                otpCallback.onError(t.getMessage());
            }
        });
    }



    public interface OtpCallback {
        void onSuccess(String successMessage);
        void onError(String errorMessage);
    }
}
