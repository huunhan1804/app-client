package com.example.dietarysupplementshop.interfaces;

import com.example.dietarysupplementshop.model.ProductSeller;
import com.example.dietarysupplementshop.model.ResponseModel;
import com.example.dietarysupplementshop.requests.AddProductRequest;
import com.example.dietarysupplementshop.requests.SearchRequest;
import com.example.dietarysupplementshop.responses.ProductInformation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

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


    @GET("seller/products")
    Call<List<ProductSeller>> getAllSellerProducts();

    @GET("seller/products")
    Call<List<ProductSeller>> getSellerProductsByStatus(@Query("status") String status);

    @POST("seller/products")
    Call<ProductSeller> addProduct(@Body AddProductRequest productRequest);

    @PUT("seller/products/{productId}")
    Call<ProductSeller> updateProductStatus(@Path("productId") String productId, @Query("newStatus") String newStatus);

    @PUT("seller/products/{productId}")
    Call<ProductSeller> updateProduct(@Path("productId") String productId, @Body AddProductRequest productRequest);

    @PUT("seller/products/{productId}")
    Call<Void> deleteProduct(String productId);
}
