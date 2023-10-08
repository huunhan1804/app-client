package com.example.dietarysupplementshop;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.dietarysupplementshop.adapter.Product2Adapter;
import com.example.dietarysupplementshop.adapter.ProductAdapter;
import com.example.dietarysupplementshop.model.Product;
import com.google.android.gms.common.logging.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SeachResultProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SeachResultProductFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ImageButton horizontalButton;
    private ImageButton menuButton;

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private Product2Adapter product2Adapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SeachResultProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SeachResultProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SeachResultProductFragment newInstance(String param1, String param2) {
        SeachResultProductFragment fragment = new SeachResultProductFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_seach_result_product, container, false);
        horizontalButton = view.findViewById(R.id.iconHamburger);
        menuButton = view.findViewById(R.id.iconMenu);
        recyclerView = view.findViewById(R.id.productRecyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        productAdapter = new ProductAdapter(getListProduct());
        recyclerView.setAdapter(productAdapter);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setLayoutManager(gridLayoutManager);
                recyclerView.setAdapter(productAdapter);
                productAdapter.notifyDataSetChanged();
            }
        });

        horizontalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                product2Adapter = new Product2Adapter(getListProduct());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(product2Adapter);
                product2Adapter.notifyDataSetChanged(); // Cập nhật giao diện sau khi thay đổi Adapter
            }
        });

        return view;
    }

    private List<Product> getListProduct(){
        List<Product> productList = new ArrayList<>();
        productList.add(new Product(1, "https://dl.dropbox.com/s/t0tjm1ase3p9uj0/OIP.jpg?dl=0", "Cốm Tăng Cân Bạch Mai", "250.000 ₫", 0));
        productList.add(new Product(2, "https://dl.dropbox.com/s/t0tjm1ase3p9uj0/OIP.jpg?dl=0", "Cốm Tăng Cân Bạch Mai", "250.000 ₫", 0));
        productList.add(new Product(3, "https://dl.dropbox.com/s/t0tjm1ase3p9uj0/OIP.jpg?dl=0", "Cốm Tăng Cân Bạch Mai", "250.000 ₫", 0));
        productList.add(new Product(4, "https://dl.dropbox.com/s/t0tjm1ase3p9uj0/OIP.jpg?dl=0", "Cốm Tăng Cân Bạch Mai", "250.000 ₫", 0));
        productList.add(new Product(5, "https://dl.dropbox.com/s/t0tjm1ase3p9uj0/OIP.jpg?dl=0", "Cốm Tăng Cân Bạch Mai", "250.000 ₫", 0));
        productList.add(new Product(6, "https://dl.dropbox.com/s/t0tjm1ase3p9uj0/OIP.jpg?dl=0", "Cốm Tăng Cân Bạch Mai", "250.000 ₫", 0));
        return productList;
    }

    public void receiveSearchText(String searchText) {
        Log.d("MyTag", "Received search text: " + searchText);
    }

}