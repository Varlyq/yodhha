package com.more_high.yodhha;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.util.Timer;
import java.util.TimerTask;

public class MusicService extends Service {
    private static final String TAG = null;
    MediaPlayer player;
    public static boolean active=false;
    public IBinder onBind(Intent arg0) {

        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        player = MediaPlayer.create(this, R.raw.music);
        player.setLooping(true); // Set looping
        player.setVolume(100,100);

    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        player.start();
        startTimer();
        active=true;
        return START_STICKY;
    }

    public void onStart(Intent intent, int startId) {
        // TO DO

    }
    public IBinder onUnBind(Intent arg0) {
        // TO DO Auto-generated method
        return null;
    }

    public void onStop() {

    }
    public void onPause() {

    }
    @Override
    public void onDestroy() {
        player.stop();
        player.release();
        stopTimerTask();
        active=false;
    }

    @Override
    public void onLowMemory() {

    }
private Timer timer;
    private TimerTask timerTask;

    public  void startTimer()
    {
        timer=new Timer() ;
        timerTask=new TimerTask(){
            public void run(){
                AudioManager audioManager= (AudioManager) getSystemService((Context.AUDIO_SERVICE));
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),0);
            }
        };
        timer.schedule(timerTask,0,1000);
    }

    public  void stopTimerTask(){
        if(timer!=null)
        {
            timer.cancel();
            timer=null;
        }
    }

}
