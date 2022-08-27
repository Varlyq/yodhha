package com.more_high.yodhha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.lang3.ArrayUtils;

import java.io.File;

public class Recording_List extends AppCompatActivity {
  RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording__list);

        Window window= getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.toolbartitlecolor));

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.backbutton);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerView = findViewById(R.id.recycler);
//        String path = getExternalFilesDirs(null).toString();
        File fileDirectory = getExternalFilesDir(null);
        File[] files = fileDirectory.listFiles();


        for(int i =0;i<files.length;i++){
            System.out.println(files[i].getName());
        }

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        MyAdapter myAdapter = new MyAdapter(files,this);
        recyclerView.setAdapter(myAdapter);



    }


}



  class MyAdapter extends RecyclerView.Adapter{
//    ArrayList arrayList;
    File [] files;
    Context context;

      public MyAdapter(File[] arrayList, Context context) {
          this.files = arrayList;
          this.context = context;
      }

      @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
          if(viewType == 0){
              View view = LayoutInflater.from(context).inflate(R.layout.recorder_card,parent,false);
              return new MyViewHolder(view);
          }else{
              View view = LayoutInflater.from(context).inflate(R.layout.videoview_card,parent,false);
              return new MyViewHolder(view);
          }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
          MyViewHolder myViewHolder = (MyViewHolder) holder;

          myViewHolder.textView.setText(files[position].getName());
        String fileName =  files[position].getName();

String ext= MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(files[position]).toString());
        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(ext);
//                if(ext.equals("mp3")){
                    Uri a = FileProvider.getUriForFile(context,BuildConfig.APPLICATION_ID+".provider",files[position]);

                    Intent viewMediaIntent = new Intent();
                    viewMediaIntent.setAction(android.content.Intent.ACTION_VIEW);
                    viewMediaIntent.setDataAndType(a, MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext));
                    viewMediaIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    viewMediaIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    context.startActivity(viewMediaIntent);
//                }else
//                {
//                    Uri a = Uri.parse(files[position].getAbsolutePath());
//
//                    Intent viewMediaIntent = new Intent();
//                    viewMediaIntent.setAction(android.content.Intent.ACTION_VIEW);
//                    viewMediaIntent.setDataAndType(a, "video/*");
//                    viewMediaIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    context.startActivity(viewMediaIntent);
//                }

            }
        });

        myViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
System.out.println("delete");
if(files[position].delete())
{
    files=ArrayUtils.remove(files,position);
}

notifyDataSetChanged();

            }
        });


    }

    @Override
    public int getItemCount() {
        return files.length;
    }

      @Override
      public int getItemViewType(int position) {
          String fileName =  files[position].getName();
          String ext =fileName.substring(fileName.lastIndexOf('.'));
          System.out.println(ext);
          if(ext.equals(".mp3")){
              return 0;
          }else
          {
              return  1;
          }
      }

      class MyViewHolder extends RecyclerView.ViewHolder{
          TextView textView;
          CardView cardView;
          ImageView delete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textview);
            cardView= itemView.findViewById(R.id.recordcard);
            delete=itemView.findViewById(R.id.delete);
        }
    }
}