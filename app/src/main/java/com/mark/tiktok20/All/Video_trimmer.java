package com.mark.tiktok20.All;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.daasuu.gpuv.composer.GPUMp4Composer;
import com.mark.tiktok20.R;

import java.io.File;

import cn.ymex.popup.controller.ProgressController;
import cn.ymex.popup.dialog.DialogManager;
import cn.ymex.popup.dialog.PopupDialog;
import life.knowledge4.videotrimmer.K4LVideoTrimmer;
import life.knowledge4.videotrimmer.interfaces.OnTrimVideoListener;

public class Video_trimmer extends AppCompatActivity {

    String  video_path;
    private static final int DIALOUGE = 1;
    DialogManager dialogManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_trimmer);

        video_path = getIntent().getStringExtra("video_path");
        dialogManager = new DialogManager();
        PopupDialog.create(Video_trimmer.this)
                .manageMe(dialogManager)
                .priority(DIALOUGE)
                .controller(ProgressController.build().message("Preparing.."));
        String des_path = "/storage/emulated/0/Musica_Cache/";
        File file = new File(des_path);
        if (!file.exists()){
            file.mkdir();
        }
        K4LVideoTrimmer videoTrimmer = ((K4LVideoTrimmer) findViewById(R.id.timeLine));
        if (videoTrimmer != null) {
            videoTrimmer.setVideoURI(Uri.parse(video_path));
            videoTrimmer.setMaxDuration(15);
            videoTrimmer.setOnTrimVideoListener(new OnTrimVideoListener() {
                @Override
                public void getResult(Uri uri) {
                    String des_path = "/storage/emulated/0/Musica_Cache/"+System.currentTimeMillis()+".mp4";
                    Chnage_Video_size(String.valueOf(uri),des_path);
                    Video_trimmer.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialogManager.show(DIALOUGE);
                        }
                    });

                }
                @Override
                public void cancelAction() {
                    finish();
                }
            });
        }


    }
    public void Chnage_Video_size(final String src_path, final String destination_path){

        new GPUMp4Composer(src_path, destination_path)
                .size(720, 1280)
                .videoBitrate((int) (0.25 * 16 * 540 * 300))
                .listener(new GPUMp4Composer.Listener() {
                    @Override
                    public void onProgress(double progress) {
                        Log.e("resp",""+(int) (progress*100));
                    }
                    @Override
                    public void onCompleted() {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("Status","Completed");
                                File file = new File(src_path);
                                if (file.exists()){
                                    file.delete();
                                }
                                dialogManager.destroy();
                                Intent intent = new Intent(Video_trimmer.this,Upload_progress.class);
                                intent.putExtra("final_path",destination_path);
                                startActivity(intent);
                            }
                        });


                    }

                    @Override
                    public void onCanceled() {
                        Log.e("resp", "onCanceled");
                    }

                    @Override
                    public void onFailed(Exception exception) {

                        Log.e("resp",exception.toString());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Toast.makeText(Video_trimmer.this, "Try Again", Toast.LENGTH_SHORT).show();
                                    dialogManager.destroy();
                                }catch (Exception e){

                                }
                            }
                        });

                    }
                })
                .start();

    }
}