package com.dasyel.dotsanddashes;

import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;


public class GameActivity extends ActionBarActivity {
    public DrawView drawView;
    private static final int GRIDSIZE = 10;
    private static final float POINT_RADIUS = 15;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.drawView = new DrawView(this);
        setContentView(this.drawView);
        this.drawView.requestFocus();
        ViewTreeObserver vto = this.drawView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                LayerDrawable ld = (LayerDrawable)drawView.getBackground();
                //ld.setLayerInset(1, 0, drawView.getHeight() / 2, 0, 0);

                float width = drawView.getWidth();
                float widthPadding = width/20;
                width -= 2 * widthPadding;
                float height = drawView.getHeight();
                float heightPadding = (height - width) / 2;
                float stepSize = width/(GRIDSIZE-1);
                drawView.setStepSize(stepSize);
                drawView.setTurnRect((int) widthPadding - 20, (int) heightPadding - 20,
                        (int) (width + widthPadding + 20), (int) (height - heightPadding + 20));
                for (int i = 0; i < GRIDSIZE; i++){
                    for (int j = 0; j < GRIDSIZE; j++) {
                        float x = i * stepSize + widthPadding;
                        float y = j * stepSize + heightPadding;
                        drawView.addPoint(x, y, POINT_RADIUS);
                        if (i < GRIDSIZE - 1 && j < GRIDSIZE - 1) {
                            drawView.addBox(x, y, stepSize);
                        }
                    }
                }

                ViewTreeObserver obs = drawView.getViewTreeObserver();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    obs.removeOnGlobalLayoutListener(this);
                } else {
                    obs.removeGlobalOnLayoutListener(this);
                }
            }
        });
    }


}
