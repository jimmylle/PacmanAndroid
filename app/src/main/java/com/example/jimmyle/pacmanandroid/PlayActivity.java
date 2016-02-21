package com.example.jimmyle.pacmanandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

public class PlayActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frameLayout = new FrameLayout(this);
        View mapView = new MapView(this);
        View interactiveView = new InteractiveView(this);
        frameLayout.addView(mapView);
        frameLayout.addView(interactiveView);
        setContentView(frameLayout);
    }
}
