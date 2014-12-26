package com.tiny.wizard.uiexample;
// Created by wizard on 12/26/14.

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

public class CircularBar extends View {


    private boolean mIsInitializing = true;

    private int mGravity = Gravity.CENTER;
    private int mThumbRadius = 20;
    private int mCircleStrokeWidth = 10;
    private int mProgressColor;
    private int mProgressBackgroundColor;
    private float mProgress = 0.3f;
    private boolean mOverrdraw = false;

    private boolean mIsMarkerEnabled = false;
    private boolean mIsThumbEnabled = true;

    private Paint mBackgroundColorPaint = new Paint();
    private Paint mMarkerColorPaint = new Paint();
    private Paint mProgressColorPaint = new Paint();
    private Paint mThumbColorPaint = new Paint();
    private Paint mWholeBackgroundColorPaint = new Paint();


    private float mMarkerProgress = 0.0f;

    private float mTranslationOffsetX;
    private float mTranslationOffsetY;
    private RectF mCircleBounds = new RectF();
    private float mThumbPosX;
    private float mThumbPosY;
    private RectF mSquareRect = new RectF();

    private int mHorizontalInset;
    private int mVerticalInset;
    private float mRadius;

    @Override
    protected void onDraw(final Canvas canvas) {

        // All of our positions are using our internal coordinate system.
        // Instead of translating
        // them we let Canvas do the work for us.
        canvas.translate(mTranslationOffsetX, mTranslationOffsetY);

        final float progressRotation = getCurrentRotation();

        canvas.drawCircle(0, 0, mRadius, mWholeBackgroundColorPaint);

        // draw the background
        if (!mOverrdraw) {
            canvas.drawArc(mCircleBounds, 270, -(360 - progressRotation), false, mBackgroundColorPaint);
        }

        // draw the progress or a full circle if overdraw is true
        canvas.drawArc(mCircleBounds, 270, mOverrdraw ? 360 : progressRotation, false, mProgressColorPaint);

        // draw the marker at the correct rotated position
        if (mIsMarkerEnabled) {
            final float markerRotation = getMarkerRotation();

            canvas.save();
            canvas.rotate(markerRotation - 90);
            canvas.drawLine((float) (mThumbPosX + mThumbRadius / 2 * 1.4), mThumbPosY,
                    (float) (mThumbPosX - mThumbRadius / 2 * 1.4), mThumbPosY, mMarkerColorPaint);
            canvas.restore();
        }

        if (isThumbEnabled()) {
            // draw the thumb square at the correct rotated position
            canvas.save();
            canvas.rotate(progressRotation - 90);
            // rotate the square by 45 degrees
            canvas.rotate(45, mThumbPosX, mThumbPosY);
            mSquareRect.left = mThumbPosX - mThumbRadius / 3;
            mSquareRect.right = mThumbPosX + mThumbRadius / 3;
            mSquareRect.top = mThumbPosY - mThumbRadius / 3;
            mSquareRect.bottom = mThumbPosY + mThumbRadius / 3;
            canvas.drawRect(mSquareRect, mThumbColorPaint);
            canvas.restore();
        }
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        final int height = getDefaultSize(
                getSuggestedMinimumHeight() + getPaddingTop() + getPaddingBottom(),
                heightMeasureSpec);
        final int width = getDefaultSize(
                getSuggestedMinimumWidth() + getPaddingLeft() + getPaddingRight(),
                widthMeasureSpec);

        final int diameter;
        if (heightMeasureSpec == MeasureSpec.UNSPECIFIED) {
            // ScrollView
            diameter = width;
            computeInsets(0, 0);
        } else if (widthMeasureSpec == MeasureSpec.UNSPECIFIED) {
            // HorizontalScrollView
            diameter = height;
            computeInsets(0, 0);
        } else {
            // Default
            diameter = Math.min(width, height);
            computeInsets(width - diameter, height - diameter);
        }

        setMeasuredDimension(diameter, diameter);

        final float halfWidth = diameter * 0.5f;

        // width of the drawed circle (+ the drawedThumb)
        final float drawedWith;
        if (isThumbEnabled()) {
            drawedWith = mThumbRadius * (5f / 6f);
        } else if (isMarkerEnabled()) {
            drawedWith = mCircleStrokeWidth * 1.4f;
        } else {
            drawedWith = mCircleStrokeWidth / 2f;
        }

        // -0.5f for pixel perfect fit inside the viewbounds
        mRadius = halfWidth - drawedWith - 0.5f;

        mCircleBounds.set(-mRadius, -mRadius, mRadius, mRadius);

        mThumbPosX = (float) (mRadius * Math.cos(0));
        mThumbPosY = (float) (mRadius * Math.sin(0));

        mTranslationOffsetX = halfWidth + mHorizontalInset;
        mTranslationOffsetY = halfWidth + mVerticalInset;

    }

    /**
     * Compute insets.
     *
     * <pre>
     *  ______________________
     * |_________dx/2_________|
     * |......| /'''''\|......|
     * |-dx/2-|| View ||-dx/2-|
     * |______| \_____/|______|
     * |________ dx/2_________|
     * </pre>
     *
     * @param dx the dx the horizontal unfilled space
     * @param dy the dy the horizontal unfilled space
     */
    private void computeInsets(final int dx, final int dy) {
        int absoluteGravity = mGravity;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            absoluteGravity = Gravity.getAbsoluteGravity(mGravity, getLayoutDirection());
        }

        switch (absoluteGravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
            case Gravity.LEFT:
                mHorizontalInset = 0;
                break;
            case Gravity.RIGHT:
                mHorizontalInset = dx;
                break;
            case Gravity.CENTER_HORIZONTAL:
            default:
                mHorizontalInset = dx / 2;
                break;
        }
        switch (absoluteGravity & Gravity.VERTICAL_GRAVITY_MASK) {
            case Gravity.TOP:
                mVerticalInset = 0;
                break;
            case Gravity.BOTTOM:
                mVerticalInset = dy;
                break;
            case Gravity.CENTER_VERTICAL:
            default:
                mVerticalInset = dy / 2;
                break;
        }
    }

    public CircularBar(Context context) {
        this(context, null);
    }

    public CircularBar(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.circularProgressBarStyle);
    }

    public CircularBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.HoloCircularProgressBar, defStyleAttr, 0);

        if (attributes != null) {
            try {
                setProgressColor(attributes.getColor(R.styleable.HoloCircularProgressBar_progress_color, Color.CYAN));
                setProgressBackgroundColor(attributes.getColor(R.styleable.HoloCircularProgressBar_progress_background_color, Color.GREEN));
                setProgress(attributes.getFloat(R.styleable.HoloCircularProgressBar_progress, 0.0f));
                setMarkerProgress(attributes.getFloat(R.styleable.HoloCircularProgressBar_marker_progress, 0.0f));
                setWheelSize((int) attributes.getDimension(R.styleable.HoloCircularProgressBar_stroke_width, 10));
                setThumbEnabled(attributes.getBoolean(R.styleable.HoloCircularProgressBar_thumb_visible, true));
                setMarkerEnabled(attributes.getBoolean(R.styleable.HoloCircularProgressBar_marker_visible, true));

                mGravity = attributes.getInt(R.styleable.HoloCircularProgressBar_android_gravity, Gravity.CENTER);
            } finally {
                // make sure recycle is always called.
                attributes.recycle();
            }
        }

        mThumbRadius = mCircleStrokeWidth * 2;

        updateBackgroundColor();

        updateMarkerColor();

        updateProgressColor();

        updateWholeBackgroundColor();

        mIsInitializing = false;
    }

    private void updateBackgroundColor() {
        mBackgroundColorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundColorPaint.setColor(mProgressBackgroundColor);
        mBackgroundColorPaint.setStyle(Paint.Style.STROKE);
        mBackgroundColorPaint.setStrokeWidth(mCircleStrokeWidth);

        invalidate();
    }

    public void setThumbEnabled(final boolean enabled) {
        mIsThumbEnabled = enabled;
    }

    public void setWheelSize(final int dimension) {
        mCircleStrokeWidth = dimension;

        // update the paints
        updateBackgroundColor();
        updateMarkerColor();
        updateProgressColor();
    }

    private void updateProgressColor() {
        mProgressColorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressColorPaint.setColor(mProgressColor);
        mProgressColorPaint.setStyle(Paint.Style.STROKE);
        mProgressColorPaint.setStrokeWidth(mCircleStrokeWidth);

        mThumbColorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mThumbColorPaint.setColor(mProgressColor);
        mThumbColorPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mThumbColorPaint.setStrokeWidth(mCircleStrokeWidth);

        invalidate();
    }

    private void updateWholeBackgroundColor(){
        mWholeBackgroundColorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mWholeBackgroundColorPaint.setColor(Color.BLACK);
        mWholeBackgroundColorPaint.setStyle(Paint.Style.FILL);

        invalidate();
    }

    private void updateMarkerColor() {

        mMarkerColorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMarkerColorPaint.setColor(mProgressBackgroundColor);
        mMarkerColorPaint.setStyle(Paint.Style.STROKE);
        mMarkerColorPaint.setStrokeWidth(mCircleStrokeWidth / 2);

        invalidate();
    }

    public void setProgress(final float progress) {
        if (progress == mProgress) {
            return;
        }

        if (progress == 1) {
            mOverrdraw = false;
            mProgress = 1;
        } else {
            mOverrdraw = progress >= 1;
            mProgress = progress % 1.0f;
        }

        if (!mIsInitializing) {
            invalidate();
        }
    }

    private void setProgressColor(final int color) {
        mProgressColor = color;

        updateProgressColor();
    }

    public void setProgressBackgroundColor(final int color) {
        mProgressBackgroundColor = color;

        updateMarkerColor();
        updateBackgroundColor();
    }

    public void setMarkerProgress(final float progress) {
        mIsMarkerEnabled = true;
        mMarkerProgress = progress;
    }

    public void setMarkerEnabled(boolean markerEnabled) {
        mIsMarkerEnabled = markerEnabled;
    }

    public int getCircleStrokeWidth() {
        return mCircleStrokeWidth;
    }

    public float getMarkerProgress() {
        return mMarkerProgress;
    }

    public float getProgress() {
        return mProgress;
    }

    public int getProgressColor() {
        return mProgressColor;
    }

    public boolean isMarkerEnabled() {
        return mIsMarkerEnabled;
    }

    public boolean isThumbEnabled() {
        return mIsThumbEnabled;
    }

    private float getCurrentRotation() {
        return 360 * mProgress;
    }

    private float getMarkerRotation() {
        return 360 * mMarkerProgress;
    }
}
