package com.ryanwelch.weather.ui.components;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.plattysoft.leonids.ParticleSystem;
import com.ryanwelch.weather.R;
import com.ryanwelch.weather.domain.models.WeatherIcon;

public class WeatherIconView extends RelativeLayout {

    private static final String TAG = "WeatherIconView";

    private WeatherIconView ctx;
    private WeatherIcon mType;

    private ImageView mShadowImage;
    private ImageView mSunImage;
    private ImageView mSunBgImage;
    private ImageView mCloudImage;
    private ImageView mBoltImage;
    private RelativeLayout mIconLayout;

    private Drawable mCloudDrawable;
    private Drawable mDropletDrawable;
    private Drawable mSnowflakeDrawable;
    private Drawable mBoltDrawable;

    private AnimatorSet as;

    private ParticleSystem mParticleSystem;
    private boolean isEmitting = false;

    public WeatherIconView(Context context) {
        super(context);
        init();
    }

    public WeatherIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WeatherIconView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        ctx = this;

        mCloudDrawable = ContextCompat.getDrawable(getContext(), R.drawable.cloud);
        mDropletDrawable = ContextCompat.getDrawable(getContext(), R.drawable.droplet);
        mSnowflakeDrawable = ContextCompat.getDrawable(getContext(), R.drawable.snowflake);
        mBoltDrawable = ContextCompat.getDrawable(getContext(), R.drawable.lightning_bolt);

        Log.v(TAG, "Initialized new icon");
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
                PropertyValuesHolder.ofFloat("scaleX", 1.1f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f),
                PropertyValuesHolder.ofFloat("alpha", 0.8f, 0.3f));
        anim.setDuration(3000);
        anim.setRepeatCount(ObjectAnimator.INFINITE);
        anim.setRepeatMode(ObjectAnimator.REVERSE);
        return anim;
    }

    private void createParticleSystem(ObjectAnimator animation, final ImageView imageView, final int numParticles, final ParticleCallback callback) {
        animation.addUpdateListener((valueAnimator) -> {
            if(mParticleSystem != null) {
                mParticleSystem.updateEmitPoint(imageView, Gravity.BOTTOM);
            }
        });

        ViewTreeObserver vto = imageView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(() -> {
            if(isEmitting) return;
            // Emit once we know the layout sizes have been computed
            mParticleSystem = callback.createParticleSystem();
            mParticleSystem.emitWithGravity(imageView, Gravity.BOTTOM, numParticles);
            isEmitting = true;
        });
    }

    public void setIcon(WeatherIcon type) {
        if(type != mType) {
            mType = type;
        }
    }

    public void clearIcon() {
        clearIcon(false);
    }

    private void clearIcon(boolean full) {
        removeAllViews();
        if(mIconLayout != null) {
            mIconLayout.removeAllViews();
        }
        isEmitting = false;
        if(as != null) {
            as.removeAllListeners();
            as.end();
            as.cancel();
        }
        if(full) {
            mCloudImage = null;
        }
    }

    private void createIcon(WeatherIcon type) {
        clearIcon(true);

        Log.v(TAG, "Create icon: " + type.toString());

        as = new AnimatorSet();

        switch(type) {
            case SUNNY:
                createSun();
                mIconLayout.addView(mSunImage);
                mIconLayout.addView(mSunBgImage);
                addView(mIconLayout);
                createShadow();
                addView(mShadowImage);

                as.playTogether(
                        createPulseAnim(mSunBgImage),
                        createHoverAnim(mIconLayout),
                        createHoverShadowAnim(mShadowImage));
                as.start();

                break;
            case CLOUDY:
                createSun();
                createCloud(0xffffffff, 60, 30, true);
                mIconLayout.addView(mSunImage);
                mIconLayout.addView(mSunBgImage);
                mIconLayout.addView(mCloudImage);
                addView(mIconLayout);
                createShadow();
                addView(mShadowImage);

                as.playTogether(
                        createPulseAnim(mSunBgImage),
                        createHoverAnim(mIconLayout),
                        createHoverShadowAnim(mShadowImage));
                as.start();

                break;
            case RAIN:
                createCloud(0xffcccccc);
                addView(mCloudImage);
                createShadow();
                addView(mShadowImage);

                ObjectAnimator hoverRainAnim = createHoverAnim(mCloudImage);

                as.playTogether(
                        hoverRainAnim,
                        createHoverShadowAnim(mShadowImage));
                as.start();

                createParticleSystem(hoverRainAnim, mCloudImage, 14, () -> {
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
                });

                break;

            case SNOW:
                createCloud(0xffffffff);
                addView(mCloudImage);
                createShadow();
                addView(mShadowImage);

                ObjectAnimator hoverSnowAnim = createHoverAnim(mCloudImage);

                as.playTogether(
                        hoverSnowAnim,
                        createHoverShadowAnim(mShadowImage));
                as.start();

                createParticleSystem(hoverSnowAnim, mCloudImage, 10, () -> {
                    return new ParticleSystem(ctx, mSnowflakeDrawable, 50, 1500)
                            .setSpeedByComponentsRange(0f, 0f, 0.03f, 0.05f)
                            .setScaleRange(0.2f, 0.2f)
                            .setFadeOut(300, new AccelerateInterpolator())
                            .setPadding(20, 20, 0, 0)
                            .setLayoutParams(new RelativeLayout.LayoutParams(
                                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()),
                                    LayoutParams.WRAP_CONTENT
                            ));
                });

                break;
            case THUNDERSTORM:
                createCloud(0xff393939);
                createBolt();
                mIconLayout.addView(mCloudImage);
                mIconLayout.addView(mBoltImage);
                addView(mIconLayout);
                createShadow();
                addView(mShadowImage);

                ObjectAnimator hoverThunderAnim = createHoverAnim(mIconLayout);

                as.playTogether(
                        hoverThunderAnim,
                        createHoverShadowAnim(mShadowImage));
                as.start();

                mBoltImage.setAlpha(0f);
                hoverThunderAnim.addUpdateListener((valueAnimator) -> {
                    long time = valueAnimator.getCurrentPlayTime() % valueAnimator.getDuration();
                    if(time > 0 && time < 150) {
                        mBoltImage.setAlpha(1f);
                        mBoltImage.setPivotX(mBoltImage.getWidth()/2);
                        mBoltImage.setPivotY(mBoltImage.getHeight()/2);
                        mBoltImage.setRotation(15);
                    } else if(time > 150 && time < 300) {
                        mBoltImage.setAlpha(1f);
                        mBoltImage.setPivotX(mBoltImage.getWidth()/2);
                        mBoltImage.setPivotY(mBoltImage.getHeight()/2);
                        mBoltImage.setRotation(-10);
                    } else if(time > 300 && time < 450) {
                        mBoltImage.setAlpha(1f);
                        mBoltImage.setPivotX(mBoltImage.getWidth()/2);
                        mBoltImage.setPivotY(mBoltImage.getHeight()/2);
                        mBoltImage.setRotation(5);
                    } else {
                        mBoltImage.setAlpha(0f);
                    }
                });

        }
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
    }

    private void createCloud(int color, int width, int height, boolean bottomRight) {
        if(mCloudImage == null) {
            mCloudImage = new ImageView(getContext());
            LayoutParams params = new LayoutParams(
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, getResources().getDisplayMetrics()),
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, getResources().getDisplayMetrics())
            );
            if(bottomRight) {
                params.setMargins(
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100-width, getResources().getDisplayMetrics()),
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100-height-10, getResources().getDisplayMetrics()), 0, 0);
            }
            mCloudImage.setLayoutParams(params);
            // TODO: Just use different drawables? More efficient?
            mCloudDrawable = mCloudDrawable.mutate(); // Prevent affecting other versions of the cloud drawable
            mCloudDrawable.setColorFilter( color , PorterDuff.Mode.MULTIPLY );
            mCloudImage.setImageDrawable(mCloudDrawable);
        }
    }

    private void createCloud(int color) {
        createCloud(color, 100, 50, false);
    }

    private void createBolt() {
        if(mIconLayout == null) {
            mIconLayout = new RelativeLayout(getContext());
            mIconLayout.setLayoutParams(new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
            ));
        } else {
            mIconLayout.removeAllViews();
        }

        if(mBoltImage == null) {
            mBoltImage = new ImageView(getContext());
            LayoutParams params = new LayoutParams(
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()),
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics())
            );
            params.addRule(CENTER_HORIZONTAL, TRUE);
            params.setMargins(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics()), 0, 0);
            mBoltImage.setLayoutParams(params);
            mBoltImage.setImageDrawable(mBoltDrawable);
        }
    }

    private interface ParticleCallback {
        ParticleSystem createParticleSystem();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        Log.v(TAG, "Attached weather icon to window");

        if(!isInEditMode()) createIcon(mType);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        Log.v(TAG, "Detached weather icon from window");

        if(!isInEditMode()) clearIcon();
    }
}
