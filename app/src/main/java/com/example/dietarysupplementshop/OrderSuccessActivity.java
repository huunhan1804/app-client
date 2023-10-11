package com.example.dietarysupplementshop;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.core.models.Size;
import nl.dionsegijn.konfetti.xml.KonfettiView;

public class OrderSuccessActivity extends AppCompatActivity {
    private KonfettiView konfettiView = null;
    private Shape.DrawableShape drawableShape = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);

        final Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_heart);
        drawableShape = new Shape.DrawableShape(drawable, true, true);

        konfettiView = findViewById(R.id.konfettiView);
        EmitterConfig emitterConfig = new Emitter(5L, TimeUnit.SECONDS).perSecond(50);
        Party party = new PartyFactory(emitterConfig).angle(270).spread(90).setSpeedBetween(1f, 5f).timeToLive(2000L).shapes(new Shape.Rectangle(0.2f), drawableShape).sizes(new Size(12, 5f, 0.2f)).position(0.0, 0.0, 1.0, 0.0).build();
        konfettiView.start(party);


        ImageView zoomImageView = findViewById(R.id.zoomImageView);

        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0.5f, 1.0f, 0.5f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );

        scaleAnimation.setDuration(1500); // Đổi giá trị theo nhu cầu
        scaleAnimation.setRepeatCount(Animation.INFINITE);
        scaleAnimation.setRepeatMode(Animation.REVERSE);

        zoomImageView.startAnimation(scaleAnimation);

        Button btnOrderDetails = findViewById(R.id.btnOrderDetails);
        Button btnContinueShopping = findViewById(R.id.btnContinueShopping);

        btnOrderDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý sự kiện khi người dùng nhấn nút "Order Details"
                // Ví dụ: Chuyển đến một Activity khác
                Intent intent = new Intent(OrderSuccessActivity.this, OrderDetailsActivity.class);
                startActivity(intent);
            }
        });

        btnContinueShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý sự kiện khi người dùng nhấn nút "Continue Shopping"
                // Ví dụ: Chuyển đến một Activity khác
                Intent intent = new Intent(OrderSuccessActivity.this, HomepageActivity.class);
                startActivity(intent);
            }
        });

    }
}