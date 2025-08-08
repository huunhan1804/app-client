package com.example.dietarysupplementshop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietarysupplementshop.adapter.SupportArticleAdapter;
import com.example.dietarysupplementshop.interfaces.RetrofitClient;
import com.example.dietarysupplementshop.interfaces.SupportAPI;
import com.example.dietarysupplementshop.model.ResponseModel;
import com.example.dietarysupplementshop.model.SupportArticle;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupportArticleListFragment extends Fragment {
    private static final String ARG_CATEGORY_ID = "category_id";
    private Long categoryId;
    private RecyclerView recyclerView;

    public static SupportArticleListFragment newInstance(Long categoryId) {
        SupportArticleListFragment fragment = new SupportArticleListFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_CATEGORY_ID, categoryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryId = getArguments().getLong(ARG_CATEGORY_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_support_article_list, container, false);
        recyclerView = view.findViewById(R.id.recycler_articles);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fetchArticles();
        return view;
    }

    private void fetchArticles() {
        SupportAPI api = RetrofitClient.getRetrofitInstance().create(SupportAPI.class);
        api.getArticlesByCategory(categoryId).enqueue(new Callback<ResponseModel<List<SupportArticle>>>() {
            @Override
            public void onResponse(Call<ResponseModel<List<SupportArticle>>> call, Response<ResponseModel<List<SupportArticle>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<SupportArticle> articles = response.body().getData();
                    recyclerView.setAdapter(new SupportArticleAdapter(getContext(), articles, articleId -> {
                        if (getActivity() instanceof SupportCenterActivity) {
                            ((SupportCenterActivity) getActivity()).showFragment(SupportArticleDetailFragment.newInstance(articleId));
                        }
                    }));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<List<SupportArticle>>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi tải bài viết", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
