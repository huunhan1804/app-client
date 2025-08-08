package com.example.dietarysupplementshop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietarysupplementshop.adapter.SupportCategoryAdapter;
import com.example.dietarysupplementshop.interfaces.RetrofitClient;
import com.example.dietarysupplementshop.interfaces.SupportAPI;
import com.example.dietarysupplementshop.model.ResponseModel;
import com.example.dietarysupplementshop.model.SupportCategory;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupportCenterActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_center);

        recyclerView = findViewById(R.id.recycler_categories);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        ImageButton btnChat = findViewById(R.id.btn_chatbox);
        btnChat.setOnClickListener(v -> {
            Intent intent = new Intent(SupportCenterActivity.this, SupportCenterChatActivity.class);
            intent.putExtra("receiverId", "admin_1");
            startActivity(intent);
        });

        btnChat.setOnTouchListener(new View.OnTouchListener() {
            float dX, dY;
            long startClickTime;
            static final int CLICK_DURATION_THRESHOLD = 200;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = view.getX() - event.getRawX();
                        dY = view.getY() - event.getRawY();
                        startClickTime = System.currentTimeMillis();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        float newX = event.getRawX() + dX;
                        float newY = event.getRawY() + dY;

                        View parent = (View) view.getParent();
                        int parentWidth = parent.getWidth();
                        int parentHeight = parent.getHeight();

                        newX = Math.max(0, Math.min(newX, parentWidth - view.getWidth()));
                        newY = Math.max(0, Math.min(newY, parentHeight - view.getHeight()));

                        view.setX(newX);
                        view.setY(newY);
                        break;

                    case MotionEvent.ACTION_UP:
                        long clickDuration = System.currentTimeMillis() - startClickTime;
                        if (clickDuration < CLICK_DURATION_THRESHOLD) {
                            view.performClick();
                        }
                        break;
                }
                return true;
            }
        });

        fetchCategories();
    }

    private void fetchCategories() {
        SupportAPI api = RetrofitClient.getRetrofitInstance().create(SupportAPI.class);
        api.getAllCategories().enqueue(new Callback<ResponseModel<List<SupportCategory>>>() {
            @Override
            public void onResponse(Call<ResponseModel<List<SupportCategory>>> call, Response<ResponseModel<List<SupportCategory>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<SupportCategory> categories = response.body().getData();
                    recyclerView.setAdapter(new SupportCategoryAdapter(SupportCenterActivity.this, categories, categoryId -> {
                        showFragment(SupportArticleListFragment.newInstance(categoryId));
                    }));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<List<SupportCategory>>> call, Throwable t) {
                Toast.makeText(SupportCenterActivity.this, "Lỗi tải danh mục", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showFragment(Fragment fragment) {
        // Ẩn danh mục khi chuyển
        findViewById(R.id.recycler_categories).setVisibility(View.GONE);
        findViewById(R.id.support_fragment_container).setVisibility(View.VISIBLE);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.support_fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }

        // Hiện lại danh sách nếu quay lại gốc
        if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
            findViewById(R.id.recycler_categories).setVisibility(View.VISIBLE);
            findViewById(R.id.support_fragment_container).setVisibility(View.GONE);
        }
    }

}