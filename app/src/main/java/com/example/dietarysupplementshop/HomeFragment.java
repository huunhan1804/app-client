package com.example.dietarysupplementshop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.dietarysupplementshop.adapter.CategoryAdapter;
import com.example.dietarysupplementshop.adapter.FakeProductAdapter;
import com.example.dietarysupplementshop.adapter.ProductAdapter;
import com.example.dietarysupplementshop.model.Category;
import com.example.dietarysupplementshop.model.Product;
import com.example.dietarysupplementshop.viewModel.ProductViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
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
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
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