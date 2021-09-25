package com.aurd_more.yodhha;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class HumanSafety extends AppCompatActivity implements SurfaceHolder.Callback {


    Toolbar toolbar;
    ImageView imgAlert;
    Bitmap bitmap;


    public static SurfaceView mSurfaceView;
    public static SurfaceHolder mSurfaceHolder;
    LinearLayoutCompat linearLayout;

    TextView viewContact;
    TextView viewMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_human_safety);
        Window window= getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.toolbartitlecolor));



        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.backbutton);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView1);
//        mSurfaceHolder = mSurfaceView.getHolder();
//        mSurfaceHolder.addCallback(this);
//        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        imgAlert = findViewById(R.id.btnalert);
//        textView1= findViewById(R.id.textfirstnumber);
//        textView2= findViewById(R.id.textsecondnumber);
//        textView3= findViewById(R.id.textthirdnumber);
//        btnToEdit=findViewById(R.id.screenbtn);
        viewContact=findViewById(R.id.viewContact);
        viewMessage=findViewById(R.id.viewMessage);

        viewMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
Intent intent=new Intent(HumanSafety.this,SafetyMessage.class);
startActivity(intent);

            }
        });

        linearLayout=findViewById(R.id.linearlayout);
//        SurfaceView surface=BackgroundService.tempview;
//        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
//                100, 100,
//                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
//                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
//                PixelFormat.TRANSLUCENT
//        );
//        surface.setLayoutParams(layoutParams);
//        linearLayout.addView(BackgroundService.tempview);
//        PreferenceManager.getDefaultSharedPreferences(HumanSafety.this).edit().putString("firstNUMBER","8770466317").putString("secondNUMBER","7000982007").apply();
//
//        s1= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("firstNUMBER","");
//        s2= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("secondNUMBER","");
//        s3= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("thirdNUMBER","");
//        textView1.setText(s1);
//        textView2.setText(s2);
//        textView3.setText(s3);

        if(backgroundContactService.active==true){
            imgAlert.setImageResource(R.drawable.ic_baseline_stop_circle_24);
        }else{
            imgAlert.setImageResource(R.drawable.btnalert);
        }
//
//        btnToEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(),SavedContacts.class);
//                startActivity(intent);
//            }
//
//        });

        if(backgroundContactService.active==true){
            imgAlert.setImageResource(R.drawable.ic_baseline_stop_circle_24);
        }else{
            imgAlert.setImageResource(R.drawable.btnalert);
        }


        imgAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(backgroundContactService.active==false){
                    imgAlert.setImageResource(R.drawable.ic_baseline_stop_circle_24);
                    Intent smsIntent = new Intent(HumanSafety.this,backgroundContactService.class);
                    startService(smsIntent);
                }else{
                    imgAlert.setImageResource(R.drawable.btnalert);
                    Intent smsIntent = new Intent(HumanSafety.this,backgroundContactService.class);
                    stopService(smsIntent);
                }

            }


        });

        viewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HumanSafety.this,Contacts.class);
                startActivity(intent);
            }
        });

//        toolbar.inflateMenu(R.menu.main_menu);

//        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
//        bitmap = bitmapDrawable.getBitmap();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri videoUri = data.getData();
        String path = videoUri.getPath();
        System.out.println(path);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main_menu, menu);
//
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle item selection
//        switch (item.getItemId()) {
//            case R.id.action_pick_contact:
//                pickContact();
//                return true;
//            case R.id.action_message:
//                return true;
////            case R.id.profile:
////                Intent intent = new Intent(HumanSafety.this,ProfilePage.class);
////                startActivity(intent);
////                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }


    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

//    public void pickContact()
//    {
//        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//        startActivityForResult(intent, 1);
//    }
//


}