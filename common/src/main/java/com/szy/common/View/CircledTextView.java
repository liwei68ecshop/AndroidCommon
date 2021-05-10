package com.szy.common.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.TextView;

import com.szy.common.R;

/**
 * Created by zongren on 16/5/27.
 * All Rights Reserved By 秦皇岛商之翼网络科技有限公司.
 */
public class CircledTextView extends TextView {
    private int mCircleColor;
    private int mBadgeBackground;
    private int mCircleWidth;
    private RectF mOval;
    private Paint mStrokePaint;
    private Paint mFillPaint;

    public CircledTextView(Context context) {
        this(context, null);
    }

    public CircledTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircledTextView);
        mCircleColor = array.getColor(R.styleable.CircledTextView_circleColor, 0);
        mCircleWidth = array.getDimensionPixelSize(R.styleable.CircledTextView_circleWidth, 2);
        mStrokePaint = new Paint();
        mOval = new RectF();
        array.recycle();
    }

    public int getBadgeBackground() {
        return mBadgeBackground;
    }

    public void setBadgeBackground(int badgeBackground) {
        mBadgeBackground = badgeBackground;
    }

    public int getCircleColor() {
        return mCircleColor;
    }

    public void setCircleColor(int circleColor) {
        this.mCircleColor = circleColor;
    }

    public int getCircleWidth() {
        return mCircleWidth;
    }

    public void setCircleWidth(int circleWidth) {
        this.mCircleWidth = circleWidth;
    }

    @Override
    public void onDraw(Canvas canvas) {

        if (mCircleColor == 0) {
            mCircleColor = getCurrentTextColor();
        }
        mFillPaint = this.getPaint();
        mFillPaint.setColor(mBadgeBackground);
        mStrokePaint.setAntiAlias(true);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setColor(mCircleColor);
        mStrokePaint.setStrokeWidth(mCircleWidth);
        mOval.left = mCircleWidth;
        mOval.top = mCircleWidth;
        mOval.right = mOval.left + (getWidth() - 2 * mCircleWidth);
        mOval.bottom = mOval.top + (getHeight() - 2 * mCircleWidth);
        float r = (mOval.bottom - mOval.top) / 2;
        canvas.drawRoundRect(mOval, r, r, mFillPaint);
        canvas.drawRoundRect(mOval, r, r, mStrokePaint);
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

    }
}
