package com.example.dietarysupplementshop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dietarysupplementshop.interfaces.RetrofitClient;
import com.example.dietarysupplementshop.interfaces.SupportAPI;
import com.example.dietarysupplementshop.model.ResponseModel;
import com.example.dietarysupplementshop.model.SupportArticle;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupportArticleDetailFragment extends Fragment {
    private static final String ARG_ARTICLE_ID = "article_id";
    private Long articleId;
    private TextView tvTitle, tvContent;

    public static SupportArticleDetailFragment newInstance(Long articleId) {
        SupportArticleDetailFragment fragment = new SupportArticleDetailFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ARTICLE_ID, articleId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            articleId = getArguments().getLong(ARG_ARTICLE_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_support_article_detail, container, false);
        tvTitle = view.findViewById(R.id.article_title);
        tvContent = view.findViewById(R.id.article_content);
        fetchArticle();
        return view;
    }

    private void fetchArticle() {
        SupportAPI api = RetrofitClient.getRetrofitInstance().create(SupportAPI.class);
        api.getArticleById(articleId).enqueue(new Callback<ResponseModel<SupportArticle>>() {
            @Override
            public void onResponse(Call<ResponseModel<SupportArticle>> call, Response<ResponseModel<SupportArticle>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SupportArticle article = response.body().getData();
                    tvTitle.setText(article.getArticleTitle());
                    tvContent.setText(article.getArticleContent());
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<SupportArticle>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi tải nội dung bài viết", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
