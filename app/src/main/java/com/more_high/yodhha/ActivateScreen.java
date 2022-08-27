package com.more_high.yodhha;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ActivateScreen extends AppCompatActivity {
    EditText activateCode;
    Button enterButton,buyNow;
    String stringCode;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    String imei;
    String checkToken = "";
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate_screen);
        Window window= getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.toolbartitlecolor));

        activateCode = findViewById(R.id.token);
        enterButton = findViewById(R.id.btnEnter);
        buyNow = findViewById(R.id.btnBuyNow);

        firebaseAuth = FirebaseAuth.getInstance();

        stringCode = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("activationCODE","");

        intent = getIntent();
        if(intent.hasExtra("token")){
            checkToken=intent.getStringExtra("token");
        }


        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.moreandhigh.com/payment"));
                startActivity(browserIntent);
            }
        });
        enterButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                String stringCode = activateCode.getText().toString();

                if(stringCode.isEmpty()){
                    Toast.makeText(ActivateScreen.this, "Please complete fields", Toast.LENGTH_LONG).show();

                }



//                else if(!checkToken.isEmpty()){
//                    FirebaseFirestore.getInstance().collection("Users").whereEqualTo("token",activateCode.getText().toString()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                        @Override
//                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                            if( queryDocumentSnapshots.getDocuments().isEmpty()){
//                                Toast.makeText(ActivateScreen.this, "Invalid Code Entered", Toast.LENGTH_SHORT).show();
//                            }
//                            if(!queryDocumentSnapshots.getDocuments().isEmpty()){
//                                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("login", true).apply();
//                                Intent intent = new Intent(ActivateScreen.this, HomeScreen.class);
//                                intent.putExtra("context","Login");
//                                startActivity(intent);
//                                finish();
//                            }
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            e.printStackTrace();
//                            Toast.makeText(ActivateScreen.this, "Something went wrong", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }

                else {
                    System.out.println( activateCode.getText().toString());
                    FirebaseFirestore.getInstance().collection("tokens").whereEqualTo("tokenId",
                            activateCode.getText().toString()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            String tokenDocumentId=queryDocumentSnapshots.getDocuments().get(0).getId();
                            if(queryDocumentSnapshots.getDocuments().isEmpty()){
                                Toast.makeText(ActivateScreen.this, "Invalid Code Entered", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                DocumentSnapshot doc = queryDocumentSnapshots.getDocuments().get(0);
                                String tokenCode= queryDocumentSnapshots.getDocuments().get(0).get("tokenId").toString();
                                boolean registion = queryDocumentSnapshots.getDocuments().get(0).getBoolean("registered");

                                if(registion == true)
                                {
                                    Toast.makeText(ActivateScreen.this, "Already in use", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    FirebaseFirestore.getInstance().collection("Users")
                                            .whereEqualTo("userId", userId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            if(!queryDocumentSnapshots.getDocuments().isEmpty()){
                                                String androidId = Settings.Secure.getString(
                                                        getApplicationContext().getContentResolver(),
                                                        Settings.Secure.ANDROID_ID);
                                               String docRef = queryDocumentSnapshots.getDocuments().get(0).getId();
                                                Map<String,Object> map=new HashMap<>();
                                                map.put("token",tokenCode);
                                                map.put("androidId", androidId);
                                                FirebaseFirestore.getInstance().collection("Users").document(docRef).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        Map<String,Object> map=new HashMap<>();
                                                        map.put("userId",userId);
                                                        map.put("registeredAt",System.currentTimeMillis());
                                                        map.put("registered",true);
                                                        FirebaseFirestore.getInstance().collection("tokens").document(tokenDocumentId).update(map);
                                                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("login", true).apply();
                                                        Intent intent = new Intent(ActivateScreen.this, HomeScreen.class);
                                                        intent.putExtra("context","Login");
                                                        startActivity(intent);
                                                        finish();
                                                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("message","Alert").apply();

//                                                        updateToken(doc);

                                                    }
                                                });


                                            }
                                        }
                                    });
                                }


                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                            Toast.makeText(ActivateScreen.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });


                }

            }
        });
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

                        Map<String,Object> map=new HashMap<>();
                        map.put("userId",token);
                        map.put("registeredAt",imei);
                        FirebaseFirestore.getInstance().collection("tokens").document(doc.getId()).update(map);

                    }
                });
    }


}