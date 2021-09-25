package com.aurd_more.yodhha;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class MyAdmin extends DeviceAdminReceiver  {
    public static int count=0;
    @Override
    public void onEnabled(Context context, Intent intent) {
        Toast.makeText(context, "Device Admin : enabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        Toast.makeText(context, "Device Admin : disabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPasswordFailed(@NonNull Context context, @NonNull Intent intent) {
        super.onPasswordFailed(context, intent);
        count=count+1;

        if(count>2 && PreferenceManager.getDefaultSharedPreferences(context).contains("appPassword"))
        {
            Intent inte = new Intent(context, MusicService.class);
            context.startService(inte);
            Intent in= new Intent(context,LockScreen.class);
            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(in);
            PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("screenlock", true).apply();
        }

    }

    @Override
    public void onPasswordSucceeded(@NonNull Context context, @NonNull Intent intent, @NonNull UserHandle user) {




        count=0;
        if(Accessibility.dialogThread!=null)
        {
            Accessibility.dialogThread.setRun(false);
                  }
        Accessibility.screenOff=false;

        super.onPasswordSucceeded(context, intent, user);

    }


}
