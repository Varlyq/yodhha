  package com.example.theftapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

  public class Register extends AppCompatActivity implements SensorEventListener {

    EditText txtName,txtEmail,txtPhoneNumber,txtAlternateNumber,txtPassword,txtConfirmPassword,txtbirthdate;
    TextView signInLink;
    Button registerButton;

    String dateday,datemonth,dateyear;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser user;

    ImageView imageView; SensorManager sensorManager;
    Bitmap bitmap;

//    EditText txtmonth,txtyear;

//    AutoCompleteTextView month;
//    AutoCompleteTextView year;
//    String selectedYear;
//    String selectedMonth;

//    String[] monthName = {"January","February","March","April","May","June","July","August","September","October","November","December"};
//    String[] yearArray = {"1995","1996","1997","1998","1999","2000","2001","2002","2003","2004","2005","2006","2007"};

      final Calendar myCalendar = Calendar.getInstance();
      ImageView imgcalender;


      @Override
    protected void onResume() {
        super.onResume();
        // Register this class as a listener for the accelerometer sensor
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        // ...and the orientation sensor
//        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
//                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStop() {
        // Unregister the listener
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Window window= getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.toolbartitlecolor));

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.backbutton);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



        txtName = findViewById(R.id.txtname);
        txtEmail = findViewById(R.id.txtemail);
        txtPhoneNumber = findViewById(R.id.phonenumber);
        txtAlternateNumber = findViewById(R.id.alternatephonenumber);
        txtPassword = findViewById(R.id.password);
        txtConfirmPassword = findViewById(R.id.confirmpassword);
        signInLink = findViewById(R.id.signinlink);
        registerButton = findViewById(R.id.btnRegister);
        firebaseAuth = FirebaseAuth.getInstance();
        txtbirthdate = findViewById(R.id.birthdate);
//        txtmonth = findViewById(R.id.birthmonth);
//        txtyear= findViewById(R.id.birthyear);
        imgcalender = findViewById(R.id.calender);


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

//        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>
//                (this,android.R.layout.select_dialog_item,monthName);
//        //Getting the instance of AutoCompleteTextView
//        month = findViewById(R.id.birthmonth);
//        month.setThreshold(1);//will start working from first character
//        month.setAdapter(monthAdapter);//setting the adapter data into the AutoCompleteTextView
//        month.setTextColor(getResources().getColor(R.color.black));
//        month.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                 selectedMonth = (String) parent.getItemAtPosition(position);
//                System.out.println(selectedMonth);
//            }
//        });
//
//
//        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>
//                (this,android.R.layout.select_dialog_item,yearArray);
//        //Getting the instance of AutoCompleteTextView
//        year = findViewById(R.id.birthyear);
//        year.setThreshold(1);//will start working from first character
//        year.setAdapter(yearAdapter);//setting the adapter data into the AutoCompleteTextView
//        year.setTextColor(getResources().getColor(R.color.black));
//        year.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                 selectedYear = (String) parent.getItemAtPosition(position);
//                System.out.println(selectedYear);
//            }
//        });




//        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
//        bitmap = bitmapDrawable.getBitmap();


        signInLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this,Login.class);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseFirestore.getInstance().collection("Users")
                        .whereEqualTo("phoneNumber",txtPhoneNumber.getText().toString()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.getDocuments().isEmpty())
                        {
                            FirebaseFirestore.getInstance().collection("Users").whereEqualTo("email",txtEmail.getText().toString())
                                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    if(queryDocumentSnapshots.getDocuments().isEmpty())
                                    {
                                        registerUser();
                                    }
                                   else{
                                        Toast.makeText(Register.this, "Email already Exists", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Register.this, "SomeThing went wrong", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            });

                        }
                        else {
                            Toast.makeText(Register.this, "User already exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        Toast.makeText(Register.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
//                registerUser();
            }
        });



        imgcalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(Register.this,R.style.CalenderDialogTheme,date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }
      DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

          @Override
          public void onDateSet(DatePicker view, int year, int monthOfYear,
                                int dayOfMonth) {
              // TODO Auto-generated method stub
              myCalendar.set(Calendar.YEAR, year);
              myCalendar.set(Calendar.MONTH, monthOfYear);
              myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
              System.out.println(year);
              System.out.println(monthOfYear+1);
              System.out.println(dayOfMonth);

            String monthname =  myCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
            System.out.println(monthname);
              txtbirthdate.setText(dayOfMonth+" / "+monthname+" / "+year);
              dateday=String.valueOf(dayOfMonth);
              datemonth=monthname;
              dateyear=String.valueOf(year);
          }

      };





      private void registerUser(){

          Pattern pattern = Pattern.compile("^\\d{10}$");

          String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                  "[a-zA-Z0-9_+&*-]+)*@" +
                  "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                  "A-Z]{2,7}$";

          Pattern pat = Pattern.compile(emailRegex);

          if(txtName.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Please enter a Name", Toast.LENGTH_SHORT).show();
        }
         else if(txtPhoneNumber.getText().toString().isEmpty() ||    !pattern.matcher(txtPhoneNumber.getText().toString()).matches())
        {

              Toast.makeText(this, "Please enter a valid Phone Number", Toast.LENGTH_SHORT).show();
          }
          else if(txtAlternateNumber.getText().toString().isEmpty() || !pattern.matcher(txtAlternateNumber.getText().toString()).matches())
          {
              Toast.makeText(this, "Please enter a valid Alternate Number", Toast.LENGTH_SHORT).show();
          }
        else if(txtEmail.getText().toString().isEmpty() || !pat.matcher(txtEmail.getText().toString()).matches())
        {
            Toast.makeText(this, "Please enter a valid Email", Toast.LENGTH_SHORT).show();
        }
        else if(txtPassword.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Please enter a valid Password", Toast.LENGTH_SHORT).show();
        }
        else if(txtConfirmPassword.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Please confirmm your password", Toast.LENGTH_SHORT).show();
        }

        else if(!txtPassword.getText().toString().equals(txtConfirmPassword.getText().toString()))
        {
            Toast.makeText(this, "Password do not match", Toast.LENGTH_SHORT).show();
        }
        else if(txtbirthdate.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Please choose your birthDate", Toast.LENGTH_SHORT).show();
        }


else{
            String name = txtName.getText().toString();
            String phonenumber = txtPhoneNumber.getText().toString();
            String alternatePhoneNumber = txtAlternateNumber.getText().toString();
            String inputEmail = txtEmail.getText().toString();
            String inputPassword =txtPassword.getText().toString();
            String confirmPassword = txtConfirmPassword.getText().toString();

            firebaseAuth.createUserWithEmailAndPassword(inputEmail, inputPassword)
                    .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                System.out.println(task.getException());
                                Toast.makeText(Register.this, "Authentication failed." + task.getException(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                user = FirebaseAuth.getInstance().getCurrentUser();
                                firebaseFirestore = FirebaseFirestore.getInstance();

                                DocumentReference documentReference = firebaseFirestore.collection("Users").document();
                                UserModal userModal = new UserModal();
                                userModal.setName(name);
                                userModal.setEmail(inputEmail);
                                userModal.setPassword(inputPassword);
                                userModal.setPhoneNumber(phonenumber);
                                userModal.setAlternatePhoneNumber(alternatePhoneNumber);
                                userModal.setUserId(user.getUid());
                                userModal.setDate(txtbirthdate.getText().toString());
//                            userModal.setMonth(txtmonth.getText().toString());
//                            userModal.setYear(txtyear.getText().toString());




                                documentReference.set(userModal).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        System.out.println(task.getException());
                                        if(task.isSuccessful()){
                                            Toast.makeText(Register.this, "User created successfully.", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(Register.this, Login.class));
                                        }
                                        else{
                                            Toast.makeText(Register.this, "User creation Unsuccessful", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });


                            }
                        }
                    });

        }





    }

    @Override
    public void onSensorChanged(SensorEvent event) {
//        if(event.values[0]>2)
//        {
//            leftSkew(Math.round(event.values[0]));
//        }
//        if(event.values[0]<-1)
//        {
//            rightSkew(Math.round(event.values[0]*-1));
//        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }



//    void leftSkew(int temp)
//    {
////        BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
////
////        Bitmap bitmap = drawable.getBitmap();
//        Matrix matrix2 = new   Matrix();
//        int oldw = bitmap.getWidth();
//        int oldh =  bitmap.getHeight();
//        float d_up = (oldh / 70)*temp;
//        float d_down = d_up + temp;
//        float[] src2 = new float[] {
//                0, 0,
//                oldw, 0,
//                oldw, oldh,
//                0, oldh };
//        float[] dst2 = new float[] {
//                0, d_up, //top left
//                oldw, 0, //top right
//                oldw, oldh, //bottom right
//                0, oldh - d_down //bottom left
//        };
//
////        float[] dst2 = new float[] {
////                0, 0, //top left
////                oldw, d_up, //top right
////                oldw, oldh - d_down, //bottom right
////                0, oldh  //bottom left
////        };
//        matrix2.setPolyToPoly(src2, 0, dst2, 0,
//                src2.length >> 1);
//        Bitmap bMatrix2 = Bitmap.createBitmap(bitmap, 0, 0,
//                bitmap.getWidth(), bitmap.getHeight(),
//                matrix2, true);
//        imageView.setImageBitmap(bMatrix2);
//
//    }
//
//    void rightSkew(int temp)
//
//    {
////        BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
////
////        Bitmap bitmap = drawable.getBitmap();
//        Matrix matrix2 = new   Matrix();
//        int oldw = bitmap.getWidth();
//        int oldh =  bitmap.getHeight();
//        float d_up = (oldh / 70)*temp;
//        float d_down = d_up + temp;
//        float[] src2 = new float[] {
//                0, 0,
//                oldw, 0,
//                oldw, oldh,
//                0, oldh };
////        float[] dst2 = new float[] {
////                0, d_up, //top left
////                oldw, 0, //top right
////                oldw, oldh, //bottom right
////                0, oldh - d_down //bottom left
////        };
//
//        float[] dst2 = new float[] {
//                0, 0, //top left
//                oldw, d_up, //top right
//                oldw, oldh - d_down, //bottom right
//                0, oldh  //bottom left
//        };
//        matrix2.setPolyToPoly(src2, 0, dst2, 0,
//                src2.length >> 1);
//        Bitmap bMatrix2 = Bitmap.createBitmap(bitmap, 0, 0,
//                bitmap.getWidth(), bitmap.getHeight(),
//                matrix2, true);
//        imageView.setImageBitmap(bMatrix2);
//    }
}