package com.example.dietarysupplementshop.interfaces;

import com.example.dietarysupplementshop.model.FilterItem;

import java.util.List;

public interface FilterDialogListener {
    void onFiltersApplied(List<FilterItem> selectedFilters);
}
