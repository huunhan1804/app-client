package com.example.dietarysupplementshop;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietarysupplementshop.adapter.OrderAdapter;
import com.example.dietarysupplementshop.model.Order;
import com.example.dietarysupplementshop.viewModel.AccountViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderedFragment extends Fragment implements OrderAdapter.OrderActionListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AccountViewModel accountViewModel;


    private RecyclerView rcv_orders;

    private TextView totalOrderText;

    private OrderAdapter orderAdapter;

    public OrderedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderedFragment newInstance(String param1, String param2) {
        OrderedFragment fragment = new OrderedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        accountViewModel = MyApplication.getInstance().getAccountViewModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ordered, container, false);
        rcv_orders = view.findViewById(R.id.rcv_orders);
        totalOrderText = view.findViewById(R.id.totalOrderText);

        accountViewModel.getOrderListResource().observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case SUCCESS:
                        ((HomepageActivity) getActivity()).hideProgressBar();
                        if (resource.getData() != null) {

                            orderAdapter = new OrderAdapter(resource.getData(),getContext(),this);
                            rcv_orders.setLayoutManager(new LinearLayoutManager(getContext()));
                            rcv_orders.setAdapter(orderAdapter);

                            // Update total order text
                            totalOrderText.setText("Total order: " + resource.getData().size() + " order");

                            // Show appropriate view based on order list size
                            if (resource.getData().size() == 0) {
                                view.findViewById(R.id.EmptyOrderItem).setVisibility(View.VISIBLE);
                                view.findViewById(R.id.HaveOrderItem).setVisibility(View.GONE);
                            } else {
                                view.findViewById(R.id.EmptyOrderItem).setVisibility(View.GONE);
                                view.findViewById(R.id.HaveOrderItem).setVisibility(View.VISIBLE);
                            }
                        }
                    case ERROR:
                        ((HomepageActivity) getActivity()).hideProgressBar();
                        break;
                    case LOADING:
                        ((HomepageActivity) getActivity()).showProgressBar();
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