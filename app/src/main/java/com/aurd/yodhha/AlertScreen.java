package com.example.theftapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.app.admin.SystemUpdatePolicy;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import android.view.ViewGroup;
import android.view.Window;

import android.view.WindowManager;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class AlertScreen extends AppCompatActivity {
DevicePolicyManager devicePolicyManager;
ComponentName compName;
TextView alert;
    static boolean active = false;
    boolean lock=false;
    private androidx.appcompat.app.AlertDialog.Builder dialogBuilder;
    private androidx.appcompat.app.AlertDialog dialog;



    @Override
    protected void onDestroy() {
        super.onDestroy();
        dialog.dismiss();
        Intent intent= new Intent(AlertScreen.this,LockScreen.class);
        startActivity(intent);
        PreferenceManager.getDefaultSharedPreferences(AlertScreen.this).edit().putBoolean("screenlock", true).apply();
//
    }

    private Timer timer;
    private TimerTask timerTask;
    public void startTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                sendBroadcast(closeDialog);
              if(!PreferenceManager.getDefaultSharedPreferences(AlertScreen.this).getBoolean("lock",false)&& dialog.isShowing()){
                 dialog.setCancelable(true);
                 dialog.cancel();
//                  dialog.dismiss();
//                  finish();
                  stoptimertask();
              }

                Log.i("Count acc", "sdjcsdffgh");
            }
        };
        timer.schedule(timerTask, 1, 1); //
    }

    public void stoptimertask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }






    void hideNaviBar(){
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        |View.SYSTEM_UI_FLAG_FULLSCREEN
                        |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        |View.SYSTEM_UI_FLAG_IMMERSIVE);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startTimer();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        |View.SYSTEM_UI_FLAG_FULLSCREEN

                        |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);







        setContentView(R.layout.activity_alert_screen);

        alert=findViewById(R.id.textalert);

        alert.setText("This Device belongs to "+ PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("UserName","")
                + " \n" + "Contact Number - "+ PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AlternateNumber",""));
        findViewById(R.id.alertscreen).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                devicePolicyManager.lockNow();
                hideNaviBar();
                return true;
            }
        });


        devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        compName = new ComponentName(this, MyAdmin.class);



        Intent send=new Intent(com.example.theftapp.RecieveSms.class.getName());
        send.putExtra("value","START");
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(send);


        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getStringExtra("value")=="STOP"){
                    dialog.setCancelable(true);

                    finish();
                    stoptimertask();
                }
            }
        },new IntentFilter(RecieveSms.class.getName()));

//        startLockTask();

        initiateDialog(getApplicationContext());
        showDialog();
    }

    @Override
    public void onBackPressed() {

    }

    private void setUserRestriction(String restriction, boolean disallow){
        if (disallow) {
            devicePolicyManager.addUserRestriction(compName,restriction);
        } else {
            devicePolicyManager.clearUserRestriction(compName,restriction);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        lock= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("lock",false);
   active=true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        sendBroadcast(closeDialog);

        if(!hasFocus && lock==true)
        {
           devicePolicyManager.lockNow();
        }
    }

        @Override
    protected void onPause() {
        super.onPause();
        active=false;
//        if(lock==true){
//            devicePolicyManager.lockNow();
//        }
    }



    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {


        int action = event.getAction();
        int keyCode = event.getKeyCode();
        System.out.println(action +"   "+keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {

                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {

                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }




    private void initiateDialog(Context context) {
        // initialize only when the service is not running

        dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(new ContextThemeWrapper(context, R.style.my_dialog_theme));
        // dialogBuilder.setTitle(R.string.app_name);
        LayoutInflater inflater2 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater2.inflate(R.layout.activity_alert_screen, null);
        TextView  text=dialogView.findViewById(R.id.textalert);
        text.setText("This Device belongs to "+ PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("UserName","")
                + " \n" + "Contact Number - "+ PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AlternateNumber",""));
        dialogBuilder.setView(dialogView);


        dialog = dialogBuilder.create();
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F0F0F8")));

        final WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        final Window dialogWindow = dialog.getWindow();
        if (dialogWindow != null) {
            dialogWindow.setAttributes(layoutParams);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                dialogWindow.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
            } else {
                dialogWindow.setType(WindowManager.LayoutParams.TYPE_PHONE);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        initiateDialog(getApplicationContext());
//        showDialog();
    }

    public void showDialog() {
        try {
            if (dialog != null && dialog.isShowing()) {
                //show no questionnaire when previously question is not answer
                return;
            }


            if (dialog == null) {
                return;
            }
            if (!dialog.isShowing()) {
                dialog.show();
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}