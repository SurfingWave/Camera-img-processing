package com.example.a50500.camera;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    final static int CAMERA_RESULT = 0;
    private Button button;
    private ImageView imageView;
    private Button buttonChoose;
    private Button buttonUpload;
    private Button buttonView;

    private static String TAG = "main";
    private static int image_counter = 0;
    String  mCurrentPhotoPath;

    private Bitmap bitmap;
    private Uri filePath=null;

    // params for image uploading
    public static final String UPLOAD_URL = "http://182.254.219.248:80/img_upload.php";
    public static final String UPLOAD_KEY = "image";
    public static final String TAG_Msg = "MY MESSAGE";

    private Context mContext=MainActivity.this;
    private static final int REQUEST = 112;
    private static final int PICK_IMAGE_REQUEST = 100;
    private static final int TAKE_PHOTO_REQUEST = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView);

        if (Build.VERSION.SDK_INT >= 23) {
            String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET};
            if (!hasPermissions(mContext, PERMISSIONS)) {
                ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS, REQUEST);
            } else {
            }
        } else {
        }

        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        buttonView = (Button) findViewById(R.id.buttonViewImage);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(mContext, "The app was not allowed to read/write your store and access your internet", Toast.LENGTH_LONG).show();
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
        startActivityForResult(intent, TAKE_PHOTO_REQUEST);
        Log.e(TAG, "Absolute path  "+ Environment.getExternalStorageDirectory());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && intent != null && intent.getData() != null) {
            filePath = intent.getData();
            try{
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Log.e("Pickup image file path", filePath.toString());
                imageView.setImageBitmap(bitmap);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        // take photo for immediate use, otherwise choose anohter photo from directory
        else if(requestCode==TAKE_PHOTO_REQUEST && resultCode == RESULT_OK){

            Bundle extras=intent.getExtras();
            Bitmap bitmap = (Bitmap)extras.get("data");
//          Bitmap newbitmap = rotateBitmapByDegree(bitmap, 90);

            filePath=Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "wyk", "good"));
            imageView.setImageBitmap(bitmap);
//            filePath=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Log.e("take photo file path", filePath.toString());
        }
    }


    // choose image to display
    public void chooseImg(View view){
        showFileChooser();
    }


    // upload image to server
    public void uploadImg(View view){
        uploadImage();
    }


    private void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }


    // Upload Images to Server: AsyncTask Http-Method: POST
    private void uploadImage(){
        class UploadImage extends AsyncTask<Bitmap, Void, String>{

            ProgressDialog loading;
            imgUpload rh = new imgUpload();

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Uploading...", null, true, true);
            }

            @Override
            protected void onPostExecute(String s){
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params){
                uploadMultipart();
                return "Image Uploading Complete!!!";
            }
        }
        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }


    public void uploadMultipart() {

        String name = "Test img";
        if(filePath==null) {
            Toast.makeText(this, "Must choose a image before upload", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.e("take image file path", filePath.toString());
        String path = getPath(filePath);

        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                    .addFileToUpload(path, "image") //Adding file
                    .addParameter("name", name) //Adding text parameter to the request
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }


    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }


    // Convert a bitmap image to StringBase64 for uploading
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    // Rotate the image by certain degree
    public static Bitmap rotateBitmapByDegree(Bitmap bitmap, int degree) {
        Bitmap result = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        return result;
    }
}
