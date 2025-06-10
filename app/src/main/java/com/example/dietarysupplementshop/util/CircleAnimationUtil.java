package com.example.dietarysupplementshop.util;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.appcompat.widget.AppCompatImageView;

import java.lang.ref.WeakReference;

public class CircleAnimationUtil {
    private static final int DEFAULT_DURATION_DISAPPEAR = 20;
    private View mTarget;
    private View mDest;

    private int mCircleDuration = 50;
    private int mMoveDuration = 50;
    private int mDisappearDuration = DEFAULT_DURATION_DISAPPEAR;

    private WeakReference<Activity> mContextReference;

    private int mBorderWidth = 4;
    private int mBorderColor = Color.BLACK;

    private CircleImageView mImageView;
    private Animator.AnimatorListener mAnimationListener;

    public CircleAnimationUtil() {
    }

    public CircleAnimationUtil attachActivity(Activity activity) {
        mContextReference = new WeakReference<>(activity);
        return this;
    }

    public CircleAnimationUtil setTargetView(View view) {
        mTarget = view;
        return this;
    }

    public CircleAnimationUtil setDestView(View view) {
        mDest = view;
        return this;
    }

    public CircleAnimationUtil setBorderWidth(int width) {
        mBorderWidth = width;
        return this;
    }

    public CircleAnimationUtil setBorderColor(int color) {
        mBorderColor = color;
        return this;
    }

    public CircleAnimationUtil setCircleDuration(int duration) {
        mCircleDuration = duration;
        return this;
    }

    public CircleAnimationUtil setMoveDuration(int duration) {
        mMoveDuration = duration;
        return this;
    }

    private boolean prepare() {
        if (mContextReference.get() != null) {
            ViewGroup decorView = (ViewGroup) mContextReference.get().getWindow().getDecorView();

            if (mImageView == null) {
                mImageView = new CircleImageView(mContextReference.get());
                mImageView.setBorderWidth(mBorderWidth);
                mImageView.setBorderColor(mBorderColor);
                decorView.addView(mImageView, new FrameLayout.LayoutParams(mTarget.getWidth(), mTarget.getHeight()));
            }

            Bitmap bitmap = drawViewToBitmap(mTarget, mTarget.getWidth(), mTarget.getHeight());
            mImageView.setImageBitmap(bitmap);
        }
        return true;
    }

    public void startAnimation() {
        if (prepare()) {
            mTarget.setVisibility(View.INVISIBLE);
            getAvatarRevealAnimator().start();
        }
    }

    private AnimatorSet getAvatarRevealAnimator() {
        final float endRadius = Math.max(mDest.getWidth(), mDest.getHeight()) / 2;
        final float startRadius = Math.max(mTarget.getWidth(), mTarget.getHeight());

        @SuppressLint("ObjectAnimatorBinding") Animator mRevealAnimator = ObjectAnimator.ofFloat(mImageView, "drawableRadius", startRadius, endRadius * 1.05f, endRadius * 0.9f, endRadius);
        mRevealAnimator.setDuration(mCircleDuration);

        final float scaleFactor = 0.2f;

        mImageView.animate()
                .scaleX(scaleFactor)
                .scaleY(scaleFactor)
                .setDuration(mCircleDuration)
                .withStartAction(() -> {
                    if (mAnimationListener != null) {
                        mAnimationListener.onAnimationStart(null);
                    }
                })
                .withEndAction(() -> {
                    int[] src = new int[2];
                    int[] dest = new int[2];
                    mImageView.getLocationOnScreen(src);
                    mDest.getLocationOnScreen(dest);

                    float x = mImageView.getX();
                    float y = mImageView.getY();

                    mImageView.animate()
                            .x(x + dest[0] - (src[0] + (mTarget.getWidth() * scaleFactor - 2 * endRadius * scaleFactor) / 2) + (0.5f * mDest.getWidth() - scaleFactor * endRadius))
                            .y(y + dest[1] - (src[1] + (mTarget.getHeight() * scaleFactor - 2 * endRadius * scaleFactor) / 2) + (0.5f * mDest.getHeight() - scaleFactor * endRadius))
                            .setInterpolator(input -> (float) (-Math.pow(input - 1, 2) + 1f))
                            .setDuration(mMoveDuration)
                            .withEndAction(() -> {
                                mImageView.animate()
                                        .scaleX(0)
                                        .scaleY(0)
                                        .setDuration(mDisappearDuration)
                                        .withEndAction(() -> {
                                            if (mAnimationListener != null) {
                                                mAnimationListener.onAnimationEnd(null);
                                            }
                                            reset();
                                        })
                                        .start();
                            })
                            .start();
                })
                .start();

        AnimatorSet animatorCircleSet = new AnimatorSet();
        animatorCircleSet.playTogether(mRevealAnimator);
        return animatorCircleSet;
    }

    private Bitmap drawViewToBitmap(View view, int width, int height) {
        Bitmap dest = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(dest));
        return dest;
    }

    private void reset() {
        if (mImageView != null) {
            ViewGroup parent = (ViewGroup) mImageView.getParent();
            if (parent != null) {
                parent.removeView(mImageView);
            }
        }
        if (mTarget != null) {
            mTarget.setVisibility(View.VISIBLE);
        }
    }

    public CircleAnimationUtil setAnimationListener(Animator.AnimatorListener listener) {
        mAnimationListener = listener;
        return this;
    }
}
