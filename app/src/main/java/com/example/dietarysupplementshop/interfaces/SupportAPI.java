package com.example.dietarysupplementshop.interfaces;

import com.example.dietarysupplementshop.model.ResponseModel;
import com.example.dietarysupplementshop.model.SupportArticle;
import com.example.dietarysupplementshop.model.SupportCategory;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SupportAPI {
    @POST("api/support/all-categories/")
    Call<ResponseModel<List<SupportCategory>>> getAllCategories();

    @POST("api/support/all-by-category/{categoryId}")
    Call<ResponseModel<List<SupportArticle>>> getArticlesByCategory(@Path("categoryId") Long id);

    @POST("api/support/article/{articleId}")
    Call<ResponseModel<SupportArticle>> getArticleById(@Path("articleId") Long id);
}
