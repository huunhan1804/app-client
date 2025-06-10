package com.example.dietarysupplementshop.services;

import android.util.Log;

import com.example.dietarysupplementshop.MyApplication;
import com.example.dietarysupplementshop.interfaces.AuthenticateAPI;
import com.example.dietarysupplementshop.interfaces.RetrofitClient;
import com.example.dietarysupplementshop.model.ResponseModel;
import com.example.dietarysupplementshop.requests.AuthenticateRequest;
import com.example.dietarysupplementshop.requests.AuthenticateSocialRequest;
import com.example.dietarysupplementshop.requests.ForgotPasswordRequest;
import com.example.dietarysupplementshop.requests.RefreshTokenRequest;
import com.example.dietarysupplementshop.requests.RegistrationRequest;
import com.example.dietarysupplementshop.responses.AuthenticateResponse;
import com.example.dietarysupplementshop.token.TokenManager;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthService {

    private AuthenticateAPI authenticateAPI;
    private TokenManager tokenManager;

    public AuthService(TokenManager tokenManager) {
        this.authenticateAPI = RetrofitClient.getRetrofitInstance().create(AuthenticateAPI.class);
        this.tokenManager = tokenManager;
    }

    public void authenticate(String email, String password, final AuthCallback callback) {
        AuthenticateRequest request = new AuthenticateRequest(email, password);
        Call<ResponseModel<AuthenticateResponse>> call = authenticateAPI.authenticate(request);

        call.enqueue(new Callback<ResponseModel<AuthenticateResponse>>() {
            @Override
            public void onResponse(Call<ResponseModel<AuthenticateResponse>> call, Response<ResponseModel<AuthenticateResponse>> response) {
                ResponseModel<AuthenticateResponse> responseModel = response.body();
                if (response.isSuccessful()) {
                    if (responseModel != null && responseModel.getStatus() == 200) {
                        AuthenticateResponse authResponse = responseModel.getData();
                        tokenManager.saveTokens(authResponse.getAccess_token(), authResponse.getRefresh_token());
                        callback.onSuccess(responseModel.getMessage());
                    } else {
                        callback.onError(responseModel != null ? responseModel.getMessage() : "Authentication failed");
                    }
                } else {
                    callback.onError(responseModel != null ? responseModel.getMessage() : "Authentication failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<AuthenticateResponse>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }


    public String refreshAccessToken(String refreshToken) {
        RefreshTokenRequest request = new RefreshTokenRequest(refreshToken);
        Call<ResponseModel<AuthenticateResponse>> call = authenticateAPI.refreshAccessToken(request);

        try {
            Response<ResponseModel<AuthenticateResponse>> response = call.execute();
            if (response.isSuccessful()) {
                ResponseModel<AuthenticateResponse> responseModel = response.body();
                if (responseModel != null && responseModel.getStatus() == 200) {
                    AuthenticateResponse authResponse = responseModel.getData();
                    return authResponse.getAccess_token();
                } else {
                    // Handle the case where the response is not successful or the token refresh fails
                    return null;
                }
            } else {
                // Handle the case where the response is not successful or the token refresh fails
                return null;
            }
        } catch (IOException e) {
            // Handle the case where an exception occurs during the refresh
            return null;
        }
    }


    public interface AuthCallback {
        void onSuccess(String errorMessage);

        void onError(String errorMessage);
    }


    public void registration(String loginId, String password, String fullname, String otp_code, final AuthCallback authCallback) {
        RegistrationRequest registrationRequest = new RegistrationRequest(loginId, password, fullname, otp_code);
        Call<ResponseModel<AuthenticateResponse>> call = authenticateAPI.registration(registrationRequest);
        call.enqueue(new Callback<ResponseModel<AuthenticateResponse>>() {
            @Override
            public void onResponse(Call<ResponseModel<AuthenticateResponse>> call, Response<ResponseModel<AuthenticateResponse>> response) {
                ResponseModel<AuthenticateResponse> responseModel = response.body();
                if (response.isSuccessful()) {
                    if (responseModel != null && responseModel.getStatus() == 200) {
                        AuthenticateResponse authResponse = responseModel.getData();
                        tokenManager.saveTokens(authResponse.getAccess_token(), authResponse.getRefresh_token());

                        authCallback.onSuccess(responseModel.getMessage());

                    } else {
                        authCallback.onError(responseModel != null ? responseModel.getMessage() : "Authentication failed");
                    }
                } else {
                    authCallback.onError(responseModel != null ? responseModel.getMessage() : "Authentication failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<AuthenticateResponse>> call, Throwable t) {
                authCallback.onError(t.getMessage());
            }
        });
    }

    public void authenticateGoogle(String idToken, final AuthCallback authCallback) {
        AuthenticateSocialRequest authenticateSocialRequest = new AuthenticateSocialRequest(idToken);
        Call<ResponseModel<AuthenticateResponse>> call = authenticateAPI.authenticateGoogle(authenticateSocialRequest);
        call.enqueue(new Callback<ResponseModel<AuthenticateResponse>>() {
            @Override
            public void onResponse(Call<ResponseModel<AuthenticateResponse>> call, Response<ResponseModel<AuthenticateResponse>> response) {
                ResponseModel<AuthenticateResponse> responseModel = response.body();
                if (response.isSuccessful()) {
                    if (responseModel != null && responseModel.getStatus() == 200) {
                        AuthenticateResponse authResponse = responseModel.getData();
                        tokenManager.saveTokens(authResponse.getAccess_token(), authResponse.getRefresh_token());
                        authCallback.onSuccess(responseModel.getMessage());

                    } else {
                        authCallback.onError(responseModel != null ? responseModel.getMessage() : "Authentication failed");
                    }
                } else {
                    authCallback.onError(responseModel != null ? responseModel.getMessage() : "Authentication failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<AuthenticateResponse>> call, Throwable t) {
                authCallback.onError(t.getMessage());
            }
        });
    }

    public void forgotPassword(String loginId, String new_password, String otp_code, final ForgotPasswordCallBack forgotPasswordCallBack) {
        ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest(loginId, new_password, otp_code);
        Call<ResponseModel<String>> call = authenticateAPI.forgotPassword(forgotPasswordRequest);
        call.enqueue(new Callback<ResponseModel<String>>() {
            @Override
            public void onResponse(Call<ResponseModel<String>> call, Response<ResponseModel<String>> response) {
                ResponseModel<String> responseModel = response.body();
                if (response.isSuccessful()) {
                    if (responseModel != null && responseModel.getStatus() == 200) {
                        forgotPasswordCallBack.onSuccess(responseModel.getMessage());
                    }
                } else {
                    forgotPasswordCallBack.onError(String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<String>> call, Throwable t) {
                forgotPasswordCallBack.onError(t.getMessage());
            }
        });
    }

    public interface ForgotPasswordCallBack {
        void onSuccess(String successMessage);

        void onError(String errorMessage);
    }


}
