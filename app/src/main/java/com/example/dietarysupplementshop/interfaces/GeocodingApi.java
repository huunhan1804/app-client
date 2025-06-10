package com.example.dietarysupplementshop.interfaces;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;



public interface GeocodingApi {
    @GET("maps/api/geocode/json")
    Call<GeocodingResponse> getCoordinatesFromAddress(@Query("address") String address, @Query("key") String apiKey);

    @GET("maps/api/geocode/json")
    Call<GeocodingResponse> getAddressFromCoordinates(@Query("latlng") String latlng, @Query("key") String apiKey);

    public class GeocodingResponse {
        public List<Result> results;
        public String status;
    }

    public class Result {
        public Geometry geometry;
    }

    public class Geometry {
        public Location location;
    }

    public class Location {
        public double lat;
        public double lng;
    }
}

