package com.mark.tiktok20.All;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.daasuu.gpuv.composer.GPUMp4Composer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.mark.tiktok20.R;

import java.io.File;

import cn.ymex.popup.controller.ProgressController;
import cn.ymex.popup.dialog.DialogManager;
import cn.ymex.popup.dialog.PopupDialog;

public class Recorded_video_supressor extends AppCompatActivity {

    SimpleExoPlayer player;
    String video_path;
    private static final int DIALOUGE = 1;
    DialogManager dialogManager;
    Button continue_upload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorded_video_supressor);
        video_path = getIntent().getStringExtra("video_path");
        dialogManager = new DialogManager();
        PopupDialog.create(this)
                .manageMe(dialogManager)
                .priority(DIALOUGE)
                .controller(ProgressController.build().message("Preparing.."));
        String des_path = "/storage/emulated/0/Musica_Cache/";
        File file = new File(des_path);
        if (!file.exists()){
            file.mkdir();
        }
        continue_upload = findViewById(R.id.continue_to_upload);
        player = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector());
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "Musica"));
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(video_path));
        player.prepare(videoSource);
        player.setRepeatMode(Player.REPEAT_MODE_ALL);
        PlayerView playerView= findViewById(R.id.playerview);
        playerView.setPlayer(player);
        player.setPlayWhenReady(true);
        String dess = "/storage/emulated/0/Musica_Cache/"+System.currentTimeMillis()+".mp4";
        continue_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Chnage_Video_size(video_path,dess);
                dialogManager.show(DIALOUGE);
            }
        });

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
                                Intent intent = new Intent(Recorded_video_supressor.this,Upload_progress.class);
                                intent.putExtra("final_path",destination_path);
                                player.release();
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
                                    Toast.makeText(Recorded_video_supressor.this, "Try Again", Toast.LENGTH_SHORT).show();
                                    dialogManager.destroy();
                                }catch (Exception e){

                                }
                            }
                        });

                    }
                })
                .start();

    }

    @Override
    protected void onDestroy() {
        player.release();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        player.release();
        super.onBackPressed();
    }
}