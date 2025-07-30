package com.example.dietarysupplementshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietarysupplementshop.OrderDetailsActivity;
import com.example.dietarysupplementshop.R;
import com.example.dietarysupplementshop.constant.Validation; // Giả sử bạn có lớp này
import com.example.dietarysupplementshop.model.Order;
import com.example.dietarysupplementshop.responses.OrderDetailResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<Order> orderList;
    private Context context;
    private OrderActionListener orderActionListener;


    public interface OrderActionListener {
        void onCancelOrderClicked(Order order);
        void onReceivedOrderClicked(Order order);
        void onReturnRefundClicked(Order order);
    }

    public OrderAdapter(List<Order> orderList, Context context, OrderActionListener listener) {
        this.orderList = orderList;
        this.context = context;
        this.orderActionListener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        if (order == null) {
            return;
        }

        if (order.getOrder_detail() != null && !order.getOrder_detail().isEmpty()) {
            OrderDetailResponse firstProduct = order.getOrder_detail().get(0);

            String imageUrl = getImageUrlFromOrderDetail(firstProduct);
            if (imageUrl != null) {
                Picasso.get().load(imageUrl).placeholder(R.drawable.logo_2).into(holder.ivOrderProduct);
            } else {
                holder.ivOrderProduct.setImageResource(R.drawable.logo_2);
            }

            int totalItems = order.getOrder_detail().size();
            holder.tvOrderDetails.setText("Chi tiết: " + totalItems + (totalItems > 1 ? " sản phẩm" : " sản phẩm") + ", Tổng: " + order.getTotalBill());
        } else {
            holder.ivOrderProduct.setImageResource(R.drawable.logo_2); // Ảnh mặc định
            holder.tvOrderDetails.setText("Chi tiết: 0 sản phẩm, Tổng: " + order.getTotalBill());
        }

        holder.tvOrderId.setText("Mã đơn: #" + order.getOrder_id());
        if (order.getOrder_date() != null) {
            holder.tvOrderDate.setText("Ngày: " + Validation.formatDate(order.getOrder_date()));
        }
        holder.tvOrderStatus.setText("Trạng thái: " + order.getOrder_status());

        holder.cancelBtn.setVisibility(View.GONE);
        holder.receivedBtn.setVisibility(View.GONE);
        holder.returnRefundBtn.setVisibility(View.GONE);
        holder.viewDetailBtn.setVisibility(View.VISIBLE);

        String orderStatus = order.getOrder_status().toUpperCase();

        switch (orderStatus) {
            case "PENDING":
                holder.cancelBtn.setVisibility(View.VISIBLE);
                holder.cancelBtn.setEnabled(true);
                break;
            case "SHIPPING":
                holder.receivedBtn.setVisibility(View.VISIBLE);
                holder.receivedBtn.setEnabled(true);
                break;
            case "DELIVERED":
                holder.returnRefundBtn.setVisibility(View.VISIBLE);
                holder.returnRefundBtn.setEnabled(true);
                break;
            default:
                break;
        }

        holder.viewDetailBtn.setOnClickListener(view -> {
            Intent intent = new Intent(context, OrderDetailsActivity.class);
            intent.putExtra("orderId", order.getOrder_id());
            context.startActivity(intent);
        });

        holder.cancelBtn.setOnClickListener(view -> {
            if (orderActionListener != null) {
                orderActionListener.onCancelOrderClicked(order);
            }
        });

        holder.receivedBtn.setOnClickListener(view -> {
            if (orderActionListener != null) {
                orderActionListener.onReceivedOrderClicked(order);
            }
        });

        holder.returnRefundBtn.setOnClickListener(view -> {
            if (orderActionListener != null) {
                orderActionListener.onReturnRefundClicked(order);
            }
        });
    }

    private String getImageUrlFromOrderDetail(OrderDetailResponse orderDetail) {
        if (orderDetail.getProductVariantDTO() != null && orderDetail.getProductVariantDTO().getProduct_variant_image_url() != null) {
            return orderDetail.getProductVariantDTO().getProduct_variant_image_url();
        } else if (orderDetail.getProductInfoDTO() != null && orderDetail.getProductInfoDTO().getMedia_url() != null && !orderDetail.getProductInfoDTO().getMedia_url().isEmpty()) {
            return orderDetail.getProductInfoDTO().getMedia_url().get(0);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return orderList != null ? orderList.size() : 0;
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        ImageView ivOrderProduct;
        TextView tvOrderId, tvOrderDate, tvOrderStatus, tvOrderDetails;
        Button cancelBtn, viewDetailBtn, receivedBtn, returnRefundBtn; // Thêm các nút mới

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            ivOrderProduct = itemView.findViewById(R.id.iv_order_product);
            tvOrderId = itemView.findViewById(R.id.tv_order_id);
            tvOrderDate = itemView.findViewById(R.id.tv_order_date);
            tvOrderStatus = itemView.findViewById(R.id.tv_order_status);
            tvOrderDetails = itemView.findViewById(R.id.tv_order_details);

            cancelBtn = itemView.findViewById(R.id.btn_order_cancel);
            viewDetailBtn = itemView.findViewById(R.id.btn_order_view);
            receivedBtn = itemView.findViewById(R.id.btn_order_received);
            returnRefundBtn = itemView.findViewById(R.id.btn_order_return_refund);}
    }
}