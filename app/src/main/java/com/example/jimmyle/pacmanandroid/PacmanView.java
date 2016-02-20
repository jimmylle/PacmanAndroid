package com.example.jimmyle.pacmanandroid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

public class PacmanView extends View{
    private Paint paint;
    private Bitmap[] pacmanRight, pacmanDown, pacmanLeft, pacmanUp, currentPacman;
    private int totalFrame = 4;     // Total amount of frames fo each direction
    private int currentFrame = 0;   // Current frame to draw
    private long frameTicker;       // Current time frame has been drawn
    private float xPos = 5;         // x-axis position of pacman
    private float yPos = 5;         // y-axis position of pacman

    public PacmanView(Context context) {
        super(context);
        loadBitmapImages();
        frameTicker = 1000/totalFrame;
        paint = new Paint();
    }

    @Override
    public void onDraw(Canvas canvas) {
        // Set background color to black
        canvas.drawColor(Color.BLACK);

        update(System.currentTimeMillis());
        canvas.drawBitmap(pacmanRight[currentFrame], xPos, yPos, paint);

        // Only moves in the x axis for now
        xPos += 5;
        yPos += 5;
        if (xPos >= canvas.getWidth()) {
            xPos = 5.0f;
        }
        if (yPos >= canvas.getHeight()) {
            yPos = 5.0f;
        }
        // Similar to java repaint() method, forces the canvas to redraw
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return true;
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
    }

}
