package com.ryanwelch.weather.ui.views;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.plattysoft.leonids.ParticleSystem;
import com.ryanwelch.weather.R;
import com.ryanwelch.weather.models.WeatherIcon;

public class WeatherIconView extends RelativeLayout {

    private WeatherIconView ctx;

    private ImageView mShadowImage;
    private ImageView mSunImage;
    private ImageView mSunBgImage;
    private ImageView mCloudImage;
    private RelativeLayout mIconLayout;

    private Drawable mCloudDrawable;
    private Drawable mDropletDrawable;
    private Drawable mSnowflakeDrawable;

    private ParticleSystem mParticleSystem;
    private boolean isEmitting = false;

    public WeatherIconView(Context context, AttributeSet attrs) {
        super(context, attrs);

        ctx = this;

        mCloudDrawable = ContextCompat.getDrawable(getContext(), R.drawable.cloud);
        mDropletDrawable = ContextCompat.getDrawable(getContext(), R.drawable.droplet);
        mSnowflakeDrawable = ContextCompat.getDrawable(getContext(), R.drawable.snowflake);
    }

    private ObjectAnimator createPulseAnim(View view) {
        ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(view,
                PropertyValuesHolder.ofFloat("scaleX", 0.8f),
                PropertyValuesHolder.ofFloat("scaleY", 0.8f));
        anim.setDuration(3000);
        anim.setRepeatCount(ObjectAnimator.INFINITE);
        anim.setRepeatMode(ObjectAnimator.REVERSE);
        return anim;
    }

    private ObjectAnimator createHoverAnim(View view) {
        ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(view,
                PropertyValuesHolder.ofFloat("translationY", 0f, 20f));
        anim.setDuration(3000);
        anim.setRepeatCount(ObjectAnimator.INFINITE);
        anim.setRepeatMode(ObjectAnimator.REVERSE);
        return anim;
    }

    private ObjectAnimator createHoverShadowAnim(View view) {
        ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(view,
                PropertyValuesHolder.ofFloat("scaleX", 1.4f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f),
                PropertyValuesHolder.ofFloat("alpha", 0.8f, 0.3f));
        anim.setDuration(3000);
        anim.setRepeatCount(ObjectAnimator.INFINITE);
        anim.setRepeatMode(ObjectAnimator.REVERSE);
        return anim;
    }

    private void createParticleSystem(ObjectAnimator animation, final ImageView imageView, final int numParticles, final ParticleCallback callback) {
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if(mParticleSystem != null) {
                    mParticleSystem.updateEmitPoint(imageView, Gravity.BOTTOM);
                }
            }
        });

        ViewTreeObserver vto = imageView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                if(isEmitting) return;
                // Emit once we know the layout sizes have been computed
                mParticleSystem = callback.createParticleSystem();
                mParticleSystem.emitWithGravity(imageView, Gravity.BOTTOM, numParticles);
                isEmitting = true;
            }

        });
    }

    public void createIcon(WeatherIcon type) {
        clearIcon();

        AnimatorSet as = new AnimatorSet();

        switch(type) {
            case SUNNY:
                createSun();
                createShadow();

                as.playTogether(
                        createPulseAnim(mSunBgImage),
                        createHoverAnim(mIconLayout),
                        createHoverShadowAnim(mShadowImage));
                as.start();

                break;
            case RAIN:
                createCloud(0xffcccccc);
                createShadow();

                ObjectAnimator hoverRainAnim = createHoverAnim(mCloudImage);

                as.playTogether(
                        hoverRainAnim,
                        createHoverShadowAnim(mShadowImage));
                as.start();

                createParticleSystem(hoverRainAnim, mCloudImage, 14, new ParticleCallback() {
                    @Override
                    public ParticleSystem createParticleSystem() {
                        return new ParticleSystem(ctx, mDropletDrawable, 50, 600)
                                .setSpeedByComponentsRange(0f, 0f, 0.05f, 0.1f)
                                .setAcceleration(0.0001f, 90)
                                .setScaleRange(0.2f, 0.2f)
                                .setFadeOut(300, new AccelerateInterpolator())
                                .setPadding(20, 20, 0, 0)
                                .setLayoutParams(new RelativeLayout.LayoutParams(
                                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()),
                                        LayoutParams.WRAP_CONTENT
                                ));
                    }
                });

                break;

            case SNOW:
                createCloud(0xffffffff);
                createShadow();

                ObjectAnimator hoverSnowAnim = createHoverAnim(mCloudImage);

                as.playTogether(
                        hoverSnowAnim,
                        createHoverShadowAnim(mShadowImage));
                as.start();

                createParticleSystem(hoverSnowAnim, mCloudImage, 10, new ParticleCallback() {
                    @Override
                    public ParticleSystem createParticleSystem() {
                        return new ParticleSystem(ctx, mSnowflakeDrawable, 50, 1500)
                                .setSpeedByComponentsRange(0f, 0f, 0.03f, 0.05f)
                                //.setAcceleration(0.00005f, 90)
                                .setScaleRange(0.2f, 0.2f)
                                .setFadeOut(300, new AccelerateInterpolator())
                                .setPadding(20, 20, 0, 0)
                                .setLayoutParams(new RelativeLayout.LayoutParams(
                                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()),
                                        LayoutParams.WRAP_CONTENT
                                ));
                    }
                });

                break;
        }
    }

    public void clearIcon() {
        removeAllViews();
    }

    private void createShadow() {
        if(mShadowImage == null) { // Avoid creating a new image view unless necessary
            mShadowImage = new ImageView(getContext());
            LayoutParams params = new LayoutParams(
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics()),
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics())
            );
            params.addRule(CENTER_HORIZONTAL, TRUE);
            params.addRule(ALIGN_PARENT_BOTTOM, TRUE);
            mShadowImage.setPadding(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()), 0, 0);
            mShadowImage.setLayoutParams(params);
            mShadowImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.shadow));
        }
        addView(mShadowImage);
    }

    private void createSun() {
        if(mIconLayout == null) {
            mIconLayout = new RelativeLayout(getContext());
            mIconLayout.setLayoutParams(new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
            ));
        } else {
            mIconLayout.removeAllViews();
        }

        if(mSunBgImage == null) {
            mSunBgImage = new ImageView(getContext());
            LayoutParams params = new LayoutParams(
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()),
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics())
            );
            mSunBgImage.setLayoutParams(params);
            mSunBgImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.sun_bg));
        }

        if(mSunImage == null) {
            mSunImage = new ImageView(getContext());
            LayoutParams params = new LayoutParams(
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics()),
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics())
            );
            params.addRule(CENTER_IN_PARENT, TRUE);
            mSunImage.setLayoutParams(params);
            mSunImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.sun));
        }

        mIconLayout.addView(mSunImage);
        mIconLayout.addView(mSunBgImage);
        addView(mIconLayout);
    }

    private void createCloud(int color) {
        if(mCloudImage == null) {
            mCloudImage = new ImageView(getContext());
            LayoutParams params = new LayoutParams(
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()),
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics())
            );
            mCloudImage.setLayoutParams(params);
            // TODO: Just use different drawables? More efficient?
            mCloudDrawable = mCloudDrawable.mutate(); // Prevent affecting other versions of the cloud drawable
            mCloudDrawable.setColorFilter( color , PorterDuff.Mode.MULTIPLY );
            mCloudImage.setImageDrawable(mCloudDrawable);
        }
        addView(mCloudImage);
    }

    private interface ParticleCallback {
        ParticleSystem createParticleSystem();
    }
}
