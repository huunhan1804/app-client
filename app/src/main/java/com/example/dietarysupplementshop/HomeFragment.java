package com.example.dietarysupplementshop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.dietarysupplementshop.MyApplication;
import com.example.dietarysupplementshop.R;
import com.example.dietarysupplementshop.adapter.CategoryAdapter;
import com.example.dietarysupplementshop.adapter.FakeProductAdapter;
import com.example.dietarysupplementshop.adapter.ProductAdapter;
import com.example.dietarysupplementshop.model.Category;
import com.example.dietarysupplementshop.model.Product;
import com.example.dietarysupplementshop.viewModel.ProductViewModel;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView rcvCategory;
    private RecyclerView rcvBestSeller;
    private RecyclerView rcvBestOrder;

    private List<Category> mCategoryList;
    private List<Product> mBestSellerList;
    private List<Product> mBestOrderList;

    private CategoryAdapter categoryAdapter;
    private ProductAdapter productBestSellerAdapter;
    private ProductAdapter productBestOrderAdapter;

    private ProductViewModel productViewModel;


    private ImageSlider imageSlider;
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        productViewModel = MyApplication.getInstance().getProductViewModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        rcvCategory = view.findViewById(R.id.categoryRecyclerView);
        rcvBestSeller = view.findViewById(R.id.bestSellerRecyclerView);
        rcvBestOrder = view.findViewById(R.id.bestOrderRecyclerView);

        imageSlider = view.findViewById(R.id.imageSlider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.slide_1, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slide_2, ScaleTypes.FIT));
        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

        FakeProductAdapter fakeProductAdapter = new FakeProductAdapter(productViewModel.createFakeProducts(10));
        rcvBestSeller.setAdapter(fakeProductAdapter);
        rcvBestOrder.setAdapter(fakeProductAdapter);

        productViewModel.getBestSellers().observe(getViewLifecycleOwner(), products -> {
            mBestSellerList = products;
            ProductAdapter productAdapter = new ProductAdapter(mBestSellerList, requireContext());
            rcvBestSeller.setAdapter(productAdapter);
        });

        productViewModel.getBestOrders().observe(getViewLifecycleOwner(), products -> {
            mBestOrderList = products;
            productBestOrderAdapter = new ProductAdapter(mBestOrderList, requireContext());
            rcvBestOrder.setAdapter(productBestOrderAdapter);
        });


        productViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            mCategoryList = categories;
            categoryAdapter = new CategoryAdapter(mCategoryList, getContext());
            rcvCategory.setAdapter(categoryAdapter);
        });

        return view;
    }

}