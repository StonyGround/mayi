package com.mayizaixian.myzx.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

import com.mayizaixian.myzx.R;

/**
 * Created by Administrator on 2016/6/23.
 */
public class TriangleView extends View {

    public TriangleView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.text_grey_dark));
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        Path path = new Path();
        path.moveTo(80, 200);// 此点为多边形的起点
        path.lineTo(120, 250);
        path.lineTo(80, 250);
        path.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path, paint);
    }
}
