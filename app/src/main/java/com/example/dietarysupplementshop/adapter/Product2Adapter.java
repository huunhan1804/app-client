package com.example.dietarysupplementshop.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietarysupplementshop.ProductInfoActivity;
import com.example.dietarysupplementshop.R;
import com.example.dietarysupplementshop.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Product2Adapter extends RecyclerView.Adapter<Product2Adapter.Product2ViewHolder>{

    private final List<Product> productList;
    private Context context;

    public Product2Adapter(Context context,List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public Product2Adapter.Product2ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_horizontal, parent, false);
        return new Product2Adapter.Product2ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Product2Adapter.Product2ViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.productNameTextView.setText(product.getProduct_name());
        holder.productPriceTextView.setText(product.getProduct_price());
        holder.productRatingBar.setRating(Float.parseFloat(String.valueOf(product.getRating())));

        Picasso.get()
                .load(product.getImage_url())
                .into(holder.productImageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductInfoActivity.class);
                intent.putExtra("productId", product.getProduct_id());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(productList != null){
            return productList.size();
        }
        return 0;
    }

    public static class Product2ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageView;
        TextView productNameTextView;
        TextView productPriceTextView;
        RatingBar productRatingBar;

        public Product2ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.productImageView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            productPriceTextView = itemView.findViewById(R.id.productPriceTextView);
            productRatingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
}

