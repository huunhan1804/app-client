package com.example.dietarysupplementshop.interfaces;

import com.example.dietarysupplementshop.model.ResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryAPI {
    @GET("api/category/all/56")
    Call<ResponseModel> getCategories();
}
