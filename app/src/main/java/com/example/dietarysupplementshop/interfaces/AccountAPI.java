package com.example.dietarysupplementshop.interfaces;

import com.example.dietarysupplementshop.model.ResponseModel;
import com.example.dietarysupplementshop.responses.AccountInformation;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface AccountAPI {
    @GET("/api/account/current-user")
    Call<ResponseModel<AccountInformation>> getCurrentUser();
}
