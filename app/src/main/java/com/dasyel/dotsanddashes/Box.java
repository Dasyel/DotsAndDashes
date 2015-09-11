package com.dasyel.dotsanddashes;

import android.graphics.Color;
import android.graphics.Paint;

public class Box {
    float x, y;
    float sideLength;
    Paint paint;
    int dashes;

    public Box(Float x, Float y, Float sideLength){
        this.x = x;
        this.y = y;
        this.sideLength = sideLength;
        this.dashes = 0;
        this.paint = new Paint();
        this.paint.setColor(Color.WHITE);
        this.paint.setAntiAlias(true);
    }
}
