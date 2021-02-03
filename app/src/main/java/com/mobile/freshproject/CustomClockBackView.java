package com.mobile.freshproject;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.Date;

public class CustomClockBackView extends View {

    private float bigCircGrade = -90;
    private float smallCircGrade = -90;

    private float bigCircCenterX;

    private float bigCircRadius;
    private float smallCircRadius;

    private float bigCircFixedX;
    private float bigCircFixedY;

    private float bigCircMovingX;
    private float bigCircMovingY;

    private float smallCircMovingX;
    private float smallCircMovingY;

    private ValueAnimator smallValueAnimator = new ValueAnimator();
    private ValueAnimator bigValueAnimator = new ValueAnimator();

    private float paddingUp;
    public float bigCircCenterY;

    private int textSize = 40;
    private float textX;

    private boolean isTextPosSet;
    private boolean isDefault = true;
    private boolean measureElapsingTime;

    public long seconds = 0;
    public long millis = 0;
    public long lastTimeMillis = 0;
    public long startTime = 0;
    private long elapsedTime = 0;

    private double radianUnit = Math.PI / 180;


    private Paint blackPaint;
    private Paint whitePaint;
    private Paint grayDarkPaint;
    private Paint grayLightPaint;
    private Paint textPaint;

    private Rect r = new Rect();

    public CustomClockBackView(Context context) {
        super(context);
    }

    public CustomClockBackView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setInitAttr(attrs);
        setBigCircCenter();
        setHeight();
    }

    public CustomClockBackView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setInitAttr(attrs);
    }

    public CustomClockBackView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setInitAttr(attrs);
    }

    private void setInitAttr(AttributeSet attr) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attr, R.styleable.CustomClockBackView);

        paddingUp = typedArray.getDimension(R.styleable.CustomClockBackView_paddingUp, 0);

        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        setPaintColors(textSize);

        drawBigCirc(canvas);
        drawSmallCirc(canvas);

        // format time
        drawCenterText(canvas, textPaint, getReadyTime());
    }

    private void drawBigCirc(Canvas canvas) {
        // setting center and drawing arrows of BIG circle
        setBigCircCenter();
        drawFixedBigCirc(canvas);

        // setting arrow coordinates and drawing
        if (isDefault) {
            setCenterCoordinatesOfBigCirc(true, 0, bigCircRadius * (-1));
            isDefault = false;
        } else {
            setBigCircArrowCoordinates();
        }
        drawBigCircArrow(canvas);

        // drawing black-colored subBigCircle
        canvas.drawCircle(bigCircCenterX, bigCircCenterY, bigCircRadius - 50, blackPaint);
    }

    private void drawSmallCirc(Canvas canvas) {
        // drawing fixed background of SMALL circle
        drawFixedCircSmall(canvas);

        // setting coordinates and drawing arrows of SMALL circle
        if (isDefault) {
            setCenterCoordinatesOfSmallCirc(true, 0, smallCircRadius * (-1));
            isDefault = false;
        } else {
            setSmallCircArrowCoordinates();
        }
        drawSmallCircArrow(canvas);

        // drawing smallest circle inside small circle
        canvas.drawCircle(bigCircCenterX, bigCircCenterY + bigCircCenterY / 4.f, 6, grayLightPaint);
        canvas.drawCircle(bigCircCenterX, bigCircCenterY + bigCircCenterY / 4.f, 4, blackPaint);
    }

    private void drawBigCircArrow(Canvas canvas) {
        // drawing big seconds arrow
        canvas.drawLine(bigCircCenterX, bigCircCenterY, bigCircCenterX + bigCircMovingX, bigCircCenterY + bigCircMovingY, whitePaint);
    }

    private void drawSmallCircArrow(Canvas canvas) {
        // drawing small seconds arrow
        canvas.drawLine(bigCircCenterX, bigCircCenterY + bigCircCenterY / 4.f, bigCircCenterX + smallCircMovingX, bigCircCenterY + bigCircCenterY / 4.f + smallCircMovingY, grayLightPaint);
    }

    private void drawFixedBigCirc(Canvas canvas) {
        // drawing big circ fixed arrows
        for (int i = 0; i < 500; i += 3) {
            drawBigFixedCircCoordinates(i);
            canvas.drawLine(bigCircCenterX, bigCircCenterY, bigCircCenterX + bigCircFixedX, bigCircCenterY + bigCircFixedY, grayDarkPaint);
        }
    }

    private void drawFixedCircSmall(Canvas canvas) {
        // drawing small circle's border
        canvas.drawCircle(bigCircCenterX, bigCircCenterY + bigCircCenterY / 4.f, smallCircRadius, grayLightPaint);
        canvas.drawCircle(bigCircCenterX, bigCircCenterY + bigCircCenterY / 4.f, smallCircRadius - 5, blackPaint);
    }

    private void setBigCircArrowCoordinates() {
        float j = bigCircGrade % 90;

        if (j != 0) {

            if (j < 0) {
                setCenterCoordinatesOfBigCirc(false, Math.cos(j * radianUnit), Math.sin(j * radianUnit));
            } else if (bigCircGrade > 0 && bigCircGrade < 90) {
                setCenterCoordinatesOfBigCirc(false, Math.cos(j * radianUnit), Math.sin(j * radianUnit));
            } else if (bigCircGrade > 90 && bigCircGrade < 180) {
                setCenterCoordinatesOfBigCirc(false, Math.sin(j * radianUnit) * (-1), Math.cos(j * radianUnit));
            } else if (bigCircGrade > 180 && bigCircGrade < 270) {
                setCenterCoordinatesOfBigCirc(false, Math.cos(j * radianUnit) * (-1), Math.sin(j * radianUnit) * (-1));
            }

        } else if (bigCircGrade == -90 || bigCircGrade == 270) {
            setCenterCoordinatesOfBigCirc(true, 0, bigCircRadius * (-1));
        } else if (bigCircGrade == 0) {
            setCenterCoordinatesOfBigCirc(true, bigCircRadius, 0);
        } else if (bigCircGrade == 90) {
            setCenterCoordinatesOfBigCirc(true, 0, bigCircRadius);
        } else if (bigCircGrade == 180) {
            setCenterCoordinatesOfBigCirc(true, bigCircRadius * (-1), 0);
        }
    }

    private boolean setTextForHours;
    public String getReadyTime() {
        if (measureElapsingTime) {
            setMillis();
        }

        if (millis < 3600000)
            return String.format("%s:%s.%s",
                    setTimeFormat((seconds / 60) % 60),
                    setTimeFormat(seconds % 60),
                    setTimeFormat((millis / 10) % 100));
        else {
            if (!setTextForHours) {
                isTextPosSet = false;
                setTextForHours = true;
                textSize = 35;
            }
            return String.format("%s:%s:%s.%s",
                    setTimeFormat((seconds / 60) / 60),
                    setTimeFormat((seconds / 60) % 60),
                    setTimeFormat(seconds % 60),
                    setTimeFormat((millis / 10) % 100));
        }
        }


    private void setMillis(){
        millis = new Date().getTime() - startTime;
        seconds = millis / 1000;
    }

    private void drawCenterText(Canvas canvas, Paint paint, String text) {
        if (!isTextPosSet) {
            canvas.getClipBounds(r);
            int cWidth = r.width();
            paint.setTextAlign(Paint.Align.LEFT);
            paint.getTextBounds(text, 0, text.length(), r);
            textX = cWidth / 2f - r.width() / 2f - r.left;
            isTextPosSet = true;
        }
        canvas.drawText(text, textX, bigCircCenterY + r.height()/2.f, paint);
    }

    // returns String in the form like 01 or 11
    private String setTimeFormat(long time) {
        return time < 10 ? String.format("0%d", time) : ("" + time);
    }

    private void setSmallCircArrowCoordinates() {
        float j = smallCircGrade % 90;

        if (j != 0) {

            if (j < 0) {
                setCenterCoordinatesOfSmallCirc(false, Math.cos(j * radianUnit), Math.sin(j * radianUnit));
            } else if (smallCircGrade > 0 && smallCircGrade < 90) {
                setCenterCoordinatesOfSmallCirc(false, Math.cos(j * radianUnit), Math.sin(j * radianUnit));
            } else if (smallCircGrade > 90 && smallCircGrade < 180) {
                setCenterCoordinatesOfSmallCirc(false, Math.sin(j * radianUnit) * (-1), Math.cos(j * radianUnit));
            } else if (smallCircGrade > 180 && smallCircGrade < 270) {
                setCenterCoordinatesOfSmallCirc(false, Math.cos(j * radianUnit) * (-1), Math.sin(j * radianUnit) * (-1));
            }

        } else if (smallCircGrade == -90 || smallCircGrade == 270) {
            setCenterCoordinatesOfSmallCirc(true, 0, smallCircRadius * (-1));
        } else if (smallCircGrade == 0) {
            setCenterCoordinatesOfSmallCirc(true, smallCircRadius, 0);
        } else if (smallCircGrade == 90) {
            setCenterCoordinatesOfSmallCirc(true, 0, smallCircRadius);
        } else if (smallCircGrade == 180) {
            setCenterCoordinatesOfSmallCirc(true, smallCircRadius * (-1), 0);
        }
    }

    private void setCenterCoordinatesOfBigCirc(boolean isArrows, double argumentX, double argumentY) {
        bigCircMovingX = (float) (isArrows ? argumentX : bigCircRadius * argumentX);
        bigCircMovingY = (float) (isArrows ? argumentY : bigCircRadius * argumentY);
    }

    private void setCenterCoordinatesOfSmallCirc(boolean isArrows, double argumentX, double argumentY) {
        smallCircMovingX = (float) (isArrows ? argumentX : smallCircRadius * argumentX);
        smallCircMovingY = (float) (isArrows ? argumentY : smallCircRadius * argumentY);
    }

    private void drawBigFixedCircCoordinates(int i) {
        float j = i % 90;
        bigCircFixedX = (float) (bigCircRadius * Math.sin(j * Math.PI / 180));
        bigCircFixedY = (float) (bigCircRadius * Math.cos(j * Math.PI / 180));

        if (i == 0 || i == 180) {
            bigCircFixedX = bigCircRadius * (i == 0 ? 1 : (-1));
            bigCircFixedY = 0;
        }

        if (i >= 90 && i < 180) {
            bigCircFixedX *= -1;
        } else if (i > 180 && i < 270) {
            bigCircFixedY *= -1;
            bigCircFixedX *= -1;
        } else if (i >= 270) {
            bigCircFixedY *= -1;
        }
    }

    public void startBigCircAnimator() {
        bigValueAnimator.setFloatValues(0, 360);
        bigValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                bigCircGrade = animatedValue - 90;
                postInvalidate();
            }
        });
        bigValueAnimator.setDuration(60000);
        bigValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        bigValueAnimator.setInterpolator(new LinearInterpolator());
        bigValueAnimator.start();
    }

    public void startSmallCircAnimator() {
        smallValueAnimator.setFloatValues(0, 360);
        smallValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                smallCircGrade = animatedValue - 90;
                postInvalidate();
            }
        });
        smallValueAnimator.setDuration(1000);
        smallValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        smallValueAnimator.setInterpolator(new LinearInterpolator());
        smallValueAnimator.start();
    }

    public void startAction() {
        startTime = new Date().getTime();
        startAnimators();
        measureElapsingTime = true;
    }

    public void pauseAction() {
        measureElapsingTime = false;
        elapsedTime = new Date().getTime() - startTime;

        pauseAnimators();
    }

    public void resumeAction() {
        startTime = new Date().getTime() - elapsedTime;
        measureElapsingTime = true;

        resumeAnimators();
    }

    public void startAnimators() {
        startBigCircAnimator();
        startSmallCircAnimator();
    }

    public void pauseAnimators() {

        smallValueAnimator.pause();
        bigValueAnimator.pause();
    }

    public void resumeAnimators() {
        smallValueAnimator.resume();
        bigValueAnimator.resume();
    }

    public void killAnimators() {
        smallValueAnimator.end();
        bigValueAnimator.end();
    }

    public void makeDefault() {
        startTime = 0;

        millis = 0;
        seconds = 0;

        bigCircGrade = -90;
        smallCircGrade = -90;

        measureElapsingTime = false;
        isTextPosSet = false;
        setTextForHours = false;
        textSize = 40;
        killAnimators();
    }

    public void changeCircPosition() {
        bigCircCenterY = bigCircRadius + convertDpToPx(10);
        postInvalidate();
    }

    private void setBigCircCenter() {
        bigCircCenterX = getWidth() / 2.f;
        bigCircRadius = bigCircCenterX - convertDpToPx(55);
        bigCircCenterY = getHeight() / 2.f;

        smallCircRadius = bigCircRadius / 7;

        Constants.bigCircCenterX = bigCircRadius;
        Constants.bigCircRadius = bigCircRadius;
    }
    public void setHeight() {
            measure(getWidth(), (int) (bigCircRadius * 2f));
    }

    public void setLastTimeMillis(){
        this.lastTimeMillis = millis;
    }

    private float convertDpToPx(float valueDp) {
        return valueDp * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    private void setPaintColors(int size) {
        blackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        blackPaint.setColor(Color.BLACK);
        blackPaint.setStrokeWidth(2);

        whitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        whitePaint.setColor(Color.WHITE);
        whitePaint.setStrokeWidth(5);

        grayDarkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        grayDarkPaint.setColor(Color.DKGRAY);
        grayDarkPaint.setStrokeWidth(5);

        grayLightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        grayLightPaint.setColor(Color.GRAY);
        grayLightPaint.setStrokeWidth(5);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(convertDpToPx(40));
        textPaint.setColor(Color.WHITE);
        textPaint.setStrokeWidth(5);
    }
}
