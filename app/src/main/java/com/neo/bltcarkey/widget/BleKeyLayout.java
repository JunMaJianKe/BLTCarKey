package com.neo.bltcarkey.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.neo.bltcarkey.R;
import com.neo.bltcarkey.common.Config;

/**
 * author : SenXia
 * e-mail : Sen_Xia@human-horizons.com
 * time   : 2020/05/13
 * desc   : This is BleKeyLayout
 * version: 1.0
 */
public class BleKeyLayout extends View {

    private Paint mPaint;
    private int mColorPb;
    private int mColorPe;
    private int mColorPs;
    private int mCurrentStatus;
    private int mCurrentWidth;
    private int mCurrentHeight;
    private int mCurrentRadius;

    public BleKeyLayout(Context context) {
        super(context);
    }

    public BleKeyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mColorPb = ContextCompat.getColor(context, R.color.pb_range_color);
        mColorPe = ContextCompat.getColor(context, R.color.pe_range_color);
        mColorPs = ContextCompat.getColor(context, R.color.ps_range_color);
        initPaint();
        setStatus(Config.STATUS_PB);
    }

    public BleKeyLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BleKeyLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        mCurrentWidth = getWidth() / 2;
        mCurrentHeight = getHeight() / 2;
        switch (mCurrentStatus) {
            case Config.STATUS_PB:
                mCurrentRadius = getWidth()/3;
                break;
            case Config.STATUS_PE:
                mCurrentRadius = getWidth()/5;
                break;
            case Config.STATUS_PS:
                mCurrentRadius = getWidth()/12;
                break;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mCurrentStatus == Config.STATUS_PB || mCurrentStatus == Config.STATUS_PE) {
            canvas.drawCircle(mCurrentWidth, mCurrentHeight, mCurrentRadius, mPaint);
        }
    }

    public void setStatus(int status) {
        mCurrentStatus = status;
        switch (status) {
            case Config.STATUS_PB:
                mPaint.setColor(mColorPb);
                break;
            case Config.STATUS_PE:
                mPaint.setColor(mColorPe);
                break;
            case Config.STATUS_PS:
                mPaint.setColor(mColorPs);
                break;
        }
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(8);
    }
}
