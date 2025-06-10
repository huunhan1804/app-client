package com.example.dietarysupplementshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietarysupplementshop.ProductInfoActivity;
import com.example.dietarysupplementshop.R;
import com.example.dietarysupplementshop.responses.OrderDetailResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder> {

    private List<OrderDetailResponse> orderDetailList;
    private Context context;

    public OrderDetailAdapter(Context context, List<OrderDetailResponse> orderDetailList) {
        this.context = context;
        this.orderDetailList = orderDetailList;
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_order_detail, parent, false);
        return new OrderDetailViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
        OrderDetailResponse orderDetail = orderDetailList.get(position);

        if (orderDetail.getProductVariantDTO().getProduct_variant_image_url() != null) {
            Picasso.get()
                    .load(orderDetail.getProductVariantDTO().getProduct_variant_image_url())
                    .into(holder.productImageView);
        } else {
            for (String image : orderDetail.getProductInfoDTO().getMedia_url()) {
                Picasso.get()
                        .load(image)
                        .into(holder.productImageView);
            }
        }

        holder.productNameTextView.setText(orderDetail.getProductInfoDTO().getProduct_name());
        holder.productPriceTextValue.setText(orderDetail.getPrice());
        holder.quantityTextValue.setText(String.valueOf(orderDetail.getQuantity()));
        holder.subTotalTextValue.setText(orderDetail.getSub_total());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductInfoActivity.class);
            intent.putExtra("productId", orderDetail.getProductInfoDTO().getProduct_id());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return orderDetailList.size();
    }

    public static class OrderDetailViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageView;
        TextView productNameTextView, productPriceTextValue, quantityTextValue, subTotalTextValue;

        public OrderDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.productImageView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            productPriceTextValue = itemView.findViewById(R.id.productPriceTextValue);
            quantityTextValue = itemView.findViewById(R.id.quantityTextValue);
            subTotalTextValue = itemView.findViewById(R.id.subTotalTextValue);
        }
    }
}
