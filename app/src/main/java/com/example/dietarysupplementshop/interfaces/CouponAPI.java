package com.example.dietarysupplementshop.interfaces;

import com.example.dietarysupplementshop.model.AccountCoupon;
import com.example.dietarysupplementshop.model.Coupon;
import com.example.dietarysupplementshop.model.ResponseModel;
import com.example.dietarysupplementshop.requests.ApplyCouponRequest;
import com.example.dietarysupplementshop.responses.AppliedCouponResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CouponAPI {

    // Lấy danh sách tất cả voucher mà người dùng sở hữu
    @GET("api/user/coupons")
    Call<ResponseModel<List<AccountCoupon>>> getUserCoupons(@Query("status") String status); // status: USABLE, USED, EXPIRED

    // Lấy một voucher cụ thể của người dùng
    @GET("api/user/coupons/{accountCouponId}")
    Call<ResponseModel<AccountCoupon>> getAccountCouponById(@Path("accountCouponId") Long accountCouponId);

    // Áp dụng voucher cho một giỏ hàng/đơn hàng
    @POST("api/cart/apply-coupon")
    Call<ResponseModel<AppliedCouponResponse>> applyCouponToCart(@Body ApplyCouponRequest request);

    // Xóa voucher đã áp dụng khỏi giỏ hàng
    @POST("api/cart/remove-coupon")
    Call<ResponseModel<AppliedCouponResponse>> removeCouponFromCart(@Body ApplyCouponRequest request);

    // Lấy voucher chung nếu có (ví dụ: voucher public)
    @GET("api/coupons/public")
    Call<ResponseModel<List<Coupon>>> getPublicCoupons();
}