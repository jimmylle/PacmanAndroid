package com.example.jimmyle.pacmanandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class HelpActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_layout);
        startService(new Intent(this, BackgroundMusicService.class));
    }

    @Override
    public void onPause() {
        super.onPause();
        stopService(new Intent(this, BackgroundMusicService.class));
    }

    @Override
    public void onResume() {
        super.onResume();
        startService(new Intent(this, BackgroundMusicService.class));
    }
}
