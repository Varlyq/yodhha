//package com.more_high.yodhha;
//
//import android.Manifest;
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.preference.PreferenceManager;
//import android.telephony.SmsManager;
//
//import androidx.core.app.ActivityCompat;
//
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.LocationServices;
//
//import java.util.ArrayList;
//
//public  class SmsThreadHuman extends Thread{
//
//    String msg;
//    Context context;
//    private GoogleApiClient mGoogleApiClient;
//    private ArrayList<Object> arrayList;
//    long timeStamp=System.currentTimeMillis();
//    boolean run=false;
//
//    public boolean isRun() {
//        return run;
//    }
//
//    public void setRun(boolean run) {
//        this.run = run;
//    }
//
//    public SmsThreadHuman(String msg, Context context, GoogleApiClient mGoogleApiClient, ArrayList arrayList) {
//        this.msg=msg;
//        this.context=context;
//        this.mGoogleApiClient=mGoogleApiClient;
//        this.arrayList=arrayList;
//    }
//
//
//    public void run(){
//        while(run)
//        {
//            if(System.currentTimeMillis()-timeStamp>10000)
//            {
//                timeStamp=System.currentTimeMillis();
//                try {
//                    String message=msg;
//                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                        // TODO: Consider calling
//                        //    ActivityCompat#requestPermissions
//                        // here to request the missing permissions, and then overriding
//                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                        //                                          int[] grantResults)
//                        // to handle the case where the user grants the permission. See the documentation
//                        // for ActivityCompat#requestPermissions for more_high details.
//                        return;
//                    }
//                    Location l = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//                    if(l!=null)
//                    {
//
//                        message=message+"\nhttps://moreandhigh.com/location?lat="+l.getLatitude()+"&lng="+l.getLongitude()+"&name="+
//                                PreferenceManager.getDefaultSharedPreferences(context).getString("UserName","");
//
//                    }
//                    else{
//                        message=message+"\nhttps://moreandhigh.com/location?lat=0.00&lng=0.00&name="+
//                                PreferenceManager.getDefaultSharedPreferences(context).getString("UserName","");
//
//                    }
//
//                    SmsManager sms = SmsManager.getDefault();
//                    String finalMessage = message;
//                    arrayList.forEach(o -> {
//                        sms.sendTextMessage(((Contact) o).number, null, finalMessage, null, null);
//
//
//                    });
//
//                }catch ( Exception e)
//                {
//                    System.out.println(e);
//                }
//            }
//        }
//
//    }
//}
