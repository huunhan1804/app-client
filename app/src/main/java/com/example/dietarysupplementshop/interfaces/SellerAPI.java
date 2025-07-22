package com.example.dietarysupplementshop.interfaces;

import com.example.dietarysupplementshop.model.Order;
import com.example.dietarysupplementshop.model.ResponseModel;
import com.example.dietarysupplementshop.requests.UpdateStatusRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SellerAPI {
    @GET("api/seller/orders")
    Call<ResponseModel<List<Order>>> getSellerOrders(@Query("status") String status);

    @POST("api/seller/orders/{orderId}/status")
    Call<ResponseModel<Order>> updateOrderStatus(@Path("orderId") long orderId, @Body UpdateStatusRequest request);
}