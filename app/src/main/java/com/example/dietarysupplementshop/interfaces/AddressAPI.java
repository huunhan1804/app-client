package com.example.dietarysupplementshop.interfaces;

import com.example.dietarysupplementshop.model.Address;
import com.example.dietarysupplementshop.model.District;
import com.example.dietarysupplementshop.model.Province;
import com.example.dietarysupplementshop.model.ResponseModel;
import com.example.dietarysupplementshop.requests.AddAddressRquest;
import com.example.dietarysupplementshop.requests.UpdateAddressRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AddressAPI {
    @GET("/api/")
    Call<List<Province>> getProvinces(@Query("depth") int depth);

    @GET("/api/d/search/")
    Call<List<District>> searchDistrict(@Query("q") String query);
    @GET("/api/address/all")
    Call<ResponseModel<List<Address>>> getListAddress();
    @GET("/api/address/info/{addressId}")
    Call<ResponseModel<Address>> getInfoAddress(@Path("addressId") long addressId);

    @GET("/api/address/set-default/{addressId}")
    Call<ResponseModel<List<Address>>> setDefaultAddress(@Path("addressId") long addressId);

    @POST("/api/address/add")
    Call<ResponseModel<List<Address>>> addAddress(@Body AddAddressRquest addAddressRquest);

    @POST("/api/address/update")
    Call<ResponseModel<List<Address>>> updateAddress(@Body UpdateAddressRequest updateAddressRequest);

}
