package com.mark.tiktok20.All;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mark.tiktok20.R;
import com.mark.tiktok20.models.Posts;
import com.mark.tiktok20.models.User_uploads;

import java.io.ByteArrayOutputStream;
import java.io.File;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;

public class Upload_progress extends AppCompatActivity {

    String final_path;
    private StorageReference storageReference;
    int count =0;
    private FirebaseAuth mAuth;
    private long counter;
    CircularProgressIndicator circularProgress;
    String userId;

    String downloadurl,thumb_url;
    private DatabaseReference userRef,userRef_post;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_progress);
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        final_path = getIntent().getStringExtra("final_path");
        Toast.makeText(this, "Uploading now..", Toast.LENGTH_SHORT).show();
        storageReference = FirebaseStorage.getInstance().getReference().child("video_posts");
        userRef = FirebaseDatabase.getInstance().getReference("usersposts");
        userRef_post = FirebaseDatabase.getInstance().getReference("posts");
        circularProgress = findViewById(R.id.circular_progress);
        circularProgress.setMaxProgress(100);
        userRef_post.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                counter = dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*long id = getVideoIdFromFilePath(final_path,this.getContentResolver());
        Log.e("Status", String.valueOf(id));*/

        StorageReference sample = storageReference.child(userId);
        String Id = userRef.push().getKey();
        String Id2 = userRef.push().getKey();
        final StorageReference name = sample.child(Id);
        final StorageReference name2 = sample.child(Id2);
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("video/mp4")
                .build();
        Bitmap bMap = ThumbnailUtils.createVideoThumbnail(final_path, MediaStore.Video.Thumbnails.MINI_KIND);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bMap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            }
        });
        byte[] data = baos.toByteArray();
        name2.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                name2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        thumb_url = String.valueOf(uri);
                        Log.e("Thumb url",thumb_url);
                    }
                });
            }
        });
        name.putFile(Uri.fromFile(new File(final_path)),metadata).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.e("status","reached");
                name.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        downloadurl = String.valueOf(uri);
                        User_uploads user_uploads = new User_uploads(downloadurl,thumb_url);
                        userRef.child(userId).push().setValue(user_uploads);
                        Posts posts = new Posts(downloadurl,counter);
                        userRef_post.push().setValue(posts);
                        Toast.makeText(Upload_progress.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                        goto_main();
                        Log.e("Video Url",downloadurl);
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                circularProgress.setCurrentProgress(progress);
            }
        });

    }
    private void goto_main() {
        File file = new File(final_path);
        if (file.exists()){
            file.delete();
        }
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(Upload_progress.this,MainActivity.class);
                startActivity(i);
            }
        }, 2000);
    }
    @Override
    public void onBackPressed() {
        /*Intent i = new Intent(Upload_progress.this,MainActivity.class);
        startActivity(i);*/
        super.onBackPressed();
    }
}