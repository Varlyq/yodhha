package com.example.theftapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

public class SplashScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        String androidId = Settings.Secure.getString(
                getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        System.out.println("android id"+androidId);

        if(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).contains("UserID") &&
                PreferenceManager.getDefaultSharedPreferences(SplashScreen.this).getBoolean("login",false))
        {
            String userId= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("UserID","");
                    FirebaseFirestore.getInstance().collection("Users").whereEqualTo("userId",userId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if(!queryDocumentSnapshots.getDocuments().isEmpty())
                            {

                                if(queryDocumentSnapshots.getDocuments().get(0).getString("androidId").equals(androidId))
                                {
                                    Intent intent = new Intent(SplashScreen.this, HomeScreen.class);
                                    intent.putExtra("context", "Login");
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().clear().apply();
                                    Intent intent = new Intent(SplashScreen.this,Login.class);
                                    startActivity(intent);
                                    finish();
                                }

                            }
                            else {
                                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().clear().apply();
                                Intent intent = new Intent(SplashScreen.this,Login.class);
                                startActivity(intent);
                                finish();

                            }


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SplashScreen.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        else{
            Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                    Intent intent = new Intent(SplashScreen.this,Login.class);
                    startActivity(intent);
                    finish();

            }
        }, 1000);
        }





    }
}