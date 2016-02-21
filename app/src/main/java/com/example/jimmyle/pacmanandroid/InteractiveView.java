package com.example.jimmyle.pacmanandroid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class InteractiveView extends View {
    private Paint paint;
    private Bitmap[] pacmanRight, pacmanDown, pacmanLeft, pacmanUp, currentPacman;
    private Bitmap ghostBitmap;
    private int totalFrame = 4;             // Total amount of frames fo each direction
    private int currentFrame = 0;           // Current frame to draw
    private long frameTicker;               // Current time frame has been drawn
    private float xPosPacman = 5.0f;        // x-axis position of pacman
    private float yPosPacman = 5.0f;        // y-axis position of pacman
    private float xPosGhost = 5.0f;         // x-axis position of ghost
    private float yPosGhost = 5.0f;         // y-axis position of ghost
    private float x1, x2, y1, y2;           // Initial/Final positions of swipe
    private float densityDPI;               // DPI of the screen
    private int direction;                  // Direction of the swipe
    private int screenWidth;
    private int screenHeight;
    public static int LONG_PRESS_TIME = 500; // Time in miliseconds
    final Handler handler = new Handler();
    private Toast toast;

    public InteractiveView(Context context) {
        super(context);
        frameTicker = 1000/totalFrame;
        paint = new Paint();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        densityDPI = metrics.density;
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        loadBitmapImages();
    }

    // Similar to java paintComponent method
    // Canvas is like the graphics object in java
    @Override
    public void onDraw(Canvas canvas) {
        // Set background color to black
        canvas.drawColor(Color.TRANSPARENT);

        update(System.currentTimeMillis());
        canvas.drawBitmap(ghostBitmap, xPosGhost, yPosGhost, paint);
        // Draws and moves the pacman based on direction
        if(direction == 0) {
            canvas.drawBitmap(pacmanUp[currentFrame], xPosPacman, yPosPacman, paint);
            yPosPacman += -5;
        }
        else if (direction == 1) {
            canvas.drawBitmap(pacmanRight[currentFrame], xPosPacman, yPosPacman, paint);
            xPosPacman += 5;
        }
        else if (direction == 2) {
            canvas.drawBitmap(pacmanDown[currentFrame], xPosPacman, yPosPacman, paint);
            yPosPacman += 5;
        }
        else if( direction == 3) {
            canvas.drawBitmap(pacmanLeft[currentFrame], xPosPacman, yPosPacman, paint);
            xPosPacman += -5;
        }
        // Boundary checking
        if (xPosPacman >= canvas.getWidth() - (screenWidth/20)) {
            xPosPacman = canvas.getWidth() - (screenWidth/20);
        }
        if (yPosPacman >= canvas.getHeight() -(screenWidth/20)) {
            yPosPacman = canvas.getHeight() -(screenWidth/20);
        }
        if (xPosPacman <= 0) {
            xPosPacman = 0;
        }
        if (yPosPacman <= 0) {
            yPosPacman = 0;
        }

        xPosGhost += 5.0f;
        if (xPosGhost >= canvas.getWidth()) {
            xPosGhost = 5.0f;
            yPosGhost += 50.0f;
        }

        if (yPosGhost >= canvas.getHeight()) {
            yPosGhost = 5.0f;
        }
        // Similar to java repaint() method, forces the canvas to redraw
        invalidate();
    }

    Runnable longPressed = new Runnable() {
        public void run() {
            Log.i("info", "LongPress");
            toast = Toast.makeText(getContext(), "Long Clicked", Toast.LENGTH_SHORT);
            toast.show();
        }
    };

//     Method to get touch events
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case (MotionEvent.ACTION_DOWN): {
                x1 = event.getX();
                y1 = event.getY();
                handler.postDelayed(longPressed, LONG_PRESS_TIME);
                break;
            }
            case (MotionEvent.ACTION_UP): {
                x2 = event.getX();
                y2 = event.getY();
                calculateSwipeDirection();
                handler.removeCallbacks(longPressed);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                handler.removeCallbacks(longPressed);
            }
        }
        return true;
    }

    // Calculates which direction the user swipes
    // based on calculating the differences in
    // initial position vs final position of the swipe
    private void calculateSwipeDirection() {
        float xDiff = (x2 - x1);
        float yDiff = (y2 - y1);

        // Directions
        // 0 means going up
        // 1 means going right
        // 2 means going down
        // 3 means going left

        // Checks which axis has the greater distance
        // in order to see which direction the swipe is
        if (Math.abs(yDiff) > Math.abs(xDiff)) {
            if (yDiff < 0) {
                direction = 0;
            }
            else if (yDiff > 0){
                direction = 2;
            }
        }
        else {
            if (xDiff < 0) {
                direction = 3;
            }
            else if (xDiff > 0){
                direction = 1;
            }
        }
    }

    // Check to see if we should update the current frame
    // based on time passed so the animation won't be too
    // quick and look bad
    private void update(long gameTime) {
        // If enough time has passed go to next frame
        if (gameTime > frameTicker + (totalFrame * 30)) {
            frameTicker = gameTime;

            // Increment the frame
            currentFrame++;
            // Loop back the frame when you have gone through all the frames
            if (currentFrame >= totalFrame) {
                currentFrame = 0;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        this.setMeasuredDimension(parentWidth, parentHeight);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void loadBitmapImages() {
        // Add bitmap images of pacman facing right
        int blockSize = screenWidth/20;
        pacmanRight = new Bitmap[totalFrame];
        pacmanRight[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(),R.drawable.pacman_right1), blockSize ,blockSize, false);
        pacmanRight[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_right2), blockSize, blockSize, false);
        pacmanRight[2] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_right3), blockSize, blockSize, false);
        pacmanRight[3] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_right), blockSize, blockSize, false);
        // Add bitmap images of pacman facing down
        pacmanDown = new Bitmap[totalFrame];
        pacmanDown[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_down1), blockSize, blockSize, false);
        pacmanDown[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_down2), blockSize, blockSize, false);
        pacmanDown[2] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_down3), blockSize, blockSize, false);
        pacmanDown[3] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_down), blockSize, blockSize, false);
        // Add bitmap images of pacman facing left
        pacmanLeft = new Bitmap[totalFrame];
        pacmanLeft[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_left1), blockSize, blockSize, false);
        pacmanLeft[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_left2), blockSize, blockSize, false);
        pacmanLeft[2] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_left3), blockSize, blockSize, false);
        pacmanLeft[3] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_left), blockSize, blockSize, false);
        // Add bitmap images of pacman facing up
        pacmanUp = new Bitmap[totalFrame];
        pacmanUp[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_up1), blockSize, blockSize, false);
        pacmanUp[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_up2), blockSize, blockSize, false);
        pacmanUp[2] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_up3), blockSize, blockSize, false);
        pacmanUp[3] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_up), blockSize, blockSize, false);

        ghostBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.ghost), blockSize, blockSize, false);
    }
}
