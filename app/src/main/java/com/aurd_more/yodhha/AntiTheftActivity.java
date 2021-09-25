package com.aurd_more.yodhha;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rm.rmswitch.RMSwitch;

public class AntiTheftActivity extends AppCompatActivity implements SensorEventListener {

//    Switch switch2,switch3,switch4,switch5;
   boolean sw1,sw2,sw3,sw4,sw5;
//    private BackgroundService mYourService; Intent mServiceIntent;

    RMSwitch switch1,switch2,switch3,switch4,switch5;
    TextView txtsimCard,txtCharging,txtUsbEject,txtUsbConnect;

    ImageView imageView,backbtn;
    Bitmap bitmap;
    SensorManager sensorManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anti_theft);
        Window window= getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.toolbartitlecolor));

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.backbutton);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        txtsimCard = findViewById(R.id.txtSimCard);
        txtCharging = findViewById(R.id.txtCharging);
        txtUsbConnect= findViewById(R.id.txtConnect);
        txtUsbEject= findViewById(R.id.txtUsbEject);


//        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
//        bitmap = bitmapDrawable.getBitmap();
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);


        switch1=findViewById(R.id.switch1);
        switch2=findViewById(R.id.switch2);
        switch3=findViewById(R.id.switch3);
        switch4=findViewById(R.id.switch4);
        switch5=findViewById(R.id.switch5);


SharedPreferences sharedPreference=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        if(sharedPreference.contains("sw1")){
//            sw1=sharedPreference.getBoolean("sw1",false);
//            System.out.println("sw1 "+sw1);
//        }
//        else {
//          sharedPreference.edit().putBoolean("sw1",false).apply();
//        }
        if(sharedPreference.contains("sw2")){
            sw2=sharedPreference.getBoolean("sw2",false);
        }
        else {
            sharedPreference.edit().putBoolean("sw2",false).apply();
        }
        if(sharedPreference.contains("sw3")){
            sw3=sharedPreference.getBoolean("sw3",false);
        }
        else {
            sharedPreference.edit().putBoolean("sw3",false).apply();
        }
        if(sharedPreference.contains("sw4")){
            sw4=sharedPreference.getBoolean("sw4",false);
        }
        else {
            sharedPreference.edit().putBoolean("sw4",false).apply();
        }
        if(sharedPreference.contains("sw5")){
            sw5=sharedPreference.getBoolean("sw5",false);
        }
        else {
            sharedPreference.edit().putBoolean("sw5",false).apply();
        }


        switch1.setClickable(false);
        switch2.setChecked(sw2);
        switch3.setChecked(sw3);
        switch4.setChecked(sw4);
        switch5.setChecked(sw5);

//        switch1.setOnCheckedChangeListener( (compoundButton, b) -> {
//            System.out.println("checked "+b);
//           // switch1.setChecked(b);
//            sharedPreference.edit().putBoolean("sw1",b).apply();
//            mYourService = new BackgroundService();
//            mServiceIntent = new Intent(this, mYourService.getClass());
//
//
//            if(b){
//                if (!isMyServiceRunning(mYourService.getClass())) {
//                    startService(mServiceIntent);
//                }
//            }else{
//                stopService(mServiceIntent);
//            }
//
//
//        });


        switch2.addSwitchObserver(new RMSwitch.RMSwitchObserver() {
            @Override
            public void onCheckStateChange(RMSwitch switchView, boolean isChecked) {
                sharedPreference.edit().putBoolean("sw2",isChecked).apply();
                if(isChecked==true){
                    txtsimCard.setTextColor(getResources().getColor(R.color.black));
                    Toast.makeText(AntiTheftActivity.this, "Sim lock is enabled", Toast.LENGTH_SHORT).show();
                }
                else{
                    txtsimCard.setTextColor(getResources().getColor(R.color.textsecColor));
                    Toast.makeText(AntiTheftActivity.this, "Sim lock is disabled", Toast.LENGTH_SHORT).show();
                }
            }
        });

        switch3.addSwitchObserver(new RMSwitch.RMSwitchObserver() {
            @Override
            public void onCheckStateChange(RMSwitch switchView, boolean isChecked) {
                sharedPreference.edit().putBoolean("sw3",isChecked).apply();
                if(isChecked==true){
                    txtCharging.setTextColor(getResources().getColor(R.color.black));
                    Intent in=new Intent(getApplicationContext(),MotionDetect.class);
                    startService(in);
                    Toast.makeText(AntiTheftActivity.this, "Do not touch is enabled", Toast.LENGTH_SHORT).show();
                }else{
                    txtCharging.setTextColor(getResources().getColor(R.color.textsecColor));
                    if(MusicService.active && PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("keyLock",false))
                    {
                        Intent intent=new Intent(getApplicationContext(),MusicService.class);
                        stopService(intent);
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("keyLock",false).apply();
                        Intent in=new Intent(getApplicationContext(),MotionDetect.class);
                        stopService(in);
                    }
                    Toast.makeText(AntiTheftActivity.this, "Do not touch is disabled", Toast.LENGTH_SHORT).show();
                }
            }
        });



        switch4.addSwitchObserver(new RMSwitch.RMSwitchObserver() {
            @Override
            public void onCheckStateChange(RMSwitch switchView, boolean isChecked) {
                sharedPreference.edit().putBoolean("sw4",isChecked).apply();
                if(isChecked){
                    Toast.makeText(AntiTheftActivity.this, "USB Eject is Enable", Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(AntiTheftActivity.this, "USB Eject is Disable", Toast.LENGTH_SHORT).show();

                }
            }
        });

        switch5.addSwitchObserver(new RMSwitch.RMSwitchObserver() {
            @Override
            public void onCheckStateChange(RMSwitch switchView, boolean isChecked) {
                sharedPreference.edit().putBoolean("sw5",isChecked).apply();
                if(isChecked){
                    Toast.makeText(AntiTheftActivity.this, "USB Connect is Enable", Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(AntiTheftActivity.this, "USB Connect is Disable", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("Service status", "Running");
                return true;
            }
        }
        Log.i ("Service status", "Not running");
        return false;
    }


    @Override
    protected void onDestroy() {
        //stopService(mServiceIntent);

        if(sw1){
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("restartservice");
            broadcastIntent.setClass(this, Restarter.class);
            this.sendBroadcast(broadcastIntent);
        }

        super.onDestroy();
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Register this class as a listener for the accelerometer sensor
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        // ...and the orientation sensor
//        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
//                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStop() {
        // Unregister the listener
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
//        if(event.values[0]>2)
//        {
//            leftSkew(Math.round(event.values[0]));
//        }
//        if(event.values[0]<-1)
//        {
//            rightSkew(Math.round(event.values[0]*-1));
//        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


//
//    void leftSkew(int temp)
//    {
////        BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
////
////        Bitmap bitmap = drawable.getBitmap();
//        Matrix matrix2 = new   Matrix();
//        int oldw = bitmap.getWidth();
//        int oldh =  bitmap.getHeight();
//        float d_up = (oldh / 70)*temp;
//        float d_down = d_up + temp;
//        float[] src2 = new float[] {
//                0, 0,
//                oldw, 0,
//                oldw, oldh,
//                0, oldh };
//        float[] dst2 = new float[] {
//                0, d_up, //top left
//                oldw, 0, //top right
//                oldw, oldh, //bottom right
//                0, oldh - d_down //bottom left
//        };
//
////        float[] dst2 = new float[] {
////                0, 0, //top left
////                oldw, d_up, //top right
////                oldw, oldh - d_down, //bottom right
////                0, oldh  //bottom left
////        };
//        matrix2.setPolyToPoly(src2, 0, dst2, 0,
//                src2.length >> 1);
//        Bitmap bMatrix2 = Bitmap.createBitmap(bitmap, 0, 0,
//                bitmap.getWidth(), bitmap.getHeight(),
//                matrix2, true);
//        imageView.setImageBitmap(bMatrix2);
//
//    }
//
//    void rightSkew(int temp)
//
//    {
////        BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
////
////        Bitmap bitmap = drawable.getBitmap();
//        Matrix matrix2 = new   Matrix();
//        int oldw = bitmap.getWidth();
//        int oldh =  bitmap.getHeight();
//        float d_up = (oldh / 70)*temp;
//        float d_down = d_up + temp;
//        float[] src2 = new float[] {
//                0, 0,
//                oldw, 0,
//                oldw, oldh,
//                0, oldh };
////        float[] dst2 = new float[] {
////                0, d_up, //top left
////                oldw, 0, //top right
////                oldw, oldh, //bottom right
////                0, oldh - d_down //bottom left
////        };
//
//        float[] dst2 = new float[] {
//                0, 0, //top left
//                oldw, d_up, //top right
//                oldw, oldh - d_down, //bottom right
//                0, oldh  //bottom left
//        };
//        matrix2.setPolyToPoly(src2, 0, dst2, 0,
//                src2.length >> 1);
//        Bitmap bMatrix2 = Bitmap.createBitmap(bitmap, 0, 0,
//                bitmap.getWidth(), bitmap.getHeight(),
//                matrix2, true);
//        imageView.setImageBitmap(bMatrix2);
//    }
}