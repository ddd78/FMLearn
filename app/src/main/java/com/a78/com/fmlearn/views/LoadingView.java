package com.a78.com.fmlearn.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.a78.com.fmlearn.R;

/**
 * Created by home on 2020/2/2.
 */

@SuppressLint("AppCompatCustomView")
public class LoadingView extends ImageView {

    private int retateDegree = 0;

    private boolean isNeedRotate = false;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setImageResource(R.mipmap.loading);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isNeedRotate = true;
        post(new Runnable() {
            @Override
            public void run() {
                retateDegree -= 30;
                retateDegree = retateDegree >= 0  ? retateDegree : 360;
                invalidate();
                if (isNeedRotate){
                    postDelayed(this,50);
                }
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isNeedRotate = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(retateDegree, getWidth()/2, getHeight()/2);
        super.onDraw(canvas);
    }
}
