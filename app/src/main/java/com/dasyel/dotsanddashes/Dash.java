package com.dasyel.dotsanddashes;

import android.graphics.Paint;

public class Dash {
    Point a, b;
    float centerX, centerY;
    Paint paint;

    public Dash(Point a, Point b, Paint paint){
        this.a = a;
        this.b = b;
        this.centerX = (a.x + b.x)/2;
        this.centerY = (a.y + b.y)/2;
        this.paint = paint;
    }

    @Override
    public String toString() {
        return a + ", " + b;
    }
}
