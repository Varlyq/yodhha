package com.example.theftapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toolbar;



import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class HomeScreen extends AppCompatActivity {
    ImageView antiTheftCard, humansafetyCard, recordingCard, youTubeCard;
    ComponentName compName;

    ImageView iv;
    Bitmap bitmap;
    ImageView personimg;

    TelephonyManager telMgr;
    private static final int IGNORE_BATTERY_OPTIMIZATION_REQUEST = 1002;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList nameList = new ArrayList(Arrays.asList("Anti-theft", "Human Safety", "Recording", "Youtube"));
    ArrayList imageList = new ArrayList(Arrays.asList(R.drawable.imgantitheft, R.drawable.imghumansafety, R.drawable.imgrecording, R.drawable.imgyoutube));


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();


        int simstate = telMgr.getSimState(0);
        System.out.println("SIM STATE " + simstate);


    }

    @Override
    protected void onStop() {
        // Unregister the listener

        super.onStop();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_screen);

        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.toolbartitlecolor));


        Intent chkIntent = getIntent();

        if (chkIntent.hasExtra("context")) {
            if (chkIntent.getStringExtra("context").equals("Login")) {
                if (!PreferenceManager.getDefaultSharedPreferences(HomeScreen.this).contains("appPassword")) {
                    Intent intent = new Intent(HomeScreen.this, LockScreen.class);
                    intent.putExtra("set", "enterPassword");
                    startActivity(intent);
                } else {
                    Intent lockIntent = new Intent(this, LockScreen.class);
                    lockIntent.putExtra("set", "checkPassword");
                    startActivity(lockIntent);
                }
            }

        }

       if (!PreferenceManager.getDefaultSharedPreferences(HomeScreen.this).contains("Autostart")) {
            AutoStartHelper.getInstance().getAutoStartPermission(this);
            PreferenceManager.getDefaultSharedPreferences(HomeScreen.this).edit().putString("Autostart", "start").apply();


        }
        if (!PreferenceManager.getDefaultSharedPreferences(HomeScreen.this).contains("SpecialPermission")) {
            AutoStartHelper.getInstance().getSpecialPermission(this);
            PreferenceManager.getDefaultSharedPreferences(HomeScreen.this).edit().putString("SpecialPermission", "permission").apply();
//            final Intent[] POWERMANAGER_INTENTS = {
//                    new Intent().setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")),
//                    new Intent().setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity")),
//                    new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity")),
//                    new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")),
//                    new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity")),
//                    new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")),
//                    new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.startupapp.StartupAppListActivity")),
//                    new Intent().setComponent(new ComponentName("com.oppo.safe", "com.oppo.safe.permission.startup.StartupAppListActivity")),
//                    new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")),
//                    new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager")),
//                    new Intent().setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")),
//                    new Intent().setComponent(new ComponentName("com.samsung.android.lool", "com.samsung.android.sm.battery.ui.BatteryActivity")),
//                    new Intent().setComponent(new ComponentName("com.samsung.android.lool", "com.samsung.android.sm.ui.battery.BatteryActivity")),
//                    new Intent().setComponent(new ComponentName("com.htc.pitroad", "com.htc.pitroad.landingpage.activity.LandingPageActivity")),
//                    new Intent().setComponent(new ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.MainActivity")),
//                    new Intent().setComponent(new ComponentName("com.transsion.phonemanager", "com.itel.autobootmanager.activity.AutoBootMgrActivity"))
//            };
//
//            for (Intent intent : POWERMANAGER_INTENTS)
//                if (getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
//                    startActivity(intent);
//                    break;
//                }

        }


        antiTheftCard = findViewById(R.id.antiTheftCard);
        humansafetyCard = findViewById(R.id.humanSafetyCard);
        personimg = (ImageView) findViewById(R.id.profileimage);
        recordingCard = findViewById(R.id.recordingcard);
        youTubeCard = findViewById(R.id.youtubecard);

//        BitmapDrawable bitmapDrawable = (BitmapDrawable) iv.getDrawable();
//        bitmap = bitmapDrawable.getBitmap();

        youTubeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/file/d/1gSfLEYApJpn8y3szhfD65I8lbKI-eEAx/view?usp=sharing"));
                startActivity(browserIntent);
            }
        });

        compName = new ComponentName(this, MyAdmin.class);


        int PERMISSION_ALL = 1;

        String[] PERMISSIONS = {

                Manifest.permission.READ_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.FOREGROUND_SERVICE,
                Manifest.permission.SYSTEM_ALERT_WINDOW,
                Manifest.permission.REORDER_TASKS,
                Manifest.permission.SYSTEM_ALERT_WINDOW,
                Manifest.permission.RECEIVE_BOOT_COMPLETED,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_PHONE_NUMBERS

        };

        if (!hasPermissions(this, PERMISSIONS)) {

            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);

        }







//        getBatteryStatus();


//        Intent inteset = new Intent("miui.intent.action.APP_PERM_EDITOR");
//        inteset.setClassName("com.miui.securitycenter",
//                "com.miui.permcenter.permissions.PermissionsEditorActivity");
//        inteset.putExtra("extra_pkgname", getPackageName());
//        startActivity(inteset);


        antiTheftCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreen.this, AntiTheftActivity.class);
                startActivity(intent);

            }
        });

        humansafetyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreen.this, HumanSafety.class);
                startActivity(intent);
            }
        });

        recordingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreen.this, Recording.class);
                startActivity(intent);
            }
        });

        personimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("btn Onclick ");

                Intent intent = new Intent(HomeScreen.this, ProfilePage.class);
                startActivity(intent);
            }
        });


//        TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(this);
//
//
//        String imeiSIM1 = telephonyInfo.getImsiSIM1();
//        String imeiSIM2 = telephonyInfo.getImsiSIM2();
//        boolean isDualSIM = telephonyInfo.isDualSIM();
//
//
//        System.out.println("Dual Sim"+ isDualSIM);


        telMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            System.out.println(telMgr.getSimCarrierIdName());
        }
        int simState = telMgr.getSimState();
        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT:
                System.out.println("Sim absent");
                break;
            case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                System.out.println("sim network locked");
                break;
            case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                // do something
                break;
            case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                // do something
                break;
            case TelephonyManager.SIM_STATE_READY:
                System.out.println("sim state ready");
                break;
            case TelephonyManager.SIM_STATE_UNKNOWN:
                System.out.println("sim state unknown");
                break;
        }


    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {

            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (pm != null && !pm.isIgnoringBatteryOptimizations(getPackageName())) {
                    askIgnoreOptimization();
                } else {
                    // already ignoring battery optimization code here next you want to do
                }
            } else {
                // already ignoring battery optimization code here next you want to do
            }


            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);


//            Intent intent = new Intent();
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            String packageName = getApplicationContext().getPackageName();
//            PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
//            if (pm.isIgnoringBatteryOptimizations(packageName))
//                intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
//            else {
//                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
//                intent.setData(Uri.parse("package:" + packageName));
//            }
//            getApplicationContext().startActivity(intent);


//            if(!Settings.canDrawOverlays(getApplicationContext())){
//                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                        Uri.parse("package:" + getApplicationContext().getPackageName()));
//                // request permission via start activity for result
//                startActivityForResult(intent, 101);
//            }
        } else if (requestCode == 11) {
            if (!Settings.canDrawOverlays(getApplicationContext())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getApplicationContext().getPackageName()));
                // request permission via start activity for result
                startActivityForResult(intent, 101);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            Intent intent1 = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent1.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
            intent1.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Additional text explaining why we need this permission");
            startActivityForResult(intent1, 11);

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        String[] PERMISSIONS ={
//
//                Manifest.permission.READ_SMS,
//                Manifest.permission.RECEIVE_SMS,
//                Manifest.permission.FOREGROUND_SERVICE,
//                Manifest.permission.SYSTEM_ALERT_WINDOW,
//                Manifest.permission.REORDER_TASKS,
//                Manifest.permission.SYSTEM_ALERT_WINDOW,
//                Manifest.permission.RECEIVE_BOOT_COMPLETED,
//                Manifest.permission.CAMERA,
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.MANAGE_EXTERNAL_STORAGE,
//
//        };
//
//        if(hasPermissions(getApplicationContext(),PERMISSIONS))
//        {
//
//        }
//    }


    private void askIgnoreOptimization() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            System.out.println("PrintBatteryOptimization");

            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, IGNORE_BATTERY_OPTIMIZATION_REQUEST);

        } else {
            System.out.println("BatteryOptimization");
        }
    }

}

