package com.aurd_more.yodhha;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;

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


