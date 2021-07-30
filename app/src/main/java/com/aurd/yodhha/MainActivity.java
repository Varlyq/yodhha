package com.example.theftapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CardView antitheftCard = (CardView) findViewById(R.id.antiTheftCard);
        CardView  humansafetyCard = findViewById(R.id.humanSafetyCard);


//        antitheftCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this,AntiTheftActivity.class);
//                startActivity(intent);
//
//            }
//        });


    }
}


