package com.example.theftapp;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import org.json.JSONArray;
import org.json.JSONException;

public class backgroundContactService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    public static boolean active=false;

    Handler handler;
    Runnable runnable;
//    private Timer timer;
//    private TimerTask timerTask;

//    SmsThreadHuman smsThread;

    long timeStamp=System.currentTimeMillis();

    ArrayList arrayList=new ArrayList();

    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private static final String LOGSERVICE = "#######";

    Location lastlocation;


String msg="Alert";

    @Override
    public void onCreate() {
        try {

            buildGoogleApiClient();

            LocationManager locationManager = (LocationManager)
                    getSystemService(Context.LOCATION_SERVICE);

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

            msg=PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                    .getString("message","Alert");
            JSONArray jsonArray=new JSONArray(PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                    .getString("contacts","[]"));

            for(int i=0;i<jsonArray.length();i++)
            {
                Contact contact=new Gson().fromJson(jsonArray.getJSONObject(i).toString(),Contact.class);
                arrayList.add(contact);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            startMyOwnForeground();
        }else
        {   startForeground(1, new Notification());

        }
        super.onCreate();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground()
    {

        Intent notificationIntent = new Intent(this, HumanSafety.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent=PendingIntent.getActivity(this, 0,
                notificationIntent,0 );

        String NOTIFICATION_CHANNEL_ID = "example.permanence_contact";
        String channelName = "Background Contact Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("Your Device is Secured")
                .setContentText("Your Device is Secured")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(2, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (!mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();

        active=true;
        Toast.makeText(this, "SMS Sending Started", Toast.LENGTH_SHORT).show();
        super.onStartCommand(intent, flags, startId);
        startTimer();
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        active=false;
        Toast.makeText(this, "Message sending Stopped", Toast.LENGTH_SHORT).show();
        stopTimerTask();
        stopLocationUpdate();
        super.onDestroy();
    }


    public  void startTimer()
    {
//smsThread=new SmsThreadHuman(msg,getApplicationContext(),mGoogleApiClient,arrayList);
//smsThread.setRun(true);
//smsThread.start();
handler=new Handler();

           runnable=new Runnable() {
               @Override
               public void run() {
                   try {
                       String message=msg;
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
                       if(l!=null)
                       {

                           message=message+"\nhttps://moreandhigh.com/location?lat="+l.getLatitude()+"&lng="+l.getLongitude()+"&name="+
                                   PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("UserName","");

                       }
                       else{
                           message=message+"\nhttps://moreandhigh.com/location?lat=0.00&lng=0.00&name="+
                                   PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("UserName","");

                       }

                       SmsManager sms = SmsManager.getDefault();
                       String finalMessage = message;
                       arrayList.forEach(o -> {
                           sms.sendTextMessage(((Contact) o).number, null, finalMessage, null, null);


                       });

                   }catch ( Exception e)
                   {
                       System.out.println(e);
                   }
handler.postDelayed(this,10000);
               }
           };
//
        handler.post(runnable);
//        timer=new Timer() ;
//        timerTask=new TimerTask(){
//            public void run(){
//
//
//
//            }
//        };
//        timer.schedule(timerTask,20,20000);
    }

    public  void stopTimerTask(){

//        if(smsThread!=null)
//        {
//        smsThread.setRun(false);
//        }
//        if(timer!=null)
//        {
//            timer.cancel();
//            timer=null;
//        }
        if(handler!=null)
        handler.removeCallbacks(runnable);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
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

}


