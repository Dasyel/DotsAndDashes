package com.dasyel.dotsanddashes;

class Point {
    float x, y, radius;

    public Point(float x, float y, float radius){
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    @Override
    public String toString() {
        return x + ", " + y;
    }
}
