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
        tabTitles.add("Active");          // "Còn hàng"
        tabTitles.add("Out of Stock");    // "Hết hàng"
        tabTitles.add("Pending Approval");// "Chờ duyệt"
        tabTitles.add("Violated");        // "Vi phạm" (or "Rejected")
        tabTitles.add("Hidden");          // "Ẩn"
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