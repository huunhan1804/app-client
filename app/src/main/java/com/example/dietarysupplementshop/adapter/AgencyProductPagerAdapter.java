package com.example.dietarysupplementshop.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.dietarysupplementshop.ProductListFragment;

public class AgencyProductPagerAdapter extends FragmentStateAdapter {

    private final String[] tabTitles = {"Còn hàng", "Hết hàng", "Chờ duyệt", "Vi phạm", "Ẩn"};
    private final String[] statusCodes = {"APPROVED", "OUT_OF_STOCK", "PENDING", "REJECTED", "HIDDEN"};

    public AgencyProductPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return ProductListFragment.newInstance(statusCodes[position]);
    }

    @Override
    public int getItemCount() {
        return tabTitles.length;
    }

    public String getTabTitle(int position) {
        return tabTitles[position];
    }

    public String getStatusCode(int position) {
        return statusCodes[position];
    }
}