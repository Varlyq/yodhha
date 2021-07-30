
package com.example.theftapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class ProfilePage extends AppCompatActivity {
    TextView txtEdit,txttoolbar;
    EditText txtname,txtemail,txtmainPhoneNumber,txtpassword,txtAlterPhoneNumber;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String userId="";
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        Window window= getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.toolbartitlecolor));

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.backbutton);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        txttoolbar = findViewById(R.id.txthead);
        txtEdit = findViewById(R.id.btnedit);
        txtname= findViewById(R.id.name);
        txtname.setEnabled(false);
        txtemail = findViewById(R.id.email);
        txtemail.setEnabled(false);
        txtpassword = findViewById(R.id.password);
        txtpassword.setEnabled(false);
        txtmainPhoneNumber = findViewById(R.id.mobilenumber);
        txtmainPhoneNumber.setEnabled(false);
        txtAlterPhoneNumber = findViewById(R.id.alterphonenumber);
        txtAlterPhoneNumber.setEnabled(false);

        btnLogout = findViewById(R.id.logout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("-----------------------Logout------------------------");

                PreferenceManager.getDefaultSharedPreferences(ProfilePage.this).edit().clear().apply();
                firebaseAuth.signOut();
                Intent intent = new Intent(ProfilePage.this,Login.class);
                startActivity(intent);
                finishAffinity();

            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        System.out.println("UserID"+  PreferenceManager.getDefaultSharedPreferences(ProfilePage.this).getString("UserID",""));
        userId = PreferenceManager.getDefaultSharedPreferences(ProfilePage.this).getString("UserID","");


        firebaseFirestore.collection("Users").whereEqualTo("userId",userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot documentSnapshot : task.getResult()){
                        System.out.println((CharSequence)documentSnapshot.get("email"));
                        txtname.setText((CharSequence)documentSnapshot.get("name"));
                        txtemail.setText((CharSequence)documentSnapshot.get("email"));
                        txtpassword.setText((CharSequence)documentSnapshot.get("password"));
                        txtmainPhoneNumber.setText((CharSequence)documentSnapshot.get("phoneNumber"));
                        txtAlterPhoneNumber.setText((CharSequence)documentSnapshot.get("alternatePhoneNumber"));
                    }
                }
            }
        });




        txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtEdit.getText().equals("Edit")){
                    txtname.setEnabled(true);
                    txtemail.setEnabled(true);
                    txtpassword.setEnabled(true);
                    txtmainPhoneNumber.setEnabled(true);
                    txtAlterPhoneNumber.setEnabled(true);
                    txtEdit.setText("Save");
                    txttoolbar.setText("Update Profile");

                }
                else if(txtEdit.getText().equals("Save")){

                    UserModal userModal = new UserModal();
                    userModal.setName(txtname.getText().toString());
                    userModal.setEmail(txtemail.getText().toString());
                    userModal.setPassword(txtpassword.getText().toString());
                    userModal.setPhoneNumber(txtmainPhoneNumber.getText().toString());
                    userModal.setAlternatePhoneNumber(txtAlterPhoneNumber.getText().toString());

                    CollectionReference collectionReference = firebaseFirestore.collection("Users");

                     collectionReference.whereEqualTo("userId",userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Map<Object, String> hashMap = new HashMap<>();
                                    hashMap.put("name",txtname.getText().toString());
                                    hashMap.put("email",txtemail.getText().toString());
                                    hashMap.put("password",txtpassword.getText().toString());
                                    hashMap.put("alternatePhoneNumber",txtAlterPhoneNumber.getText().toString());
                                    hashMap.put("phoneNumber",txtmainPhoneNumber.getText().toString());
                                    collectionReference.document(document.getId()).set(hashMap, SetOptions.merge());
                                }

                                Toast.makeText(ProfilePage.this, "Updated", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


//
                }
            }
        });








    }
}