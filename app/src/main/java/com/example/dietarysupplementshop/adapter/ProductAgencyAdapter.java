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
import com.example.dietarysupplementshop.responses.ApprovalStatusDTO;
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
        if (product == null) {
            return;
        }

        // 1. Hiển thị ảnh sản phẩm
        if (product.getMedia_url() != null && !product.getMedia_url().isEmpty()) {
            Glide.with(context).load(product.getMedia_url().get(0)).into(holder.productImageView);
        } else {
            Glide.with(context).load(R.drawable.product_image).into(holder.productImageView);
        }

        // 2. Hiển thị thông tin sản phẩm
        holder.productNameTextView.setText(product.getProduct_name());

        // Lấy giá bán và giá khuyến mãi của sản phẩm
        String productPrice = product.getProduct_price();

        // Hiển thị giá: ưu tiên giá khuyến mãi nếu có
        if (productPrice != null && !productPrice.isEmpty() && !productPrice.equals("0 ₫")) {
            holder.productPriceTextView.setText(productPrice);
        } else {
            holder.productPriceTextView.setText("Chưa cập nhật giá");
        }

        holder.tvStockQuantity.setText(String.valueOf(product.getQuantity_in_stock()));
        holder.tvSold.setText(String.valueOf(product.getSold_amount()));

        // 3. Xử lý trạng thái sản phẩm
        String approvalStatusCode = null;
        String approvalStatusName = "Không rõ";

        ApprovalStatusDTO statusDto = product.getApproval_status();
        if (statusDto != null) {
            if (statusDto.getStatusCode() != null) {
                approvalStatusCode = statusDto.getStatusCode();
            }
            if (statusDto.getStatusName() != null) {
                approvalStatusName = statusDto.getStatusName();
            }
        }

        holder.tvRecentStatus.setText(approvalStatusName);

        // 4. Ẩn/hiện nút Sửa và Xóa dựa trên mã trạng thái
        if ("APPROVED".equals(approvalStatusCode) || "PENDING".equals(approvalStatusCode)) {
            holder.btnActionEdit.setVisibility(View.VISIBLE);
            holder.btnActionDelete.setVisibility(View.VISIBLE);
        } else if ("REJECTED".equals(approvalStatusCode)) {
            holder.btnActionEdit.setVisibility(View.VISIBLE);
            holder.btnActionDelete.setVisibility(View.VISIBLE);
        } else {
            holder.btnActionEdit.setVisibility(View.GONE);
            holder.btnActionDelete.setVisibility(View.GONE);
        }

        // 5. Sự kiện click
        holder.itemView.setOnClickListener(v ->
                Toast.makeText(context, "Chi tiết: " + product.getProduct_name(), Toast.LENGTH_SHORT).show()
        );
        holder.btnActionEdit.setOnClickListener(v -> listener.onEditProduct(product));
        holder.btnActionDelete.setOnClickListener(v -> showDeleteConfirmationDialog(product));
    }

    private void showDeleteConfirmationDialog(ProductInfoDTO product) {
        new AlertDialog.Builder(context)
                .setTitle("Xóa sản phẩm")
                .setMessage("Bạn có chắc chắn muốn xóa sản phẩm này không?")
                .setPositiveButton("Xóa", (d, id) -> listener.onDeleteProduct(product))
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductAgencyViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageView;
        TextView productNameTextView, productPriceTextView, tvStockQuantity, tvSold, tvRecentStatus;
        Button btnActionEdit, btnActionDelete;

        public ProductAgencyViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.productImageView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            productPriceTextView = itemView.findViewById(R.id.productPriceTextView);
            tvStockQuantity = itemView.findViewById(R.id.tvStockQuantity);
            tvSold = itemView.findViewById(R.id.tvSold);
            tvRecentStatus = itemView.findViewById(R.id.tvRecentStatus);
            btnActionEdit = itemView.findViewById(R.id.btnActionEdit);
            btnActionDelete = itemView.findViewById(R.id.btnActionDelete);
        }
    }
}