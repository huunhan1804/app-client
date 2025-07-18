
package com.example.dietarysupplementshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dietarysupplementshop.R;
import com.example.dietarysupplementshop.model.ProductSeller;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductSellerAdapter extends RecyclerView.Adapter<ProductSellerAdapter.ProductSellerViewHolder> {

    private final Context context;
    private final List<ProductSeller> productList;
    private final OnProductActionListener listener;

    public interface OnProductActionListener {
        void onEditProduct(ProductSeller product);

        void onHideProduct(ProductSeller product);

        void onUnHideProduct(ProductSeller product);

        void onDeleteProduct(ProductSeller product);

        void onViewProductDetail(ProductSeller product);
    }

    public ProductSellerAdapter(Context context, List<ProductSeller> productList, OnProductActionListener listener) {
        this.context = context;
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductSellerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_seller_product, parent, false);
        return new ProductSellerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductSellerViewHolder holder, int position) {
        ProductSeller product = productList.get(position);

        Glide.with(context)
                .load(product.getImageUrl())
                .placeholder(R.drawable.product_image) // Default image
                .error(R.drawable.product_image) // Error image
                .into(holder.productImageView);

        holder.productNameTextView.setText(product.getProductName());

        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        formatter.setMaximumFractionDigits(0);

        String priceText;
        if (product.getMinPrice() == product.getMaxPrice()) {
            priceText = formatter.format(product.getMinPrice());
        } else {
            priceText = formatter.format(product.getMinPrice()) + " - " + formatter.format(product.getMaxPrice());
        }
        holder.productPriceTextView.setText(priceText);

        holder.tvStockQuantity.setText(String.valueOf(product.getStockQuantity()));
        holder.tvSold.setText(String.valueOf(product.getSoldQuantity()));
        holder.tvRecentStatus.setText(product.getProductStatus());
        holder.btnActionHide.setVisibility(View.GONE);
        holder.btnActionEdit.setVisibility(View.GONE);
        holder.btnActionMore.setVisibility(View.GONE);

        switch (product.getProductStatus()) {
            case "Còn hàng":
            case "Hết hàng":
                holder.btnActionHide.setVisibility(View.VISIBLE);
                holder.btnActionHide.setText("Ẩn");
                holder.btnActionEdit.setVisibility(View.VISIBLE);
                holder.btnActionMore.setVisibility(View.VISIBLE);
                break;
            case "Ẩn":
                holder.btnActionHide.setVisibility(View.VISIBLE);
                holder.btnActionHide.setText("Hiện");
                holder.btnActionEdit.setVisibility(View.VISIBLE);
                holder.btnActionMore.setVisibility(View.VISIBLE);
                break;
            case "Chờ duyệt":
            case "Vi phạm":
                holder.btnActionMore.setVisibility(View.VISIBLE);
                holder.btnActionMore.setText("...");
                break;
        }
        holder.btnActionHide.setOnClickListener(v -> {
            if ("Ẩn".equals(holder.btnActionHide.getText().toString())) {
                listener.onHideProduct(product);
            } else if ("Hiện".equals(holder.btnActionHide.getText().toString())) {
                listener.onUnHideProduct(product);
            }
        });

        holder.btnActionEdit.setOnClickListener(v -> listener.onEditProduct(product));

        holder.btnActionMore.setOnClickListener(v -> {
            Toast.makeText(context, "More options for " + product.getProductName(), Toast.LENGTH_SHORT).show();

        });

        holder.itemView.setOnClickListener(v -> listener.onViewProductDetail(product));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductSellerViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageView;
        TextView productNameTextView, productPriceTextView, tvStockQuantity, tvSold, tvRecentStatus;
        Button btnActionHide, btnActionEdit, btnActionMore;

        public ProductSellerViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.productImageView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            productPriceTextView = itemView.findViewById(R.id.productPriceTextView);
            tvStockQuantity = itemView.findViewById(R.id.tvStockQuantity);
            tvSold = itemView.findViewById(R.id.tvSold);
            tvRecentStatus = itemView.findViewById(R.id.tvRecentStatus);
            btnActionHide = itemView.findViewById(R.id.btnActionHide);
            btnActionEdit = itemView.findViewById(R.id.btnActionEdit);
            btnActionMore = itemView.findViewById(R.id.btnActionMore);
        }
    }
}