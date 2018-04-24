package com.example.a50500.camera;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    final static int CAMERA_RESULT = 0;
    private Button button;
    private ImageView imageView;
    private static String TAG = "main";
    private static int image_counter = 0;
    String  mCurrentPhotoPath;

    private Context mContext=MainActivity.this;
    private static final int REQUEST = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView);

        if (Build.VERSION.SDK_INT >= 23) {
            String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (!hasPermissions(mContext, PERMISSIONS)) {
                ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS, REQUEST);
            } else {
                //do here
            }
        } else {
            //do here
        }

//        button = (Button) findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent, CAMERA_RESULT);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.parse("file://" + mFilePath));
//            }
//        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //do here
                } else {
                    Toast.makeText(mContext, "The app was not allowed to read your store.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    public void startCamareActivity(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        File photoFile = null;
//        try {
//            photoFile = createImageFile();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//        if (photoFile != null) {
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
//            startActivityForResult(intent, CAMERA_RESULT);
//        }
        startActivityForResult(intent, CAMERA_RESULT);
        Log.e(TAG, "Absolute path  "+ Environment.getExternalStorageDirectory());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        if(resultCode == RESULT_OK){
            Bundle extras=intent.getExtras();
            Bitmap bitmap = (Bitmap)extras.get("data");
//            Bitmap newbitmap = rotateBitmapByDegree(bitmap, 90);
            MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "wyk", "good");


//            try{
//                File file = new File("/storage/self/primary/", image_counter + ".jpg");
//                if( !file.exists() ){
//                    try {
//                        file.createNewFile();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                else {
//                    file.delete();
//                    try {
//                        file.createNewFile();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
//                bos.flush();
//                bos.close();
//                Log.e(TAG, "img: "+image_counter);
//                image_counter += 1;
//            }catch(Exception e){
//                e.printStackTrace();
//            }

            imageView.setImageBitmap(bitmap);
        }
    }


    /*
      Rotate the image by certain degree
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bitmap, int degree) {
        Bitmap result = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (Exception e) {
        }
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        return result;
    }
}
