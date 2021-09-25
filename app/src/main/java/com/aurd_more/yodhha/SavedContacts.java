package com.aurd_more.yodhha;

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

import com.google.firebase.auth.FirebaseAuth;

public class SavedContacts extends AppCompatActivity {
    String ss1,ss2,ss3;
    String sn1,sn2,sn3;
    EditText firstName,firstNumber,secondName,secondNumber,thirdName,thirdNumber;

    TextView savebtn,txttoolbar;
    FirebaseAuth firebaseAuth;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_contacts);
        firstName = findViewById(R.id.firstname);
        secondName = findViewById(R.id.secondname);
        thirdName = findViewById(R.id.thirdname);
        firstNumber = findViewById(R.id.firstnumber);
        secondNumber = findViewById(R.id.secondnumber);
        thirdNumber = findViewById(R.id.thirdnumber);

        Window window= getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.toolbartitlecolor));

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.backbutton);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        txttoolbar = findViewById(R.id.txthead);
        savebtn = findViewById(R.id.btnedit);
        btnLogout = findViewById(R.id.logout);

        btnLogout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                PreferenceManager.getDefaultSharedPreferences(SavedContacts.this).edit().clear().apply();
                firebaseAuth.signOut();
                Intent intent = new Intent(SavedContacts.this,Login.class);
                startActivity(intent);
                finishAffinity();
            }
        });

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SavedContacts.this,"Contacts Saved",Toast.LENGTH_SHORT).show();
                ss1=firstNumber.getText().toString();
                ss2=secondNumber.getText().toString();
                ss3=thirdNumber.getText().toString();
                sn1=firstName.getText().toString();
                sn2=secondName.getText().toString();
                sn3=thirdName.getText().toString();
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("firstNUMBER",ss1).putString("secondNUMBER",ss2).putString("thirdNUMBER",ss3).apply();
                Intent intent = new Intent(getApplicationContext(),HumanSafety.class);
                startActivity(intent);
                finish();
            }
        });
    }
}