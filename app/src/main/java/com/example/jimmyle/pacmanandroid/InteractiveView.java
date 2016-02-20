package com.example.jimmyle.pacmanandroid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

public class InteractiveView extends View{
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
    private float x1, x2, y1, y2;
    private int direction;

    public InteractiveView(Context context) {
        super(context);
        loadBitmapImages();
        frameTicker = 1000/totalFrame;
        paint = new Paint();
    }

    // Similar to java paintComponent method
    // Canvas is like the graphics object in java
    @Override
    public void onDraw(Canvas canvas) {
        // Set background color to black
        canvas.drawColor(Color.BLACK);

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
        if (xPosPacman >= canvas.getWidth()) {
            xPosPacman = canvas.getWidth();
        }
        if (yPosPacman >= canvas.getHeight()) {
            yPosPacman = canvas.getHeight();
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case (MotionEvent.ACTION_DOWN): {
                x1 = event.getX();
                y1 = event.getY();
                break;
            }
            case (MotionEvent.ACTION_UP): {
                x2 = event.getX();
                y2 = event.getY();
                calculateSwipeDirection();
                break;
            }
        }
        return true;
    }

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
            else {
                direction = 2;
            }
        }
        else {
            if (xDiff < 0) {
                direction = 3;
            }
            else {
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
        pacmanRight = new Bitmap[totalFrame];
        pacmanRight[0] = BitmapFactory.decodeResource(getResources(),R.drawable.pacman_right1);
        pacmanRight[1] = BitmapFactory.decodeResource(getResources(),R.drawable.pacman_right2);
        pacmanRight[2] = BitmapFactory.decodeResource(getResources(),R.drawable.pacman_right3);
        pacmanRight[3] = BitmapFactory.decodeResource(getResources(),R.drawable.pacman_right);
        // Add bitmap images of pacman facing down
        pacmanDown = new Bitmap[totalFrame];
        pacmanDown[0] = BitmapFactory.decodeResource(getResources(),R.drawable.pacman_down1);
        pacmanDown[1] = BitmapFactory.decodeResource(getResources(),R.drawable.pacman_down2);
        pacmanDown[2] = BitmapFactory.decodeResource(getResources(),R.drawable.pacman_down3);
        pacmanDown[3] = BitmapFactory.decodeResource(getResources(),R.drawable.pacman_down);
        // Add bitmap images of pacman facing left
        pacmanLeft = new Bitmap[totalFrame];
        pacmanLeft[0] = BitmapFactory.decodeResource(getResources(),R.drawable.pacman_left1);
        pacmanLeft[1] = BitmapFactory.decodeResource(getResources(),R.drawable.pacman_left2);
        pacmanLeft[2] = BitmapFactory.decodeResource(getResources(),R.drawable.pacman_left3);
        pacmanLeft[3] = BitmapFactory.decodeResource(getResources(),R.drawable.pacman_left);
        // Add bitmap images of pacman facing up
        pacmanUp = new Bitmap[totalFrame];
        pacmanUp[0] = BitmapFactory.decodeResource(getResources(),R.drawable.pacman_up1);
        pacmanUp[1] = BitmapFactory.decodeResource(getResources(),R.drawable.pacman_up2);
        pacmanUp[2] = BitmapFactory.decodeResource(getResources(),R.drawable.pacman_up3);
        pacmanUp[3] = BitmapFactory.decodeResource(getResources(),R.drawable.pacman_up);

        ghostBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ghost);
    }

}
