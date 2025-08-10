package com.example.dietarysupplementshop.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.dietarysupplementshop.ProductListFragment;

public class AgencyProductPagerAdapter extends FragmentStateAdapter {

    private final String[] tabTitles = {"Đã duyệt", "Chờ duyệt", "Từ chối"};
    private final String[] statusCodes = {"APPROVED", "PENDING", "REJECTED"};

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
