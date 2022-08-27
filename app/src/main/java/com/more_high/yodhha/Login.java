package com.more_high.yodhha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;


import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    Button loginButton;
    TextView forgetButtonText, signupLink;
    EditText textemailPhone;
    EditText textPasword;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    SensorManager sensorManager;
    ImageView imageView;
    Bitmap bitmap;
    //    GifImageView imageView;
    String imei;

    @Override
    protected void onResume() {
        super.onResume();


    }


    @Override
    protected void onStop() {
        // Unregister the listener
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        mBackground  = (ParallaxImageView) findViewById(R.id.parallex);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.toolbartitlecolor));

        textemailPhone = findViewById(R.id.email_phone);
        textPasword = findViewById(R.id.password);
        forgetButtonText = findViewById(R.id.forgetPassword);
        loginButton = findViewById(R.id.btnLogin);
        signupLink = findViewById(R.id.signuplink);
        firebaseAuth = FirebaseAuth.getInstance();


//
//        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
//         bitmap = bitmapDrawable.getBitmap();

//        GifDrawable bitmapDrawable = (GifDrawable) imageView.getDrawable();
//         bitmap = bitmapDrawable.getCurrentFrame();

        forgetButtonText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, ForgetPasswordActivity.class);
                startActivity(i);
            }
        });

        if (PreferenceManager.getDefaultSharedPreferences(Login.this).getBoolean("login", false)) {
            Intent intent = new Intent(Login.this, HomeScreen.class);
            intent.putExtra("context", "Login");
            startActivity(intent);
            finish();
            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("message","Alert").apply();

        }


        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String useremail = textemailPhone.getText().toString();
                String userpassword = textPasword.getText().toString();


                if (useremail.isEmpty() || userpassword.isEmpty()) {
                    Toast.makeText(Login.this, "Please complete fields", Toast.LENGTH_LONG).show();

                } else {

                    FirebaseFirestore.getInstance().collection("Users").whereEqualTo("phoneNumber",
                            textemailPhone.getText().toString()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (queryDocumentSnapshots.getDocuments().isEmpty()) {
                                Toast.makeText(Login.this, "No User Found", Toast.LENGTH_SHORT).show();
                            } else {
                                DocumentSnapshot doc = queryDocumentSnapshots.getDocuments().get(0);
                                String email_Phone = queryDocumentSnapshots.getDocuments().get(0).get("email").toString();
                                String token = "";
                                if (queryDocumentSnapshots.getDocuments().get(0).contains("token")) {
                                    token = queryDocumentSnapshots.getDocuments().get(0).getString("token");
                                }
//
                                String finalToken = token;
                                firebaseAuth.signInWithEmailAndPassword(email_Phone, userpassword)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {

                                                if (task.isSuccessful()) {
                                                    user = FirebaseAuth.getInstance().getCurrentUser();
                                                    System.out.println(user.getUid());
                                                    PreferenceManager.getDefaultSharedPreferences(Login.this).edit().putString("UserID", user.getUid()).apply();
//                                                   PreferenceManager.getDefaultSharedPreferences(Login.this).edit().putBoolean("login", true).apply();
                                                    PreferenceManager.getDefaultSharedPreferences(Login.this).edit()
                                                            .putString("AlternateNumber", doc.getString("alternatePhoneNumber"))
                                                            .putString("UserName", doc.getString("name"))
                                                            .putString("phoneNumber",doc.getString("phoneNumber")).apply();


                                                    if (finalToken.isEmpty()) {
                                                        Intent intent = new Intent(Login.this, ActivateScreen.class);
                                                        //   intent.putExtra("context","Login");
                                                        intent.putExtra("token", finalToken);
                                                        startActivity(intent);
                                                        finish();
                                                    } else {
                                                        FirebaseFirestore.getInstance().collection("tokens").whereEqualTo("tokenId",doc.getString("token")).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                DocumentSnapshot tokenDoc=queryDocumentSnapshots.getDocuments().get(0);
                                                                if(tokenDoc.contains("type"))
                                                                {

                                                                    if(System.currentTimeMillis()-tokenDoc.getLong("registeredAt")>31556952000L)
                                                                    {
                                                                        Toast.makeText(Login.this, "Subscription Expired", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                    else {
                                                                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("login", true).apply();
                                                                        Intent intent = new Intent(Login.this, HomeScreen.class);
                                                                        intent.putExtra("context", "Login");
                                                                        startActivity(intent);
                                                                        updateDeviceId(doc);
                                                                        finish();
                                                                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("message","Alert").apply();

                                                                    }
                                                                }else{
                                                                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("login", true).apply();
                                                                    Intent intent = new Intent(Login.this, HomeScreen.class);
                                                                    intent.putExtra("context", "Login");
                                                                    startActivity(intent);
                                                                    updateDeviceId(doc);
                                                                    finish();
                                                                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("message","Alert").apply();

                                                                }
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(Login.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });

                                                    }


                                                    updateToken(doc);
//


                                                } else {
                                                    if (userpassword.length() < 6) {
                                                        Toast.makeText(Login.this, "Password length is invalid", Toast.LENGTH_LONG).show();
                                                    } else {
                                                        Toast.makeText(Login.this, "Please enter valid password", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                            Toast.makeText(Login.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }
        });

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
//        imei = telephonyManager.getDeviceId();


    }

    private void updateToken(DocumentSnapshot doc) {

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        Map<String, Object> map = new HashMap<>();
                        map.put("deviceId", token);
                        map.put("imei", imei);
                        FirebaseFirestore.getInstance().collection("Users").document(doc.getId()).update(map);

                    }
                });
    }


    private void updateDeviceId(DocumentSnapshot doc) {
        String androidId = Settings.Secure.getString(
                getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Map<String, Object> map = new HashMap<>();
        map.put("androidId", androidId);
        FirebaseFirestore.getInstance().collection("Users").document(doc.getId()).update(map);


    }
}





