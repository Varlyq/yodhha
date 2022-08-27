package com.more_high.yodhha;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        System.out.println("message "+remoteMessage.getData().get("task"));

        if(remoteMessage.getData().containsKey("task"))
        {
            if(remoteMessage.getData().get("task").equals("lock"))
            {
                AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 100, 0);
                Intent in = new Intent(getApplicationContext(), AlertScreen.class);
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                getApplicationContext().startActivity(in);
//                        mediaPlayer.start();

                Intent inte = new Intent(getApplicationContext(), MusicService.class);
                getApplicationContext().startService(inte);
//                lock = true;

                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("lock", true).apply();


//                Intent startIntent = context
//                        .getPackageManager()
//                        .getLaunchIntentForPackage(context.getPackageName());
//
//                startIntent.setFlags(
//                        Intent.FLAG_ACTIVITY_REORDER_TO_FRONT |
//                                Intent.FLAG_ACTIVITY_NEW_TASK |
//                                Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
//                );
//                context.startActivity(startIntent);

//                Toast.makeText(, sms, Toast.LENGTH_LONG).show();

                Intent locatIntent=new Intent(getApplicationContext(),LocatioinService.class);
                getApplicationContext().startService(locatIntent);
                if(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).contains("UserID"))
                {
                    FirebaseFirestore.getInstance().collection("Users").whereEqualTo("userId",
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("UserID",""))
                            .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if(!queryDocumentSnapshots.getDocuments().isEmpty())
                            {
                                Map<String,Object> map=new HashMap<>();
                                map.put("locked",true);
                                FirebaseFirestore.getInstance().collection("Users").document(queryDocumentSnapshots.getDocuments()
                                        .get(0).getId()).update(map);
                            }


                        }
                    });

                }
            }
            else if(remoteMessage.getData().get("task").equals("unlock"))
            {
                Intent in = new Intent(RecieveSms.class.getName());
                in.putExtra("value", "STOP");
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(in);

//                        mediaPlayer.stop();
                Intent inte = new Intent(getApplicationContext(), MusicService.class);
                getApplicationContext().stopService(inte);
//                lock = false;
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("lock", false).apply();
                Intent locatIntent=new Intent(getApplicationContext(),LocatioinService.class);
                getApplicationContext().stopService(locatIntent);
                if(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).contains("UserID"))
                {
                    FirebaseFirestore.getInstance().collection("Users").whereEqualTo("userId",
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("UserID",""))
                            .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if(!queryDocumentSnapshots.getDocuments().isEmpty())
                            {
                                Map<String,Object> map=new HashMap<>();
                                map.put("locked",false);
                                FirebaseFirestore.getInstance().collection("Users").document(queryDocumentSnapshots.getDocuments()
                                        .get(0).getId()).update(map);
                            }


                        }
                    });

                }
            }
        }
    }


    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        System.out.println("token ---  "+s);
        if(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).contains("UserID"))
        {
            FirebaseFirestore.getInstance().collection("Users").whereEqualTo("userId",
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("UserID",""))
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
if(!queryDocumentSnapshots.getDocuments().isEmpty())
{
    Map<String,Object> map=new HashMap<>();
    map.put("deviceId",s);
    FirebaseFirestore.getInstance().collection("Users").document(queryDocumentSnapshots.getDocuments()
            .get(0).getId()).update(map);
}


                }
            });

        }

    }
}
