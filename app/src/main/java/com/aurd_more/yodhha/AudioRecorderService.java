package com.aurd_more.yodhha;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AudioRecorderService extends Service {
    private MediaRecorder recorder;
    public static boolean active = false;

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            startMyOwnForeground();
        }else
        {   startForeground(1, new Notification());

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground()
    {

        Intent notificationIntent = new Intent(this, LockScreen.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent=PendingIntent.getActivity(this, 0,
                notificationIntent,0 );

        String NOTIFICATION_CHANNEL_ID = "example.permanence";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("Your Device is Secured")
                .setContentText("Your Device is Secured")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(2, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        active = true;
        startRecording();
        Toast.makeText(this, "Audio Recording Started", Toast.LENGTH_SHORT).show();

        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }
    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }
//    @Override
//    public void onStart(Intent intent, int startId) {
//
//        super.onStart(intent, startId);
//    }

    @Override
    public void onDestroy() {
        active = false;
        stopRecording();
        Toast.makeText(this, "Audio Recording Stopped", Toast.LENGTH_SHORT).show();

        stopSelf();
        super.onDestroy();
    }

    private void startRecording(){
        System.out.println("audio recording--------------------");
        try {
            recorder = new MediaRecorder();

            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);


            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String reportDate = formatter.format(today);


//            File instanceRecordDirectory = new File(getExternalFilesDirs(null)
//                    + File.separator + "AntiTheft");

//            if(!instanceRecordDirectory.exists()){
//
//                instanceRecordDirectory.mkdirs();
//            }

            File instanceRecord = new File(getExternalFilesDir(null) + File.separator + reportDate + "_Recordsound.mp3");
            if(!instanceRecord.exists()){
                instanceRecord.createNewFile();
            }
            recorder.setOutputFile(instanceRecord.getAbsolutePath());

            recorder.prepare();
            recorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
//        if (recorder != null) {
//
//            recorder = null;
//        }
    }
}
