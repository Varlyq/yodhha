package com.example.theftapp;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

public class DialogThread extends Thread{
   boolean run=false;
    Context context;


    public boolean isRun() {
        return run;
    }

    public void setRun(boolean run) {
        this.run = run;
    }

    public DialogThread(Context context) {
        this.context=context;
    }


    @Override
    public void run() {

      while (run)
      {

          Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
          context.sendBroadcast(closeDialog);
          try {
              Thread.sleep(100);

          } catch (InterruptedException e) {
              e.printStackTrace();
          }
      }
    }
}
