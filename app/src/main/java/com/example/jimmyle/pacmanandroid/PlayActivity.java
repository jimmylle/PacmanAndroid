package com.example.jimmyle.pacmanandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class PlayActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = new MapView(this);
        setContentView(view);
    }
}
