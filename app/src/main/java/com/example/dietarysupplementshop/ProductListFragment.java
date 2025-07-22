
package com.example.dietarysupplementshop;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dietarysupplementshop.adapter.ProductSellerAdapter;
import com.example.dietarysupplementshop.model.ProductSeller;

import java.util.ArrayList;
import java.util.List;

public class ProductListFragment extends Fragment implements ProductSellerAdapter.OnProductActionListener {

    private static final String ARG_PRODUCT_STATUS = "product_status";
    private String productStatus;
    private RecyclerView recyclerView;
    private ProductSellerAdapter adapter;
    private List<ProductSeller> productList = new ArrayList<>();

    public ProductListFragment() {
    }

    public static ProductListFragment newInstance(String productStatus) {
        ProductListFragment fragment = new ProductListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PRODUCT_STATUS, productStatus);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productStatus = getArguments().getString(ARG_PRODUCT_STATUS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProductSellerAdapter(getContext(), productList, this); // Pass this as listener
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void updateProductList(List<ProductSeller> newProducts) {
        productList.clear();
        productList.addAll(newProducts);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onEditProduct(ProductSeller product) {
        Intent intent = new Intent(getActivity(), SellerAddProductActivity.class);
        intent.putExtra("product_to_edit", product);
        startActivity(intent);
    }

    @Override
    public void onHideProduct(ProductSeller product) {
        if (getActivity() instanceof SellerProductActivity) {
            ((SellerProductActivity) getActivity()).onHideProduct(product);
        }
        Toast.makeText(getContext(), "Đã yêu cầu ẩn sản phẩm: " + product.getProductName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUnHideProduct(ProductSeller product) {
        if (getActivity() instanceof SellerProductActivity) {
            ((SellerProductActivity) getActivity()).onUnHideProduct(product);
        }
        Toast.makeText(getContext(), "Đã yêu cầu hiển thị sản phẩm: " + product.getProductName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteProduct(ProductSeller product) {
        if (getActivity() instanceof SellerProductActivity) {
            ((SellerProductActivity) getActivity()).onDeleteProduct(product);
        }
        Toast.makeText(getContext(), "Đã yêu cầu xóa sản phẩm: " + product.getProductName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onViewProductDetail(ProductSeller product) {
        Toast.makeText(getContext(), "Xem chi tiết sản phẩm: " + product.getProductName(), Toast.LENGTH_SHORT).show();
        // Intent intent = new Intent(getActivity(), ProductInfoActivity.class);
        // intent.putExtra("product_id", product.getProductId());
        // startActivity(intent);
    }
}