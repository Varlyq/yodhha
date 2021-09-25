package com.aurd_more.yodhha;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BackgroundService extends Service implements SurfaceHolder.Callback {
    public int counter=0;

    MediaPlayer mediaPlayer;
    boolean lock = false;
    private WindowManager windowManager;
    private SurfaceView surfaceView;
    private Camera camera = null;
    private MediaRecorder mediaRecorder = null;
    public static SurfaceView tempview;
    public  static  boolean recording = false;
    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {

            startMyOwnForeground();
//            mediaPlayer = MediaPlayer.create(this, R.raw.music);
//            mediaPlayer.setLooping(true); // Set looping
//            mediaPlayer.setVolume(100, 100);
//            System.out.println("adroid");
//
////           LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
////                   new BroadcastReceiver() {
////                       @Override
////                       public void onReceive(Context context, Intent intent) {
////                           System.out.println(intent.getStringExtra("value"));
////
////                           if(intent.getStringExtra("value")=="START"){
//////                               KeyguardManager myKM = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
//////                               if( myKM.inKeyguardRestrictedInputMode()) {
//////                                   System.out.println("locked "+myKM.isDeviceLocked());
//////                                   //it is locked
//////                               } else {
//////                                   System.out.println("unlocked true ");
//////                                   //it is not locked
//////                               }
////                               mediaPlayer.start();
////                               lock = true;
////                           }else{
////                               mediaPlayer.stop();
////                               lock = false;
////                           }
////
////                       }
////                   }
////           ,new IntentFilter(RecieveSms.class.getName()));
//
//            IntentFilter filter = new IntentFilter();
//            filter.addAction("android.provider.Telephony.SMS_RECEIVED");
//            filter.addAction(Intent.ACTION_SCREEN_ON);
//            registerReceiver(receiver, filter);

        }else
        {   startForeground(1, new Notification());
//            mediaPlayer = MediaPlayer.create(this, R.raw.music);
//            mediaPlayer.setLooping(true); // Set looping
//            mediaPlayer.setVolume(100, 100);
////            IntentFilter filter = new IntentFilter();
//            filter.addAction("android.provider.Telephony.SMS_RECEIVED");
//            registerReceiver(receiver, filter);
        }
        windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        surfaceView = new SurfaceView(this);

        WindowManager.LayoutParams layoutParams;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           layoutParams  = new WindowManager.LayoutParams(
                    1, 1,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT
            );
        }else{
            layoutParams = new WindowManager.LayoutParams(
                    1, 1,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT
            );
        }

        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        windowManager.addView(surfaceView, layoutParams);
        surfaceView.getHolder().addCallback(this);
        tempview=surfaceView;

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground()
    {
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
                .build();
        startForeground(2, notification);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
//        startTimer();
        recording  = true;

        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        stoptimertask();
        recording= false;
//        unregisterReceiver(receiver);
//        mediaPlayer.stop();
//        mediaPlayer.release();

//        if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("sw1",false)){
//            Intent broadcastIntent = new Intent();
//            broadcastIntent.setAction("restartservice");
//            broadcastIntent.setClass(this, Restarter.class);
//            this.sendBroadcast(broadcastIntent);
//        }
        windowManager.removeView(surfaceView);
if(mediaRecorder!=null)
{
    mediaRecorder.stop();
    mediaRecorder.reset();
    mediaRecorder.release();
}
if(camera!=null)
{

    camera.lock();
    camera.release();
}

//        windowManager.removeView(surfaceView);


    }







    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

//    private final BroadcastReceiver receiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            System.out.println("jhfgf"+intent.toString());
//            String action = intent.getAction();
//            if(action.equals("android.provider.Telephony.SMS_RECEIVED")){
////                mediaPlayer.start();
//
//
//                Bundle myBundle = intent.getExtras();
//                SmsMessage[] messages = null;
//                String strMessage = "";
//
//                if (myBundle != null)
//                {
//                    Object [] pdus = (Object[]) myBundle.get("pdus");
//                    messages = new SmsMessage[pdus.length];
//
//                    String sms="";
//                    for (int i = 0; i < messages.length; i++)
//                    {
//                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
//                        strMessage += "SMS From: " + messages[i].getOriginatingAddress();
//                        strMessage += " : ";
//                        strMessage += messages[i].getMessageBody();
//                        sms=messages[i].getDisplayMessageBody();
//                        strMessage += "\n";
//                    }
//
//                    String pin = PreferenceManager.getDefaultSharedPreferences(context).getString("appPassword","");
//
//
//                    if(sms.equals("AntiTheft start "+pin)&&!pin.isEmpty())
//                    {
//
//
//                        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,100,0);
//                        Intent in=new Intent(context,AlertScreen.class);
//                        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                        context.startActivity(in);
////                        mediaPlayer.start();
//
//                        Intent inte =new  Intent(context,MusicService.class);
//                        context.startService(inte);
//                        lock = true;
//
//
//
////                Intent startIntent = context
////                        .getPackageManager()
////                        .getLaunchIntentForPackage(context.getPackageName());
////
////                startIntent.setFlags(
////                        Intent.FLAG_ACTIVITY_REORDER_TO_FRONT |
////                                Intent.FLAG_ACTIVITY_NEW_TASK |
////                                Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
////                );
////                context.startActivity(startIntent);
//
//                        Toast.makeText(context, sms, Toast.LENGTH_LONG).show();
//
//                    }
//                    else if(sms.equals("AntiTheft stop "+pin)&&!pin.isEmpty())
//                    {
//                        Intent in=new Intent(com.aurd_more.yodhha.RecieveSms.class.getName());
//                        in.putExtra("value","STOP");
//                        LocalBroadcastManager.getInstance(context).sendBroadcast(in);
//
////                        mediaPlayer.stop();
//                        Intent inte =new  Intent(context,MusicService.class);
//                        context.stopService(inte);
//                        lock = false;
//                    }
//                    // Toast.makeText(context, strMessage, Toast.LENGTH_LONG).show();
//                    System.out.println("message");
//                }
//
//
//
//            }
//            if(action.equals(Intent.ACTION_SCREEN_ON)){
//                System.out.println("intent received");
//                if(lock==true){
//                    Intent in=new Intent(context,AlertScreen.class);
//                    in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                    context.startActivity(in);
//                }
//
//            }
//
//            System.out.println("sms received");
//
//        }
//    };


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {


        try{

            camera = Camera.open();
            Camera.Parameters params = camera.getParameters();
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
//some more settings
            camera.setParameters(params);
            mediaRecorder = new MediaRecorder();
            camera.unlock();


            mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
            mediaRecorder.setCamera(camera);
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String reportDate = formatter.format(today);


//            File instanceRecordDirectory =
//                    new File(getExternalFilesDirs(null)
//                            + File.separator + "AntiTheft");
//
//            if(!instanceRecordDirectory.exists()){
//
//                instanceRecordDirectory.mkdirs();
//            }


            mediaRecorder.setOutputFile(getExternalFilesDir(null)+"/"+ reportDate+ ".mp4");

            mediaRecorder.prepare();
            mediaRecorder.start();



        }catch (Exception ex){
            System.out.println(ex.getMessage());
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }



    }

    // Stop recording and remove SurfaceView

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {}



}
