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
import com.example.dietarysupplementshop.model.OrderDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderedFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private RecyclerView rcv_orders;

    private List<Order> orderList;

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
        orderList = new ArrayList<>();
        orderList.add(new Order(1,"23/02/2023", "1.400.000 đ", "PENDING_PAYMENT", "Name: Trần Quang Quí, Phone: 0945605514, Address: Ấp Phong Lưu, Xã Tân Hưng, Huyện Cái Nước, Tỉnh Cà Mau", getOrderDetailExample()));
        orderList.add(new Order(2,"23/02/2023", "1.400.000 đ", "PENDING_PAYMENT", "Name: Trần Quang Quí, Phone: 0945605514, Address: Ấp Phong Lưu, Xã Tân Hưng, Huyện Cái Nước, Tỉnh Cà Mau", getOrderDetailExample()));
        orderList.add(new Order(3,"23/02/2023", "1.400.000 đ", "PENDING_PAYMENT", "Name: Trần Quang Quí, Phone: 0945605514, Address: Ấp Phong Lưu, Xã Tân Hưng, Huyện Cái Nước, Tỉnh Cà Mau", getOrderDetailExample()));
        orderList.add(new Order(4,"23/02/2023", "1.400.000 đ", "PENDING_PAYMENT", "Name: Trần Quang Quí, Phone: 0945605514, Address: Ấp Phong Lưu, Xã Tân Hưng, Huyện Cái Nước, Tỉnh Cà Mau", getOrderDetailExample()));
        orderList.add(new Order(5,"23/02/2023", "1.400.000 đ", "PENDING_PAYMENT", "Name: Trần Quang Quí, Phone: 0945605514, Address: Ấp Phong Lưu, Xã Tân Hưng, Huyện Cái Nước, Tỉnh Cà Mau", getOrderDetailExample()));
        orderList.add(new Order(6,"23/02/2023", "1.400.000 đ", "PENDING_PAYMENT", "Name: Trần Quang Quí, Phone: 0945605514, Address: Ấp Phong Lưu, Xã Tân Hưng, Huyện Cái Nước, Tỉnh Cà Mau", getOrderDetailExample()));

    }

    public static List<OrderDetail> getOrderDetailExample(){
        List<OrderDetail> orderDetails = new ArrayList<>();
        orderDetails.add(new OrderDetail(1,1, "200.00 đ", "200.000 đ"));
        orderDetails.add(new OrderDetail(2,1, "200.00 đ", "200.000 đ"));
        orderDetails.add(new OrderDetail(3,1, "200.00 đ", "200.000 đ"));
        orderDetails.add(new OrderDetail(4,1, "200.00 đ", "200.000 đ"));
        orderDetails.add(new OrderDetail(5,1, "200.00 đ", "200.000 đ"));
        orderDetails.add(new OrderDetail(6,1, "200.00 đ", "200.000 đ"));
        orderDetails.add(new OrderDetail(7,1, "200.00 đ", "200.000 đ"));
        return orderDetails;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ordered, container, false);
        rcv_orders = view.findViewById(R.id.rcv_orders);
        totalOrderText = view.findViewById(R.id.totalOrderText);

        // Set up RecyclerView
        orderAdapter = new OrderAdapter(getContext(), orderList);
        rcv_orders.setLayoutManager(new LinearLayoutManager(getContext()));
        rcv_orders.setAdapter(orderAdapter);

        // Update total order text
        totalOrderText.setText("Total order: " + orderList.size() + " order");

        // Show appropriate view based on order list size
        if (orderList.size() == 0) {
            view.findViewById(R.id.EmptyOrderItem).setVisibility(View.VISIBLE);
            view.findViewById(R.id.HaveOrderItem).setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.EmptyOrderItem).setVisibility(View.GONE);
            view.findViewById(R.id.HaveOrderItem).setVisibility(View.VISIBLE);
        }
        return view;
    }
}