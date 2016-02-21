package com.example.jimmyle.pacmanandroid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class InteractiveView extends View {
    private Paint paint;
    private Bitmap[] pacmanRight, pacmanDown, pacmanLeft, pacmanUp, currentPacman;
    private Bitmap ghostBitmap;
    private int totalFrame = 4;             // Total amount of frames fo each direction
    private int currentFrame = 0;           // Current frame to draw
    private long frameTicker;               // Current time frame has been drawn
    private int xPosPacman;                 // x-axis position of pacman
    private int yPosPacman;                 // y-axis position of pacman
    private float xPosGhost = 5.0f;         // x-axis position of ghost
    private float yPosGhost = 5.0f;         // y-axis position of ghost
    private float x1, x2, y1, y2;           // Initial/Final positions of swipe
    private int direction = 4;              // Direction of the swipe, initial direction is right
    private int nextDirection = 4;          // Buffer for the next direction you choose
    private int screenWidth;                // Width of the phone screen
    private int blockSize;                  // Size of a block on the map
    public static int LONG_PRESS_TIME = 500; // Time in milliseconds
    final Handler handler = new Handler();

    public InteractiveView(Context context) {
        super(context);
        frameTicker = 1000/totalFrame;
        paint = new Paint();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        screenWidth = metrics.widthPixels;
        blockSize = screenWidth/17;
        blockSize = (blockSize / 5) * 5;
        xPosPacman = 8 * blockSize;
        yPosPacman = 13 * blockSize;
        loadBitmapImages();
    }

    // Similar to java paintComponent method
    // Canvas is like the graphics object in java
    @Override
    public void onDraw(Canvas canvas) {
        // Set background color to black
        canvas.drawColor(Color.TRANSPARENT);

        updateFrame(System.currentTimeMillis());
        canvas.drawBitmap(ghostBitmap, xPosGhost, yPosGhost, paint);

        // Draws and moves the pacman based on direction
        move(canvas);
        // Similar to java repaint() method, forces the canvas to redraw
        invalidate();
    }

    // Updates the character sprite and handles collisions
    public void move(Canvas canvas) {
        short ch;

        if ( (xPosPacman % blockSize == 0) && (yPosPacman  % blockSize == 0 ) ) {

            ch = leveldata1[yPosPacman / blockSize][xPosPacman / blockSize];

            if (!((nextDirection == 3 && (ch & 1) != 0) ||
                    (nextDirection == 1 && (ch & 4) != 0) ||
                    (nextDirection == 0 && (ch & 2) != 0) ||
                    (nextDirection == 2 && (ch & 8) != 0))) {
                direction = nextDirection;
            }

            if ((direction == 3 && (ch & 1) != 0) ||
                    (direction == 1 && (ch & 4) != 0) ||
                    (direction == 0 && (ch & 2) != 0) ||
                    (direction == 2 && (ch & 8) != 0)) {
                direction = 4;
            }
        }

        if (xPosPacman < 0) {
            xPosPacman = blockSize * 17;
        }
        if (xPosPacman > blockSize * 17) {
            xPosPacman = 0;
        }

        // Depending on the direction, draw the appropriate sprite image
        if (direction == 0) {
            canvas.drawBitmap(pacmanUp[currentFrame], xPosPacman, yPosPacman, paint);
            yPosPacman += -blockSize/15;
        } else if (direction == 1) {
            canvas.drawBitmap(pacmanRight[currentFrame], xPosPacman, yPosPacman, paint);
            xPosPacman += blockSize/15;
        } else if (direction == 2) {
            canvas.drawBitmap(pacmanDown[currentFrame], xPosPacman, yPosPacman, paint);
            yPosPacman += blockSize/15;
        } else if (direction == 3) {
            canvas.drawBitmap(pacmanLeft[currentFrame], xPosPacman, yPosPacman, paint);
            xPosPacman += -blockSize/15;
        } else {
            canvas.drawBitmap(pacmanDown[0], xPosPacman, yPosPacman, paint);
        }

        xPosGhost += 5.0f;
        if (xPosGhost >= canvas.getWidth()) {
            xPosGhost = 5.0f;
            yPosGhost += 50.0f;
        }

        if (yPosGhost >= canvas.getHeight()) {
            yPosGhost = 5.0f;
        }
    }

    Runnable longPressed = new Runnable() {
        public void run() {
            Log.i("info", "LongPress");
            Intent pauseIntent = new Intent(getContext(), PauseActivity.class);
            getContext().startActivity(pauseIntent);
        }
    };

    // Method to get touch events
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
        // 4 means stop moving, look at move function

        // Checks which axis has the greater distance
        // in order to see which direction the swipe is
        // going to be (buffering of direction)
        if (Math.abs(yDiff) > Math.abs(xDiff)) {
            if (yDiff < 0) {
                nextDirection = 0;
            } else if (yDiff > 0) {
                nextDirection = 2;
            }
        } else {
            if (xDiff < 0) {
                nextDirection = 3;
            } else if (xDiff > 0) {
                nextDirection = 1;
            }
        }
    }

    // Check to see if we should update the current frame
    // based on time passed so the animation won't be too
    // quick and look bad
    private void updateFrame(long gameTime) {
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
        int spriteSize = screenWidth/17;
        spriteSize = (spriteSize / 5) * 5;
        pacmanRight = new Bitmap[totalFrame];
        pacmanRight[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(),R.drawable.pacman_right1), spriteSize, spriteSize, false);
        pacmanRight[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_right2), spriteSize, spriteSize, false);
        pacmanRight[2] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_right3), spriteSize, spriteSize, false);
        pacmanRight[3] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_right), spriteSize, spriteSize, false);
        // Add bitmap images of pacman facing down
        pacmanDown = new Bitmap[totalFrame];
        pacmanDown[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_down1), spriteSize, spriteSize, false);
        pacmanDown[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_down2), spriteSize, spriteSize, false);
        pacmanDown[2] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_down3), spriteSize, spriteSize, false);
        pacmanDown[3] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_down), spriteSize, spriteSize, false);
        // Add bitmap images of pacman facing left
        pacmanLeft = new Bitmap[totalFrame];
        pacmanLeft[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_left1), spriteSize, spriteSize, false);
        pacmanLeft[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_left2), spriteSize, spriteSize, false);
        pacmanLeft[2] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_left3), spriteSize, spriteSize, false);
        pacmanLeft[3] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_left), spriteSize, spriteSize, false);
        // Add bitmap images of pacman facing up
        pacmanUp = new Bitmap[totalFrame];
        pacmanUp[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_up1), spriteSize, spriteSize, false);
        pacmanUp[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_up2), spriteSize, spriteSize, false);
        pacmanUp[2] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_up3), spriteSize, spriteSize, false);
        pacmanUp[3] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_up), spriteSize, spriteSize, false);

        ghostBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.ghost), spriteSize, spriteSize, false);
    }

    final short leveldata1[][] = new short[][]{
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {19, 26, 26, 18, 26, 26, 26, 22, 0, 19, 26, 26, 26, 18, 26, 26, 22},
            {21, 0, 0, 21, 0, 0, 0, 21, 0, 21, 0, 0, 0, 21, 0, 0, 21},
            {17, 26, 26, 16, 26, 18, 26, 24, 26, 24, 26, 18, 26, 16, 26, 26, 20},
            {25, 26, 26, 20, 0, 25, 26, 22, 0, 19, 26, 28, 0, 17, 26, 26, 28},
            {0, 0, 0, 21, 0, 0, 0, 21, 0, 21, 0, 0, 0, 21, 0, 0, 0},
            {0, 0, 0, 21, 0, 19, 26, 24, 26, 24, 26, 22, 0, 21, 0, 0, 0},
            {26, 26, 26, 16, 26, 20, 0, 0, 0, 0, 0, 17, 26, 16, 26, 26, 26},
            {0, 0, 0, 21, 0, 17, 26, 26, 26, 26, 26, 20, 0, 21, 0, 0, 0},
            {0, 0, 0, 21, 0, 21, 0, 0, 0, 0, 0, 21, 0, 21, 0, 0, 0},
            {19, 26, 26, 16, 26, 24, 26, 22, 0, 19, 26, 24, 26, 16, 26, 26, 22},
            {21, 0, 0, 21, 0, 0, 0, 21, 0, 21, 0, 0, 0, 21, 0, 0, 21},
            {25, 22, 0, 21, 0, 0, 0, 17, 2, 20, 0, 0, 0, 21, 0, 19, 28}, // "2" in this line stands for where the pacman spawn
            {0, 21, 0, 17, 26, 26, 18, 24, 24, 24, 18, 26, 26, 20, 0, 21, 0},
            {19, 24, 26, 28, 0, 0, 25, 18, 26, 18, 28, 0, 0, 25, 26, 24, 22},
            {21, 0, 0, 0, 0, 0, 0, 21, 0, 21, 0, 0, 0, 0, 0, 0, 21},
            {25, 26, 26, 26, 26, 26, 26, 24, 26, 24, 26, 26, 26, 26, 26, 26, 28},
    };
}