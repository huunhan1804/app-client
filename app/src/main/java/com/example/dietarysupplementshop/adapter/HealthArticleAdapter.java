package com.example.dietarysupplementshop.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietarysupplementshop.ArticleDetailActivity;
import com.example.dietarysupplementshop.R; // Đảm bảo đúng package của bạn
import com.example.dietarysupplementshop.model.HealthArticle;
import com.bumptech.glide.Glide; // Sử dụng Glide để tải ảnh (đảm bảo đã thêm dependency)

import java.util.List;

public class HealthArticleAdapter extends RecyclerView.Adapter<HealthArticleAdapter.ArticleViewHolder> {

    private List<HealthArticle> articleList;

    public HealthArticleAdapter(List<HealthArticle> articleList) {
        this.articleList = articleList;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_health_article_card, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        HealthArticle article = articleList.get(position);
        Glide.with(holder.itemView.getContext())
                .load(article.getImageUrl())
                .placeholder(R.drawable.image_baibao)
                .into(holder.ivThumbnail);
        holder.tvTitle.setText(article.getTitle());
        holder.tvSummary.setText(article.getSummary());
        holder.tvInfo.setText(String.format("Ngày đăng: %s | Tác giả: %s", article.getPublishDate(), article.getAuthor()));
        holder.itemView.setOnClickListener(v -> {
            Log.d("AdapterClick", "Sending Article ID: " + article.getId());
            Intent intent = new Intent(v.getContext(), ArticleDetailActivity.class);
            intent.putExtra("ARTICLE_ID", article.getId());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public void updateList(List<HealthArticle> newList) {
        articleList.clear();
        articleList.addAll(newList);
        notifyDataSetChanged();
    }

    static class ArticleViewHolder extends RecyclerView.ViewHolder {
        ImageView ivThumbnail;
        TextView tvTitle;
        TextView tvSummary;
        TextView tvInfo;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            ivThumbnail = itemView.findViewById(R.id.iv_article_thumbnail);
            tvTitle = itemView.findViewById(R.id.tv_article_title);
            tvSummary = itemView.findViewById(R.id.tv_article_summary);
            tvInfo = itemView.findViewById(R.id.tv_article_info);
        }
    }
}