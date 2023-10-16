package com.example.dietarysupplementshop.interfaces;

import com.example.dietarysupplementshop.model.District;
import com.example.dietarysupplementshop.model.Province;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AddressAPI {
    @GET("/api/")
    Call<List<Province>> getProvinces(@Query("depth") int depth);

    @GET("/api/d/search/")
    Call<List<District>> searchDistrict(@Query("q") String query);
}
