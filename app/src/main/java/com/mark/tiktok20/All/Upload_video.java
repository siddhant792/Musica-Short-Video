package com.mark.tiktok20.All;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mark.tiktok20.R;
import com.mark.tiktok20.ui.CameraActivity;

import java.util.List;

import cn.ymex.popup.controller.ProgressController;
import cn.ymex.popup.dialog.DialogManager;
import cn.ymex.popup.dialog.PopupDialog;

public class Upload_video extends AppCompatActivity {

    private static final int DIALOUGE = 1;
    Button select,upload,record;
    int count =0;
    private StorageReference storageReference;
    String filePath;
    Uri uri,videouri;
    String final_path;
    List<String> mPaths;
    VideoView previewvideo;
    private FirebaseAuth mAuth;
    static final int OPEN_MEDIA_PICKER = 1;
    DialogManager dialogManager;
    String uri_path;
    static final int REQUEST_VIDEO_CAPTURED = 1;
    String downloadurl;
    private DatabaseReference userRef,userRef_post;
    private long counter;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        select = findViewById(R.id.select_gallery);
        record = findViewById(R.id.select_camera);
        //trimmer = findViewById(R.id.trimmer);
        storageReference = FirebaseStorage.getInstance().getReference().child("video_posts");
        userRef = FirebaseDatabase.getInstance().getReference("usersposts");
        final Vibrator vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        userRef_post = FirebaseDatabase.getInstance().getReference("posts");
        upload = findViewById(R.id.upload_firebase);
        previewvideo = findViewById(R.id.preview_video);
        dialogManager = new DialogManager();
        PopupDialog.create(Upload_video.this)
                .manageMe(dialogManager)
                .priority(DIALOUGE)
                .controller(ProgressController.build().message("Preparing.."));
        /*userRef_post.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                counter = dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (uri_path!=null){
                    Intent intent = new Intent(getApplicationContext(),Video_trimmer.class);
                    intent.putExtra("video_path",uri_path);
                    startActivity(intent);
                }else {
                    Toast.makeText(Upload_video.this, "Select a video", Toast.LENGTH_SHORT).show();
                    vibe.vibrate(100);
                }

            }
        });
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*new VideoPicker.Builder(Upload_video.this)
                        .mode(VideoPicker.Mode.GALLERY)
                        .directory(VideoPicker.Directory.DEFAULT)
                        .extension(VideoPicker.Extension.MP4)
                        .enableDebuggingMode(true)
                        .build();*/

                /*Intent pickIntent = new Intent();
                pickIntent.setType("video/*");
                pickIntent.setAction(Intent.ACTION_GET_CONTENT);*/
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i,OPEN_MEDIA_PICKER);

            }
        });
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*new VideoPicker.Builder(Upload_video.this)
                        .mode(VideoPicker.Mode.CAMERA)
                        .directory(VideoPicker.Directory.DEFAULT)
                        .extension(VideoPicker.Extension.MP4)
                        .enableDebuggingMode(true)
                        .build();*/

               /* Intent intent=new Intent("android.media.action.VIDEO_CAPTURE");
                intent.putExtra("android.intent.extra.durationLimit", 15);
                startActivityForResult(intent, REQUEST_VIDEO_CAPTURED);
                count++;*/

               Intent i = new Intent(Upload_video.this, CameraActivity.class);
               startActivity(i);

            }
        });

    }
    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VideoPicker.VIDEO_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            mPaths =  data.getStringArrayListExtra(VideoPicker.EXTRA_VIDEO_PATH);
            previewvideo.setVideoURI(Uri.parse(mPaths.get(0)));
            previewvideo.start();
            final_path = String.valueOf(mPaths.get(0));
        }
    }*/

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK ){
            videouri = data.getData();
            previewvideo.setVideoURI(videouri);
            previewvideo.start();

            uri_path = getPath(videouri);
            Log.e("Video_real_path",uri_path);
        }


    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if(cursor!=null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        else return null;
    }


}