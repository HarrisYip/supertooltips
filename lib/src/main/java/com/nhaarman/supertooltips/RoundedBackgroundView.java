package com.nhaarman.supertooltips;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by hyip on 2016-11-03.
 */

public class RoundedBackgroundView extends FrameLayout
{
    private int _color = 0;
    private int _width = 0;
    private int _radius = -1;
    private int _borderColor = 0;
    private boolean _showBorder;

    public RoundedBackgroundView(Context context)
    {
        super(context);
    }

    public RoundedBackgroundView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public RoundedBackgroundView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        int width = _width > 0 ? _width : 2;
        int color = _color != 0 ? _color : Color.WHITE;
        int borderColor = _borderColor != 0 ? _borderColor : Color.BLACK;
        int radius = _radius >= 0 ? _radius : 20;

        Path p = new Path();
        RectF r = new RectF();
        Paint paint = new Paint();

        r.set(0, 0, getWidth(), getHeight());
        p.addRoundRect(r, radius, radius, Path.Direction.CCW);

        paint.setColor(color);
        paint.setAntiAlias(true);

        canvas.drawPath(p, paint);

        if (_showBorder) {
            Path p2 = new Path();
            r.set(1, 1, getWidth() - 1, getHeight() - 1);
            p2.addRoundRect(r, radius, radius, Path.Direction.CCW);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(borderColor);
            paint.setStrokeWidth(width);
            paint.setAntiAlias(true);
            canvas.drawPath(p2, paint);
        }
    }

    public void setColor(int color)
    {
        _color = color;
    }

    public void setBorderWidth(int width)
    {
        _width = width;
    }

    public void setRadius(int radius)
    {
        _radius = radius;
    }

    public void setBorderColor(int borderColor)
    {
        _borderColor = borderColor;
    }

    public void setShowBorder(boolean showBorder)
    {
        _showBorder = showBorder;
    }
}
