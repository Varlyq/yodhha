package com.more_high.yodhha;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

public class SimStateChangedReceiver extends BroadcastReceiver {

    private static final String EXTRA_SIM_STATE = "ss";

    @Override
    public void onReceive(Context context, Intent intent) {

        System.out.println(intent.getAction());
        String state = intent.getExtras().getString(EXTRA_SIM_STATE);
        if (state == null) {
            return;
        }

        System.out.println("sim state---------" + state);
        // Do stuff depending on state
        switch (state) {
            case "ABSENT":
                PreferenceManager.getDefaultSharedPreferences(context).edit().putString("simcard", "absent").apply();
                Toast.makeText(context, "Sim State change", Toast.LENGTH_SHORT).show();
                ;

            case "LOADED":
                TelephonyManager tm = (TelephonyManager)
                        context.getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS)
                        != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_NUMBERS)
                                != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
                                != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more_high details.
                    return;
                }
                String simID = tm.getLine1Number();
                System.out.println("simId------"+simID);
            case "NETWORK_LOCKED": break;
            // etc.
        }
    }
}