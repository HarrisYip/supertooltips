package com.nhaarman.supertooltips;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hyip on 2016-11-03.
 */

public class DownTriangleShapeView extends View
{
    private int _color = 0;
    private int _borderColor = 0;
    private int _width = 0;
    private int _tipArcSize = 0;
    private boolean _showBorder;

    public DownTriangleShapeView(Context context)
    {
        super(context);
    }

    public DownTriangleShapeView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public DownTriangleShapeView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        int color = _color != 0 ? _color : Color.WHITE;
        int borderColor = _borderColor != 0 ? _borderColor : Color.BLACK;

        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
        int newTriangleWidth = (int) (((getHeight() - _tipArcSize) / (float) getHeight()) * (getWidth() / 2f));

        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(newTriangleWidth, getHeight() - _tipArcSize);
        path.quadTo(getWidth() / 2, getHeight(), getWidth() - newTriangleWidth, getHeight() - _tipArcSize);
        path.lineTo(getWidth(), 0);
        path.lineTo(0, 0);

        Paint p = new Paint();
        p.setColor(color);
        p.setAntiAlias(true);

        canvas.drawPath(path, p);

        if (_showBorder) {
            int borderWidth = _width > 0 ? _width : 2;
            Paint borderPaint = new Paint();
            borderPaint.setColor(borderColor);
            borderPaint.setStrokeWidth(borderWidth);
            borderPaint.setStyle(Paint.Style.STROKE);
            borderPaint.setAntiAlias(true);

            Path borderPath = new Path();
            borderPath.moveTo(0, Math.abs(layoutParams.topMargin) - 1);
            borderPath.lineTo(newTriangleWidth, getHeight() - _tipArcSize);
            borderPath.quadTo(getWidth() / 2, getHeight(), getWidth() - newTriangleWidth, getHeight() - _tipArcSize);
            borderPath.lineTo(getWidth(), Math.abs(layoutParams.topMargin) - 1);

            canvas.drawPath(borderPath, borderPaint);
        }
    }

    public void setColor(int color)
    {
        _color = color;
    }

    public void setBorderColor(int borderColor)
    {
        _borderColor = borderColor;
    }

    public void setBorderWidth(int width)
    {
        _width = width;
    }

    public void setShowBorder(boolean showBorder)
    {
        _showBorder = showBorder;
    }

    public void setTipArcSize(int tipArcSize)
    {
        _tipArcSize = tipArcSize;
    }
}
