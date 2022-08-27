 package com.more_high.yodhha;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

 public class Recording extends AppCompatActivity {
     LinearLayout audioRecorder,videoRecorderBack,videoRecorderFront;
     ImageView fileview,videoplayback, videoplayfront ,audioplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);


        Window window= getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.toolbartitlecolor));

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.backbutton);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        audioRecorder = findViewById(R.id.audiorecording);
        videoRecorderBack = findViewById(R.id.videorecordingback);
        videoRecorderFront = findViewById(R.id.videorecordingfront);
        fileview = findViewById(R.id.imgfolder);
        videoplayback = findViewById(R.id.videoplayback);

        videoplayfront = findViewById(R.id.videoplayfront);
        audioplay = findViewById(R.id.audioplay);

        if(BackgroundService.recording== true){
            videoplayback.setImageResource(R.drawable.ic_baseline_stop_circle_24);

        }else{
            videoplayback.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
        }

        if(CameraFrontService.recording== true){
            videoplayfront.setImageResource(R.drawable.ic_baseline_stop_circle_24);

        }else{
            videoplayfront.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
        }


        videoplayback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (BackgroundService.recording == false) {
                    if(CameraFrontService.recording==true)
                    {
                        Intent recordIntent = new Intent(Recording.this, BackgroundService.class);
                        stopService(recordIntent);
                    }
                    videoplayback.setImageResource(R.drawable.ic_baseline_stop_circle_24);
                    Intent recordIntent = new Intent(Recording.this, BackgroundService.class);
//                    recordIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startService(recordIntent);
                } else {
                    videoplayback.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
                    Intent recordIntent = new Intent(Recording.this, BackgroundService.class);
//                    recordIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    stopService(recordIntent);
                }

            }
        });

        videoplayfront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (CameraFrontService.recording == false) {
                    if(BackgroundService.recording==true)
                    {
                        Intent recordIntent = new Intent(Recording.this, BackgroundService.class);
                        stopService(recordIntent);
                    }
                    videoplayfront.setImageResource(R.drawable.ic_baseline_stop_circle_24);
                    Intent recordIntent = new Intent(Recording.this, CameraFrontService.class);
//                    recordIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startService(recordIntent);
                } else {
                    videoplayfront.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
                    Intent recordIntent = new Intent(Recording.this, CameraFrontService.class);
//                    recordIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    stopService(recordIntent);
                }


            }
        });



        if(AudioRecorderService.active==true){
            audioplay.setImageResource(R.drawable.ic_baseline_stop_circle_24);
        }else{
            audioplay.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
        }


        audioplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AudioRecorderService.active==false){
                    audioplay.setImageResource(R.drawable.ic_baseline_stop_circle_24);
                    Intent audioIntent = new Intent(Recording.this,AudioRecorderService.class);
                    startService(audioIntent);
                }else{
                    audioplay.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
                    Intent audioIntent = new Intent(Recording.this,AudioRecorderService.class);
                    stopService(audioIntent);
                }

            }
        });



        fileview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Recording.this,Recording_List.class);
                startActivity(intent);

            }
        });

    }
}