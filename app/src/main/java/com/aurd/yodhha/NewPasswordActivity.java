package com.example.theftapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewPasswordActivity extends AppCompatActivity {

    EditText newPassword,confirmpassword;
    AppCompatButton submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        Intent intent=getIntent();
        newPassword=findViewById(R.id.newpassword);
        confirmpassword=findViewById(R.id.confirmpassword);
        submit=findViewById(R.id.btnNewPassword);

//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println("onclick");
//            }
//        });
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(confirmpassword.getText().toString().trim().equals(newPassword.getText().toString().trim())&&!newPassword.getText().toString().trim().isEmpty()){
                    FirebaseFirestore.getInstance().collection("Users").whereEqualTo("phoneNumber",intent.getStringExtra("number")).get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                    System.out.println(list.size());
                                    if(!list.isEmpty()){
                                        DocumentSnapshot doc=list.get(0);
                                        System.out.println(doc.getString("email"));
                                        System.out.println(doc.getString("password"));
                                   FirebaseAuth.getInstance().signInWithEmailAndPassword(doc.getString("email"),doc.getString("password")).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                       @Override
                                       public void onSuccess(AuthResult authResult) {
                                          FirebaseUser user= authResult.getUser();
                                           user.updatePassword(confirmpassword.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                               @Override
                                               public void onSuccess(Void unused) {
                                                   Map<String,Object> map=new HashMap<>();
                                                   map.put("password",confirmpassword.getText().toString().trim());

                                                   FirebaseFirestore.getInstance().collection("Users").document(doc.getId()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                       @Override
                                                       public void onSuccess(Void unused) {
                                                           Toast.makeText(NewPasswordActivity.this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                                                           Intent in=new Intent(getApplicationContext(),Login.class);
                                                           startActivity(in);
                                                           finish();

                                                       }
                                                   });
                                               }
                                           });

                                       }
                                   });


//                                        user.updatePassword(confirmpassword.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void unused) {
//                                                Map<String,Object> map=new HashMap<>();
//                                                map.put("password",confirmpassword.getText().toString().trim());
//
//                                                FirebaseFirestore.getInstance().collection("Users").document(doc.getId()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                    @Override
//                                                    public void onSuccess(Void unused) {
//                                                        Toast.makeText(NewPasswordActivity.this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
//                                                    Intent in=new Intent(getApplicationContext(),Login.class);
//                                                    startActivity(in);
//                                                    finish();
//
//                                                    }
//                                                });
//                                            }
//                                        });
                                    }
                                }
                            });
                }else{
                    Toast.makeText(NewPasswordActivity.this, "password do not match", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}