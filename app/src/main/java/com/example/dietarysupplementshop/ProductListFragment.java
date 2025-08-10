package com.example.dietarysupplementshop;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.dietarysupplementshop.adapter.ProductAgencyAdapter;
import com.example.dietarysupplementshop.responses.ProductInfoDTO;
import com.example.dietarysupplementshop.viewModel.AgencyProductViewModel;
import java.util.ArrayList;
import java.util.List;

public class ProductListFragment extends Fragment implements ProductAgencyAdapter.OnProductActionListener {

    private static final String ARG_STATUS_CODE = "status_code";
    private String statusCode;
    private RecyclerView recyclerView;
    private ProductAgencyAdapter adapter;
    private List<ProductInfoDTO> productList = new ArrayList<>();
    private AgencyProductViewModel viewModel;

    public static ProductListFragment newInstance(String statusCode) {
        ProductListFragment fragment = new ProductListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_STATUS_CODE, statusCode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            statusCode = getArguments().getString(ARG_STATUS_CODE);
        }
        viewModel = new ViewModelProvider(requireActivity()).get(AgencyProductViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProductAgencyAdapter(getContext(), productList, this, viewModel);
        recyclerView.setAdapter(adapter);

        // Lắng nghe LiveData dựa trên statusCode của fragment
        if ("APPROVED".equals(statusCode)) {
            viewModel.approvedProducts.observe(getViewLifecycleOwner(), this::updateProductList);
        } else if ("PENDING".equals(statusCode)) {
            viewModel.pendingProducts.observe(getViewLifecycleOwner(), this::updateProductList);
        } else if ("REJECTED".equals(statusCode)) {
            viewModel.rejectedProducts.observe(getViewLifecycleOwner(), this::updateProductList);
        }

        return view;
    }

    public void updateProductList(List<ProductInfoDTO> products) {
        productList.clear();
        if (products != null) {
            productList.addAll(products);
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onEditProduct(ProductInfoDTO product) {
        // Gọi phương thức từ Activity để xử lý việc chuyển màn hình
        ((ProductAgencyAdapter.OnProductActionListener) requireActivity()).onEditProduct(product);
    }

    @Override
    public void onDeleteProduct(ProductInfoDTO product) {
        // Gọi phương thức từ Activity để xử lý việc xóa
        ((ProductAgencyAdapter.OnProductActionListener) requireActivity()).onDeleteProduct(product);
    }
}