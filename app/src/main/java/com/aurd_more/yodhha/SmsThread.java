package com.aurd_more.yodhha;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SmsThread extends Thread{

    Context context;
    private GoogleApiClient mGoogleApiClient;
    private String timestamp;
    private int smsCount;
    private String number;
    private String alternateNumber;
    private String cameraBoolean;
    boolean run=false;
    long time=System.currentTimeMillis();


    public SmsThread(Context context,GoogleApiClient mGoogleApiClient, String timestamp, int smsCount, String number,String alternateNumber, String cameraBoolean) {
        this.context=context;
        this.alternateNumber=alternateNumber;
        this.mGoogleApiClient=mGoogleApiClient;
        this.timestamp=timestamp;
        this.smsCount=smsCount;
        this.number=number;
        this.cameraBoolean=cameraBoolean;

    }

    public boolean isRun() {
        return run;
    }

    public void setRun(boolean run) {
        this.run = run;
    }

    public void run() {

        while (run )
        {
            if(System.currentTimeMillis()-time>5000) {
                time=System.currentTimeMillis();
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Location l = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (l != null)
                    System.out.println(" lat " + l.getLatitude() + " lon " + l.getLongitude());
//                Toast.makeText(getApplicationContext(), " lat " + l.getLatitude() + " lon " + l.getLongitude(), Toast.LENGTH_SHORT).show();
////
                Log.i("Count acc", "=========  ");

                if (l != null) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("latitude", String.valueOf(l.getLatitude()));
                    map.put("longitude", String.valueOf(l.getLongitude()));
                    map.put("session", timestamp);
                    map.put("timestamp", System.currentTimeMillis());
                    map.put("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    FirebaseFirestore.getInstance().collection("Location").add(map);
                    sendSms(String.valueOf(l.getLatitude()), String.valueOf(l.getLongitude()));


                } else {
                    sendSms(String.valueOf(0.00), String.valueOf(0.00));

                }


                if (!CameraBackService.active && cameraBoolean == "back") {
                    Intent in = new Intent(context, CameraBackService.class);
                    in.putExtra("Front_Request", true);
                    context.startService(in);
                    cameraBoolean = "front";
                } else if (!CameraBackService.active && cameraBoolean == "front") {
                    Intent in = new Intent(context, CameraBackService.class);

                    context.startService(in);
                    cameraBoolean = "back";
                }
            }
        }

    }

    private void sendSms(String latitude, String longitude) {


        String msg="https://moreandhigh.com/location?lat="+latitude+"&lng="+longitude+"&name="+
                PreferenceManager.getDefaultSharedPreferences(context).getString("UserName","");


        if(smsCount<5 && (!number.isEmpty()))
        {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(number,null,msg,null,null);
                smsCount++;
//                Toast.makeText(getApplicationContext(), "Message Sent",
//                        Toast.LENGTH_LONG).show();
            } catch (Exception ex) {
//                Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
//                        Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }
        }

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(alternateNumber, null, msg, null, null);
//                Toast.makeText(getApplicationContext(), "Message Sent",
//                        Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
//                Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
//                        Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }


    }
}
