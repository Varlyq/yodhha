package com.aurd_more.yodhha;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;


public class    RecieveSms extends BroadcastReceiver {
    boolean lock;


    public void onReceive(Context context, Intent intent) {

//                mediaPlayer.start();
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("screenlock", false).apply();

        lock=PreferenceManager.getDefaultSharedPreferences(context).getBoolean("lock",false);
        Bundle myBundle = intent.getExtras();
        SmsMessage[] messages = null;
        String strMessage = "";

        String number="";
        if (myBundle != null) {
            Object[] pdus = (Object[]) myBundle.get("pdus");
            messages = new SmsMessage[pdus.length];

            String sms = "";
            for (int i = 0; i < messages.length; i++) {
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                strMessage += "SMS From: " + messages[i].getOriginatingAddress();
                strMessage += " : ";
                strMessage += messages[i].getMessageBody();
                sms = messages[i].getDisplayMessageBody();
                strMessage += "\n";
                number=messages[i].getOriginatingAddress();


            }


            String pin = PreferenceManager.getDefaultSharedPreferences(context).getString("appPassword","");
            System.out.println("Pin"+pin);

            if (sms.equals("START "+pin) &&!pin.isEmpty()) {


                AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 100, 0);
                Intent in = new Intent(context, AlertScreen.class);
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(in);
//                        mediaPlayer.start();

                Intent inte = new Intent(context, MusicService.class);
                context.startService(inte);
                lock = true;

                PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("lock", true).apply();


                Toast.makeText(context, sms, Toast.LENGTH_LONG).show();

                Intent locatIntent=new Intent(context,LocatioinService.class);
                locatIntent.putExtra("number",number);
                context.startService(locatIntent);
try {
    if (PreferenceManager.getDefaultSharedPreferences(context).contains("UserID")) {
        FirebaseFirestore.getInstance().collection("Users").whereEqualTo("userId",
                PreferenceManager.getDefaultSharedPreferences(context).getString("UserID", ""))
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.getDocuments().isEmpty()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("locked", true);
                    FirebaseFirestore.getInstance().collection("Users").document(queryDocumentSnapshots.getDocuments()
                            .get(0).getId()).update(map);
                }


            }
        });

    }
}catch (Exception e)
{

}
            }
            else if (sms.equals("STOP "+pin)&&!pin.isEmpty()) {
                Intent in = new Intent(RecieveSms.class.getName());
                in.putExtra("value", "STOP");
                LocalBroadcastManager.getInstance(context).sendBroadcast(in);

//                        mediaPlayer.stop();
                Intent inte = new Intent(context, MusicService.class);
                context.stopService(inte);
                lock = false;
                PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("lock", false).apply();
                Intent locatIntent=new Intent(context,LocatioinService.class);
                context.stopService(locatIntent);
                try {
                    if (PreferenceManager.getDefaultSharedPreferences(context).contains("UserID")) {
                        FirebaseFirestore.getInstance().collection("Users").whereEqualTo("userId",
                                PreferenceManager.getDefaultSharedPreferences(context).getString("UserID", ""))
                                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (!queryDocumentSnapshots.getDocuments().isEmpty()) {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("locked", false);
                                    FirebaseFirestore.getInstance().collection("Users").document(queryDocumentSnapshots.getDocuments()
                                            .get(0).getId()).update(map);
                                }


                            }
                        });

                    }
                }catch (Exception e)
                {

                }

            }
            else if(sms.equals("BACKSC "+pin) &&!pin.isEmpty()){
                Intent recordIntent = new Intent(context, BackgroundService.class);
                recordIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    startService(intent);
//                }
                context.startService(recordIntent);
            }
            else if(sms.equals("BACKEC "+pin) &&!pin.isEmpty()){
                Intent recordIntent = new Intent(context, BackgroundService.class);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    startService(intent);
//                }
                context.stopService(recordIntent);
            }
            else if(sms.equals("FRONTSC "+pin) &&!pin.isEmpty()){
                Intent recordIntent = new Intent(context, CameraFrontService.class);
                recordIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    startService(intent);
//                }
                context.startService(recordIntent);
            }
            else if(sms.equals("FRONTEC "+pin) &&!pin.isEmpty()){
                Intent recordIntent = new Intent(context, CameraFrontService.class);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    startService(intent);
//                }
                context.stopService(recordIntent);
            }
            else if(sms.equals("VOICES "+pin) &&!pin.isEmpty()){
                Intent recordIntent = new Intent(context, AudioRecorderService.class);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    startService(intent);
//                }
                context.startService(recordIntent);
            }

            else if(sms.equals("VOICEE "+pin) &&!pin.isEmpty()){
                Intent recordIntent = new Intent(context, AudioRecorderService.class);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    startService(intent);
//                }
                context.stopService(recordIntent);
            }


            // Toast.makeText(context, strMessage, Toast.LENGTH_LONG).show();
            System.out.println("message");
        }


    }



}



