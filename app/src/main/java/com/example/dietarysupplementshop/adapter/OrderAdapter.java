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

import com.example.dietarysupplementshop.MyApplication;
import com.example.dietarysupplementshop.OrderDetailsActivity;
import com.example.dietarysupplementshop.R;
import com.example.dietarysupplementshop.constant.Validation;
import com.example.dietarysupplementshop.model.Order;
import com.example.dietarysupplementshop.responses.OrderDetailResponse;
import com.example.dietarysupplementshop.viewModel.AccountViewModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<Order> orderList;
    private Context context;
    private OrderActionListener orderActionListener;

    public OrderAdapter(List<Order> orderList, Context context, OrderActionListener listener) {
        this.orderList = orderList;
        this.context = context;
        this.orderActionListener = listener;
    }


    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderAdapter.OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        if (order != null) {
            OrderDetailResponse orderDetail = order.getOrder_detail().get(0);

            if (orderDetail.getProductVariantDTO().getProduct_variant_image_url() != null) {
                Picasso.get()
                        .load(orderDetail.getProductVariantDTO().getProduct_variant_image_url())
                        .into(holder.ivOrderProduct);
            } else {
                for (String image : orderDetail.getProductInfoDTO().getMedia_url()) {
                    Picasso.get()
                            .load(image)
                            .into(holder.ivOrderProduct);
                }
            }

            holder.tvOrderId.setText("Order ID: #" + order.getOrder_id());

            holder.tvOrderDate.setText("Date: " + Validation.formatDate(order.getOrder_date()));

            holder.tvOrderStatus.setText("Status: " + order.getOrder_status());
            holder.tvOrderDetails.setText("Details: " + (order.getOrder_detail().size() > 1 ? order.getOrder_detail().size() + " items" : "1 item") + ", Total: " + order.getTotalBill());

            if (!order.getOrder_status().equals("SHIPPING")) {
                holder.cancelBtn.setVisibility(View.GONE);
            } else {
                holder.cancelBtn.setEnabled(true);
            }

            holder.viewDetailBtn.setOnClickListener(view -> {
                Intent intent = new Intent(context, OrderDetailsActivity.class);
                intent.putExtra("orderId", order.getOrder_id());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            });

            holder.cancelBtn.setOnClickListener(view -> {
                if (orderActionListener != null) {
                    orderActionListener.onCancelOrderClicked(order);
                }
            });


        }
    }

    @Override
    public int getItemCount() {
        if (orderList != null) {
            return orderList.size();
        }
        return 0;
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        ImageView ivOrderProduct;
        TextView tvOrderId, tvOrderDate, tvOrderStatus, tvOrderDetails;

        Button cancelBtn, viewDetailBtn;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            ivOrderProduct = itemView.findViewById(R.id.iv_order_product);
            tvOrderId = itemView.findViewById(R.id.tv_order_id);
            tvOrderDate = itemView.findViewById(R.id.tv_order_date);
            tvOrderStatus = itemView.findViewById(R.id.tv_order_status);
            tvOrderDetails = itemView.findViewById(R.id.tv_order_details);
            cancelBtn = itemView.findViewById(R.id.btn_order_cancel);
            viewDetailBtn = itemView.findViewById(R.id.btn_order_view);

        }
    }

    public interface OrderActionListener {
        void onCancelOrderClicked(Order order);
    }

}
