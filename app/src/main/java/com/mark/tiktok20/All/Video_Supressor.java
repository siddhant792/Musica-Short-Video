package com.mark.tiktok20.All;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.daasuu.gpuv.composer.GPUMp4Composer;
import com.mark.tiktok20.R;

public class Video_Supressor extends AppCompatActivity {

    String  video_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video__supressor);

        video_path = getIntent().getStringExtra("video_path");
        String des_path = "/storage/emulated/0/Aaaaa/123.mp4";
        Chnage_Video_size(video_path,des_path);


    }

    public void Chnage_Video_size(final String src_path, String destination_path){

        new GPUMp4Composer(src_path, destination_path)
                .size(720, 1280)
                .videoBitrate((int) (0.25 * 16 * 540 * 120))
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
                                Log.e("STatus","Completed");
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
                                    Toast.makeText(Video_Supressor.this, "Try Again", Toast.LENGTH_SHORT).show();
                                }catch (Exception e){

                                }
                            }
                        });

                    }
                })
                .start();

    }

}