package com.example.jimmyle.pacmanandroid;

import android.app.Activity;
import android.os.Bundle;

public class HelpActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_layout);
        MainActivity.getPlayer().start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MainActivity.getPlayer().pause();
    }

}
