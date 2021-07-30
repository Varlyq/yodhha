package com.example.theftapp;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.eventbus.EventBus;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.LatLng;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class LocatioinService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

   public String timestamp;
    public LocatioinService() {
        this.timestamp=String.valueOf(System.currentTimeMillis());
    }

//    SmsThread smsThread;


    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private static final String LOGSERVICE = "#######";


    public static String latitude;
    public static String longitude;
    private FusedLocationProviderClient mFusedLocationClient;
    Location lastlocation;
    int smsCount=0;
    String number="";
    String alternateNumber="";

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        buildGoogleApiClient();

        System.out.println("service started");

        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);




//        LocationListener locationListener = new MyLocationListener(getApplicationContext());
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
//        locationManager.requestLocationUpdates(
//                LocationManager.GPS_PROVIDER, 1000, 1, locationListener);



    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(LOGSERVICE, "onStartCommand");

        if (!mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();

        if(intent.hasExtra("number"))
        {
            number=intent.getStringExtra("number");
        }


        alternateNumber=PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("AlternateNumber","");

        startTimer();
        return START_STICKY;
    }




    @Override
    public void onConnected(Bundle bundle) {
        Log.i(LOGSERVICE, "onConnected" + bundle);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        if (l != null) {
            Log.i(LOGSERVICE, "lat " + l.getLatitude());
            Log.i(LOGSERVICE, "lng " + l.getLongitude());

        }

        startLocationUpdate();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(LOGSERVICE, "onConnectionSuspended " + i);

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(LOGSERVICE, "lat " + location.getLatitude());
        Log.i(LOGSERVICE, "lng " + location.getLongitude());
//        Toast.makeText(this, " lat " + location.getLatitude() + " lon " + location.getLongitude(), Toast.LENGTH_SHORT).show();
//        EventBus.getDefault().post(mLocation);
    }


//    private Timer timer;
//    private TimerTask timerTask;

    Handler handler;
    Runnable runnable;
    String cameraBoolean="back";
    public void startTimer() {


//        smsThread=new SmsThread(getApplicationContext(),mGoogleApiClient,timestamp,smsCount,number,alternateNumber,cameraBoolean);
//        smsThread.setRun(true);
//        smsThread.start();
//
        handler=new Handler();

        runnable=new Runnable() {
            @Override
            public void run() {


               if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                    Intent in = new Intent(getApplicationContext(), CameraBackService.class);
                    in.putExtra("Front_Request", true);
                    getApplicationContext().startService(in);
                    cameraBoolean = "front";
                } else if (!CameraBackService.active && cameraBoolean == "front") {
                    Intent in = new Intent(getApplicationContext(), CameraBackService.class);

                    getApplicationContext().startService(in);
                    cameraBoolean = "back";
                }

                handler.postDelayed(this,5000);
            }


        };


        handler.post(runnable);

//        timer = new Timer();
//        timerTask = new TimerTask() {
//            public void run() {
//
//
//            }
//        };
//        timer.schedule(timerTask, 5, 5000); //
    }

    public void stoptimertask() {
//        if (smsThread!=null)
//        {
//            smsThread.setRun(false);
//        }


        if(handler!=null)
        {
            handler.removeCallbacks(runnable);
        }
//        if (timer != null) {
//            timer.cancel();
//            timer = null;
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(LOGSERVICE, "onDestroy - Estou sendo destruido ");
        stopLocationUpdate();
        stoptimertask();

    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(LOGSERVICE, "onConnectionFailed ");

    }

    private void initLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    private void startLocationUpdate() {
        initLocationRequest();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    private void stopLocationUpdate() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void sendSms(String latitude, String longitude) {


        String msg="https://moreandhigh.com/location?lat="+latitude+"&lng="+longitude+"&name="+
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("UserName","");


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




