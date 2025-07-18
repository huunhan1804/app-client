package com.example.dietarysupplementshop;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietarysupplementshop.adapter.OrderSellerAdapter;
import com.example.dietarysupplementshop.model.Order;
import com.example.dietarysupplementshop.viewModel.SellerViewModel;

import java.util.ArrayList;

public class SellerOrderFragment extends Fragment implements OrderSellerAdapter.OnOrderActionButtonClickListener {

    private static final String ARG_STATUS = "status";
    private String status;
    private SellerViewModel sellerViewModel;
    private RecyclerView recyclerView;
    private OrderSellerAdapter adapter;
    private ProgressBar progressBar;
    private TextView emptyTextView;

    public static SellerOrderFragment newInstance(String status) {
        SellerOrderFragment fragment = new SellerOrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_STATUS, status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            status = getArguments().getString(ARG_STATUS);
        }
        sellerViewModel = new ViewModelProvider(this).get(SellerViewModel.class);
    }

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewOrders);
        emptyTextView = view.findViewById(R.id.emptyStateTextView);
        progressBar = view.findViewById(R.id.progressBar);

        setupRecyclerView();
        observeViewModel();

        return view;
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OrderSellerAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);
    }

    private void observeViewModel() {
        sellerViewModel.getSellerOrders(status).observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case LOADING:
                        progressBar.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        emptyTextView.setVisibility(View.GONE);
                        break;
                    case SUCCESS:
                        progressBar.setVisibility(View.GONE);
                        if (resource.getData() != null && !resource.getData().isEmpty()) {
                            recyclerView.setVisibility(View.VISIBLE);
                            emptyTextView.setVisibility(View.GONE);
                            adapter.setOrderList(resource.getData());
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            emptyTextView.setVisibility(View.VISIBLE);
                            emptyTextView.setText("Không có đơn hàng nào.");
                        }
                        break;
                    case ERROR:
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        emptyTextView.setVisibility(View.VISIBLE);
                        emptyTextView.setText("Lỗi: " + resource.getMessage());
                        Toast.makeText(getContext(), "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    @Override
    public void onActionButton1Click(Order order, int position) {
        Toast.makeText(getContext(), "Xử lý nút 1 cho đơn: " + order.getOrder_id(), Toast.LENGTH_SHORT).show();
        if ("PENDING".equals(order.getOrder_status())) {
            // sellerViewModel.updateOrderStatus(order.getOrder_id(), "CONFIRMED");
        }
    }

    @Override
    public void onActionButton2Click(Order order, int position) {
        Toast.makeText(getContext(), "Xử lý nút 2 cho đơn: " + order.getOrder_id(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(Order order, int position) {
        Toast.makeText(getContext(), "Xem chi tiết đơn: " + order.getOrder_id(), Toast.LENGTH_SHORT).show();
        // Intent intent = new Intent(getActivity(), SellerOrderDetailActivity.class);
        // intent.putExtra("orderId", order.getOrder_id());
        // startActivity(intent);
    }
}