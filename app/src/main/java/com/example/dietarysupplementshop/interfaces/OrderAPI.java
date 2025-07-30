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
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface OrderAPI {
    @POST("api/order/add")
    Call<ResponseModel<Order>> addOrder(@Body OrderRequest orderRequest);

    @GET("api/order/all") // Lấy tất cả đơn hàng của người dùng hiện tại (người mua)
    Call<ResponseModel<List<Order>>> getAllOrdered();

    @PUT("api/order/cancel/{orderId}") // Sử dụng PUT cho các hành động thay đổi trạng thái
    Call<ResponseModel<List<Order>>> cancelOrder(@Path("orderId") long orderId);

    @PUT("api/order/received/{orderId}") // Người mua đánh dấu đã nhận hàng
    Call<ResponseModel<List<Order>>> markOrderAsReceived(@Path("orderId") long orderId);

    @PUT("api/order/return-request/{orderId}") // Người mua yêu cầu trả hàng/hoàn tiền
    Call<ResponseModel<List<Order>>> requestReturnRefund(@Path("orderId") long orderId);

    // API dành riêng cho người bán (nếu có các endpoint riêng)
    @PUT("api/agency-info/order/confirm/{orderId}") // Người bán xác nhận đơn PENDING -> SHIPPING
    Call<ResponseModel<List<Order>>> confirmOrder(@Path("orderId") long orderId);

    @PUT("api/agency-info/order/delivered/{orderId}") // Người bán đánh dấu đã giao hàng SHIPPING -> DELIVERED
    Call<ResponseModel<List<Order>>> markOrderAsDeliveredByAgency(@Path("orderId") long orderId);

    @PUT("api/agency-info/order/approve-return/{orderId}") // Người bán đồng ý hoàn tiền
    Call<ResponseModel<List<Order>>> approveReturnRefund(@Path("orderId") long orderId);

    // Có thể cần một request body cho lý do từ chối
    @PUT("api/agency-info/order/reject-refund/{orderId}") // Người bán từ chối hoàn tiền
    Call<ResponseModel<List<Order>>> rejectReturnRefund(@Path("orderId") long orderId, @Body String rejectionReason);

    @GET("api/order/checkout")
    Call<ResponseModel<List<OrderDetailResponse>>> getOrderDetailCheckout(@Body CheckoutRequest request);

    @GET("api/order/detail/{orderId}")
    Call<ResponseModel<Order>> getOrderDetail(@Path("orderId") long orderId);

    // API để người bán lấy các đơn hàng liên quan đến sản phẩm của họ
    @GET("api/agency-info/orders/all")
    Call<ResponseModel<List<Order>>> getAllSellerOrders();
}