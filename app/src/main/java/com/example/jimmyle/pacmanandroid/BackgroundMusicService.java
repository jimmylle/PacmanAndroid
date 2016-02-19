package com.example.jimmyle.pacmanandroid;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class BackgroundMusicService extends Service {
    MediaPlayer player;

    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        player = MediaPlayer.create(this,R.raw.pacman_song);
        player.setLooping(true);
        player.setVolume(100,100);
        player.start();
    }

//    public int onStartCommand(Intent intent, int flags, int startID) {
//        player.start();
//        return 1;
//    }

    @Override
    public void onDestroy() {
        player.stop();
        player.release();
    }

}
