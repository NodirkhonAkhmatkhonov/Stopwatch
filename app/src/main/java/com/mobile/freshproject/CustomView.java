package com.mobile.freshproject;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

public class CustomView extends View {

//    public float x;
//    public float y;
    public float grade;
    public float startAngle;

    public final float center = 100;
    private Paint blackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint whitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        blackPaint.setColor(Color.BLACK);
        blackPaint.setStrokeWidth(20);

        whitePaint.setColor(Color.WHITE);
        whitePaint.setStrokeWidth(20);

        canvas.drawArc(50,50 , 150, 150, -90, grade, true, blackPaint);
        canvas.drawArc(70,70 , 130, 130, -90, grade, true, whitePaint);
//
//          canvas.drawArc(50, 50, 150, 150, -90, 270, true, blackPaint);
    }

    public void startAnimator() {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setFloatValues(0, 360);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                grade = animatedValue;
                postInvalidate();
            }
        });
        valueAnimator.setDuration(3000);
        valueAnimator.setInterpolator(new OvershootInterpolator());
        valueAnimator.start();

    }
}
