package com.example.jimmyle.pacmanandroid;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class PlayActivity extends Activity {
    private DrawingView drawingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawingView = new DrawingView(this);
        setContentView(drawingView);
    }


    @Override
    protected void onPause() {
        Log.i("info", "onPause");
        super.onPause();
        drawingView.pause();
    }

    @Override
    protected void onResume() {
        Log.i("info", "onResume");
        super.onResume();
        drawingView.resume();
    }
}
