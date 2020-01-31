package com.a78.com.fmlearn.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;


/**
 * Created by home on 2020/1/31.
 */

public class RoundRectImageView extends AppCompatImageView{
    private float roundRation = 0.1f;
    private Path path;

    public RoundRectImageView(Context context,  AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (path == null){
            path = new Path();
            path.addRoundRect(new RectF(0, 0, getWidth(), getHeight()), roundRation * getWidth(), roundRation * getHeight(), Path.Direction.CW);
        }
        canvas.save();
        canvas.clipPath(path);
        super.onDraw(canvas);
        canvas.restore();
    }
}
