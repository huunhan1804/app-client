package com.example.dietarysupplementshop.interfaces;

import com.example.dietarysupplementshop.model.ResponseModel;
import com.example.dietarysupplementshop.requests.SearchRequest;
import com.example.dietarysupplementshop.responses.ProductInformation;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ProductAPI {
    @GET("api/product/all-by-category/{categoryId}")
    Call<ResponseModel> getListProductByCategory(@Path("categoryId") long categoryId);

    @GET("api/product/best-seller")
    Call<ResponseModel> getListBestSellerProduct();

    @GET("api/product/best-order")
    Call<ResponseModel> getListBestOrderProduct();

    @GET("api/product/info/{productId}")
    Call<ResponseModel<ProductInformation>> getProductInfo(@Path("productId") long productId);
    @GET("api/product/related/{productId}")
    Call<ResponseModel> getListRelatedProduct(@Path("productId") long productId);
    @POST("api/product/search")
    Call<ResponseModel> getListSearchProduct(@Body SearchRequest searchRequest);
}
