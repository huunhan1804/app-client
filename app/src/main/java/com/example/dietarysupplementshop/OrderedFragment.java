package com.example.dietarysupplementshop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietarysupplementshop.adapter.OrderAdapter;
import com.example.dietarysupplementshop.model.Order;
import com.example.dietarysupplementshop.viewModel.AccountViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class OrderedFragment extends Fragment implements OrderAdapter.OrderActionListener {

    private static final String ARG_STATUS = "status";
    private static final String ARG_PARAM2 = "param2";

    private String status;
    private String mParam2;

    private AccountViewModel accountViewModel;
    private RecyclerView rcv_orders;
    private TextView totalOrderText;
    private OrderAdapter orderAdapter;

    public OrderedFragment() {
    }

    public static OrderedFragment newInstance(String status, String param2) {
        OrderedFragment fragment = new OrderedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_STATUS, status);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            status = getArguments().getString(ARG_STATUS);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        accountViewModel = MyApplication.getInstance().getAccountViewModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ordered, container, false);
        rcv_orders = view.findViewById(R.id.rcv_orders);
        totalOrderText = view.findViewById(R.id.totalOrderText);

        accountViewModel.getOrderListResource().observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                if (getActivity() instanceof HomepageActivity) {
                    ((HomepageActivity) getActivity()).hideProgressBar();
                }
                switch (resource.getStatus()) {
                    case SUCCESS:
                        if (resource.getData() != null) {
                            List<Order> allOrders = resource.getData();
                            List<Order> filteredOrders;
                            if (status != null && !status.equals("ALL")) {
                                filteredOrders = allOrders.stream()
                                        .filter(order -> status.equalsIgnoreCase(order.getOrder_status()))
                                        .collect(Collectors.toList());
                            } else {
                                filteredOrders = allOrders;
                            }

                            orderAdapter = new OrderAdapter(filteredOrders, getContext(), this);
                            rcv_orders.setLayoutManager(new LinearLayoutManager(getContext()));
                            rcv_orders.setAdapter(orderAdapter);

                            totalOrderText.setText("Total order: " + filteredOrders.size() + " order");

                            if (filteredOrders.isEmpty()) {
                                view.findViewById(R.id.EmptyOrderItem).setVisibility(View.VISIBLE);
                                view.findViewById(R.id.HaveOrderItem).setVisibility(View.GONE);
                            } else {
                                view.findViewById(R.id.EmptyOrderItem).setVisibility(View.GONE);
                                view.findViewById(R.id.HaveOrderItem).setVisibility(View.VISIBLE);
                            }
                        }
                        break;
                    case ERROR:
                        break;
                    case LOADING:
                        if (getActivity() instanceof HomepageActivity) {
                            ((HomepageActivity) getActivity()).showProgressBar();
                        }
                        break;
                }
            }
        });
        return view;
    }

    @Override
    public void onCancelOrderClicked(Order order) {
        showDialogConfirmCancelOrder(order);
    }

    private void showDialogConfirmCancelOrder(Order order) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("Confirm Cancel Order");
        builder.setMessage("Are you sure you want to cancel this order?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            accountViewModel.cancelOrder(order.getOrder_id());
        });
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
}