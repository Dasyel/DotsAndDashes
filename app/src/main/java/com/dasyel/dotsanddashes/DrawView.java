package com.dasyel.dotsanddashes;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class DrawView extends View implements OnTouchListener {
    private static final String TAG = "DrawView";

    private float stepSize;
    private List<Point> points = new ArrayList<>();
    private Point selectedPoint = null;
    private List<Dash> dashes = new ArrayList<>();
    private List<Box> boxes = new ArrayList<>();
    private Paint defaultPaint = new Paint();
    private Paint bluePaint = new Paint();
    private Paint redPaint = new Paint();
    private Paint currentPaint = bluePaint;
    private Paint whitePaint = new Paint();
    private Paint textPaint = new Paint();
    private Rect turnRect;
    private int redBoxes = 0;
    private int blueBoxes = 0;
    private boolean gameDone = false;

    public DrawView(Context context) {
        super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setOnTouchListener(this);

        defaultPaint.setColor(Color.BLACK);
        defaultPaint.setAntiAlias(true);
        bluePaint.setColor(Color.BLUE);
        bluePaint.setAntiAlias(true);
        bluePaint.setStrokeWidth(10);
        redPaint.setColor(Color.RED);
        redPaint.setAntiAlias(true);
        redPaint.setStrokeWidth(10);
        whitePaint.setColor(Color.WHITE);
        whitePaint.setAntiAlias(true);
        textPaint.setColor(Color.BLACK);
        textPaint.setAntiAlias(true);
        //textPaint.setStrokeWidth(50);
        textPaint.setTextSize(80);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawColor(currentPaint.getColor());
        canvas.drawRect(turnRect, whitePaint);
        float textPadding = 40 + textPaint.getTextSize() / 2;
        float textDistance = 20 + textPaint.getTextSize();
        canvas.drawText("Blue: " + blueBoxes, textPadding, textPadding, textPaint);
        canvas.drawText("Red: " + redBoxes, textPadding, textPadding + textDistance, textPaint);
        if (gameDone){
            canvas.drawText("Game Over! Touch to start again!", textPadding,
                    canvas.getHeight() - 2 * textPadding, textPaint);
        }
        for (Box box : boxes){
            canvas.drawRect(box.x, box.y, box.x + box.sideLength, box.y + box.sideLength, box.paint);
        }
        for (Dash dash : dashes){
            canvas.drawLine(dash.a.x, dash.a.y, dash.b.x, dash.b.y, dash.paint);
        }
        for (Point point : points) {
            canvas.drawCircle(point.x, point.y, point.radius, defaultPaint);
            Log.d(TAG, "Painting: "+point);
        }
        if (selectedPoint != null) {
            canvas.drawCircle(selectedPoint.x, selectedPoint.y, selectedPoint.radius, currentPaint);
        }
    }

    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN){
            return super.onTouchEvent(event);
        }
        if (gameDone){
            resetGame();
            invalidate();
            return true;
        }
        int x = (int) event.getX();
        int y = (int) event.getY();
        int touchSize = (int) (stepSize / 2) - 10;
        Rect touchRect = new Rect(x-touchSize, y-touchSize, x+touchSize, y+touchSize);
        Rect pointRect;
        for(Point point : this.points){
            Log.d(TAG, "checking" + touchRect.toString() + " with " + point);
            int pointX = (int) point.x;
            int pointY = (int) point.y;
            int pointRadius = (int) point.radius;
            pointRect = new Rect(pointX - pointRadius, pointY - pointRadius, pointX + pointRadius, pointY + pointRadius);
            if (Rect.intersects(touchRect, pointRect)){
                if(selectedPoint == null) {
                    selectedPoint = point;
                    Log.d(TAG, "selecting point: " + point);
                } else if (selectedPoint == point){
                    selectedPoint = null;
                } else if (selectedPoint.x == point.x || selectedPoint.y == point.y){
                    float distance = (selectedPoint.y - point.y) + (selectedPoint.x - point.x);
                    if (distance <= stepSize + 5 && !dashExists(selectedPoint, point)) {
                        this.addDash(selectedPoint, point, currentPaint);
                        selectedPoint = null;
                    }
                }
                break;
            }
        }
        gameDone = true;
        for (Box box : boxes){
            if (box.paint != bluePaint && box.paint != redPaint){
                gameDone = false;
            }
        }
        invalidate();
        return true;
    }

    public void addDash(Point a, Point b, Paint paint){
        Dash dash = new Dash(a, b, paint);
        boolean boxFilled = false;
        this.dashes.add(dash);
        int dashX = (int) dash.centerX;
        int dashY = (int) dash.centerY;
        Rect dashRect = new Rect(dashX - 15, dashY - 15, dashX + 15, dashY + 15);
        Rect boxRect;
        int boxX, boxY, boxSide;
        for (Box box : this.boxes){
            boxX = (int) box.x;
            boxY = (int) box.y;
            boxSide = (int) box.sideLength;
            boxRect = new Rect(boxX, boxY, boxX + boxSide, boxY + boxSide);
            if (Rect.intersects(dashRect, boxRect)){
                box.dashes++;
                if (box.dashes == 4){
                    box.paint = currentPaint;
                    boxFilled = true;
                    if (currentPaint == bluePaint){
                        blueBoxes++;
                    } else {
                        redBoxes++;
                    }
                }
            }
        }
        if (!boxFilled) {
            if (currentPaint == bluePaint) {
                currentPaint = redPaint;
            } else {
                currentPaint = bluePaint;
            }
        }
    }

    public void addBox(float x, float y, float radius){
        this.boxes.add(new Box(x, y, radius));
    }

    public void addPoint(float x, float y, float radius){
        this.points.add(new Point(x, y, radius));
    }

    public void setStepSize(float stepSize){
        this.stepSize = stepSize;
    }

    public void setTurnRect(int left, int top, int right, int bottom){
        this.turnRect = new Rect(left, top, right, bottom);
    }

    private boolean dashExists(Point a, Point b){
        for (Dash dash : dashes){
            if (dash.a == a || dash.a == b){
                if (dash.b == a || dash.b == b){
                    return true;
                }
            }
        }
        return false;
    }

    private void resetGame(){
        blueBoxes = 0;
        redBoxes = 0;
        for (Box box : boxes){
            box.paint = whitePaint;
            box.dashes = 0;
        }
        dashes = new ArrayList<>();
        this.gameDone = false;
    }
}