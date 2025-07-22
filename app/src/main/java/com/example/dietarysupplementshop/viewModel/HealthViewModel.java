package com.example.dietarysupplementshop.viewModel;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dietarysupplementshop.interfaces.HealthAPI;
import com.example.dietarysupplementshop.interfaces.RetrofitClient;
import com.example.dietarysupplementshop.model.HealthArticle;
import com.example.dietarysupplementshop.model.PagedResponse;
import com.example.dietarysupplementshop.model.ResponseModel;
import com.example.dietarysupplementshop.repositories.Resource;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HealthViewModel extends ViewModel {

    private final HealthAPI healthAPI;
    private static final boolean USE_FAKE_DATA = false;
    private int currentPage = 0;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private List<HealthArticle> cumulativeArticles = new ArrayList<>();

    private final MutableLiveData<Resource<List<HealthArticle>>> articlesLiveData = new MutableLiveData<>();

    public HealthViewModel() {
        this.healthAPI = RetrofitClient.getRetrofitInstance().create(HealthAPI.class);
    }


    public LiveData<Resource<List<HealthArticle>>> getArticlesResult() {
        return articlesLiveData;
    }


    public void fetchArticles(String category, String search, boolean isRefresh) {
        if (USE_FAKE_DATA) {
            if (isRefresh) {
                currentPage = 0;
                cumulativeArticles.clear();
            }
            if (isLoading || (isLastPage && !isRefresh)) return;

            isLoading = true;
            if (isRefresh) articlesLiveData.setValue(Resource.loading(null));

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                List<HealthArticle> allFakeArticles = createFakeArticleList();
                int start = currentPage * 10;
                int end = Math.min(start + 10, allFakeArticles.size());

                if (start >= allFakeArticles.size()) {
                    isLastPage = true;
                } else {
                    List<HealthArticle> pageContent = allFakeArticles.subList(start, end);
                    cumulativeArticles.addAll(pageContent);
                    articlesLiveData.setValue(Resource.success(new ArrayList<>(cumulativeArticles)));
                    currentPage++;
                    isLastPage = end >= allFakeArticles.size();
                }
                isLoading = false;
            }, 1000);
            return;
        }
        if (isRefresh) {
            currentPage = 0;
            isLastPage = false;
            cumulativeArticles.clear();
        }

        if (isLoading || isLastPage) {
            return;
        }

        isLoading = true;
        if (isRefresh) {
            articlesLiveData.setValue(Resource.loading(null));
        }

        String apiCategory = "Tất cả".equalsIgnoreCase(category) ? "" : category;

        healthAPI.getArticles(apiCategory, search, currentPage, 10).enqueue(new Callback<ResponseModel<PagedResponse<HealthArticle>>>() {
            @Override
            public void onResponse(Call<ResponseModel<PagedResponse<HealthArticle>>> call, Response<ResponseModel<PagedResponse<HealthArticle>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    PagedResponse<HealthArticle> pagedData = response.body().getData();
                    cumulativeArticles.addAll(pagedData.getContent());
                    articlesLiveData.setValue(Resource.success(new ArrayList<>(cumulativeArticles)));
                    isLastPage = pagedData.isLast();
                    if (!isLastPage) {
                        currentPage++;
                    }
                } else {
                    articlesLiveData.setValue(Resource.error("Lỗi tải danh sách", null, response.code()));
                }
                isLoading = false;
            }

            @Override
            public void onFailure(Call<ResponseModel<PagedResponse<HealthArticle>>> call, Throwable t) {
                articlesLiveData.setValue(Resource.error(t.getMessage(), null, null));
                isLoading = false;
            }
        });
    }


    public LiveData<Resource<HealthArticle>> getArticleDetails(long articleId) {
        MutableLiveData<Resource<HealthArticle>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));
        if (USE_FAKE_DATA) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                List<HealthArticle> allFakeArticles = createFakeArticleList();
                HealthArticle foundArticle = null;
                for (HealthArticle article : allFakeArticles) {
                    if (article.getId() == articleId) {
                        foundArticle = article;
                        break;
                    }
                }
                if (foundArticle != null) {
                    data.setValue(Resource.success(foundArticle));
                } else {
                    data.setValue(Resource.error("Không tìm thấy bài viết", null, 404));
                }
            }, 500);
            return data;
        }
        healthAPI.getArticleDetails(articleId).enqueue(new Callback<ResponseModel<HealthArticle>>() {
            @Override
            public void onResponse(Call<ResponseModel<HealthArticle>> call, Response<ResponseModel<HealthArticle>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(Resource.success(response.body().getData()));
                } else {
                    data.setValue(Resource.error("Lỗi tải chi tiết", null, response.code()));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<HealthArticle>> call, Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null, null));
            }
        });
        return data;
    }

    private List<HealthArticle> createFakeArticleList() {
        List<HealthArticle> fakeList = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            String title = "Bài viết về Vitamin số " + i;
            String summary = "Đây là tóm tắt cho bài viết số " + i + ", nói về tầm quan trọng của việc bổ sung vitamin và khoáng chất hàng ngày.";
            String content = "<h1>Nội dung chi tiết " + i + "</h1><p>Đây là nội dung đầy đủ của bài viết. <b>Vitamin</b> rất quan trọng cho sức khỏe.</p>";
            String imageUrl = "https://via.placeholder.com/400x300.png/007BFF/FFFFFF?text=Article+" + i;

            fakeList.add(new HealthArticle(i, title, summary, "Vitamin", "Dược sĩ An", "15/07/2025", imageUrl, content));
        }
        return fakeList;
    }
}