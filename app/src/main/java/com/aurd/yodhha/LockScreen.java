package com.example.theftapp;


import android.app.Dialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.ContextCompat;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class LockScreen extends AppCompatActivity {

    public static final String TAG = "PinLockView";
    public  boolean screenLock;

    static boolean active = false;

    private PinLockView mPinLockView;
    private IndicatorDots mIndicatorDots;

    DevicePolicyManager devicePolicyManager;
    ComponentName compName;
    TextView textView;
    Intent intent;
    boolean confirmpassword=false;
    String tempPin="";

    private androidx.appcompat.app.AlertDialog.Builder dialogBuilder;
    private androidx.appcompat.app.AlertDialog dialog;


//    private  final String kiosk = getPackageName();
//    private  final String[] app_packages = {kiosk};


    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {

            if (intent.hasExtra("set")) {

            if (!confirmpassword && !pin.isEmpty() && intent.getStringExtra("set").equals("enterPassword")) {
                tempPin = pin;
                mPinLockView.resetPinLockView();
                textView.setText("Please confirm your pin");
                confirmpassword = true;

//                Intent lockIntent = new Intent(getApplicationContext(),LockScreen.class);
//                lockIntent.putExtra("set","confirmPassword");
//                lockIntent.putExtra("pin",pin);
//                startActivity(intent);
//                finish();
            } else if (confirmpassword) {
                if (tempPin.equals(pin)) {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("appPassword", pin).apply();

                    updatePin(pin);
                    finish();
                }

            } else if (intent.getStringExtra("set").equals("checkPassword")) {
                if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("appPassword", "").equals(pin)
                        && !pin.isEmpty()) {
                    finish();
                }
            }
        }

            if(MusicService.active==true &&
                    pin.equals(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("appPassword", ""))&&
                    !pin.isEmpty())
            {
                Intent in=new Intent(getApplicationContext(),MusicService.class);
                stopService(in);
            }
//


            if(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).contains("appPassword"))
            {
                if(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("appPassword","").equals(pin)){
                    screenLock=false;
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                            .putBoolean("screenlock",false).apply();
                    finish();
                }else{
                    Toast.makeText(LockScreen.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                }

            }
            Log.d(TAG, "Pin complete: " + pin);
        }

        @Override
        public void onEmpty() {
            Log.d(TAG, "Pin empty");
        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {

            Log.d(TAG, "Pin changed, new length " + pinLength + " with intermediate pin " + intermediatePin);
        }
    };

    private void updatePin(String pin) {
        FirebaseFirestore.getInstance().collection("Users").whereEqualTo("phoneNumber",
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("phoneNumber",""))
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
           if(!queryDocumentSnapshots.isEmpty())
           {
               DocumentSnapshot document=queryDocumentSnapshots.getDocuments().get(0);
               Map<String, Object> map = new HashMap<>();
               map.put("pin",pin );
               FirebaseFirestore.getInstance().collection("Users").document(document.getId()).update(map);
           }
            }
        });
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

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
////        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);


//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
//
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                |View.SYSTEM_UI_FLAG_FULLSCREEN
//                |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                |View.SYSTEM_UI_FLAG_IMMERSIVE);



        setContentView(R.layout.activity_lock_screen);
        mPinLockView = (PinLockView) findViewById(R.id.pin_lock_view);
        mIndicatorDots = (IndicatorDots) findViewById(R.id.indicator_dots);
        textView = findViewById(R.id.profile_name);





        intent = getIntent();
        if(intent.hasExtra("set")){
            if(intent.getStringExtra("set").equals("enterPassword")){
                textView.setText("Please create your pin");
            }else if(intent.getStringExtra("set").equals("checkPassword")){
                textView.setText("Enter your password");
            }

        }

        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLockListener(mPinLockListener);
        //mPinLockView.setCustomKeySet(new int[]{2, 3, 1, 5, 9, 6, 7, 0, 8, 4});
        //mPinLockView.enableLayoutShuffling();

        mPinLockView.setPinLength(4);
        mPinLockView.setTextColor(ContextCompat.getColor(this, R.color.white));
        mIndicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FILL_WITH_ANIMATION);


        if(PreferenceManager.getDefaultSharedPreferences(LockScreen.this).contains("appPassword") &&!intent.hasExtra("set")){
            devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
            compName = new ComponentName(this, MyAdmin.class);
//            devicePolicyManager.setLockTaskPackages(compName,app_packages);

            screenLock= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("screenlock",true);

            // Enables Always-on

            devicePolicyManager.lockNow();
            findViewById(R.id.lockscreen).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    devicePolicyManager.lockNow();

                    return true;
                }
            });
        }
//if(screenLock){
//    initiateDialog(getApplicationContext());
//    showDialog();
//}


    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

//        Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
//        sendBroadcast(closeDialog);

        if(PreferenceManager.getDefaultSharedPreferences(LockScreen.this).contains("appPassword")){
            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            sendBroadcast(closeDialog);
            hideNaviBar();
            if(!hasFocus && screenLock==true)
            {
                devicePolicyManager.lockNow();
            }
        }



    }

    @Override
    protected void onPause() {

//        if (timer == null) {
//            myTimerTask = new MyTimerTask();
//            timer = new Timer();
//            timer.schedule(myTimerTask, 10, 1);
//        }
        super.onPause();

//        Toast.makeText(this, "on Pause", Toast.LENGTH_SHORT).show();
//        if(screenLock==true){
//
//        }

        active=false;
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

            int action = event.getAction();
            int keyCode = event.getKeyCode();
            System.out.println(action +"   "+keyCode);
            switch (keyCode) {
                case KeyEvent.KEYCODE_VOLUME_UP:
                    if (action == KeyEvent.ACTION_DOWN && PreferenceManager.getDefaultSharedPreferences(LockScreen.this).contains("appPassword") ){

                    }
                    return true;
                case KeyEvent.KEYCODE_VOLUME_DOWN:
                    if (action == KeyEvent.ACTION_DOWN && PreferenceManager.getDefaultSharedPreferences(LockScreen.this).contains("appPassword")) {

                    }
                    return true;
                default:
                    return super.dispatchKeyEvent(event);
            }
        }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return  false;
    }


    @Override
    protected void onResume() {

//        if (timer != null) {
//            timer.cancel();
//            timer = null;
//        }
        super.onResume();
//        startLockTask();
//        if(devicePolicyManager.isLockTaskPermitted(getPackageName())){
//
//        }
        active=true;
    }



    private Timer timer;
    private TimerTask timerTask;
    public void startTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                if(!PreferenceManager.getDefaultSharedPreferences(LockScreen.this).getBoolean("screenlock",false)&&
                        dialog.isShowing()){
                    dialog.dismiss();

                    stoptimertask();
                }

                if(PreferenceManager.getDefaultSharedPreferences(LockScreen.this).getBoolean("screenlock",false))
                {
                    Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                    sendBroadcast(closeDialog);
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




    private void initiateDialog(Context context) {
        // initialize only when the service is not running
        dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(new ContextThemeWrapper(context, R.style.my_dialog_theme));
        // dialogBuilder.setTitle(R.string.app_name);
        LayoutInflater inflater2 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater2.inflate(R.layout.activity_lock_screen, null);
        dialogBuilder.setView(dialogView);


        dialog = dialogBuilder.create();
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF303030")));

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



