package com.example.dietarysupplementshop;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.dietarysupplementshop.adapter.CategoryAdapter;
import com.example.dietarysupplementshop.adapter.ProductAdapter;
import com.example.dietarysupplementshop.model.Category;
import com.example.dietarysupplementshop.model.Product;

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

    private List<Category> mCategoryList;

    private CategoryAdapter categoryAdapter;

    private ImageSlider imageSlider;


    private RecyclerView rcvBestSeller;
    private RecyclerView rcvBestOrder;
    private List<Product> mBestSellerList;
    private List<Product> mBestOrderList;
    private ProductAdapter productBestSellerAdapter;
    private ProductAdapter productBestOrderAdapter;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        // Khởi tạo RecyclerView và danh sách danh mục
        rcvCategory = view.findViewById(R.id.categoryRecyclerView);


        mCategoryList = new ArrayList<>();
        mCategoryList.add(new Category("All", R.drawable.category_all));
        mCategoryList.add(new Category("Liver", R.drawable.image_4));
        mCategoryList.add(new Category("Eyes", R.drawable.category_eyes));
        mCategoryList.add(new Category("Sleep", R.drawable.category_sleep));
        mCategoryList.add(new Category("Weight", R.drawable.weight));
        mCategoryList.add(new Category("Digestion", R.drawable.digestive_icon));
        mCategoryList.add(new Category("High Blood", R.drawable.high_blood));
        mCategoryList.add(new Category("Resistance", R.drawable.resistance_1));

        // Khởi tạo và thiết lập Adapter cho RecyclerView
        categoryAdapter = new CategoryAdapter(mCategoryList);
        rcvCategory.setAdapter(categoryAdapter);

        // Khởi tạo ImageSlider và danh sách hình ảnh trình chiếu
        imageSlider = view.findViewById(R.id.imageSlider);
        ArrayList<SlideModel> slideModels = new ArrayList<SlideModel>();
        slideModels.add(new SlideModel(R.drawable.slide_1, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slide_2, ScaleTypes.FIT));

        // Thiết lập danh sách hình ảnh trình chiếu cho ImageSlider
        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

        rcvBestSeller = view.findViewById(R.id.bestSellerRecyclerView);
        rcvBestOrder = view.findViewById(R.id.bestOrderRecyclerView);
        mBestSellerList = new ArrayList<>();
        mBestSellerList.add(new Product(1, "https://dl.dropbox.com/s/t0tjm1ase3p9uj0/OIP.jpg?dl=0", "Cốm Tăng Cân Bạch Mai", "250.000 ₫", 0));
        mBestSellerList.add(new Product(2, "https://dl.dropbox.com/s/t0tjm1ase3p9uj0/OIP.jpg?dl=0", "Cốm Tăng Cân Bạch Mai", "250.000 ₫", 0));
        mBestSellerList.add(new Product(3, "https://dl.dropbox.com/s/t0tjm1ase3p9uj0/OIP.jpg?dl=0", "Cốm Tăng Cân Bạch Mai", "250.000 ₫", 0));
        mBestSellerList.add(new Product(4, "https://dl.dropbox.com/s/t0tjm1ase3p9uj0/OIP.jpg?dl=0", "Cốm Tăng Cân Bạch Mai", "250.000 ₫", 0));
        mBestSellerList.add(new Product(5, "https://dl.dropbox.com/s/t0tjm1ase3p9uj0/OIP.jpg?dl=0", "Cốm Tăng Cân Bạch Mai", "250.000 ₫", 0));
        mBestSellerList.add(new Product(6, "https://dl.dropbox.com/s/t0tjm1ase3p9uj0/OIP.jpg?dl=0", "Cốm Tăng Cân Bạch Mai", "250.000 ₫", 0));

        productBestSellerAdapter = new ProductAdapter( mBestSellerList, requireContext());
        rcvBestSeller.setAdapter(productBestSellerAdapter);

        mBestOrderList = new ArrayList<>();
        mBestOrderList.add(new Product(1, "https://dl.dropbox.com/s/t0tjm1ase3p9uj0/OIP.jpg?dl=0", "Cốm Tăng Cân Bạch Mai", "250.000 ₫", 0));
        mBestOrderList.add(new Product(2, "https://dl.dropbox.com/s/t0tjm1ase3p9uj0/OIP.jpg?dl=0", "Cốm Tăng Cân Bạch Mai", "250.000 ₫", 0));
        mBestOrderList.add(new Product(3, "https://dl.dropbox.com/s/t0tjm1ase3p9uj0/OIP.jpg?dl=0", "Cốm Tăng Cân Bạch Mai", "250.000 ₫", 0));
        mBestOrderList.add(new Product(4, "https://dl.dropbox.com/s/t0tjm1ase3p9uj0/OIP.jpg?dl=0", "Cốm Tăng Cân Bạch Mai", "250.000 ₫", 0));
        mBestOrderList.add(new Product(5, "https://dl.dropbox.com/s/t0tjm1ase3p9uj0/OIP.jpg?dl=0", "Cốm Tăng Cân Bạch Mai", "250.000 ₫", 0));
        mBestOrderList.add(new Product(6, "https://dl.dropbox.com/s/t0tjm1ase3p9uj0/OIP.jpg?dl=0", "Cốm Tăng Cân Bạch Mai", "250.000 ₫", 0));

        productBestOrderAdapter = new ProductAdapter(mBestOrderList, requireContext());
        rcvBestOrder.setAdapter(productBestOrderAdapter);



        return view;
    }
}