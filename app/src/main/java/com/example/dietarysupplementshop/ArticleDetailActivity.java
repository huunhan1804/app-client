package com.example.dietarysupplementshop;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.dietarysupplementshop.model.HealthArticle;
import com.example.dietarysupplementshop.repositories.Resource;
import com.example.dietarysupplementshop.viewModel.HealthViewModel;

public class ArticleDetailActivity extends AppCompatActivity {

    private HealthViewModel healthViewModel;
    private ImageView ivArticleImage;
    private TextView tvArticleTitle, tvArticleMeta;
    private WebView webViewContent;
    private Toolbar toolbar;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        healthViewModel = new ViewModelProvider(this).get(HealthViewModel.class);
        initViews();
        setupToolbar();

        int articleId = getIntent().getIntExtra("ARTICLE_ID", -1);
        Log.d("DetailActivity", "Received Article ID: " + articleId);
        if (articleId != -1) {
            observeArticleDetails(articleId);
        } else {
            Toast.makeText(this, "Lỗi: Không nhận được ID bài viết.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar_detail);
        ivArticleImage = findViewById(R.id.iv_article_image_detail);
        tvArticleTitle = findViewById(R.id.tv_article_title_detail);
        tvArticleMeta = findViewById(R.id.tv_article_meta_detail);
        webViewContent = findViewById(R.id.webview_article_content);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void observeArticleDetails(long articleId) {
        healthViewModel.getArticleDetails(articleId).observe(this, resource -> {
            if (resource == null) return;

            switch (resource.getStatus()) {
                case LOADING:
                    if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    if (progressBar != null) progressBar.setVisibility(View.GONE);
                    HealthArticle article = resource.getData();
                    if (article != null) {
                        updateUI(article);
                    }
                    break;
                case ERROR:
                    if (progressBar != null) progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Lỗi: " + resource.getMessage(), Toast.LENGTH_LONG).show();
                    break;
            }
        });
    }

    private void updateUI(HealthArticle article) {
        tvArticleTitle.setText(article.getTitle());
        tvArticleMeta.setText(String.format("Tác giả: %s | Ngày đăng: %s", article.getAuthor(), article.getPublishDate()));
        Glide.with(this).load(article.getImageUrl()).into(ivArticleImage);
        webViewContent.loadDataWithBaseURL(null, article.getContent(), "text/html", "utf-8", null);
    }
}