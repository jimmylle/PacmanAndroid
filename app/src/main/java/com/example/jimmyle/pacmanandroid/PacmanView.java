package com.example.jimmyle.pacmanandroid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class PacmanView extends View{
    private Paint paint;
    private Bitmap bitmap;

    public PacmanView(Context context) {
        super(context);
        paint = new Paint();
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.pacman_right1);
    }

    @Override
    public void onDraw(Canvas canvas) {
        // Set background color to black
        canvas.drawColor(Color.BLACK);
        canvas.drawBitmap(bitmap, canvas.getWidth()/2, canvas.getHeight()/2, paint);

        // Similar to java repaint() method, forces the canvas to redraw
        invalidate();
    }

}
