package com.example.dietarysupplementshop.adapter;

import android.app.AlertDialog;
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
import com.example.dietarysupplementshop.responses.ProductInfoDTO;
import com.example.dietarysupplementshop.viewModel.AgencyProductViewModel;

import java.util.List;

public class ProductAgencyAdapter extends RecyclerView.Adapter<ProductAgencyAdapter.ProductAgencyViewHolder> {

    private final Context context;
    private final List<ProductInfoDTO> productList;
    private final OnProductActionListener listener;
    private final AgencyProductViewModel viewModel;

    public interface OnProductActionListener {
        void onEditProduct(ProductInfoDTO product);
        void onDeleteProduct(ProductInfoDTO product);
    }

    public ProductAgencyAdapter(Context context, List<ProductInfoDTO> productList, OnProductActionListener listener, AgencyProductViewModel viewModel) {
        this.context = context;
        this.productList = productList;
        this.listener = listener;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public ProductAgencyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_agency_product, parent, false);
        return new ProductAgencyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAgencyViewHolder holder, int position) {
        ProductInfoDTO product = productList.get(position);

        if (product.getMedia_url() != null && !product.getMedia_url().isEmpty()) {
            Glide.with(context).load(product.getMedia_url().get(0)).into(holder.productImageView);
        } else {
            Glide.with(context).load(R.drawable.product_image).into(holder.productImageView);
        }

        holder.productNameTextView.setText(product.getProduct_name());
        holder.productPriceTextView.setText(product.getProduct_list_price());
        holder.tvStockQuantity.setText(String.valueOf(product.getQuantity_in_stock()));
        holder.tvSold.setText(String.valueOf(product.getSoldAmount()));
        holder.tvRecentStatus.setText(product.getApprovalStatus().getStatusCode());

        // Nút Sửa
        holder.btnActionEdit.setOnClickListener(v -> listener.onEditProduct(product));

        // Nút Thêm Tùy Chọn
        holder.btnActionMore.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Tùy chọn");

            String hideUnHideOption = product.getApprovalStatus().getStatusCode().equals("HIDDEN")
                    ? "Hiện sản phẩm"
                    : "Ẩn sản phẩm";

            String[] options = {hideUnHideOption, "Xóa sản phẩm"};

            builder.setItems(options, (dialog, which) -> {
                if (which == 0) {

                    String newStatus = product.getApprovalStatus().getStatusCode().equals("HIDDEN")
                            ? "PENDING" // Khi hiện -> chuyển về chờ duyệt
                            : "HIDDEN"; // Khi ẩn

                    viewModel.updateProductStatus(product.getProduct_id(), newStatus);
                }
                else if (which == 1) {
                    showDeleteConfirmationDialog(product);
                }
            });
            builder.show();
        });

        holder.itemView.setOnClickListener(v -> Toast.makeText(context, "Chuyển đến chi tiết sản phẩm: " + product.getProduct_name(), Toast.LENGTH_SHORT).show());
    }

    private void showDeleteConfirmationDialog(ProductInfoDTO product) {
        new AlertDialog.Builder(context)
                .setTitle("Xóa sản phẩm")
                .setMessage("Bạn có chắc chắn muốn xóa sản phẩm này không? Thao tác này không thể hoàn tác.")
                .setPositiveButton("Xóa", (d, id) -> listener.onDeleteProduct(product))
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // ViewHolder class
    static class ProductAgencyViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageView;
        TextView productNameTextView, productPriceTextView, tvStockQuantity, tvSold, tvRecentStatus;
        Button btnActionEdit, btnActionMore;

        public ProductAgencyViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.productImageView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            productPriceTextView = itemView.findViewById(R.id.productPriceTextView);
            tvStockQuantity = itemView.findViewById(R.id.tvStockQuantity);
            tvSold = itemView.findViewById(R.id.tvSold);
            tvRecentStatus = itemView.findViewById(R.id.tvRecentStatus);
            btnActionEdit = itemView.findViewById(R.id.btnActionEdit);
            btnActionMore = itemView.findViewById(R.id.btnActionMore);
        }
    }
}