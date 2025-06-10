package com.example.dietarysupplementshop.interfaces;

import com.example.dietarysupplementshop.model.ResponseModel;
import com.example.dietarysupplementshop.requests.AddLoginIdRequest;
import com.example.dietarysupplementshop.requests.AddToCartRequest;
import com.example.dietarysupplementshop.requests.ChangePasswordRequest;
import com.example.dietarysupplementshop.requests.UpdateAccountRequest;
import com.example.dietarysupplementshop.requests.UpdateAvatarRequest;
import com.example.dietarysupplementshop.responses.AccountInformation;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AccountAPI {
    @GET("/api/account/current-user")
    Call<ResponseModel<AccountInformation>> getCurrentUser();

    @POST("/api/account/update-avatar-user")
    Call<ResponseModel<AccountInformation>> updateAvatar(@Body UpdateAvatarRequest updateAvatarRequest);

    @POST("/api/account/add-login-id")
    Call<ResponseModel<AccountInformation>> addLoginId(@Body AddLoginIdRequest addLoginIdRequest);
    @POST("/api/account/change-password")
    Call<ResponseModel<String>> changePassword(@Body ChangePasswordRequest changePasswordRequest);

    @POST("/api/account/update-user")
    Call<ResponseModel<AccountInformation>> updateAccountProfile(@Body UpdateAccountRequest request);

    @POST("/api/cart/add")
    Call<ResponseModel<AccountInformation>> addToCart(@Body AddToCartRequest request);

    @POST("/api/cart/increase")
    Call<ResponseModel<AccountInformation>> increaseCartItemQuantity(@Body AddToCartRequest request);
    @POST("/api/cart/decrease")
    Call<ResponseModel<AccountInformation>> decreaseCartItemQuantity(@Body AddToCartRequest request);
    @DELETE("/api/cart/delete/{cartItemId}")
    Call<ResponseModel<AccountInformation>> deleteCartItem(@Path("cartItemId") long cart_item_id);

}
