package com.example.dietarysupplementshop.interfaces;

import com.example.dietarysupplementshop.model.ResponseModel;
import com.example.dietarysupplementshop.requests.AddNewProductRequest;
import com.example.dietarysupplementshop.requests.UpdateProductRequest;
import com.example.dietarysupplementshop.responses.ProductInfoDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AgencyProductAPI {

    @POST("api/agency/products/add")
    Call<ResponseModel<ProductInfoDTO>> createProduct(@Body AddNewProductRequest request);

    @PUT("api/agency/products/{productId}")
    Call<ResponseModel<ProductInfoDTO>> updateProduct(@Path("productId") Long productId, @Body UpdateProductRequest request);

    @DELETE("api/agency/products/{productId}")
    Call<ResponseModel<ProductInfoDTO>> deleteProduct(@Path("productId") Long productId);

    @GET("api/agency/products")
    Call<ResponseModel<List<ProductInfoDTO>>> getAgencyProducts(@Query("statusCode") String statusCode);

    @PUT("api/agency/products/{productId}/status/{statusCode}")
    Call<ResponseModel<ProductInfoDTO>> updateProductStatus(
            @Path("productId") Long productId,
            @Path("statusCode") String statusCode
    );
}