package com.aurd_more.yodhha;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;


public class OtpVerification extends AppCompatActivity {
    FirebaseAuth auth;
    String verificationcode;
    EditText editText1,editText2,editText3,editText4,editText5,editText6;
    Button btnVerify;
    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        Window window= getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.toolbartitlecolor));
//
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        editText1  = findViewById(R.id.edittext1);
        editText2 = findViewById(R.id.edittext2);
        editText3 = findViewById(R.id.edittext3);
        editText4 = findViewById(R.id.edittext4);
        editText5 = findViewById(R.id.edittext5);
        editText6 = findViewById(R.id.edittext6);
        btnVerify = findViewById(R.id.btnVerify);



        Intent intent = getIntent();

        auth = FirebaseAuth.getInstance();
        verificationcode = intent.getStringExtra("verificationcode");
        number = intent.getStringExtra("number");

        setUpOTPInput();

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText1.getText().toString().trim().isEmpty()||
                        editText2.getText().toString().trim().isEmpty()||
                        editText3.getText().toString().trim().isEmpty()||
                        editText4.getText().toString().trim().isEmpty()||
                        editText5.getText().toString().trim().isEmpty()||
                        editText6.getText().toString().trim().isEmpty()){

                    Toast.makeText(OtpVerification.this, "Please validate otp", Toast.LENGTH_SHORT).show();
                }else{
                    verify();
                }
            }
        });



    }

    private  void setUpOTPInput(){
        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    editText2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    editText3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editText3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    editText4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editText4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    editText5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editText5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    editText6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public  void verify(){
        String input_code = editText1.getText().toString()+editText2.getText().toString()+editText3.getText().toString()
                +editText4.getText().toString()+editText5.getText().toString()+editText6.getText().toString();


        if(input_code.equals(verificationcode)){
            Intent intent = new Intent(this,NewPasswordActivity.class);
            intent.putExtra("number",number);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this, "Please enter valid otp", Toast.LENGTH_SHORT).show();
        }
    }

}