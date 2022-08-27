package com.more_high.yodhha;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;

public class Contacts extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    AppCompatButton addButton;
    ArrayList arrayList;
    ContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);


        Window window= getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.toolbartitlecolor));

        Toolbar toolbar = findViewById(R.id.toolbarcontact  );
        toolbar.setNavigationIcon(R.drawable.backbutton);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addButton=findViewById(R.id.addmore);
        arrayList=new ArrayList();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson=new Gson();
        try{
            if(preferences.contains("contacts")) {

                JSONArray jsonArray = new JSONArray(preferences.getString("contacts", "[]"));
                for (int i = 0; i < jsonArray.length(); i++) {
                    Contact cont = gson.fromJson(jsonArray.getJSONObject(i).toString(), Contact.class);
                    arrayList.add(cont);
                }
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }


        recyclerView = findViewById(R.id.contactsRecycler);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter=new ContactAdapter(this,arrayList);
        recyclerView.setAdapter(adapter);


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
pickContact();
            }
        });

    }

    public void pickContact()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, 1010);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("result");

        if(requestCode==1010)
        {
            if(resultCode==RESULT_OK)
            {
                try {
                Uri contactData = data.getData();
                String number = "";
                Cursor cursor = getContentResolver().query(contactData, null, null, null, null);
                cursor.moveToFirst();
                String hasPhone = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                String contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                String name=cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                if (hasPhone.equals("1")) {
                    Cursor phones = getContentResolver().query
                            (ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                            + " = " + contactId, null, null);
                    while (phones.moveToNext()) {
                        number = phones.getString(phones.getColumnIndex
                                (ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("[-() ]", "");
                    }
                    phones.close();
                   System.out.println(number);
                   if(name.equals(number))
                   {
                       name="Unknown";
                   }

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    Gson gson=new Gson();

                    Contact contact=new Contact();
                    contact.setName(name);
                    contact.setNumber(number);





                       arrayList.add(contact);
                       preferences.edit().putString("contacts",gson.toJson(arrayList)).apply();
                       adapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(getApplicationContext(), "This contact has no phone number", Toast.LENGTH_LONG).show();
                }
                cursor.close();

            }catch (Exception e){
             e.printStackTrace();
                }
            }
        }
    }
}





 class ContactAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList arrayList;
    public ContactAdapter(Context context, ArrayList arrayList) {
        this.context = context;
        this.arrayList=arrayList;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_card, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ContactAdapter.MyViewHolder myViewHolder = (ContactAdapter.MyViewHolder) holder;
        Contact contact= (Contact) arrayList.get(position);
        myViewHolder.name.setText(contact.name);
        myViewHolder.number.setText(contact.number);
        myViewHolder.avatar.setText(contact.name.substring(0,1));

        myViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList.remove(position);
                notifyDataSetChanged();
                PreferenceManager.getDefaultSharedPreferences(context)
                        .edit().putString("contacts",new Gson().toJson(arrayList).toString()).apply();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name,number,avatar;
        ImageView delete;
        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
//            name = (TextView) itemView.findViewById(R.id.name);
//            image = (ImageView) itemView.findViewById(R.id.image);
            name=itemView.findViewById(R.id.contactname);
            number=itemView.findViewById(R.id.contactnumber);
            avatar=itemView.findViewById(R.id.avatar);
            delete=itemView.findViewById(R.id.delete);
        }
    }
}

 class Contact
{
    String name;
    String number;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}