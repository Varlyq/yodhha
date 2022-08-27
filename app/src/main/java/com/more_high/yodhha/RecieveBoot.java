package com.more_high.yodhha;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

public class RecieveBoot  extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println(intent.getAction());
        if(!PreferenceManager.getDefaultSharedPreferences(context).getBoolean("lock",false))
        {
            Intent intent1 = new Intent(context,LockScreen.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
            PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("screenlock", true).apply();
//
        }
    }
}
