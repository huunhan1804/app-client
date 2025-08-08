package com.example.dietarysupplementshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietarysupplementshop.R;
import com.example.dietarysupplementshop.SupportArticleDetailFragment;
import com.example.dietarysupplementshop.SupportCenterActivity;
import com.example.dietarysupplementshop.model.SupportArticle;

import java.util.List;

public class SupportArticleAdapter extends RecyclerView.Adapter<SupportArticleAdapter.ViewHolder> {
    public interface OnArticleClickListener {
        void onArticleClick(Long articleId);
    }

    private final Context context;
    private final List<SupportArticle> articleList;
    private final OnArticleClickListener listener;

    public SupportArticleAdapter(Context context, List<SupportArticle> articleList, OnArticleClickListener listener) {
        this.context = context;
        this.articleList = articleList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_support_article, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SupportArticle article = articleList.get(position);
        holder.articleTitle.setText(article.getArticleTitle());
        holder.itemView.setOnClickListener(v -> listener.onArticleClick(article.getArticleId()));
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView articleTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            articleTitle = itemView.findViewById(R.id.article_title);
        }
    }
}
