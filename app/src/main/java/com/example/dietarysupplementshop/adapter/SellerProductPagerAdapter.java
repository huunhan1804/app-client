package com.example.dietarysupplementshop.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.dietarysupplementshop.ProductListFragment;

import java.util.ArrayList;
import java.util.List;

public class SellerProductPagerAdapter extends FragmentStateAdapter {

    private final List<String> tabTitles = new ArrayList<>();

    public SellerProductPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        tabTitles.add("Còn hàng");
        tabTitles.add("Hết hàng");
        tabTitles.add("Chờ duyệt");
        tabTitles.add("Vi phạm");
        tabTitles.add("Ẩn");
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        String status = tabTitles.get(position);
        return ProductListFragment.newInstance(status);
    }

    @Override
    public int getItemCount() {
        return tabTitles.size();
    }

    public String getTabTitle(int position) {
        return tabTitles.get(position);
    }
}