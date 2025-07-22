package com.example.dietarysupplementshop.interfaces;

import com.example.dietarysupplementshop.model.HealthArticle;
import com.example.dietarysupplementshop.model.PagedResponse;
import com.example.dietarysupplementshop.model.ResponseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HealthAPI {

    @GET("api/articles")
    Call<ResponseModel<PagedResponse<HealthArticle>>> getArticles(
            @Query("category") String category,
            @Query("search") String searchTerm,
            @Query("page") int page,
            @Query("size") int size
    );

    @GET("api/articles/{id}")
    Call<ResponseModel<HealthArticle>> getArticleDetails(@Path("id") long articleId);
}