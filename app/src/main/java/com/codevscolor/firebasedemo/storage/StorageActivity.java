package com.codevscolor.firebasedemo.storage;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.codevscolor.firebasedemo.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class StorageActivity extends AppCompatActivity {
    private FirebaseStorage storage;
    private ProgressBar progressBar;
    private Context context;
    private ImageView imageView;
    private static final String BUCKET_NAME = "gs://codevscolor-.appspot.com"; //put your bucket name here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_storage);
        imageView = (ImageView) findViewById(R.id.imageView_storage);
        progressBar.setVisibility(View.GONE);
        context = this;

        storage = FirebaseStorage.getInstance();
    }

    public void upload(View v) {
        progressBar.setVisibility(View.VISIBLE);

        // Create a storage reference
        StorageReference storageRef = storage.getReferenceFromUrl(BUCKET_NAME);

        // Create a reference to 'images/sample_image.jpg'
        StorageReference imageReference = storageRef.child("images/sample_image.jpg");

        //get byte array of the image
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        //create metadata
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpg")
                .build();

        UploadTask uploadTask = imageReference.putBytes(data, metadata);

        //register listener to the uploadtask object
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                int progress = (int) (100.0 * (taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount()));
                progressBar.setProgress(progress);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(context, "Upload failed !!" + exception, Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Toast.makeText(context, "Uploaded . You can use this download url " + downloadUrl, Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void download(View v) {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);
        // Create a storage reference
        StorageReference storageRef = storage.getReferenceFromUrl(BUCKET_NAME);

        // Create a reference of the image
        StorageReference imageRefer = storageRef.child("images/sample_image.jpg");

        File localFile = null;

        String root = Environment.getExternalStorageDirectory().toString();
        File dir = new File(root + "/firebase_images");

        try {
            dir.mkdir();
            localFile = File.createTempFile("firebase_image",".jpg",dir);
        } catch (IOException e) {
            Toast.makeText(context,"Exception occurred ",Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
            e.printStackTrace();
        }
        if (localFile != null) {
            Toast.makeText(context,"Downloading to "+localFile.getAbsolutePath(),Toast.LENGTH_LONG).show();
            imageRefer.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(context, "Downloaded !!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(context, "Failed !!" + exception, Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {

                }
            });
        }
    }

    public void delete(View v) {
        StorageReference storageRef = storage.getReferenceFromUrl(BUCKET_NAME);

        StorageReference imageRef = storageRef.child("images/sample_image.jpg");

        // Delete the file
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Deleted !!", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(context, "Failed !!" + exception, Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });

    }

}
