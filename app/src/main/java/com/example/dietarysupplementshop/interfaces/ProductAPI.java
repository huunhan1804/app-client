package com.example.dietarysupplementshop.interfaces;

import com.example.dietarysupplementshop.model.ResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProductAPI {
    @GET("api/product/best-seller")
    Call<ResponseModel> getListBestSellerProduct();
    @GET("api/product/best-order")
    Call<ResponseModel> getListBestOrderProduct();
}
