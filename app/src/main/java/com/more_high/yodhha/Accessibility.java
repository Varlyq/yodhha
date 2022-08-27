package com.more_high.yodhha;

import android.accessibilityservice.AccessibilityService;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Camera;

import android.media.AudioManager;

import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.Policy;

public class Accessibility extends AccessibilityService {

    public int counter=0;
     boolean lock;
public boolean volumeup=false,volumedown=false,power=false;

     static boolean screenOff=false;
     int timeCounter=0;
     long timestamp=System.currentTimeMillis();

     static boolean active=false;
     static boolean dim=false;

     static DialogThread dialogThread;

    @Override
    public void onCreate() {
        super.onCreate();
try {
    dialogThread = new DialogThread(getApplicationContext());
    active = true;
    loginTimer();
    startTimer();
    lock = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("lock", false);
    IntentFilter filter = new IntentFilter();
    screenOff = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("screenoff", false);

    filter.addAction(Intent.ACTION_SCREEN_ON);
    filter.addAction(Intent.ACTION_SCREEN_OFF);
    filter.addAction(Intent.ACTION_USER_PRESENT);
    filter.addAction("com.AntiTheft");
    filter.addAction("android.intent.action.ACTION_POWER_CONNECTED");
    filter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
    filter.addAction("android.intent.action.SIM_STATE_CHANGED");
    registerReceiver(receiver, filter);
    Toast.makeText(getApplicationContext(), "Service started", Toast.LENGTH_SHORT).show();

    if (lock == true) {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 100, 0);
        Intent in = new Intent(getApplicationContext(), AlertScreen.class);
        in.addCategory(Intent.CATEGORY_HOME);
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        in.putExtra("path", "boot");

        getApplicationContext().startActivity(in);


        Intent inte = new Intent(getApplicationContext(), MusicService.class);
        getApplicationContext().startService(inte);

        Intent locatIntent = new Intent(getApplicationContext(), LocatioinService.class);
        getApplicationContext().startService(locatIntent);
    }
//
}catch (Exception e)
{
    e.printStackTrace();
}
    }



    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();



    }



    @Override
public void onAccessibilityEvent(AccessibilityEvent event) {
try {
    lock = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("lock", false);


    if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
        if (lock == true && !AlertScreen.active) {
            Intent in = new Intent(getApplicationContext(), AlertScreen.class);
            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            getApplicationContext().startActivity(in);
        } else if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("screenlock", false)) {
            if (!LockScreen.active) {

                Intent in = new Intent(getApplicationContext(), LockScreen.class);
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(in);
            }

//
        }
    }
}catch (Exception e)
{
    e.printStackTrace();
}
}




@Override
public void onInterrupt() {


        active=false;
    stoptimertask();
    stoploginTimer();

        }

    Handler loginHandler;
    Runnable loginRunnable;
        public void loginTimer(){
        loginHandler=new Handler(Looper.getMainLooper());
        loginRunnable=new Runnable() {
            @Override
            public void run() {
                checkLogin();
            }
        };

        loginHandler.postDelayed(loginRunnable,50000);
        }


        public void stoploginTimer()
        {
            if(handler!=null)
                loginHandler.removeCallbacks(loginRunnable);

        }


    Handler handler;
        Runnable runnable;
    public void startTimer() {


        handler=new Handler(Looper.getMainLooper());
        runnable=new Runnable() {
            @Override
            public void run() {
                KeyguardManager keyguardManager= (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
                if(keyguardManager.isDeviceLocked())
                {
                    Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                    sendBroadcast(closeDialog);
                }


                handler.postDelayed(runnable,100);
            }

        };

        handler.postDelayed(runnable,100);


    }

    public void stoptimertask() {

    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("jhfgf"+intent.toString());
            String action = intent.getAction();
            if(action.equals(Intent.ACTION_SCREEN_ON)){

                power=true;
                if(lock==true && !AlertScreen.active){
                    Intent in=new Intent(context,AlertScreen.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    context.startActivity(in);
                }

                if(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("screenlock",false))
                {
                    if(!LockScreen.active)
                    {

                        Intent in=new Intent(context,LockScreen.class);
                        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(in);
                    }

//
                }



            }
            if (action.equals(Intent.ACTION_SCREEN_OFF))
            {

                if(System.currentTimeMillis()-timestamp<50000)
                power=true;
                else
                {
                    volumeup=false;
                    volumedown=false;
                    power=false;
                }


                if(volumedown && volumeup && power)
                {

                    Intent smsIntent = new Intent(getApplicationContext(),backgroundContactService.class);
                    smsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startService(smsIntent);
                }
//
                screenOff=true;
                PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("screenoff",true).apply();

            }
            if(action.equals(Intent.ACTION_SCREEN_OFF )&& PreferenceManager.getDefaultSharedPreferences(context).getBoolean("sw3",false))
            {
               PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("keyLock",true).apply();
            }
            System.out.println("sms received");

            if(action.equals(Intent.ACTION_USER_PRESENT))
            {

                screenOff=false;
                PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("screenoff",false).apply();

                if(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("sw3",false) && !MusicService.active &&
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("keyLock",false))
                {
                    Intent in=new Intent(getApplicationContext(),MusicService.class);
                    startService(in);
                }
            }


            if(action.equals("com.AntiTheft"))
            {
                System.out.println("AntiTeft Recieve");
                if(isMyServiceRunning(CameraBackService.class))
                    stopService(new Intent(getApplicationContext(),CameraBackService.class));
            }



            if(action.equals("android.intent.action.ACTION_POWER_CONNECTED")&&
                    PreferenceManager.getDefaultSharedPreferences(Accessibility.this).getBoolean("sw5",false))
            {
                Intent inte = new Intent(context, MusicService.class);
                context.startService(inte);
                Intent in=new Intent(context,LockScreen.class);
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(in);
                PreferenceManager.getDefaultSharedPreferences(Accessibility.this).edit().putBoolean("screenlock", true).apply();

                Toast.makeText(context, "Charging", Toast.LENGTH_SHORT).show();
            }
            if(action.equals("android.intent.action.ACTION_POWER_DISCONNECTED")&& PreferenceManager.getDefaultSharedPreferences(Accessibility.this).getBoolean("sw4",false))
            {

                Intent inte = new Intent(context, MusicService.class);
                context.startService(inte);
                Intent in=new Intent(context,LockScreen.class);
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(in);
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("screenlock", true).apply();
                Toast.makeText(context, "charge disconnect", Toast.LENGTH_SHORT).show();
            }

            if(action.equals("android.intent.action.SIM_STATE_CHANGED")
                    && PreferenceManager.getDefaultSharedPreferences(Accessibility.this).getBoolean("sw2",false)
                    && PreferenceManager.getDefaultSharedPreferences(Accessibility.this).getString("simcard","").equals("absent")){
                Toast.makeText(context, "Sim Card removed", Toast.LENGTH_SHORT).show();
                Intent inte = new Intent(context, MusicService.class);
                context.startService(inte);
                Intent in=new Intent(context,LockScreen.class);
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(in);
                try{
                    String msg="Sim card Changed for your device";
                    try {
                       String alternateNumber= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AlternateNumber","");
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(alternateNumber, null, msg, null, null);
//
                    } catch (Exception ex) {
//
                        ex.printStackTrace();
                    }
                }catch (Exception e)
                {

                }
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("screenlock", true).apply();


            }
        }
    };

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {

       System.out.println("event"+event);
//

        if(event.getKeyCode()== KeyEvent.KEYCODE_VOLUME_UP )
        {
//
            System.out.println("volume up"+volumeup);
            volumeup=true;
            volumedown=false;
            power=false;
            timestamp=System.currentTimeMillis();
        }

        if(event.getKeyCode()==KeyEvent.KEYCODE_VOLUME_DOWN && volumeup==true && System.currentTimeMillis()-timestamp<50000){
            System.out.println("volume up"+volumedown);
            volumedown=true;
            power=false;

        }



        return super.onKeyEvent(event);
    }

    public void checkLogin() {

        try {
            FirebaseFirestore.getInstance().collection("Users").whereEqualTo("userId", PreferenceManager
                    .getDefaultSharedPreferences(getApplicationContext()).getString("UserID", ""))
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (queryDocumentSnapshots.getDocuments().isEmpty()) {
//                    stopSelf();
                    } else if (!queryDocumentSnapshots.getDocuments().get(0).getString("androidId").equals(Settings.Secure.getString(
                            getApplicationContext().getContentResolver(),
                            Settings.Secure.ANDROID_ID))) {
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().clear().apply();
                        stopSelf();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Accessibility.this, "Cannot start accessability", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}