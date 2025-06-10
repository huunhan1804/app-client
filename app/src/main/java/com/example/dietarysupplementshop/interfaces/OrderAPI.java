package com.example.dietarysupplementshop.interfaces;

import com.example.dietarysupplementshop.model.Order;
import com.example.dietarysupplementshop.model.ResponseModel;
import com.example.dietarysupplementshop.requests.CheckoutRequest;
import com.example.dietarysupplementshop.requests.OrderRequest;
import com.example.dietarysupplementshop.responses.OrderDetailResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OrderAPI {
    @POST("api/order/add")
    Call<ResponseModel<Order>> addOrder(@Body OrderRequest orderRequest);

    @GET("api/order/all")
    Call<ResponseModel<List<Order>>> getAllOrdered();

    @GET("api/order/cancel/{orderId}")
    Call<ResponseModel<List<Order>>> cancelOrder(@Path("orderId") long orderId);

    @POST("api/order/checkout")
    Call<ResponseModel<List<OrderDetailResponse>>> getOrderDetailCheckout(@Body CheckoutRequest request);

    @GET("api/order/detail/{orderId}")
    Call<ResponseModel<Order>> getOrderDetail(@Path("orderId") long orderId);
}
