package com.example.jimmyle.pacmanandroid;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

public class PlayActivity extends Activity {
    static PlayActivity activity;
    private SharedPreferences sharedPreferences;
    private DrawingView drawingView;
    private Globals globals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawingView = new DrawingView(this);
        setContentView(drawingView);
        activity = this;
        globals = Globals.getInstance();
        sharedPreferences = getSharedPreferences("info", MODE_PRIVATE);
        int temp = sharedPreferences.getInt("high_score",0);
        globals.setHighScore(temp);
    }

    @Override
    protected void onPause() {
        Log.i("info", "onPause");
        super.onPause();
        drawingView.pause();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("high_score", globals.getHighScore());
        editor.apply();
        MainActivity.getPlayer().pause();
    }

    @Override
    protected void onResume() {
        Log.i("info", "onResume");
        super.onResume();
        drawingView.resume();
        MainActivity.getPlayer().start();

    }

    public static PlayActivity getInstance() {
        return activity;
    }

}
