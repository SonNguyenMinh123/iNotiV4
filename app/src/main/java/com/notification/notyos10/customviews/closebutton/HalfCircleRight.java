package com.notification.notyos10.customviews.closebutton;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class HalfCircleRight extends View{

    public HalfCircleRight(Context context) {
        super(context);
    }
    public HalfCircleRight(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HalfCircleRight(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float width = (float)getWidth();
        float height = (float)getHeight();
        float radius;
        if (width > height){
            radius = height/2;
        }else{
            radius = width/2;
        }
        Path path = new Path();
        path.addCircle(width, height, radius, Path.Direction.CW);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(0);
        paint.setStyle(Paint.Style.FILL);
        float center_x, center_y;
        final RectF oval = new RectF();
        center_x = width/2;
        center_y = height/2;
        oval.set(center_x - radius,
                center_y - radius,
                center_x + radius,
                center_y + radius);
        canvas.drawArc(oval, 90, 180, false, paint);
    }
}
