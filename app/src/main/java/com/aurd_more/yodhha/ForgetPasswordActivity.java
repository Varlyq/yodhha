package com.aurd_more.yodhha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;

public class ForgetPasswordActivity extends AppCompatActivity {

    EditText txtinputfield;
    Button btnforgetpassword;
    FirebaseAuth firebaseAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String verificationCode ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        Window window= getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.toolbartitlecolor));

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        txtinputfield = findViewById(R.id.inputfield);
        btnforgetpassword = findViewById(R.id.btnForgetPasssword);
        firebaseAuth = FirebaseAuth.getInstance();



        btnforgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
//                PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                        "+91 "+txtinputfield.getText().toString(),
//                        60,
//                        TimeUnit.SECONDS,
//                        ForgetPasswordActivity.this,
//                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                            @Override
//                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//                                System.out.println(phoneAuthCredential.getSmsCode());
//                            }
//
//                            @Override
//                            public void onVerificationFailed(@NonNull FirebaseException e) {
//                                System.out.println(e.getMessage());
//                                Toast.makeText(ForgetPasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                                super.onCodeSent(s, forceResendingToken);
//                                verificationCode = s;
//                                Toast.makeText(ForgetPasswordActivity.this, "OTP is send to registered number", Toast.LENGTH_SHORT).show();
//
//                                Intent intent = new Intent(ForgetPasswordActivity.this,OtpVerification.class);
//                                intent.putExtra("verificationcode",verificationCode);
//                                startActivity(intent);
//                            }
//
//                        }



//                );

                FirebaseFirestore.getInstance().collection("Users").whereEqualTo("phoneNumber",txtinputfield.getText().toString().trim()).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if(queryDocumentSnapshots.getDocuments().isEmpty())
                                {
                                    Toast.makeText(ForgetPasswordActivity.this, "No User Found", Toast.LENGTH_SHORT).show();
                                }else {

                                    String otp=String.format("%06d",new Random().nextInt(999999));
                                    new Lichterkette().execute(txtinputfield.getText().toString().trim(),otp);
                                    Intent intent = new Intent(ForgetPasswordActivity.this,OtpVerification.class);
                                    intent.putExtra("verificationcode",otp);
                                    intent.putExtra("number",txtinputfield.getText().toString().trim());
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ForgetPasswordActivity.this, "User Not Found", Toast.LENGTH_SHORT).show();
                    }
                });



            }
        });



    }




}

class Lichterkette extends AsyncTask<String,Void,String> {
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
       String number= params[0];
       String otp=params[1];
        StringBuilder sb=null;
        BufferedReader reader=null;
        String serverResponse=null;
        try {
            String text="More and high Your otp to reset password is "+otp;


            String msgUrl="https://www.smsgatewayhub.com/api/mt/SendSMS?APIKey=UUMejPpdjEGUN3MnuYcqzw&senderid=MNHTEC&channel=2&DCS=0&flashsms=0&number="+number+"&text="+ URLEncoder.encode(text)+"&route=clickhere&EntityId=1201161970828151095&dlttemplateid=1207162659120870065";
            URL url = new URL(msgUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            connection.connect();
            int statusCode = connection.getResponseCode();
            //Log.e("statusCode", "" + statusCode);
            if (statusCode == 200) {
                sb = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            }

            connection.disconnect();
            if (sb!=null)
                serverResponse=sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return serverResponse;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //All your UI operation can be performed here
        System.out.println(s);
    }
}