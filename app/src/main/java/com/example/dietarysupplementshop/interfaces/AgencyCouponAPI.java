package com.example.dietarysupplementshop.interfaces;

import com.example.dietarysupplementshop.model.ResponseModel;
import com.example.dietarysupplementshop.model.Coupon; // Sử dụng Coupon model
import com.example.dietarysupplementshop.requests.CreateVoucherRequest;
import com.example.dietarysupplementshop.responses.VoucherCreationResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.DELETE;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AgencyCouponAPI {

    // Tạo một voucher mới
    @POST("api/agency/coupons")
    Call<ResponseModel<VoucherCreationResponse>> createCoupon(@Body CreateVoucherRequest request);

    // Lấy tất cả voucher do người bán này tạo
    @GET("api/agency/coupons")
    Call<ResponseModel<List<Coupon>>> getAgencyCoupons(@Query("status") String status); // status: ACTIVE, INACTIVE, EXPIRED, ALL

    // Cập nhật thông tin voucher
    @PUT("api/agency/coupons/{couponId}")
    Call<ResponseModel<VoucherCreationResponse>> updateCoupon(@Path("couponId") Long couponId, @Body CreateVoucherRequest request);

    // Kích hoạt/Vô hiệu hóa voucher
    @PUT("api/agency/coupons/{couponId}/status")
    Call<ResponseModel<VoucherCreationResponse>> updateCouponStatus(@Path("couponId") Long couponId, @Query("isActive") boolean isActive);

    // Xóa voucher (chỉ khi chưa có ai dùng hoặc đã hết hạn)
    @DELETE("api/agency/coupons/{couponId}")
    Call<ResponseModel<Void>> deleteCoupon(@Path("couponId") Long couponId);
}