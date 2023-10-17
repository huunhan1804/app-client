package com.example.dietarysupplementshop.services;

import com.example.dietarysupplementshop.MyApplication;
import com.example.dietarysupplementshop.interfaces.AccountAPI;
import com.example.dietarysupplementshop.interfaces.RetrofitClient;
import com.example.dietarysupplementshop.model.ResponseModel;
import com.example.dietarysupplementshop.requests.AuthenticateRequest;
import com.example.dietarysupplementshop.responses.AccountInformation;
import com.example.dietarysupplementshop.responses.AuthenticateResponse;
import com.example.dietarysupplementshop.token.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountService {
    private AccountAPI accountAPI;

    public AccountService() {
        this.accountAPI = RetrofitClient.getRetrofitInstance().create(AccountAPI.class);
    }

    public void currentUser(final CurrentUserCallback callback) {
        Call<ResponseModel<AccountInformation>> call = accountAPI.getCurrentUser();

        call.enqueue(new Callback<ResponseModel<AccountInformation>>() {
            @Override
            public void onResponse(Call<ResponseModel<AccountInformation>> call, Response<ResponseModel<AccountInformation>> response) {
                ResponseModel<AccountInformation> responseModel = response.body();
                if (response.isSuccessful()) {
                    if (responseModel != null && responseModel.getStatus() == 200) {
                        AccountInformation accountInformation = responseModel.getData();
                        callback.onSuccess(accountInformation);
                    } else {
                        callback.onError(responseModel != null ? responseModel.getMessage() : "Get current user failed");
                    }
                } else {
                    callback.onError(responseModel != null ? responseModel.getMessage() : "Get current user failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<AccountInformation>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public interface CurrentUserCallback {
        void onSuccess(AccountInformation accountInformation);
        void onError(String errorMessage);
    }
}
