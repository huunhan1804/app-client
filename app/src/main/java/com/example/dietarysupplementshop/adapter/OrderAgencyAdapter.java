package com.example.dietarysupplementshop.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietarysupplementshop.R;
import com.example.dietarysupplementshop.model.Order;
import com.example.dietarysupplementshop.responses.OrderDetailResponse;
import com.squareup.picasso.Picasso; // Sử dụng Picasso để tải ảnh

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class OrderAgencyAdapter extends RecyclerView.Adapter<OrderAgencyAdapter.OrderAgencyViewHolder> {

    private List<Order> orderList;
    private OnOrderActionButtonClickListener listener;
    public interface OnOrderActionButtonClickListener {
        void onActionButton1Click(Order order, int position);
        void onActionButton2Click(Order order, int position);
        void onItemClick(Order order, int position);
    }

    public OrderAgencyAdapter(List<Order> orderList, OnOrderActionButtonClickListener listener) {
        this.orderList = orderList;
        this.listener = listener;
    }

    public void setOrderList(List<Order> newOrderList) {
        this.orderList = newOrderList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderAgencyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_oder_agency, parent, false);
        return new OrderAgencyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAgencyViewHolder holder, int position) {
        Order order = orderList.get(position);

        if (order.getAddress_info() != null) {
            holder.customerNameTextView.setText(order.getAddress_info().getFullname());
        } else {
            holder.customerNameTextView.setText("Không có thông tin người mua");
        }
        holder.orderStatusTextView.setText(order.getOrder_status());
        holder.orderCodeTextView.setText("#" + order.getOrder_id());
        holder.totalAmountTextView.setText(order.getTotalBill());

        if (order.getOrder_date() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            holder.orderDateTextView.setText("Ngày đặt: " + sdf.format(order.getOrder_date()));
        }

        if (order.getOrder_detail() != null && !order.getOrder_detail().isEmpty()) {
            OrderDetailResponse firstProduct = order.getOrder_detail().get(0);
            holder.productImage.setVisibility(View.VISIBLE);
            holder.productNameTextView.setVisibility(View.VISIBLE);
            holder.productVariantTextView.setVisibility(View.VISIBLE);
            holder.productPriceTextView.setVisibility(View.VISIBLE);
            holder.productQuantityTextView.setVisibility(View.VISIBLE);
            holder.totalProductsTextView.setVisibility(View.VISIBLE);
            holder.productNameTextView.setText(firstProduct.getProductInfoDTO().getProduct_name());
            holder.productPriceTextView.setText(firstProduct.getPrice());
            holder.productQuantityTextView.setText("x" + firstProduct.getQuantity());

            if (firstProduct.getProductVariantDTO() != null) {
                holder.productVariantTextView.setText("Phân loại: " + firstProduct.getProductVariantDTO().getProduct_variant_name());
                String imageUrl = firstProduct.getProductVariantDTO().getProduct_variant_image_url() != null ?
                        firstProduct.getProductVariantDTO().getProduct_variant_image_url() :
                        (firstProduct.getProductInfoDTO().getMedia_url() != null && !firstProduct.getProductInfoDTO().getMedia_url().isEmpty() ?
                                firstProduct.getProductInfoDTO().getMedia_url().get(0) : null);

                if (imageUrl != null) {
                    Picasso.get().load(imageUrl).into(holder.productImage);
                } else {
                    holder.productImage.setImageResource(R.drawable.product_image); // Ảnh mặc định
                }

            } else {
                holder.productVariantTextView.setVisibility(View.GONE);
            }

            holder.totalProductsTextView.setText(String.format("%d sản phẩm", order.getOrder_detail().size()));

        } else {
            holder.productImage.setVisibility(View.GONE);
            holder.productNameTextView.setVisibility(View.GONE);
            holder.productVariantTextView.setVisibility(View.GONE);
            holder.productPriceTextView.setVisibility(View.GONE);
            holder.productQuantityTextView.setVisibility(View.GONE);
            holder.totalProductsTextView.setText("0 sản phẩm");
        }
        holder.actionButton1.setVisibility(View.GONE);
        holder.actionButton2.setVisibility(View.GONE);
        switch (order.getOrder_status()) {
            case "PENDING":
                holder.orderStatusTextView.setTextColor(Color.parseColor("#FF9800"));
                holder.actionButton1.setVisibility(View.VISIBLE);
                holder.actionButton1.setText("Xác nhận");
                holder.actionButton2.setVisibility(View.VISIBLE);
                holder.actionButton2.setText("Hủy đơn");
                break;
            case "SHIPPING":
                holder.orderStatusTextView.setTextColor(Color.parseColor("#2196F3"));
                holder.actionButton1.setVisibility(View.VISIBLE);
                holder.actionButton1.setText("Xem chi tiết");
                holder.actionButton2.setVisibility(View.VISIBLE);
                holder.actionButton2.setText("Đã Giao");
                break;
            case "DELIVERED":
                holder.orderStatusTextView.setTextColor(Color.parseColor("#4CAF50"));
                holder.actionButton1.setVisibility(View.VISIBLE);
                holder.actionButton1.setText("Xem chi tiết");
                break;
            case "CANCELLED":
                holder.orderStatusTextView.setTextColor(Color.parseColor("#F44336"));
                holder.actionButton1.setVisibility(View.VISIBLE);
                holder.actionButton1.setText("Xem chi tiết");
                break;
            case "RETURNED":
                holder.orderStatusTextView.setTextColor(Color.parseColor("#FFC107"));
                holder.actionButton1.setVisibility(View.VISIBLE);
                holder.actionButton1.setText("Xem yêu cầu");
                holder.actionButton2.setVisibility(View.VISIBLE);
                holder.actionButton2.setText("Đồng ý hoàn tiền");
                break;
            default:
                holder.orderStatusTextView.setTextColor(Color.BLACK);
                break;
        }

        holder.actionButton1.setOnClickListener(v -> {
            if (listener != null) listener.onActionButton1Click(order, position);
        });

        holder.actionButton2.setOnClickListener(v -> {
            if (listener != null) listener.onActionButton2Click(order, position);
        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(order, position);
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderAgencyViewHolder extends RecyclerView.ViewHolder {
        ImageView userAvatar, productImage;
        TextView customerNameTextView, orderStatusTextView, orderCodeTextView, orderDateTextView;
        TextView totalAmountTextView, productNameTextView, productVariantTextView;
        TextView productPriceTextView, productQuantityTextView, totalProductsTextView;
        Button actionButton1, actionButton2;

        public OrderAgencyViewHolder(@NonNull View itemView) {
            super(itemView);
            userAvatar = itemView.findViewById(R.id.userAvatar);
            customerNameTextView = itemView.findViewById(R.id.customerNameTextView);
            orderStatusTextView = itemView.findViewById(R.id.orderStatusTextView);
            orderCodeTextView = itemView.findViewById(R.id.orderCodeTextView);
            orderDateTextView = itemView.findViewById(R.id.orderDateTextView);
            totalAmountTextView = itemView.findViewById(R.id.totalAmountTextView);
            actionButton1 = itemView.findViewById(R.id.actionButton1);
            actionButton2 = itemView.findViewById(R.id.actionButton2);
            productImage = itemView.findViewById(R.id.productImage);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            productVariantTextView = itemView.findViewById(R.id.productVariantTextView);
            productPriceTextView = itemView.findViewById(R.id.productPriceTextView);
            productQuantityTextView = itemView.findViewById(R.id.productQuantityTextView);
            totalProductsTextView = itemView.findViewById(R.id.totalProductsTextView);
        }
    }
}