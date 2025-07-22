package com.example.dietarysupplementshop.interfaces;

import com.example.dietarysupplementshop.model.Product;
import com.example.dietarysupplementshop.model.ResponseModel;
import com.example.dietarysupplementshop.model.Shop; // Bạn cần tạo model Shop
import com.example.dietarysupplementshop.model.PagedResponse; // Dùng lại model này

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ShopAPI {
    @GET("api/shops/{id}")
    Call<ResponseModel<Shop>> getShopDetails(@Path("id") long shopId);

    @GET("api/shops/{id}/products")
    Call<ResponseModel<PagedResponse<Product>>> getShopProducts(
            @Path("id") long shopId,
            @Query("page") int page,
            @Query("size") int size
    );
}