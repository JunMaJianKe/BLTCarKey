package com.neo.bltcarkey.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.neo.bltcarkey.R;
import com.neo.bltcarkey.common.Config;

/**
 * author : SenXia
 * time   : 2020/05/13
 * desc   : This is BleKeyLayout
 * version: 1.0
 */
public class BleKeyLayout extends View {

    private Paint mPaint;
    private Paint mDrawPaint;
    private int mColorPb;
    private int mColorPe;
    private int mColorPs;
    private int mCurrentStatus;
    private int mCurrentWidth;
    private int mCurrentHeight;
    private int mCarCurrentWidth;
    private int mCarCurrentHeight;
    private int mCurrentRadiusBig;
    private int mXpos;
    private int mYpos;
    private double mCurrentRadiusSmall;

    private Bitmap mBitMap;
    private Bitmap mBitMapDisplay;

    public BleKeyLayout(Context context) {
        super(context);
    }

    public BleKeyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mColorPb = ContextCompat.getColor(context, R.color.pb_range_color);
        mColorPe = ContextCompat.getColor(context, R.color.pe_range_color);
        mColorPs = ContextCompat.getColor(context, R.color.ps_range_color);
        initPaint();

        setStatus(Config.STATUS_DEFAULT);
    }

    public BleKeyLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BleKeyLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        calculatedLength();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mCurrentStatus == Config.STATUS_PB) {
            canvas.drawCircle(mCurrentWidth, mCurrentHeight, mCurrentRadiusBig, mPaint);
            canvas.drawBitmap(mBitMapDisplay, mXpos, mYpos, mDrawPaint);
        } else if (mCurrentStatus == Config.STATUS_PE) {
            canvas.drawCircle(mCurrentWidth, mCurrentHeight, (float) mCurrentRadiusSmall, mPaint);
            canvas.drawBitmap(mBitMapDisplay, mXpos, mYpos, mDrawPaint);
        } else if (mCurrentStatus == Config.STATUS_PS) {
            canvas.drawBitmap(mBitMapDisplay, mXpos, mYpos, mDrawPaint);
        } else {

        }
    }

    public void setStatus(int status) {
        switch (status) {
            case Config.STATUS_PB:
                mPaint.setColor(mColorPb);
                initDraw(R.drawable.car_white_grey);
                break;
            case Config.STATUS_PE:
                mPaint.setColor(mColorPe);
                initDraw(R.drawable.car_white_grey);
                break;
            case Config.STATUS_PS:
                initDraw(R.drawable.car_cyan_blue);
                break;
            case Config.STATUS_DEFAULT:
                break;
        }
        mCurrentStatus = status;
        invalidate();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(8);
    }

    private void initDraw(int res) {
        mBitMap = BitmapFactory.decodeResource(this.getContext().getResources(), res);
        mBitMapDisplay = mBitMap;
        mBitMapDisplay = Bitmap.createScaledBitmap(mBitMap, mCarCurrentWidth,
                mCarCurrentHeight, true);
        if (mDrawPaint == null) {
            mDrawPaint = new Paint();
        } else {
            mDrawPaint.reset();
        }
    }

    private void calculatedLength() {
        mCurrentWidth = getWidth() / 2;
        mCurrentHeight = getHeight() / 2;
        mCurrentRadiusBig = getWidth() / 3;
        mCarCurrentWidth = mCurrentWidth / 2;
        mCarCurrentHeight = mCurrentHeight / 2;
        mCurrentRadiusSmall =
                Math.sqrt(mCarCurrentWidth / 2 * mCarCurrentWidth / 2 + mCarCurrentHeight / 2 * mCarCurrentHeight / 2);
        mXpos = mCurrentWidth - mCarCurrentWidth / 2;
        mYpos = mCurrentHeight - mCarCurrentHeight / 2;
    }
}
