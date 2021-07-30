package com.example.theftapp;

import android.app.Service;
import android.content.Intent;
import android.hardware.Camera;
import android.os.IBinder;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraService extends Service
{
    //Camera variables
    //a surface holder
    private SurfaceHolder sHolder;
    //a variable to control the camera
    private Camera mCamera;
    //the camera parameters
    private Camera.Parameters parameters;
    /** Called when the activity is first created. */
    @Override
    public void onCreate()
    {
        super.onCreate();

    }
    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
        Toast.makeText(this, "Camera Service", Toast.LENGTH_SHORT).show();
        System.out.println("Camera Service");
        mCamera = Camera.open();
        SurfaceView sv = new SurfaceView(getApplicationContext());


        try {
            mCamera.setPreviewDisplay(sv.getHolder());
            parameters = mCamera.getParameters();
//            Camera.Parameters params = camera.getParameters();
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
//some more settings
//            camera.setParameters(params);
            //set camera parameters
            mCamera.setParameters(parameters);
            mCamera.startPreview();
            mCamera.takePicture(null, null, mCall);

//            stopSelf();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        //Get a surface
        sHolder = sv.getHolder();
        //tells Android that this surface will have its data constantly replaced
        sHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }



    Camera.PictureCallback mCall = new Camera.PictureCallback()
    {

        public void onPictureTaken(byte[] data, Camera camera)
        {
            //decode the data obtained by the camera into a Bitmap

            FileOutputStream outStream = null;
//            try{
//                outStream = new FileOutputStream("/sdcard/"+System.currentTimeMillis()+".jpg");
//                outStream.write(data);
//                outStream.close();
//            } catch (FileNotFoundException e){
//                Log.d("CAMERA", e.getMessage());
//            } catch (IOException e){
//                Log.d("CAMERA", e.getMessage());
//            }

        }
    };


    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
}