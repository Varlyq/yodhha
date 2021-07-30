package com.example.theftapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

public class SafetyMessage extends AppCompatActivity {

    Toolbar toolbar;
    EditText message;

    AppCompatButton save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety_message);

        Window window= getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.toolbartitlecolor));



        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.backbutton);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        message=findViewById(R.id.message);

        message.setText(PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .getString("message","Alert"));


        save=findViewById(R.id.savebutton);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("message",message.getText().toString()).apply();
                finish();
            }
        });




    }
}